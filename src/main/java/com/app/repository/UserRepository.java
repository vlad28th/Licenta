package com.app.repository;

import java.io.File;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.model.Role;
import com.app.model.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	
	User findByUsername(String username);
	
	@Query("FROM users u WHERE u.role = :role")
	List<User> findByRole(@Param("role") Role role);
	
	@Query("FROM users u where u.role='TEACHER'")
	List<User> findTeachers();
	
	
	@Transactional
	@Modifying
	@Query("update users u set u.username = :username where u.userID = :userID")
	void setUsername(@Param("username") String username, @Param("userID")int userID);
	
	@Transactional
	@Modifying
	@Query("update users u set u.email = :email where u.userID = :userID")
	void setEmail(@Param("email") String email, @Param("userID")int userID);
	
	
}