package com.app.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Transient;


@Entity(name="users")
public class User implements Serializable{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer userID;
	private String username;
	private String email;
	
	@Enumerated(EnumType.STRING)
	private Role role;

	private String password;
	
	
	@OneToOne(mappedBy = "user")
	private Teacher teacher;
	
	@OneToOne(mappedBy ="user")
	private Student student;
	
	@Transient
	private String confirmPassword;
	
	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public Role getRole() {
		return role;
	}
	
	public void setRole(String role) {
		if(role.equals("TEACHER")) this.role = Role.TEACHER;
		if(role.equals("STUDENT")) this.role = Role.STUDENT;
	}


	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getUserID() {
		return userID;
	}

	public void setId(Integer id) {
		this.userID = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	

}