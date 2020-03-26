package com.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.app.model.MyUser;
import com.app.model.User;
import com.app.repository.UserRepository;

@Controller("/home")
public class HomeController {
	

	@GetMapping("/home")
	public String home(Model model) {
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		MyUser curentUser = (MyUser)principal;
		
		model.addAttribute("mesajIntampinare", "Bun venit pe platforma de practica" + curentUser.getUsername() );
		return "home";
	}
	
	
}
