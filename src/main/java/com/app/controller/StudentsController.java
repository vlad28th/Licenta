package com.app.controller;

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
import com.app.repository.UserRepository;
import com.app.services.DateUtil;

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

//	<!--  input type="hidden" th:name="teacherID" th:value="${teacher.idprofesori}"-->
	@RequestMapping("/submitRequest")
	public String request(@RequestParam(value = "teacherID") String teacherID, RedirectAttributes redirectAttributes) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		MyUser curentUser = (MyUser) principal;
		int studentID = curentUser.getUser().getStudent().getIdstudenti();

		Teacher targetTeacher = teacherRepo.findByIdprofesori(Integer.parseInt(teacherID));

		Request request = new Request();
		request.setStatus("In asteptare (" + DateUtil.getDate() + ")");
		request.setStudent(curentUser.getUser().getStudent());
		request.setTeacher(targetTeacher);

		Request verifyRequest = requestRepo.findByStudentIdstudentiAndTeacherIdprofesori(studentID,
				Integer.valueOf(teacherID));
		if (verifyRequest != null) {
			redirectAttributes.addFlashAttribute("errorMessage", "Ai facut deja o cerere catre acest profesor!");
			return "redirect:/teachers";
		} else
			requestRepo.save(request);

		return "redirect:/teachers";
	}

	// metoda asta aduce din VIEW id-ul profului selectat. id-ul e variabila globala
	// in clasa asta
	@RequestMapping("/teacherDetails")
	public String displayTeacher(@RequestParam(value = "teacherID") String teacherID, Model model) {

		Teacher targetTeacher = teacherRepo.findByIdprofesori(Integer.parseInt(teacherID));
		model.addAttribute("teacher", targetTeacher);
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
			@RequestParam("specializare") String specializare,
			@RequestParam(value = "username") String username, @RequestParam("email") String email) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		MyUser curentUser = (MyUser) principal;
		int userID = curentUser.getUser().getUserID();

		studentRepo.setDetails(specializare, grupa, curentUser.getUser().getUserID());
		userRepo.setUsername(username, userID);
		userRepo.setEmail(email, userID);
		
		
		Authentication authentication = new UsernamePasswordAuthenticationToken(principal, curentUser.getPassword());
		SecurityContextHolder.getContext().setAuthentication(authentication);

		return "redirect:/studentWelcome";
	}

}
