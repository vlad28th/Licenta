package com.app.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.model.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, Integer>{
	
	
	 @Transactional
	 @Modifying
	@Query("update profesori u set u.departament =:departament , u.slots = :slots where u.user.userID = :userID")
	public void updateDetails(@Param("userID") int userID , @Param("departament") String departament, @Param("slots") String slots);
	
	@Query("FROM profesori u WHERE u.user.userID = :userID ")
	public List<Teacher> findByUserID(@Param("userID") int userID);
	
	
	public Teacher findByIdprofesori(int teacherID);
	
	
}
