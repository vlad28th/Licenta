package com.app.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.model.MyUser;
import com.app.model.Request;
import com.app.model.Teacher;
import com.app.repository.RequestRepository;
import com.app.repository.StudentRepository;
import com.app.repository.TeacherRepository;
import com.app.repository.TemeRepository;
import com.app.repository.UserRepository;
import com.app.services.DateUtil;
import com.app.services.SendMail;

@Controller
public class StudentsController {

	@Autowired
	TeacherRepository teacherRepo;

	@Autowired
	RequestRepository requestRepo;

	@Autowired
	StudentRepository studentRepo;

	@Autowired
	UserRepository userRepo;

	@Autowired
	SendMail sendMail;

	@Autowired
	TemeRepository projectRepo;

	@RequestMapping("/studentWelcome")
	public String welcomeStudent(Model model) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		MyUser curentUser = (MyUser) principal;

		model.addAttribute("mesaj", "Bine ai venit , " + curentUser.getUsername());
		model.addAttribute("student", curentUser.getUser().getStudent());

		return "/students/studentWelcome";
	}

	// asta trebuie sa ramana aici
	// in clasa asta avem declarat global id-ul profului selectat si avem nevoie de
	// el ca sa facem o cerere

	@RequestMapping("/submitRequest")
	public String request(@RequestParam(value = "teacherID") String teacherID,
			@RequestParam(value = "numeTema", required = false) String numeTema,
			RedirectAttributes redirectAttributes) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		MyUser curentUser = (MyUser) principal;
		int studentID = curentUser.getUser().getStudent().getIdstudenti();

		Teacher targetTeacher = teacherRepo.findByIdprofesori(Integer.parseInt(teacherID));
		
		
		Request request = new Request();
		request.setStatus("In asteptare (" + DateUtil.getDate() + ")");
		request.setStudent(curentUser.getUser().getStudent());
		request.setTeacher(targetTeacher);
		if (!numeTema.contains("Aplica fara tema"))
			request.setTema(projectRepo.findByNume(numeTema));
		
		List<Request> verifyRequest = new ArrayList();
		System.out.println("BEGINING -> " + verifyRequest.size());
		String errorMessage = "";
		
		System.out.println(numeTema);
		
		//verificare aplicare fara tema
		if (numeTema.contains("Aplica fara tema")) {
			System.out.println("tema neselectata");
			verifyRequest = requestRepo.findByStudentIdstudentiAndTeacherIdprofesori(studentID,
					Integer.valueOf(teacherID));
			errorMessage = "Ai facut deja o cerere catre acest profesor!";
			System.out.println("VLAD IS HERER");
		}

		//verificare aplicare cu tema
		if (!numeTema.contains("Aplica fara tema")) {
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
			sendMail.notifyTeacher(targetTeacher.getUser().getEmail(), targetTeacher.getUser().getUsername(),
					curentUser.getUser().getUsername());
			redirectAttributes.addFlashAttribute("succes", "Cererea a fost facuta cu succes!");
			return "redirect:/teacherDetails";
		}
	}

	// metoda asta aduce din VIEW id-ul profului selectat. id-ul e variabila globala
	// in clasa asta
	@RequestMapping("/teacherDetails")
	public String displayTeacher(@RequestParam(value = "teacherID") String teacherID, Model model, @RequestParam(value="variabilaTest", required = false) String variabilaTest) {
		System.out.println("TEST VLAD ->" + variabilaTest);
		Teacher targetTeacher = teacherRepo.findByIdprofesori(Integer.parseInt(teacherID));
		model.addAttribute("teacher", targetTeacher);
		model.addAttribute("projects", projectRepo.findByTeacherIdprofesori(Integer.valueOf(teacherID)));
		return "/students/viewTeacherDetails";
	}

	@RequestMapping("/completeDetailsStudent")
	public String completeDetails(Model model) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		MyUser curentUser = (MyUser) principal;

		model.addAttribute("student", curentUser.getUser().getStudent());

		return "/students/completeDetailsStudent";

	}

	@RequestMapping("/submitDetailsStudent")
	public String completeDetails(@RequestParam(value = "grupa") String grupa,
			@RequestParam("specializare") String specializare, @RequestParam(value = "username") String username,
			@RequestParam("email") String email, RedirectAttributes redirectAttributes) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		MyUser curentUser = (MyUser) principal;
		int userID = curentUser.getUser().getUserID();

		studentRepo.setDetails(specializare, grupa, curentUser.getUser().getUserID());
		userRepo.setUsername(username, userID);
		userRepo.setEmail(email, userID);

		Authentication authentication = new UsernamePasswordAuthenticationToken(principal, curentUser.getPassword());
		SecurityContextHolder.getContext().setAuthentication(authentication);

		redirectAttributes.addFlashAttribute("succesDetalii", "Detalii actualizate cu succes!");
		return "redirect:/completeDetailsStudent";
	}

}
