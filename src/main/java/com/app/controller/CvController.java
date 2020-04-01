package com.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.model.MyUser;
import com.app.repository.StudentRepository;

@Controller
public class CvController {

	@Autowired
	StudentRepository cvRepo;

	@GetMapping("/uploadCV")
	public String uploadCV(Model model) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		MyUser curentUser = (MyUser) principal;
		model.addAttribute("mesajCV", curentUser.getUsername()
				+ ", aici iti vei incarca CV-ul pe platforma. Asigura-te ca este complet si toate datele sunt corecte. Succes!");
		 
	//	model.addAttribute("cv", new CV());
		return "/students/uploadCV";

	}

	@PostMapping("/submitCV")
	public String uploadForm(@RequestParam("cv") MultipartFile cv, RedirectAttributes redirectAttributes) throws Exception {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		MyUser curentUser = (MyUser) principal;
		int userID = curentUser.getUserID();
		
		byte[] cvFromWeb = cv.getBytes();
		System.out.println(cvFromWeb.length);
		
		if( cvFromWeb.length == 0) { redirectAttributes.addFlashAttribute("nullCV", "Selecteaza un fisier");
									 return "redirect:/completeDetailsStudent";
		}
		cvRepo.setCV(cvFromWeb, userID);

		return "redirect:/studentWelcome";

	}

	@RequestMapping("/viewCV")
	public ResponseEntity<byte[]> viewCV(@RequestParam(required = false, value="userID") String userIDfromWeb, RedirectAttributes redirectAttributes) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		MyUser curentUser = (MyUser) principal;
		int userID = curentUser.getUserID();
		if(userIDfromWeb != null) userID = Integer.valueOf(userIDfromWeb);
		
		byte[] cvFromDB = cvRepo.getCV(userID);
		
			
		
		if( cvFromDB.length == 0) return new ResponseEntity("CV-ul nu este incarcat", HttpStatus.INTERNAL_SERVER_ERROR);
								
		
		HttpHeaders headers = new HttpHeaders();

		headers.setContentLength(cvFromDB.length);
		headers.setContentType(MediaType.parseMediaType("application/pdf"));
		headers.set("Content-Disposition", "inline; filename=test.pdf");
		headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
		headers.set("Expires","0");
		ResponseEntity<byte[]> responseE = new ResponseEntity<byte[]>(cvFromDB, headers, HttpStatus.OK);
		return responseE;

	}
}
