package com.daw.gestionformacion.servicio;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.daw.gestionformacion.modelo.Profesor;
import com.daw.gestionformacion.repositorio.ProfesorRepositorio;

import jakarta.validation.Valid;

@Service
public class ProfesorServicio {
	
	private ProfesorRepositorio profesorRepositorio;
	private PasswordEncoder codificadorPassword;
	
	public ProfesorServicio (ProfesorRepositorio profesorRepositorio, PasswordEncoder codificadorPassword) {
		this.profesorRepositorio = profesorRepositorio;
		this.codificadorPassword = codificadorPassword;
	}

	public List<Profesor> obtenerTodos() {
		return profesorRepositorio.findAll();
	}

	public List<Profesor> obtenerTodosOrdenados() {
		return profesorRepositorio.findAllByOrderByApellidosAscNombreAsc();
	}

	public Profesor buscarPorEmail(String email) {
		return profesorRepositorio.findByEmail(email).orElse(null);
	}

	public void guardar(@Valid Profesor profesor) {
		String passwordEncriptado = codificadorPassword.encode(profesor.getPassword());
		profesor.setPassword(passwordEncriptado);
		profesorRepositorio.save(profesor);
	}

	public Profesor obtenerPorId(Integer id) {
		return profesorRepositorio.findById(id).orElse(null);
	}

	public void eliminar(Integer id) {
		profesorRepositorio.deleteById(id);
	}

}
