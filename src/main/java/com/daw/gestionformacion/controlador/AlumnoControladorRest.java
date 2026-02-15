package com.daw.gestionformacion.controlador;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.daw.gestionformacion.modelo.Alumno;
import com.daw.gestionformacion.servicio.AlumnoServicio;

import jakarta.validation.Valid;


@RestController
@RequestMapping("api/alumnos")
@CrossOrigin("*")
public class AlumnoControladorRest {

    private final AlumnoServicio alumnoServicio;

    public AlumnoControladorRest(AlumnoServicio alumnoServicio) {
        this.alumnoServicio = alumnoServicio;
    }

    @GetMapping
    public List<Alumno> listar() {
        return alumnoServicio.obtenerTodosOrdenados();
    }

    @GetMapping("/{id}")
    public Alumno obtenerUnAlumno(@PathVariable Integer id) {
        return alumnoServicio.obtenerPorId(id);
    }
    
    @PostMapping("crear")
    public boolean crear(@Valid @RequestBody Alumno alumno) {
        // Forzamos el ID a null para que siempre sea un registro nuevo
        alumno.setId(null);
        alumnoServicio.guardar(alumno);
        return true;
    }
    
    @PutMapping("/{id}")
    public boolean actualizar(@PathVariable Integer id, @Valid @RequestBody Alumno alumnoModificado) {
    	boolean resultado = false;
        // Buscamos el alumno que ya existe en la BD
        Alumno alumnoActual = alumnoServicio.obtenerPorId(id);
        
        if (alumnoActual != null) {
            // Mantenemos la práctica que ya tenía (así no desaparece al editar)
            alumnoModificado.setPractica(alumnoActual.getPractica());
            alumnoServicio.guardar(alumnoModificado);
            resultado = true;
        }
        return resultado;
    }
    
}

