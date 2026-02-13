package com.daw.gestionformacion.servicio;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.daw.gestionformacion.modelo.Alumno;
import com.daw.gestionformacion.modelo.Practica;
import com.daw.gestionformacion.repositorio.PracticaRepositorio;

@Service
public class PracticaServicio {
	
	private PracticaRepositorio practicaRepositorio;
	private AlumnoServicio alumnoServicio;
	
	public PracticaServicio (PracticaRepositorio practicaRepositorio, AlumnoServicio alumnoServicio) {
		this.practicaRepositorio = practicaRepositorio;
		this.alumnoServicio = alumnoServicio;
	}
	
	public List<Practica> obtenerTodas() {
        return practicaRepositorio.findAll();
    }

    public void guardar(Practica practica) {
        practicaRepositorio.save(practica);
    }
    
    public Practica obtenerPorId(Integer id) {
        return practicaRepositorio.findById(id).orElse(null);
    }
    
    public void eliminar(Integer id) {
        practicaRepositorio.deleteById(id);
    }
    
    //Lista de alumnos sin prácticas
    public List<Alumno> obtenerAlumnosDisponibles() {
        return alumnoServicio.obtenerTodos().stream()
                .filter(a -> a.getPractica() == null)
                .collect(Collectors.toList());
    }

}
