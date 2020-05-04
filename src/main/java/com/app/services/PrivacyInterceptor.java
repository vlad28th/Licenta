package com.app.services;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.app.model.MyUser;
import com.app.model.Request;
import com.app.model.Role;
import com.app.repository.RequestRepository;

public class PrivacyInterceptor extends HandlerInterceptorAdapter{
	
	@Autowired
	RequestRepository requestRepo;


	    public boolean preHandle(
	            HttpServletRequest request,
	            HttpServletResponse response,
	            Object handler) throws Exception {
	    	
	    	
	    	Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			MyUser curentUser = (MyUser) principal;
			String loggedUserID = curentUser.getUser().getUserID().toString();
	    	
			if(request.getParameter("idCerere")==null) return false;
			
			int requestID = Integer.valueOf(request.getParameter("idCerere"));
			Request studentRequest = requestRepo.findByIdcereri(requestID);
			
			String idToVerify="";
			if(curentUser.getUser().getRole().equals(Role.STUDENT))
				 idToVerify = studentRequest.getStudent().getUser().getUserID().toString();
			
			else idToVerify = studentRequest.getTeacher().getUser().getUserID().toString();
			
			if(loggedUserID.equals(idToVerify)) return true;
			else response.sendRedirect("/welcome");
	    		
			return true;

	    }
}
