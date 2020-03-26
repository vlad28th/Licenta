package com.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
	
	@RequestMapping("/teachers")
	public String displayTeachers(Model model) {

		List<User> listaProfi = repo.findTeachers();
		model.addAttribute("users", listaProfi);
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
	
		@RequestMapping("/submitDetails")
		public String submitDetails(@RequestParam(value = "departament") String departament , @RequestParam(value = "slots") String slots) {
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			MyUser curentUser = (MyUser) principal;
			int currentUserID = curentUser.getUserID();
			
			teacherRepo.updateDetails(currentUserID, departament, slots);
			
			return "redirect:/teacherWelcome";
		}
		@RequestMapping("/completeDetails")
		public String completeDetails(Model model) {
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			MyUser curentUser = (MyUser) principal;
			model.addAttribute("mesaj","Salutare, " + curentUser.getUser().getUsername() + "\r\nAici vei completa detalii pe care studentii le vor veea atunci cand vor vrea sa faca o cerere catre tine");
			return "/teachers/completeDetails";
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
					
			System.out.println("raspunsul este catre userul ->" + studentRepo.findByIdstudenti(Integer.valueOf(studentID)).getUser().getUsername());
			System.out.println("raspunsul este -> " + status);
			
		
			requestRepo.updateRequestStatus(status, Integer.valueOf(studentID), teacherID);
			
			
			return "redirect:/viewRequests";
			
		}
		
	
		
		
}
