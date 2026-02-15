package com.daw.gestionformacion.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name = "practicas")
public class Practica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "alumno_id", unique = true)
    @NotNull(message = "El alumno es obligatorio")
    @JsonIgnore
    private Alumno alumno;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    @NotNull(message = "La empresa es obligatoria")
    private Empresa empresa;

    @NotNull(message = "La fecha de inicio es obligatoria")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaInicio;
    
    @NotNull(message = "La fecha de inicio es obligatoria")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaFin;

    @Column(columnDefinition = "TEXT")
    private String comentarios;
    
    public Practica() {
    }
    
    public Practica(Alumno alumno, Empresa empresa, LocalDate fechaInicio, LocalDate fechaFin, String comentarios) {
        this.alumno = alumno;
        this.empresa = empresa;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.comentarios = comentarios;
    }

    //Getters y Setters
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Alumno getAlumno() {
		return alumno;
	}

	public void setAlumno(Alumno alumno) {
		this.alumno = alumno;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public LocalDate getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(LocalDate fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public LocalDate getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(LocalDate fechaFin) {
		this.fechaFin = fechaFin;
	}

	public String getComentarios() {
		return comentarios;
	}

	public void setComentarios(String comentarios) {
		this.comentarios = comentarios;
	}
}
