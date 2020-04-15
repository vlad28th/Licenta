package com.app.model;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import org.springframework.web.multipart.MultipartFile;

@Entity(name="proiecte")
public class Tema implements MultipartFile{


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int idteme;
	
	@Lob
	private byte[] tema;
	
	private String nume;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idprofesori")
	private Teacher teacher;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idstudenti")
	private Student student;
	
	
	
	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public String getNume() {
		return nume;
	}

	public void setNume(String nume) {
		this.nume = nume;
	}

	public byte[] getTema() {
		return tema;
	}

	public void setTema(byte[] tema) {
		this.tema = tema;
	}
	
	public int getIdteme() {
		return idteme;
	}

	public void setIdteme(int idteme) {
		this.idteme = idteme;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getOriginalFilename() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getContentType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public long getSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public byte[] getBytes() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void transferTo(File dest) throws IOException, IllegalStateException {
		// TODO Auto-generated method stub
		
	}
	
	
}
