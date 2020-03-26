package com.app.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.model.Student;

public interface StudentRepository extends JpaRepository<Student, Integer> {

	@Transactional
	@Modifying
	@Query("update studenti c set c.cv = :file where c.user.userID = :userID")
	void setCV(@Param("file") byte[] file, @Param("userID") int userID);

	@Query("select cv from studenti c where c.user.userID = :userID")
	byte[] getCV(@Param("userID") int userID);

	public Student findByIdstudenti(int studentID);
	
	
	@Transactional
	@Modifying
	@Query("update studenti s set s.specializare = :specializare, s.grupa=:grupa where s.user.userID = :userID")
	void setDetails(@Param("specializare") String specializare, @Param("grupa") String grupa, @Param("userID") int userID);

}
