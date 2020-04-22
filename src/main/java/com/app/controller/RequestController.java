package com.app.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.model.MyUser;
import com.app.model.Request;
import com.app.model.Role;
import com.app.model.Student;
import com.app.model.Teacher;
import com.app.model.Tema;
import com.app.repository.RequestRepository;
import com.app.repository.StudentRepository;
import com.app.repository.TeacherRepository;
import com.app.repository.TemeRepository;
import com.app.services.DateUtil;
import com.app.services.SendMail;

@Controller
public class RequestController {
	
	@Autowired
	RequestRepository requestRepo;
	
	@Autowired
	TemeRepository projectRepo;
	
	@Autowired
	TeacherRepository teacherRepo;
	
	@Autowired
	SendMail sendMail;
	
	@Autowired
	StudentRepository studentRepo;
	
	
	@RequestMapping("/viewRequests")
	public String redirect(Model model) {
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		MyUser curentUser = (MyUser)principal;
		
		if(curentUser.getRole().equals(Role.STUDENT)) return "redirect:/studentsRequests";
    	if(curentUser.getRole().equals(Role.TEACHER)) return "redirect:/teachersRequests";
		return"";
	}
	
	//cereri studenti
	@RequestMapping("/studentsRequests")
	public String viewStudentRequest(Model model) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		MyUser curentUser = (MyUser)principal;
		
		List<Request> cereri = requestRepo.findByStudentIdstudenti(curentUser.getUser().getStudent().getIdstudenti());
		
		model.addAttribute("cereriStudent", cereri);
		
