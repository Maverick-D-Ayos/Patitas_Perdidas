package com.patitas_perdidas.app.servicios;



import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;



@Service
public class MailServicio {
		@Autowired(required = true)
		private JavaMailSender sender;


		@Async
		public void enviarMail(String cuerpo, String titulo, String mail) {
			SimpleMailMessage mensaje = new SimpleMailMessage();

			mensaje.setTo(mail);
			mensaje.setFrom("nuriaromeu26@gmail.com");
			mensaje.setSubject(titulo);
			mensaje.setText(cuerpo);


	    sender.send(mensaje);
	}
	}


		
		


	 

