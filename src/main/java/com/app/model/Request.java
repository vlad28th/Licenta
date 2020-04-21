package com.app.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity(name = "cereri")
public class Request {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idcereri;
	


	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idstudenti")
	private Student student;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idprofesori")
	private Teacher teacher;

	private String status;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idteme")
	private Tema tema;
	
	
	@OneToMany(mappedBy = "request")
	private List<Message> message;
	
	private String data;
	
	
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public Tema getTema() {
		return tema;
	}

	public void setTema(Tema tema) {
		this.tema = tema;
	}

	public int getIdcereri() {
		return idcereri;
	}

	public void setIdcereri(int idcereri) {
		this.idcereri = idcereri;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
