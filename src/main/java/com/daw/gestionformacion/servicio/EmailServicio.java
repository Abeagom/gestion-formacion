package com.daw.gestionformacion.servicio;

import java.time.format.DateTimeFormatter;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.daw.gestionformacion.modelo.Practica;

@Service
public class EmailServicio {

	private JavaMailSender mailSender;
	
	public EmailServicio (JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}
	
	public void enviarEmailAsignacion(Practica practica) {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        
        mensaje.setFrom("PONER CORREO DE ORIGEN");
        
        // Destinatario
        mensaje.setTo(practica.getAlumno().getEmail());
        
        // Asunto
        mensaje.setSubject("Asignación de Prácticas - " + practica.getEmpresa().getNombre());
        
        String fechaFormateada = practica.getFechaInicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        // Cuerpo del mensaje
        String cuerpo = "Estimado/a " + practica.getAlumno().getNombre() + ",\n\n" +
                        "Te informamos de que se ha tramitado tu asignación de prácticas:\n\n" +
                        "- Empresa: " + practica.getEmpresa().getNombre() + "\n" +
                        "- Fecha de inicio: " + fechaFormateada + "\n\n" +
                        "Puedes consultar todos los detalles en la plataforma de gestión.";
        
        mensaje.setText(cuerpo);

        mailSender.send(mensaje);
    }
}
