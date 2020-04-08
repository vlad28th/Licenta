package com.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.app.model.MyUser;
import com.app.model.Request;
import com.app.model.Role;
import com.app.repository.RequestRepository;
import com.app.repository.TemeRepository;

@Controller
public class RequestController {
	
	@Autowired
	RequestRepository requestRepo;
	
	@Autowired
	TemeRepository projectRepo;
	
	
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
	
	

}
