package com.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.app.model.MyUser;
import com.app.model.User;
import com.app.repository.UserRepository;

@Service
public class MyUserDetailsService implements UserDetailsService{
	
	@Autowired
	private UserRepository userRepo;
	
	//this method will actually search by the name, not username
	@Override
	public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
		
		User user = userRepo.findByUsername(name);
		System.out.println("am gasit usernamul cu numele : " + user.getUsername());
		System.out.println("are rolul " + user.getRole());
		
		if(user==null) throw new UsernameNotFoundException("User not found!");
		
		
		return new MyUser(user);
	}

}
