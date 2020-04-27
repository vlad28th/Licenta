package com.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.model.MyUser;
import com.app.model.Student;
import com.app.model.Teacher;
import com.app.repository.RequestRepository;
import com.app.repository.StudentRepository;
import com.app.repository.TeacherRepository;
import com.app.repository.TemeRepository;
import com.app.repository.UserRepository;
import com.app.services.SendMail;

@Controller
public class TeachersController {
	
	
	@Autowired
	UserRepository repo;
	@Autowired
	TeacherRepository teacherRepo;
	
	@Autowired
	StudentRepository studentRepo;
	
	@Autowired
	RequestRepository requestRepo;
	
	@Autowired
	TemeRepository projectRepo;
	
	@Autowired
	SendMail sendMail;

	
	@RequestMapping("/teacherWelcome")
    public String welcomeTeacher(Model model) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		MyUser curentUser = (MyUser)principal;
		
		model.addAttribute("teacher", curentUser.getUser().getTeacher());
        return "/teachers/teacherWelcome";
    }
	
		@RequestMapping("/submitDetailsTeacher")
		public String submitDetails(@RequestParam(value = "departament") String departament , @RequestParam(value = "slots") String slots, @RequestParam("comment") String comment,
				RedirectAttributes redirectAttributes) {
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			MyUser curentUser = (MyUser) principal;
			int currentUserID = curentUser.getUserID();
			
			
			teacherRepo.updateDetails(currentUserID, departament, slots);
			if(comment.length() != 0 ) teacherRepo.updateComment(currentUserID, comment);
			
			//Authentication authentication = new UsernamePasswordAuthenticationToken(principal, curentUser.getPassword());
			//SecurityContextHolder.getContext().setAuthentication(authentication);
			redirectAttributes.addFlashAttribute("succes","Detalii actualizate cu succes! Acestea vor putea fi vizulizate dupa un nou login!");
			return "redirect:/completeDetailsTeacher";
		}
		@RequestMapping("/completeDetailsTeacher")
		public String completeDetails(Model model) {
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			MyUser curentUser = (MyUser) principal;
			model.addAttribute("teacher",curentUser.getUser().getTeacher());
			model.addAttribute("teme", projectRepo.findByTeacherIdprofesori(curentUser.getUser().getTeacher().getIdprofesori()));
			return "/teachers/completeDetailsTeacher";
	}
		
		@RequestMapping("/studentDetails")
		public String viewStudentDetails(@RequestParam(value="studentID") String studentID, Model model) {
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			MyUser curentUser = (MyUser) principal;
			model.addAttribute("teme", projectRepo.findByTeacherIdprofesori(curentUser.getUser().getTeacher().getIdprofesori()));
			
			Student targetStudent = studentRepo.findByIdstudenti(Integer.valueOf(studentID));
			
			model.addAttribute("student",targetStudent);
			model.addAttribute("cereri",requestRepo.findByStudentIdstudentiAndTeacherIdprofesori(targetStudent.getIdstudenti(), curentUser.getUser().getTeacher().getIdprofesori()));
			
			return "/teachers/viewStudentDetails";
			
		}
		
		@RequestMapping("/deleteProject")
		public String deleteProject(@RequestParam("numeTema") String numeTema, RedirectAttributes redirectAttributes) {
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			MyUser curentUser = (MyUser) principal;
			
			
			try {
			projectRepo.delete(projectRepo.findByNumeAndTeacherIdprofesori(numeTema, curentUser.getUser().getTeacher().getIdprofesori()));
			redirectAttributes.addFlashAttribute("successDeleteProject","Tema a fost stearsa cu succes");
			return "redirect:/completeDetailsTeacher";
			}
			catch (Exception e) {
				redirectAttributes.addFlashAttribute("failedDeleteProject","Tema nu a putut fi stearsa");
				return "redirect:/completeDetailsTeacher";
			}
			
		}
}
