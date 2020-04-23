package com.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.model.MyUser;
import com.app.model.Student;
import com.app.model.Teacher;
import com.app.repository.RequestRepository;
import com.app.repository.StudentRepository;
import com.app.repository.TeacherRepository;
import com.app.repository.TemeRepository;
import com.app.repository.UserRepository;
import com.app.services.DateUtil;
import com.app.services.SendMail;

@Controller
public class TeachersController {
	
	
	@Autowired
	UserRepository repo;
	@Autowired
	TeacherRepository teacherRepo;
	
	@Autowired
	StudentRepository studentRepo;
	
	@Autowired
	RequestRepository requestRepo;
	
	@Autowired
	TemeRepository projectRepo;
	
	@Autowired
	SendMail sendMail;
	
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
	
	
	@RequestMapping("/teacherWelcome")
    public String welcomeTeacher(Model model) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		MyUser curentUser = (MyUser)principal;
		
		model.addAttribute("teacher", new Teacher());
        model.addAttribute("mesaj", "Bine ai venit, " + curentUser.getUsername() );
        model.addAttribute("teme", projectRepo.findByTeacherIdprofesori(curentUser.getUser().getTeacher().getIdprofesori()));
        return "/teachers/teacherWelcome";
    }
	/*
		@GetMapping("/teacherWelcome")
		public String addUser(Model model) {
			model.addAttribute("teacher", new Teacher());
			return "/teacherWelcome";
		}
*/		
	
		@RequestMapping("/submitDetailsTeacher")
		public String submitDetails(@RequestParam(value = "departament") String departament , @RequestParam(value = "slots") String slots, @RequestParam("comment") String comment) {
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			MyUser curentUser = (MyUser) principal;
			int currentUserID = curentUser.getUserID();
			
			
			teacherRepo.updateDetails(currentUserID, departament, slots);
			if(comment.length() != 0 ) teacherRepo.updateComment(currentUserID, comment);
			
			//Authentication authentication = new UsernamePasswordAuthenticationToken(principal, curentUser.getPassword());
			//SecurityContextHolder.getContext().setAuthentication(authentication);
			
			return "redirect:/teacherWelcome";
		}
		@RequestMapping("/completeDetailsTeacher")
		public String completeDetails(Model model) {
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			MyUser curentUser = (MyUser) principal;
			model.addAttribute("teacher",curentUser.getUser().getTeacher());
			model.addAttribute("teme", projectRepo.findByTeacherIdprofesori(curentUser.getUser().getTeacher().getIdprofesori()));
			return "/teachers/completeDetailsTeacher";
	}
		
		@RequestMapping("/studentDetails")
		public String viewStudentDetails(@RequestParam(value="studentID") String studentID, Model model) {
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			MyUser curentUser = (MyUser) principal;
			model.addAttribute("teme", projectRepo.findByTeacherIdprofesori(curentUser.getUser().getTeacher().getIdprofesori()));
			
			Student targetStudent = studentRepo.findByIdstudenti(Integer.valueOf(studentID));
			
			model.addAttribute("student",targetStudent);
			model.addAttribute("cereri",requestRepo.findByStudentIdstudentiAndTeacherIdprofesori(targetStudent.getIdstudenti(), curentUser.getUser().getTeacher().getIdprofesori()));
			
			return "/teachers/viewStudentDetails";
			
		}
		
		
		
	
		
		
}
