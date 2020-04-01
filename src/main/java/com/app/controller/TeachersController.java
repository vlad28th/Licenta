package com.app.controller;

import java.util.List;

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
import com.app.model.User;
import com.app.repository.RequestRepository;
import com.app.repository.StudentRepository;
import com.app.repository.TeacherRepository;
import com.app.repository.UserRepository;
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
			
			Authentication authentication = new UsernamePasswordAuthenticationToken(principal, curentUser.getPassword());
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
			return "redirect:/teacherWelcome";
		}
		@RequestMapping("/completeDetailsTeacher")
		public String completeDetails(Model model) {
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			MyUser curentUser = (MyUser) principal;
			model.addAttribute("teacher",curentUser.getUser().getTeacher());
			return "/teachers/completeDetailsTeacher";
	}
		
		@RequestMapping("/studentDetails")
		public String viewStudentDetails(@RequestParam(value="studentID") String studentID, Model model) {
			
			Student targetStudent = studentRepo.findByIdstudenti(Integer.valueOf(studentID));
			model.addAttribute("student",targetStudent);
			
			return "/teachers/viewStudentDetails";
			
		}
		
		@RequestMapping("/manageStudent")
		public String manageStudent(@RequestParam(value="status") String status, @RequestParam(value="studentID") String studentID) {
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			MyUser curentUser = (MyUser) principal;
			int teacherID = curentUser.getUser().getTeacher().getIdprofesori();
			int newSlots = Integer.valueOf(curentUser.getUser().getTeacher().getSlots()) -1;
			
			
			Student targetStudent = studentRepo.findByIdstudenti(Integer.parseInt(studentID));
			
			
			//update req status in DB
			requestRepo.updateRequestStatus(status, Integer.valueOf(studentID), teacherID);
			
			//update teacher slots 
			if(status.equalsIgnoreCase("Acceptat")) teacherRepo.updateSlots(curentUser.getUser().getUserID(), String.valueOf(newSlots));
			//send mail to Student
			//sendMail.sendReqStatus(targetStudent.getUser().getEmail(), curentUser.getUsername(),status);
			
			
			//reload teacher to model attribute
			Authentication authentication = new UsernamePasswordAuthenticationToken(principal, curentUser.getPassword());
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
			return "redirect:/viewRequests";
			
		}
		
	
		
		
}
