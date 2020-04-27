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
import com.app.model.Teacher;
import com.app.repository.RequestRepository;
import com.app.repository.StudentRepository;
import com.app.repository.TeacherRepository;
import com.app.repository.TemeRepository;
import com.app.repository.UserRepository;
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

	// metoda asta aduce din VIEW id-ul profului selectat. id-ul e variabila globala
	// in clasa asta
	@RequestMapping("/teacherDetails")
	public String displayTeacher(@RequestParam(value = "teacherID") String teacherID, Model model) {
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
			@RequestParam("specializare") String specializare,
			@RequestParam("email") String email, RedirectAttributes redirectAttributes) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		MyUser curentUser = (MyUser) principal;
		int userID = curentUser.getUser().getUserID();

		studentRepo.setDetails(specializare, grupa, curentUser.getUser().getUserID());
		userRepo.setEmail(email, userID);
		
		
	//	Authentication authentication = new UsernamePasswordAuthenticationToken(curentUser, curentUser.getPassword());
	//	SecurityContextHolder.getContext().setAuthentication(authentication);
		
		redirectAttributes.addFlashAttribute("succesDetalii", "Detalii actualizate cu succes! Autetificati-va din nou pentru a vedea modificarile!");
		return "redirect:/completeDetailsStudent";
	}
	
	
	@RequestMapping("/teachers")
	public String displayTeachers(Model model, @RequestParam(required = false, value="departament") String departament, @RequestParam(required = false, value="name") String name) {
		
		model.addAttribute("teachers", teacherRepo.findAll());
		if( departament != null ) { model.addAttribute("teachers", teacherRepo.findByDepartament(departament));
									model.addAttribute("departament",departament);
		}
		
		if(name != null) { model.addAttribute("teachers", teacherRepo.findByUserUsernameLike(name));
						   model.addAttribute("name",name);
						}
		
		return "/students/viewTeachers";
	}
	

}
