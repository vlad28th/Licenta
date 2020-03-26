package com.app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

@Configuration
@EnableWebSecurity
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;

	@Bean
	public AuthenticationProvider authProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());

		return provider;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/css/**", "/js/**").permitAll()
				.antMatchers("/login").permitAll()
				.antMatchers("/registration").permitAll()
				.antMatchers("/userWelcome").hasAnyAuthority("ROLE_STUDENT")
				.antMatchers("/welcome").hasAnyAuthority("ROLE_STUDENT","ROLE_TEACHER")
				.antMatchers("/teacherWelcome").hasAnyAuthority("ROLE_TEACHER")
				.antMatchers("/studentWelcome").hasAnyAuthority("ROLE_STUDENT")
				.antMatchers("/teachers").hasAnyAuthority("ROLE_STUDENT")
				.anyRequest().authenticated()
				.and()
				.formLogin().loginPage("/login").defaultSuccessUrl("/welcome",true)
				.and()
				.logout().logoutSuccessUrl("/login");
	}
}
