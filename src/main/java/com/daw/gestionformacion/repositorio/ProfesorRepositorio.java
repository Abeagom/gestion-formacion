package com.daw.gestionformacion.repositorio;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daw.gestionformacion.modelo.Profesor;

public interface ProfesorRepositorio extends JpaRepository<Profesor, Integer>{

	List<Profesor> findAllByOrderByApellidosAscNombreAsc();
	Optional<Profesor> findById(Integer id);
	
	//Se usará para el login (puede no existir)
	Optional<Profesor> findByEmail(String email);
	
}
