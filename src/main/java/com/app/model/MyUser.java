package com.app.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
//-4880422506348552744
public class MyUser implements UserDetails{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4880422506348552744L;


	private User user;
	

	private String ROLE_PREFIX = "ROLE_";
	
	
	public MyUser(User user) {
		this.user=user;
	}
	
	public Role getRole() {
		return this.user.getRole();
	}
	
	public int getUserID() {
		return user.getUserID();
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		 List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
	     list.add(new SimpleGrantedAuthority(ROLE_PREFIX + this.getRole()));

	        return list;
	}
	
	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

}
