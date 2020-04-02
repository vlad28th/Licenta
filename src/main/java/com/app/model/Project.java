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

@Entity(name="teme")
public class Project implements MultipartFile{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int idteme;
	
	@Lob
	private byte[] proiect;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idprofesori")
	private Teacher teacher;
	
	
	public int getIdteme() {
		return idteme;
	}

	public void setIdteme(int idteme) {
		this.idteme = idteme;
	}

	public byte[] getProiect() {
		return proiect;
	}

	public void setProiect(byte[] proiect) {
		this.proiect = proiect;
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
