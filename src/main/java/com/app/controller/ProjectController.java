package com.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.model.MyUser;
import com.app.repository.ProjectsRepository;

@Controller
public class ProjectController {
	
	@Autowired
	ProjectsRepository projectRepo;
	
	@PostMapping("/x")
	public String uploadProject(@RequestParam("tema") MultipartFile tema, RedirectAttributes redirectAttributes) throws Exception{
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		MyUser curentUser = (MyUser) principal;
		int userID = curentUser.getUserID();
		
		
		System.out.println("VLAD IS ALREADY HERE" + tema);
		byte[] projectFromWeb = tema.getBytes();
		
		
		if( projectFromWeb.length == 0) { redirectAttributes.addFlashAttribute("nullProject", "Selecteaza un fisier");
									 return "redirect:/completeDetailsTeacher";
		}
		
		projectRepo.setProject(projectFromWeb, userID);
		
		return "redirect:/teacherWelcome";
	}
	

}
