package com.daw.gestionformacion.servicio;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.daw.gestionformacion.modelo.Alumno;
import com.daw.gestionformacion.repositorio.AlumnoRepositorio;

@Service
public class AlumnoServicio {
    
    private final AlumnoRepositorio alumnoRepositorio;

    public AlumnoServicio(AlumnoRepositorio alumnoRepositorio) {
        this.alumnoRepositorio = alumnoRepositorio;
    }

    public List<Alumno> obtenerTodos() {
        return alumnoRepositorio.findAll();
    }
    
    public List<Alumno> obtenerTodosOrdenados() {
        return alumnoRepositorio.findAllByOrderByApellidosAscNombreAsc();
    }
    
    public Alumno obtenerPorId(Integer id) {
        return alumnoRepositorio.findById(id).orElse(null);
    }
    
    public List<Alumno> obtenerPorCurso(Integer cursoId) {
        return alumnoRepositorio.findByCursoId(cursoId);
    }
    
    public void guardar(Alumno alumno){
        alumnoRepositorio.save(alumno);
    }
    
    public Alumno buscarPorEmail(String email){
        return alumnoRepositorio.findByEmail(email);
    }
    
    public boolean existePorEmail(String email){
        return alumnoRepositorio.existsByEmail(email);
    }
    
    public void eliminar (Integer id){
        alumnoRepositorio.deleteById(id);
    }
    
    public Alumno guardarYDevolver(Alumno alumno) {
        // Forzar null para asegurar que sea nuevo
        alumno.setId(null);
        // save devuelve la entidad persistida con ID generado
        return alumnoRepositorio.save(alumno);
    }

}
