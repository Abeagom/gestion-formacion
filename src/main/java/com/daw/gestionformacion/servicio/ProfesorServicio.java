package com.daw.gestionformacion.servicio;

import java.util.List;

import org.springframework.stereotype.Service;

import com.daw.gestionformacion.modelo.Profesor;
import com.daw.gestionformacion.repositorio.ProfesorRepositorio;

@Service
public class ProfesorServicio {
	
	private ProfesorRepositorio profesorRepositorio;
	
	public ProfesorServicio (ProfesorRepositorio profesorRepositorio) {
		this.profesorRepositorio = profesorRepositorio;
	}

	public List<Profesor> obtenerTodos() {
		return profesorRepositorio.findAll();
	}

}
