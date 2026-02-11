package com.daw.gestionformacion.modelo;

import java.util.List;

public class ImportacionResultado {
	private String exito;
	private List<String> errores;

	// Getters y Setters
	public String getExito() {
		return exito;
	}

	public void setExito(String exito) {
		this.exito = exito;
	}

	public List<String> getError() {
		return errores;
	}

	public void setErrores(List<String> errores) {
		this.errores = errores;
	}
}
