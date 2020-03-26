package com.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.app.model.MyUser;
import com.app.model.Role;
import com.app.model.Teacher;
import com.app.model.User;
import com.app.repository.TeacherRepository;

@Controller
public class WelcomeController {
	
	
	@Autowired
	TeacherRepository repo;
	
	
	 @RequestMapping(value="/welcome" , method = RequestMethod.GET)
	    public String welcome(User user) {
	    	Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    	MyUser curentUser = (MyUser)principal;
	    	
	    	if(curentUser.getRole().equals(Role.STUDENT))return "redirect:/studentWelcome";
	    	if(curentUser.getRole().equals(Role.TEACHER))return "redirect:/teacherWelcome";
	        return "";
	    }
}