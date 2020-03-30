package com.app.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {

	
	
	public static String getDate() {
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
	LocalDateTime date = LocalDateTime.now();
	return dtf.format(date);
	
	}
}
