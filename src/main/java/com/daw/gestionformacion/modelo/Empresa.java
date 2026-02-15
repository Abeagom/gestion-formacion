package com.daw.gestionformacion.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name = "empresas")
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El nombre de la empresa es obligatorio")
    @Size(max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Column(nullable = false, unique = true)
    private String nombre;
    
    @Size(max = 500, message = "La descripción no puede superar los 500 caracteres")
    private String descripcion;
    
    @NotBlank(message = "El nombre del tutor es obligatorio")
    @Size(max = 50, message = "El nombre del tutor no puede superar los 50 caracteres")
    private String tutorNombre;
    
    @NotBlank(message = "El email del tutor es obligatorio")
    @Email(message = "Debe introducir un formato de email válido")
    @Column(nullable = false)
    private String tutorEmail;

    @OneToMany(mappedBy = "empresa")
    @JsonIgnore
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
