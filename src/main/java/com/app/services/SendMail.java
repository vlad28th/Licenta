package com.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class SendMail {
	
	String content;
	
	@Autowired
    private JavaMailSender javaMailSender;
	
	public void sendEmail(String destination, String username, String password) {
		
	
		content=String.format("Contul tau a fost creat cu succces!\r\n Username -> %s\r\n Parola ta este -> %s",username,password);
		
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(destination);
        msg.setSubject("no-reply@licenta2020");
        msg.setText(content);

        javaMailSender.send(msg);

    }


}
