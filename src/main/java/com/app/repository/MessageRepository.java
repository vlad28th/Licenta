package com.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.model.Message;

public interface MessageRepository extends JpaRepository<Message, Integer>{
	
	public List<Message> findByRequestIdcereri(int idCerere);

}
