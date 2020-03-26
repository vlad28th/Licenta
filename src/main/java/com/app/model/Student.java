package com.app.model;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.springframework.web.multipart.MultipartFile;

@Entity(name="studenti")
public class Student implements MultipartFile, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4170826883625593274L;



	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idstudenti;
	


	@Lob
	private byte[] cv;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userID")
	private User user;
	
	@OneToMany(mappedBy="student")
	private List<Request> cereri;
	
	public List<Request> getCereri() {
		return cereri;
	}

	public void setCereri(List<Request> cereri) {
		this.cereri = cereri;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Integer getIdstudenti() {
		return idstudenti;
	}
	
	public void setIdstudenti(Integer idstudenti) {
		this.idstudenti = idstudenti;
	}
	public byte[] getCv() {
		return cv;
	}

	public void setCv(byte[] cv) {
		this.cv = cv;
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
