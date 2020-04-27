package com.app.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.model.Tema;

public interface TemeRepository extends JpaRepository<Tema, Integer>{
	
	
	public List<Tema> findByTeacherIdprofesori(int teacherID);
	
	public Tema findByIdteme(int projectID);
	
	@Transactional
	@Modifying
	@Query("UPDATE proiecte t SET t.tema =:file WHERE t.teacher.idprofesori =:teacherID AND t.nume =:numeTema")
	public void updateTema(@Param("file") byte[] file, @Param("teacherID") int teacherID, @Param("numeTema") String numeTema);
	
	@Transactional
	@Modifying
	@Query("UPDATE proiecte t SET t.tema =:file WHERE t.idteme =:projectID")
	public void updateStudentProject(@Param("file") byte[] file, @Param("projectID") int projectID);
	
	@Query("select tema from proiecte t where t.teacher.idprofesori = :teacherID")
	byte[] getProject(@Param("teacherID") int teacherID);
	
	
	public Tema findByNume(String projectName);
	
	public Tema findByNumeAndStudentIdstudenti(String nume, int studentID);
	
	public Tema findByNumeAndTeacherIdprofesori(String nume, int teacherID);
	
}
