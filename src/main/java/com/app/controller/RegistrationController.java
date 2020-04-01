package com.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.app.model.User;
import com.app.repository.UserRepository;
import com.app.services.SendMail;

@Controller
public class RegistrationController
{	
	
	@Autowired
	SendMail sendMail;
	
	@Autowired
	UserRepository userRepo;
	
    @GetMapping("/registration")
    public String registrationForm(Model model) {
        model.addAttribute("user", new User());
        return "/registration";
    }

    @PostMapping("/registration")
    public String handleRegistration(User user) {
    	
        userRepo.save(user);
        sendMail.sendCredentials(user.getEmail(),user.getUsername(),user.getPassword());
        return "redirect:/login";
    }
}