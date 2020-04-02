package com.app.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity(name = "profesori")
public class Teacher implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idprofesori;
	

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userID")
	private User user;

	@OneToMany(mappedBy="teacher")
	private List<Request> cereri;
	
	@OneToMany(mappedBy="teacher")
	private List<Project> teme;

	private String departament;

	private String slots;
	
	private String comment;
	
	
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Integer getIdprofesori() {
		return idprofesori;
	}
	
	public void setIdprofesori(Integer idprofesori) {
		this.idprofesori = idprofesori;
	}
	


	public String getDepartament() {
		return departament;
	}

	public void setDepartament(String departament) {
		this.departament = departament;
	}

	public String getSlots() {
		return slots;
	}

	public void setSlots(String slots) {
		this.slots = slots;
	}
	
	

	public User getUser(){
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
