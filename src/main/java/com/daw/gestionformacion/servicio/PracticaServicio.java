package com.daw.gestionformacion.servicio;

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
        return empresaServicio.obtenerTodas().stream()
            .collect(Collectors.toMap(
                Empresa::getNombre,
                e -> e.getPracticas() != null ? e.getPracticas().size() : 0
            ));
    }
    
    //Obtener alumnos que realizan prácticas de cada curso
    public Map<String, Long> obtenerAlumnosConPracticaPorCurso() {
        return cursoServicio.obtenerTodos().stream()
            .collect(Collectors.toMap(
                Curso::getNombre,
                curso -> curso.getAlumnos().stream()
                    .filter(alumno -> alumno.getPractica() != null) // Solo los que tienen práctica
                    .count()
            ));
    }

}
