package com.app.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.model.Request;
import com.app.model.Tema;

public interface RequestRepository extends JpaRepository<Request, Integer> {
	
	
	List<Request> findByTeacherIdprofesori(int teacherID);
	
	List<Request> findByStudentIdstudenti(int studentID);
	
	Request findByIdcereri(int requestID);
	
	List<Request> findByStudentIdstudentiAndTeacherIdprofesori(int studentID, int teacherID);
	
	@Transactional
	@Modifying
	@Query("update studenti s set s.cv = :file where s.user.userID = :userID")
	void setCV(@Param("file") byte[] file, @Param("userID") int userID);
	
	@Transactional
	@Modifying
	@Query("update cereri c set c.status =:status where c.teacher.idprofesori=:teacherID and c.student.idstudenti=:studentID and c.tema is null")
	public void updateRequestStatus(@Param("status") String status, @Param("studentID") int studentID, @Param("teacherID") int teacherID);
	
	
	@Transactional
	@Modifying
	@Query("update cereri c set c.status =:status where c.teacher.idprofesori=:teacherID and c.tema.idteme =:idTema and c.student.idstudenti=:studentID")
	public void updateRequestStatusWithProject(@Param("status") String status, @Param("studentID") int studentID, @Param("teacherID") int teacherID, @Param("idTema") int idTema);
	
	
	public Request findByTemaNume(String numeTema);
	
	public Request findByStudentIdstudentiAndTemaNume(int studentID, String numeTema);
	
	public Request findByStudentIdstudentiAndTeacherIdprofesoriAndTema(int studentID, int teacherID, Tema project);
}
