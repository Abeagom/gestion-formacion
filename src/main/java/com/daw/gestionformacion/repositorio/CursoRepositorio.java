package com.daw.gestionformacion.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daw.gestionformacion.modelo.Curso;

public interface CursoRepositorio extends JpaRepository<Curso, Integer>{

	public Curso findByNombre(String nombre);
}
