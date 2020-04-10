package com.app.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "mesaje")
public class Message {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idmesaje;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idcereri")
	Request request;
	
	String content;
	
	
	
	public int getIdmesaje() {
		return idmesaje;
	}

	public void setIdmesaje(int idmesaje) {
		this.idmesaje = idmesaje;
	}

	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	
	
	
}
