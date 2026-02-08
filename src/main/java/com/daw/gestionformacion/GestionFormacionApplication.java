package com.daw.gestionformacion;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.daw.gestionformacion.modelo.Profesor;
import com.daw.gestionformacion.modelo.TipoProfesor;
import com.daw.gestionformacion.repositorio.ProfesorRepositorio;

@SpringBootApplication
public class GestionFormacionApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestionFormacionApplication.class, args);
	}
	
	@Bean
	CommandLineRunner crearDirector(ProfesorRepositorio repositorio, PasswordEncoder codificador) {
	    return args -> {
	    	
	        if(repositorio.findByEmail("director@iescamas.es").isEmpty()) {
	            Profesor directiva = new Profesor(
	                "Director",
	                "Directiva",
	                "director@iescamas.es",
	                codificador.encode("1234"), // contraseña encriptada
	                TipoProfesor.DIRECTIVA
	            );
	            repositorio.save(directiva);
	        }
	    };
	}


}
