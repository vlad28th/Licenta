package com.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.model.MyUser;
import com.app.model.Tema;
import com.app.repository.TemeRepository;

@Controller
public class TemeController {
	
	@Autowired
	TemeRepository projectRepo;
	
	@PostMapping("/submitTema")
	public String uploadProject(@RequestParam("tema") MultipartFile tema, @RequestParam("numeTema") String numeTema, RedirectAttributes redirectAttributes) throws Exception{
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		MyUser curentUser = (MyUser) principal;
		int teacherID = curentUser.getUser().getTeacher().getIdprofesori();
		
		System.out.println(numeTema);
		
		byte[] projectFromWeb = tema.getBytes();
		System.out.println(projectFromWeb.length);
		
		
		
		if( projectFromWeb.length == 0) { redirectAttributes.addFlashAttribute("nullProject", "Selecteaza un fisier");
									 return "redirect:/completeDetailsTeacher";
		}
		
		if(projectRepo.findByNume(numeTema) == null)
		{
			Tema projectToSave = new Tema();
			projectToSave.setTeacher(curentUser.getUser().getTeacher());
			projectToSave.setTema(projectFromWeb);
			projectToSave.setNume(numeTema);
			projectRepo.save(projectToSave);
		}
		else 
			projectRepo.updateTema(projectFromWeb, teacherID,numeTema);
		
		return "redirect:/teacherWelcome";
	}
	
	
	
	@RequestMapping("/viewProject")
	public ResponseEntity<byte[]> viewCV(@RequestParam("numeTema") String numeTema) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		MyUser curentUser = (MyUser) principal;
		//int teacherID = curentUser.getUser().getTeacher().getIdprofesori();
		
		
		System.out.println(projectRepo.findByNume(numeTema).getTema());
		byte[] projectFromDB = projectRepo.findByNume(numeTema).getTema();
		
	
		
		if( projectFromDB.length == 0) return new ResponseEntity("Tema nu este incarcata", HttpStatus.INTERNAL_SERVER_ERROR);
								
		
		HttpHeaders headers = new HttpHeaders();

		headers.setContentLength(projectFromDB.length);
		headers.setContentType(MediaType.parseMediaType("application/pdf"));
		headers.set("Content-Disposition", "inline; filename=test.pdf");
		headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
		headers.set("Expires","0");
		ResponseEntity<byte[]> responseE = new ResponseEntity<byte[]>(projectFromDB, headers, HttpStatus.OK);
		return responseE;

	}
	

}