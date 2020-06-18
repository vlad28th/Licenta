package com.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
	
	
	BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
    @GetMapping("/registration")
    public String registrationForm(Model model) {
        model.addAttribute("user", new User());
        return "/registration";
    }

    @PostMapping("/registration")
    public String handleRegistration(User user, RedirectAttributes redirectAttributes) {
    	if(userRepo.findByUsername(user.getUsername()) != null) {
    		redirectAttributes.addFlashAttribute("usernameExists", "?");
    		return "redirect:/registration";
    	}
    	
    	/*
    	if(userRepo.findByEmail(user.getEmail()) != null) {
    		redirectAttributes.addFlashAttribute("emailExists", "?");
    		return "redirect:/registration";
    	}
        */
    	String password = user.getPassword();
    	String encryptedPassword = passwordEncoder.encode(password);
    	
    
        try {
        user.setPassword(encryptedPassword);
        userRepo.save(user);
        sendMail.sendCredentials(user.getEmail(),user.getUsername(),password);
        }
        catch(Exception e) {
        	 redirectAttributes.addFlashAttribute("incorectEmail","?");
        	 return "redirect:/registration";
        }
        
        redirectAttributes.addFlashAttribute("succes","#");
        return "redirect:/login";
    }
}