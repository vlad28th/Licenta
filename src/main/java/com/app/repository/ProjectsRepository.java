package com.app.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.model.Project;

public interface ProjectsRepository extends JpaRepository<Project, Integer>{
	
	
	public List<Project> findByTeacherIdprofesori(int teacherID);
	
	
	@Transactional
	@Modifying
	@Query("update teme t set t.proiect = :file where t.teacher.user.userID = :userID")
	void setProject(@Param("file") byte[] file, @Param("userID") int userID);
	
}
