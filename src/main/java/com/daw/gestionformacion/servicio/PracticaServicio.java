package com.daw.gestionformacion.servicio;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.daw.gestionformacion.modelo.Alumno;
import com.daw.gestionformacion.modelo.Curso;
import com.daw.gestionformacion.modelo.Empresa;
import com.daw.gestionformacion.modelo.Practica;
import com.daw.gestionformacion.repositorio.PracticaRepositorio;

@Service
public class PracticaServicio {
	
	private PracticaRepositorio practicaRepositorio;
	private AlumnoServicio alumnoServicio;
	private EmpresaServicio empresaServicio;
	private CursoServicio cursoServicio;
	
	public PracticaServicio (PracticaRepositorio practicaRepositorio, AlumnoServicio alumnoServicio, EmpresaServicio empresaServicio, CursoServicio cursoServicio) {
		this.practicaRepositorio = practicaRepositorio;
		this.alumnoServicio = alumnoServicio;
		this.empresaServicio = empresaServicio;
		this.cursoServicio = cursoServicio;
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
        Practica practica = practicaRepositorio.findById(id).orElse(null);
        
        if (practica != null) {
            if (practica.getAlumno() != null) {
                practica.getAlumno().setPractica(null);
            }
            practicaRepositorio.delete(practica);
        }
    }
    
    //Lista de alumnos sin prácticas
    public List<Alumno> obtenerAlumnosDisponibles() {
        return alumnoServicio.obtenerTodos().stream()
                .filter(a -> a.getPractica() == null)
                .collect(Collectors.toList());
    }
    
 // Obtener alumnos por empresa
    public Map<String, Integer> obtenerAlumnosPorEmpresa() {
    	Map <String, Integer> resultado = new HashMap();
    	
    	for(Empresa e : empresaServicio.obtenerTodas()) {
    		int total = e.getPracticas() != null? e.getPracticas().size() : 0;
    		resultado.put(e.getNombre(), total);
    	}
    	
    	return resultado;
    }
    
    //Obtener alumnos que realizan prácticas de cada curso
    public Map<String, Integer> obtenerAlumnosConPracticaPorCurso() {
    	Map <String, Integer> resultado = new HashMap();
    	
    	for(Curso curso : cursoServicio.obtenerTodos()) {
    		int contador = 0;
    		for(Alumno alumno : curso.getAlumnos()) {
    			if(alumno.getPractica() != null) {
    				contador ++;
    			}
    		}
    		resultado.put(curso.getNombre(), contador);
    	}
    	
    	return resultado;
    }

}
