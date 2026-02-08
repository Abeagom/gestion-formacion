package com.daw.gestionformacion.servicio;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.daw.gestionformacion.modelo.Alumno;
import com.daw.gestionformacion.modelo.Curso;
import com.daw.gestionformacion.repositorio.AlumnoRepositorio;
import com.daw.gestionformacion.repositorio.CursoRepositorio;

@Service
public class CsvServicio {
	
	private AlumnoRepositorio alumnoRepositorio;
	private CursoRepositorio cursoRepositorio;
	private final DateTimeFormatter formateador = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	
	public CsvServicio (AlumnoRepositorio alumnoRepositorio, CursoRepositorio cursoRepositorio) {
		this.alumnoRepositorio=alumnoRepositorio;
		this.cursoRepositorio=cursoRepositorio;
	}
	
	public void cargarAlumnosDesdeCsv (MultipartFile csv) throws Exception {
		List<Alumno> listaAlumnos = new ArrayList();

        try {
        	BufferedReader br = new BufferedReader(new InputStreamReader(csv.getInputStream()));
            String linea;
            br.readLine(); // Saltamos la primera línea (cabecera)
            linea = br.readLine();

            while (linea != null) {
                // El CSV usa punto y coma como separador
                String[] campos = linea.split(";");
                
                if (campos.length >= 5) {
                    Alumno al = new Alumno();
                    al.setNombre(campos[0].trim());
                    al.setApellidos(campos[1].trim());
                    al.setEmail(campos[2].trim());
                    al.setFechaNacimiento(LocalDate.parse(campos[3].trim(), formateador));
                 // Buscar el curso por ID
                    Integer idCurso = Integer.parseInt(campos[4].trim());
                    Curso curso = cursoRepositorio.findById(idCurso).orElse(null);
                    al.setCurso(curso);
                    
                    listaAlumnos.add(al);
                }
                
                linea = br.readLine();
            }
            // Guardamos todos en la BD
            alumnoRepositorio.saveAll(listaAlumnos);
        }catch (Exception e) {
        	
        }
	}
}
