package com.app;

import java.io.ObjectInputFilter.Config;

import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;

public class Initializer extends AbstractHttpSessionApplicationInitializer { 

	public Initializer() {
		super(Config.class); 
	}

}