		return "/students/viewSentRequests";
	}
	
	//cereri profesori
	@RequestMapping("/teachersRequests")
	public String viewTeacherRequest(Model model) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		MyUser curentUser = (MyUser)principal;
		model.addAttribute("teme", projectRepo.findByTeacherIdprofesori(curentUser.getUser().getTeacher().getIdprofesori()));
		
		List<Request> cereri = requestRepo.findByTeacherIdprofesori(curentUser.getUser().getTeacher().getIdprofesori());
		
		model.addAttribute("cereriProfesor", cereri);
		
		return "/teachers/viewReceivedRequests";
	}
	
	
	
	@PostMapping("/submitRequest")
	public String request(@RequestParam(value = "teacherID", required=false) String teacherID,
			@RequestParam(value = "numeTema", required = false) String numeTema,
			RedirectAttributes redirectAttributes) throws Exception {
		
		//pregatire obiecte 
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		MyUser curentUser = (MyUser) principal;
		
		int studentID = curentUser.getUser().getStudent().getIdstudenti();
		Teacher targetTeacher = teacherRepo.findByIdprofesori(Integer.parseInt(teacherID));
		Tema project = new Tema();
		
		
		Request request = new Request();
		request.setStatus("In asteptare (" + DateUtil.getDate() + ")");
		request.setStudent(curentUser.getUser().getStudent());
		request.setTeacher(targetTeacher);
		
		//setare tema pentru cerere
		if (!numeTema.contains("Aplica fara tema"))
			request.setTema(projectRepo.findByNume(numeTema));
		
		
		List<Request> verifyRequest = new ArrayList();
		String errorMessage = "";
		
		
		//verificare aplicare fara tema
		if (!numeTema.equals("custom") && numeTema.contains("Aplica fara tema")) {
			System.out.println("tema neselectata");
			verifyRequest.add(requestRepo.findByStudentIdstudentiAndTeacherIdprofesoriAndTema(studentID,
							Integer.valueOf(teacherID),null));
			errorMessage = "Ai facut deja o cerere catre acest profesor!";
		}

		//verificare aplicare cu tema
		if (!numeTema.equals("custom") && !numeTema.contains("Aplica fara tema")) {
			System.out.println("tema selectata");
			verifyRequest.add(requestRepo.findByStudentIdstudentiAndTemaNume(studentID, numeTema));
			errorMessage = "Ai facut deja o cerere pentru aceasta tema!";
		}
		
		//DECIZIE REDIRECT
		redirectAttributes.addAttribute("teacherID", Integer.valueOf(teacherID));
		if (verifyRequest != null && verifyRequest.size() !=0 && verifyRequest.get(0) != null ) {
			redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
			return "redirect:/teacherDetails";
		} else {
			requestRepo.save(request);
			//sendMail.notifyTeacher(targetTeacher.getUser().getEmail(), targetTeacher.getUser().getUsername(),
		//			curentUser.getUser().getUsername());
			redirectAttributes.addFlashAttribute("succes", "Cererea a fost facuta cu succes!");
			return "redirect:/teacherDetails";
		}
		}
	
	
	
	@PostMapping("/submitRequestMyProject")
		public String saveRequestWithProject(@RequestParam(value="teacherID", required=false)String teacherID, @RequestParam("numeTema")String numeTema,  @RequestParam(value="temaPDF") MultipartFile temaPDF, RedirectAttributes redirectAttributes ) throws IOException {
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		MyUser curentUser = (MyUser)principal;
		
		Teacher targetTeacher = teacherRepo.findByIdprofesori(Integer.valueOf(teacherID));
		
		
		if(temaPDF.getBytes().length == 0) {
			redirectAttributes.addFlashAttribute("nullProject", "Selecteaza un fisier");
			redirectAttributes.addAttribute("teacherID", Integer.valueOf(teacherID));
			return "redirect:/teacherDetails";
		}
		
		
		Tema project = projectRepo.findByNume(numeTema);
		if(project == null) {
		//crearea temei studentului, caci ea nu exista + salvare in BD
		Tema newProject = new Tema();
		newProject.setTema(temaPDF.getBytes());
		newProject.setStudent(curentUser.getUser().getStudent());
		newProject.setNume(numeTema);
		
		projectRepo.save(newProject);
		project = newProject;
		}
		else {
			redirectAttributes.addFlashAttribute("errorMessage", "Ai facut deja o cerere cu aceasta tema!");
			redirectAttributes.addAttribute("teacherID", Integer.valueOf(teacherID));
			return "redirect:/teacherDetails";
		}
		
		
		//crearea cererii 
		Request request = new Request();
		request.setStatus("In asteptare (" + DateUtil.getDate() + ")");
		request.setStudent(curentUser.getUser().getStudent());
		request.setTeacher(targetTeacher);
		request.setTema(project);
		
		requestRepo.save(request);
		
		redirectAttributes.addAttribute("teacherID", Integer.valueOf(teacherID));
		redirectAttributes.addFlashAttribute("succes", "Cererea a fost facuta cu succes!");
		return "redirect:/teacherDetails";
	}

	@RequestMapping("/manageStudent")
	public String manageStudent(@RequestParam(value="status") String status, @RequestParam(value="studentID") String studentID, @RequestParam(value="numeTema")String numeTema) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		MyUser curentUser = (MyUser) principal;
		int teacherID = curentUser.getUser().getTeacher().getIdprofesori();
		int newSlots = Integer.valueOf(curentUser.getUser().getTeacher().getSlots()) -1;
		
		Student targetStudent = studentRepo.findByIdstudenti(Integer.parseInt(studentID));
		System.out.println("numele temei -> " + numeTema);
		
		//update req status in DB (aplicare fara tema)
		if(numeTema.contains("fara tema")) requestRepo.updateRequestStatus(status+"  " + DateUtil.getDate(), Integer.valueOf(studentID), teacherID);
		if(!numeTema.contains("fara tema")) requestRepo.updateRequestStatusWithProject(status+"  " + DateUtil.getDate(), Integer.valueOf(studentID), teacherID, requestRepo.findByTemaNume(numeTema).getTema().getIdteme());
		
		//update teacher slots 
		if(status.equalsIgnoreCase("Acceptat")) teacherRepo.updateSlots(curentUser.getUser().getUserID(), String.valueOf(newSlots));
		
		//send mail to Student
		sendMail.sendReqStatus(targetStudent.getUser().getEmail(), curentUser.getUsername(),status);
		
		
		//reload teacher to model attribute
		Authentication authentication = new UsernamePasswordAuthenticationToken(principal, curentUser.getPassword());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		return "redirect:/viewRequests";
		
	}
	
	@RequestMapping("/modifyRequestProject")
	public String updateRequestProject(@RequestParam("numeTema")String numeTema, @RequestParam("requestID") String requestID, @RequestParam(value="temaPDF") MultipartFile temaPDF, RedirectAttributes redirectAttributes ) throws IOException {
		
		redirectAttributes.addAttribute("idCerere", requestID);
		if(temaPDF.getBytes().length == 0) {
			redirectAttributes.addFlashAttribute("nullProject", "Selecteaza un fisier");
			return "redirect:/studentViewRequest";
		}
		
		projectRepo.update(temaPDF.getBytes(), numeTema);
		redirectAttributes.addFlashAttribute("succes", "Tema a fost modificata!");
		return "redirect:/studentViewRequest";
	}

}
