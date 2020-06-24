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
	
	
	@Query("from cereri c where c.teacher.idprofesori =:teacherID and c.student.user.username like %:studentName%")
	public List<Request> findByTeacherIdprofesoriAndStudentUserUsernameLike(int teacherID,String studentName);
	
	@Query("from cereri c where c.student.idstudenti=:studentID and c.teacher.user.username like %:teacherName%")
	public List<Request> findByStudentIdstudentiAndTeacherUserUsernameLike(int studentID,String teacherName);
	
	
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
	@Query("update cereri c set c.status =:status where c.idcereri=:requestID")
	public void updateStatus(@Param("status") String status, @Param("requestID") int requestID);
	
	@Transactional
	@Modifying
	@Query("update cereri c set c.status =:status where c.teacher.idprofesori=:teacherID and c.tema.idteme =:idTema and c.student.idstudenti=:studentID")
	public void updateRequestStatusWithProject(@Param("status") String status, @Param("studentID") int studentID, @Param("teacherID") int teacherID, @Param("idTema") int idTema);
	
	@Query("from cereri c where c.teacher.idprofesori =:teacherID and c.status like %:status%")
	public List<Request> findByTeacherIdprofesoriAndStatusLike(int teacherID,  String status);
	
	@Query("from cereri c where c.teacher.idprofesori =:teacherID and c.confirmed=1")
	public List<Request> findByTeacherIdprofesoriAndConfirmed(int teacherID);
	
	@Query("from cereri c where c.student.idstudenti =:studentID and c.status like %:status%")
	public List<Request> findByStudentIdstudentiAndStatusLike(int studentID,  String status);
	
	@Query("from cereri c where c.student.idstudenti =:studentID and c.confirmed = 1 ")
	public List<Request> findByStudentIdstudentiAndConfirmed(int studentID);
	
	public Request findByTemaNume(String numeTema);
	
	public List<Request> findByStudentIdstudentiAndTemaNume(int studentID, String numeTema);
	
	public List<Request> findByStudentIdstudentiAndTeacherIdprofesoriAndTemaNume(int studentID, int teacherID, String projectName);
	
	@Transactional
	@Modifying
	@Query("update cereri c set c.confirmed = 1 where c.idcereri=:requestID")
	public void setConfirmedByStudent(@Param("requestID") int requestID);
}
