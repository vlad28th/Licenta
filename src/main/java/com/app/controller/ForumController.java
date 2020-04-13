package com.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.model.Message;
import com.app.model.MyUser;
import com.app.model.Request;
import com.app.model.Role;
import com.app.model.Student;
import com.app.repository.MessageRepository;
import com.app.repository.RequestRepository;
import com.app.repository.StudentRepository;
import com.app.repository.TeacherRepository;
import com.app.repository.TemeRepository;
import com.app.repository.UserRepository;
import com.app.services.SendMail;

@Controller
public class ForumController {


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
	
	@Autowired
	MessageRepository messageRepo;
	
	@RequestMapping("/viewRequest")
	public String redirect(@RequestParam("idCerere") String idCerere, @RequestParam(value="studentID", required = false) String studentID, RedirectAttributes redirectAttributes) {
	Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	MyUser curentUser = (MyUser)principal;
		redirectAttributes.addAttribute("idCerere", idCerere);
		if(curentUser.getRole().equals(Role.STUDENT)) return "redirect:/studentViewRequest";
		
		if(curentUser.getRole().equals(Role.TEACHER)) { 
									redirectAttributes.addAttribute("studentID", studentID);
									return "redirect:/teacherViewRequest";
		}
    return "";
	}
	
	@RequestMapping("/studentViewRequest")
	public String studentViewRequest(Model model, @RequestParam("idCerere") String idCerere) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		MyUser curentUser = (MyUser) principal;
		
		
		model.addAttribute("cerere",requestRepo.findByIdcereri(Integer.valueOf(idCerere)));
		model.addAttribute("mesaje", messageRepo.findByRequestIdcereri(Integer.valueOf(idCerere)));
		
		return "/students/viewRequest";
	}
	
	@RequestMapping("/teacherViewRequest")
	public String teacherViewRequest(Model model, @RequestParam("idCerere") String idCerere, @RequestParam("studentID") String studentID) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		MyUser curentUser = (MyUser) principal;
		
		Student targetStudent = studentRepo.findByIdstudenti(Integer.valueOf(studentID));
		model.addAttribute("student",targetStudent);
		model.addAttribute("cerere",requestRepo.findByIdcereri(Integer.valueOf(idCerere)));
		model.addAttribute("mesaje", messageRepo.findByRequestIdcereri(Integer.valueOf(idCerere)));
		
		return "/teachers/viewRequest";
	}
	
	
	
	@RequestMapping("/submitMessage")
	public String submitMessage(@RequestParam("mesaj") String mesaj, @RequestParam("idCerere") String idCerere, RedirectAttributes redirectAttributes) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		MyUser curentUser = (MyUser) principal;
		
		
		Request targetRequest = requestRepo.findByIdcereri(Integer.valueOf(idCerere));
		
		Message messageToSave = new Message();
		String messageDirection="";
		if(curentUser.getRole().equals(Role.STUDENT)) messageDirection = "toMaster";
		else messageDirection = "toSlave";
		
		
		messageToSave.setContent("HAI NOROC!");
		messageToSave.setRequest(targetRequest);
		messageToSave.setContent(mesaj);
		messageToSave.setDirection(messageDirection);
		
		messageRepo.save(messageToSave);
		
		redirectAttributes.addAttribute("idCerere",idCerere);
		redirectAttributes.addAttribute("studentID", targetRequest.getStudent().getIdstudenti());
		return "redirect:/viewRequest";
	}
	
	
}
