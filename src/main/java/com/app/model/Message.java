package com.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.app.services.DateUtil;

@Entity(name = "mesaje")
public class Message {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idmesaje;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idcereri")
	private Request request;
	
	private String content;
	
	private String direction;
	
	private String date;
	
	
	
	public String getDate() {
		return date;
	}

	public void setDate() {
		this.date = DateUtil.getDate();
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String messageDirection) {
		this.direction = messageDirection;
	}

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
