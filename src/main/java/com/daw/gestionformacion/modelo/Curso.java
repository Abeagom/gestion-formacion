package com.daw.gestionformacion.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name = "cursos")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 20, message = "El nombre no puede superar 20 caracteres")
    @Column(unique = true, nullable = false)
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "profesor_id")
    private Profesor tutor;

    @OneToMany(mappedBy = "curso")
    @OrderBy("apellidos ASC, nombre ASC")
    @JsonIgnore
    private List<Alumno> alumnos;
    
    public Curso() {
    }
    
    public Curso(String nombre) {
        this.nombre = nombre;
        this.alumnos = new ArrayList<>();
    }

    // Getter y Setters
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

	public Profesor getTutor() {
		return tutor;
	}

	public void setTutor(Profesor tutor) {
		this.tutor = tutor;
	}

	public List<Alumno> getAlumnos() {
		return alumnos;
	}

	public void setAlumnos(List<Alumno> alumnos) {
		this.alumnos = alumnos;
	}
}
