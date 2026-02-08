package com.daw.gestionformacion.repositorio;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daw.gestionformacion.modelo.Alumno;

public interface AlumnoRepositorio extends JpaRepository<Alumno, Integer> {

	List<Alumno> findByCursoId(Integer cursoId);
	Optional<Alumno> findById(Integer id);
	Alumno findByEmail(String email);
	boolean existsByEmail(String email);

}
