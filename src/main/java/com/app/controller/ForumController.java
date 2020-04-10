package com.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.model.Message;
import com.app.model.MyUser;
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
	
	
	
	@RequestMapping("/test")
	public String displayStudentForum(Model model, @RequestParam("idCerere") String idCerere) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		MyUser curentUser = (MyUser) principal;
		
		Message mesaj = new Message();
		
		mesaj.setContent("HAI NOROC!");
		mesaj.setRequest(requestRepo.findByIdcereri(Integer.valueOf(idCerere)));
		
		model.addAttribute("mesaj",mesaj);
		
		return "/students/forum";
	}
	
	
}
