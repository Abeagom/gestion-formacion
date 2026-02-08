package com.daw.gestionformacion.servicio;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.daw.gestionformacion.modelo.Alumno;
import com.daw.gestionformacion.repositorio.AlumnoRepositorio;

@Service
public class AlumnoServicio {
    
    private final AlumnoRepositorio alumnoRepositorio;
    private final CsvServicio csvServicio;

    public AlumnoServicio(AlumnoRepositorio alumnoRepositorio, CsvServicio csvServicio) {
        this.alumnoRepositorio = alumnoRepositorio;
        this.csvServicio=csvServicio;
    }

    public List<Alumno> obtenerTodos() {
        return alumnoRepositorio.findAll();
    }
    
    public Alumno obtenerPorId(Integer id) {
        return alumnoRepositorio.findById(id).orElse(null);
    }
    
    public List<Alumno> obtenerPorCurso(Integer cursoId) {
        return alumnoRepositorio.findByCursoId(cursoId);
    }
    
    public void importar(MultipartFile archivo) throws Exception {
        csvServicio.cargarAlumnosDesdeCsv(archivo);
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
}
