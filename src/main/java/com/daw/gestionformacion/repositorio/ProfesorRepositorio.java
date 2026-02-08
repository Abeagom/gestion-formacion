package com.daw.gestionformacion.repositorio;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daw.gestionformacion.modelo.Profesor;

public interface ProfesorRepositorio extends JpaRepository<Profesor, Integer>{

	//Se usará para el login (puede no existir)
	Optional<Profesor> findByEmail(String email);
	
}
