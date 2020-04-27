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
		MyUser curentUser = (MyUser) principal;

		if (curentUser.getRole().equals(Role.STUDENT))
			return "redirect:/studentsRequests";
		if (curentUser.getRole().equals(Role.TEACHER))
			return "redirect:/teachersRequests";
		return "";
	}

	// cereri studenti
	@RequestMapping("/studentsRequests")
	public String viewStudentRequest(Model model,@RequestParam(value="status", required=false) String status, @RequestParam(value="teacherName", required=false) String teacherName) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		MyUser curentUser = (MyUser) principal;
		int studentID = curentUser.getUser().getStudent().getIdstudenti();
		List<Request> cereri = requestRepo.findByStudentIdstudenti(curentUser.getUser().getStudent().getIdstudenti());
		
		if(status != null && status.contains("Respins")) {
			cereri=requestRepo.findByStudentIdstudentiAndStatusLike(studentID,"Respins");
			model.addAttribute("status","Respins");
	}
	if(status != null && status.contains("Acceptat")) {
		cereri=requestRepo.findByStudentIdstudentiAndStatusLike(studentID,"Acceptat");
		model.addAttribute("status","Acceptat");
	}
	if(status != null && status.contains("asteptare")) {
		cereri=requestRepo.findByStudentIdstudentiAndStatusLike(studentID,"asteptare");
		model.addAttribute("status","In asteptare");
	}
	
	if(teacherName != null) {
		cereri=requestRepo.findByStudentIdstudentiAndTeacherUserUsernameLike(studentID, teacherName);
		model.addAttribute("teacherName",teacherName);
	}
		
		model.addAttribute("cereriStudent", cereri);

		return "/students/viewSentRequests";
	}

	// cereri profesori
	@RequestMapping("/teachersRequests")
	public String viewTeacherRequest(Model model, @RequestParam(value="status", required=false) String status, @RequestParam(value="studentName", required=false) String studentName) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		MyUser curentUser = (MyUser) principal;
		int teacherID = curentUser.getUser().getTeacher().getIdprofesori();
		
		model.addAttribute("teme",
				projectRepo.findByTeacherIdprofesori(curentUser.getUser().getTeacher().getIdprofesori()));
		
		List<Request> cereri = requestRepo.findByTeacherIdprofesori(curentUser.getUser().getTeacher().getIdprofesori());
		
		
		
		if(status != null && status.contains("Respins")) {
				cereri=requestRepo.findByTeacherIdprofesoriAndStatusLike(teacherID,"Respins");
				System.out.println(cereri.size());
				model.addAttribute("status","Respins");
		}
		if(status != null && status.contains("Acceptat")) {
			cereri=requestRepo.findByTeacherIdprofesoriAndStatusLike(teacherID,"Acceptat");
			model.addAttribute("status","Acceptat");
		}
		if(status != null && status.contains("asteptare")) {
			cereri=requestRepo.findByTeacherIdprofesoriAndStatusLike(teacherID,"asteptare");
			model.addAttribute("status","In asteptare");
		}
		
		if(studentName != null) {
			cereri=requestRepo.findByTeacherIdprofesoriAndStudentUserUsernameLike(teacherID, studentName);
			model.addAttribute("studentName",studentName);
		}
		
		model.addAttribute("cereriProfesor", cereri);

		return "/teachers/viewReceivedRequests";
	}

	@PostMapping("/submitRequest")
	public String request(@RequestParam(value = "teacherID", required = false) String teacherID,
			@RequestParam(value = "numeTema", required = false) String numeTema, RedirectAttributes redirectAttributes)
			throws Exception {

		// pregatire obiecte
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		MyUser curentUser = (MyUser) principal;

		int studentID = curentUser.getUser().getStudent().getIdstudenti();
		Teacher targetTeacher = teacherRepo.findByIdprofesori(Integer.parseInt(teacherID));
		Tema project = new Tema();

		Request request = new Request();
		request.setStatus("In asteptare (" + DateUtil.getDate() + ")");
		request.setStudent(curentUser.getUser().getStudent());
		request.setTeacher(targetTeacher);

		// setare tema pentru cerere
		if (!numeTema.contains("Aplica fara tema"))
			request.setTema(projectRepo.findByNumeAndTeacherIdprofesori(numeTema, Integer.parseInt(teacherID)));

		List<Request> verifyRequest = new ArrayList();
		String errorMessage = "";

		// verificare aplicare fara tema
		if (!numeTema.equals("custom") && numeTema.contains("Aplica fara tema")) {
			System.out.println("tema neselectata");
			verifyRequest.add(requestRepo.findByStudentIdstudentiAndTeacherIdprofesoriAndTemaNume(studentID,
					Integer.valueOf(teacherID), null));
			errorMessage = "Ai facut deja o cerere catre acest profesor!";
		}

		// verificare aplicare cu tema
		if (!numeTema.equals("custom") && !numeTema.contains("Aplica fara tema")) {
			System.out.println("tema selectata");
			verifyRequest = requestRepo.findByStudentIdstudentiAndTemaNume(studentID, numeTema);
			// daca s-a gasit vreo cerere facuta de student cu numele temei X, se verifica
			// daca tema apartinea studentului. daca da, se sterge.
			// aici se cauta daca studentul a aplicat pe tema unui profesor. pot exista 2
			// teme cu acelasi nume. diferenta este cine a postat-o
			for (int i = 0; i < verifyRequest.size(); i++)
				if (verifyRequest.get(i) != null && verifyRequest.get(i).getTema().getStudent() != null)
					verifyRequest.remove(i);
			errorMessage = "Ai facut deja o cerere pentru aceasta tema!";
		}

		// DECIZIE REDIRECT
		redirectAttributes.addAttribute("teacherID", Integer.valueOf(teacherID));
		if (verifyRequest != null && verifyRequest.size() != 0 && verifyRequest.get(0) != null) {
			redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
			return "redirect:/teacherDetails";
		} else {
			requestRepo.save(request);
			// sendMail.notifyTeacher(targetTeacher.getUser().getEmail(),
			// targetTeacher.getUser().getUsername(),
			// curentUser.getUser().getUsername());
			redirectAttributes.addFlashAttribute("succes", "Cererea a fost facuta cu succes!");
			return "redirect:/teacherDetails";
		}
	}

	// only students will acces it
	@PostMapping("/submitRequestMyProject")
	public String saveRequestWithProject(@RequestParam(value = "teacherID", required = false) String teacherID,
			@RequestParam("numeTema") String numeTema, @RequestParam(value = "temaPDF") MultipartFile temaPDF,
			RedirectAttributes redirectAttributes) throws IOException {

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		MyUser curentUser = (MyUser) principal;
		redirectAttributes.addAttribute("teacherID", Integer.valueOf(teacherID));

		Teacher targetTeacher = teacherRepo.findByIdprofesori(Integer.valueOf(teacherID));

		if (temaPDF.getBytes().length == 0) {
			redirectAttributes.addFlashAttribute("nullProject", "Selecteaza un fisier");
			redirectAttributes.addAttribute("teacherID", Integer.valueOf(teacherID));
			return "redirect:/teacherDetails";
		}

			// crearea temei studentului, caci ea nu exista + salvare in BD
			Tema project = new Tema();
			project.setTema(temaPDF.getBytes());
			project.setStudent(curentUser.getUser().getStudent());
			project.setNume(numeTema);

			try {
				projectRepo.save(project);
			} catch (Exception e) {
				redirectAttributes.addFlashAttribute("errorMessage", "Tema nu a fost incarcata! Dimensiune prea mare!");
				return "redirect:/teacherDetails";
			}

		

		if (requestRepo.findByStudentIdstudentiAndTeacherIdprofesoriAndTemaNume(
				curentUser.getUser().getStudent().getIdstudenti(), Integer.valueOf(teacherID), project.getNume()) != null) {
			redirectAttributes.addFlashAttribute("errorMessage", "Ai facut deja o cerere cu aceasta tema!");
			redirectAttributes.addAttribute("teacherID", Integer.valueOf(teacherID));
			return "redirect:/teacherDetails";
		} else {
			// crearea cererii
			Request request = new Request();
			request.setStatus("In asteptare (" + DateUtil.getDate() + ")");
			request.setStudent(curentUser.getUser().getStudent());
			request.setTeacher(targetTeacher);
			request.setTema(project);

			requestRepo.save(request);

			redirectAttributes.addFlashAttribute("succes", "Cererea a fost facuta cu succes!");
			return "redirect:/teacherDetails";
		}
	}

	@RequestMapping("/manageStudent")
	public String manageStudent(@RequestParam(value = "status") String status,
			@RequestParam(value = "studentID") String studentID, @RequestParam(value = "idCerere") String idCerere) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		MyUser curentUser = (MyUser) principal;
		int teacherID = curentUser.getUser().getTeacher().getIdprofesori();
		int newSlots = Integer.valueOf(curentUser.getUser().getTeacher().getSlots()) - 1;

		Student targetStudent = studentRepo.findByIdstudenti(Integer.parseInt(studentID));

		// update req status in DB
		requestRepo.updateStatus(status + "  " + DateUtil.getDate(), Integer.valueOf(idCerere));

		// update teacher slots
		if (status.equalsIgnoreCase("Acceptat"))
			teacherRepo.updateSlots(curentUser.getUser().getUserID(), String.valueOf(newSlots));

		// send mail to Student
		sendMail.sendReqStatus(targetStudent.getUser().getEmail(), curentUser.getUsername(), status);

		return "redirect:/viewRequests";

	}

	@RequestMapping("/modifyRequestProject")
	public String updateRequestProject(
			@RequestParam("requestID") String requestID, @RequestParam(value = "temaPDF") MultipartFile temaPDF,
			RedirectAttributes redirectAttributes) throws IOException {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		MyUser curentUser = (MyUser) principal;

		redirectAttributes.addAttribute("idCerere", requestID);
		if (temaPDF.getBytes().length == 0) {
			redirectAttributes.addFlashAttribute("nullProject", "Selecteaza un fisier");
			return "redirect:/studentViewRequest";
		}
		
		
		try {
		Request targetRequest = requestRepo.findByIdcereri(Integer.valueOf(requestID));
		projectRepo.updateStudentProject(temaPDF.getBytes(),targetRequest.getTema().getIdteme());
		redirectAttributes.addFlashAttribute("succes", "Tema a fost modificata!");
		return "redirect:/studentViewRequest";
		}
		catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Dimensiune prea mare! (max 16MB)");
			return "redirect:/studentViewRequest";
		}
	}

}
