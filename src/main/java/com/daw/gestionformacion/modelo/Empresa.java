package com.daw.gestionformacion.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "empresas")
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;
    private String descripcion;
    private String tutorNombre;
    private String tutorEmail;

    @OneToMany(mappedBy = "empresa")
    private List<Practica> practicas;
    
    public Empresa() {
    }
    
    public Empresa(String nombre, String descripcion, String tutorNombre, String tutorEmail) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tutorNombre = tutorNombre;
        this.tutorEmail = tutorEmail;
        this.practicas = new ArrayList<>();
    }

    //Getters y Setters
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getTutorNombre() {
		return tutorNombre;
	}

	public void setTutorNombre(String tutorNombre) {
		this.tutorNombre = tutorNombre;
	}

	public String getTutorEmail() {
		return tutorEmail;
	}

	public void setTutorEmail(String tutorEmail) {
		this.tutorEmail = tutorEmail;
	}

	public List<Practica> getPracticas() {
		return practicas;
	}

	public void setPracticas(List<Practica> practicas) {
		this.practicas = practicas;
	}
}
