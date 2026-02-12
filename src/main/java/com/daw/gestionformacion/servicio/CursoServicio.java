package com.daw.gestionformacion.servicio;

import java.util.List;

import org.springframework.stereotype.Service;

import com.daw.gestionformacion.modelo.Curso;
import com.daw.gestionformacion.repositorio.CursoRepositorio;

@Service
public class CursoServicio {
	private final CursoRepositorio cursoRepositorio;

    public CursoServicio(CursoRepositorio cursoRepositorio) {
        this.cursoRepositorio = cursoRepositorio;
    }

    public List<Curso> obtenerTodos() {
        return cursoRepositorio.findAll();
    }

    public Curso obtenerPorNombre(String nombre) {
        return cursoRepositorio.findByNombre(nombre);
    }

    public Curso obtenerPorId(Integer id) {
        return cursoRepositorio.findById(id).orElse(null);
    }

    public void guardar(Curso curso) {
        cursoRepositorio.save(curso);
    }

	public Curso buscarPorNombre(String nombre) {
		return cursoRepositorio.findByNombre(nombre);
	}

	public void eliminar(Integer id) {
		cursoRepositorio.deleteById(id);	
	}
}
