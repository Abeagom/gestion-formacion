package com.daw.gestionformacion.servicio;

import java.util.List;

import org.springframework.stereotype.Service;

import com.daw.gestionformacion.modelo.Empresa;
import com.daw.gestionformacion.repositorio.EmpresaRepositorio;

import jakarta.validation.Valid;

@Service
public class EmpresaServicio {
	
	private EmpresaRepositorio empresaRepositorio;
	
	public EmpresaServicio (EmpresaRepositorio empresaRepositorio) {
		this.empresaRepositorio = empresaRepositorio;
	}

	public List<Empresa> obtenerTodas() {
		return empresaRepositorio.findAll();
	}

	public Empresa obtenerPorId(Integer id) {
		return empresaRepositorio.findById(id).orElse(null);
	}

	public Empresa buscarPorNombre(String nombre) {
		return empresaRepositorio.findByNombre(nombre);
	}

	public void guardar(@Valid Empresa empresa) {
		empresaRepositorio.save(empresa);
		
	}

	public void eliminar(Integer id) {
		empresaRepositorio.deleteById(id);
	}

}
