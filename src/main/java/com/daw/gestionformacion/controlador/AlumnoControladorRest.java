package com.daw.gestionformacion.controlador;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.daw.gestionformacion.modelo.Alumno;
import com.daw.gestionformacion.servicio.AlumnoServicio;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/alumnos")
@CrossOrigin("*")
public class AlumnoControladorRest {

    private final AlumnoServicio alumnoServicio;

    public AlumnoControladorRest(AlumnoServicio alumnoServicio) {
        this.alumnoServicio = alumnoServicio;
    }

    @GetMapping
    public ResponseEntity <List<Alumno>> listar() {
    	List<Alumno> alumnos = alumnoServicio.obtenerTodosOrdenados();
        return ResponseEntity.ok(alumnos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Alumno> obtenerAlumno(@PathVariable Integer id) {
    	Alumno alumno = alumnoServicio.obtenerPorId(id);
        if(alumno == null) {
        	return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(alumno);
    }
    
    @PostMapping("/crear")
    public ResponseEntity<Alumno> crear(@Valid @RequestBody Alumno alumno) {
        Alumno alumnoNuevo = alumnoServicio.guardarYDevolver(alumno);
        // Construir la URI del recurso recién creado
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(alumnoNuevo.getId())
                .toUri();

        return ResponseEntity.created(uri).body(alumnoNuevo);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Alumno> actualizar(@PathVariable Integer id, @Valid @RequestBody Alumno alumnoModificado) {
        // Buscamos el alumno que ya existe en la BD
        Alumno alumnoActual = alumnoServicio.obtenerPorId(id);
        
        if(alumnoActual == null) {
        	return ResponseEntity.notFound().build();
        }
        
        //Mantenemos su práctica
        alumnoModificado.setPractica(alumnoActual.getPractica());
        alumnoModificado.setId(alumnoActual.getId());
        
        Alumno alumnoActualizado = alumnoServicio.guardarYDevolver(alumnoModificado);
        
        return ResponseEntity.ok(alumnoActualizado);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Alumno> eliminar(@PathVariable Integer id) {
        Alumno alumnoActual = alumnoServicio.obtenerPorId(id);
        
        if(alumnoActual == null) {
        	return ResponseEntity.notFound().build();
        }
        
        alumnoServicio.eliminar(id);
        
        return ResponseEntity.noContent().build();
    }
    
}

