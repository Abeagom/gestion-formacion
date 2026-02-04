package com.daw.gestionformacion.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daw.gestionformacion.modelo.Alumno;

public interface AlumnoRepository extends JpaRepository<Alumno, Integer> {

}
