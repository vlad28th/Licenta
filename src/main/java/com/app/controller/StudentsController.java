package com.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.model.MyUser;
import com.app.model.Request;
import com.app.model.Teacher;
import com.app.repository.RequestRepository;
import com.app.repository.TeacherRepository;

@Controller
public class StudentsController {

	
	@Autowired 
	TeacherRepository teacherRepo;
	
	@Autowired
	RequestRepository requestRepo;

	
	@RequestMapping("/studentWelcome")
    public String welcomeStudent(Model model) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		MyUser curentUser = (MyUser)principal;
		
        model.addAttribute("mesaj", "Bine ai venit , " + curentUser.getUsername() );
        return "/students/studentWelcome";
    }
	
	//asta trebuie sa ramana aici 
	//in clasa asta avem declarat global id-ul profului selectat si avem nevoie de el ca sa facem o cerere
	
//	<!--  input type="hidden" th:name="teacherID" th:value="${teacher.idprofesori}"-->
	@RequestMapping("/submitRequest")
	public String request(@RequestParam(value = "teacherID") String teacherID, RedirectAttributes redirectAttributes) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		MyUser curentUser = (MyUser)principal;
		int studentID = curentUser.getUser().getStudent().getIdstudenti();
		
		Teacher targetTeacher = teacherRepo.findByIdprofesori(Integer.parseInt(teacherID));
		
		System.out.println("vreau sa fac cerere catre profesorul -> " + targetTeacher.getUser().getUsername());
		Request request = new Request();
		request.setStatus("TRIMISA");
		request.setStudent(curentUser.getUser().getStudent());
		request.setTeacher(targetTeacher);
		
		Request verifyRequest = requestRepo.findByStudentIdstudentiAndTeacherIdprofesori(studentID, Integer.valueOf(teacherID));
		if(verifyRequest != null) {
				redirectAttributes.addFlashAttribute("errorMessage", "Ai facut deja o cerere catre acest profesor!");
				return "redirect:/teachers";
		}
		else requestRepo.save(request);
		
		return "redirect:/teachers";
	}
	
	
	
	// metoda asta aduce din VIEW id-ul profului selectat. id-ul e variabila globala in clasa asta
	@RequestMapping("/teacherDetails")
	public String displayTeacher(@RequestParam(value = "teacherID") String teacherID, Model model) {
		
		Teacher targetTeacher = teacherRepo.findByIdprofesori(Integer.parseInt(teacherID));
		model.addAttribute("teacher",targetTeacher);
		return "/students/viewTeacherDetails";
	}

}
