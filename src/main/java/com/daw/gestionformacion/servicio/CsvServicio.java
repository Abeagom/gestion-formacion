package com.daw.gestionformacion.servicio;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.daw.gestionformacion.modelo.Alumno;
import com.daw.gestionformacion.modelo.Curso;
import com.daw.gestionformacion.modelo.ImportacionResultado;

@Service
public class CsvServicio {

	private final AlumnoServicio alumnoServicio;
	private final CursoServicio cursoServicio;

	public CsvServicio(AlumnoServicio alumnoService, CursoServicio cursoService) {
		this.alumnoServicio = alumnoService;
		this.cursoServicio = cursoService;
	}

	public ImportacionResultado cargarAlumnosDesdeCsv(MultipartFile archivoCsv) {
		ImportacionResultado resultado = new ImportacionResultado();
		List<String> listaErrores = new ArrayList();
		int contadorExitos = 0;
		DateTimeFormatter formateador = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		
		//Comprobación de archivo vacío
		if(archivoCsv.isEmpty()) {
			listaErrores.add("El archivo está vacío");
			resultado.setErrores(listaErrores);
			return resultado;
		}
		
		//Comprobación extensión del archivo
		if(archivoCsv.getContentType() == null || !archivoCsv.getOriginalFilename().endsWith(".csv")) {
			listaErrores.add("El archivo debe ser .csv");
			resultado.setErrores(listaErrores);
			return resultado;
		}
		
		//Comprobación de tamaño
		long pesoMaximo = 2*1024*1024;
		if(archivoCsv.getSize() > pesoMaximo) {
			listaErrores.add("El archivo es demasiado grande (2MB máximo)");
			resultado.setErrores(listaErrores);
			return resultado;
		}

		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(archivoCsv.getInputStream()));
			br.readLine(); // Saltamos cabecera
			String linea;
			int numeroFila = 2;

			while ((linea = br.readLine()) != null) {
				if (linea.isBlank())
					continue;

				try {
					// Validamos y guardamos la línea
					procesarLinea(linea, formateador);
					contadorExitos++;
				} catch (IllegalArgumentException e) {
					listaErrores.add("Fila " + numeroFila + ": " + e.getMessage());
				} catch (Exception e) {
					listaErrores.add("Fila " + numeroFila + ": Error inesperado");
				}
				numeroFila++;
			}

			// Preparar mensajes
			if (contadorExitos > 0) {
				resultado.setExito("Se han importado " + contadorExitos + " alumnos.");
			}

			if (!listaErrores.isEmpty()) {
				// Unimos todos los errores con un separador claro
				resultado.setErrores(listaErrores);
			}

			br.close();

		} catch (Exception e) {
			listaErrores.add("Error crítico al procesar el archivo: " + e.getMessage());
			resultado.setErrores(listaErrores);
		}
		return resultado;
	}

	private void procesarLinea(String linea, DateTimeFormatter fmt) throws Exception {
		String[] campos = linea.split(";");
		if (campos.length < 5) {
			throw new IllegalArgumentException("Faltan datos en la fila");
		}

		Alumno al = new Alumno();

		// Para nombre
		String nombreAlumno = campos[0].trim();
		if (nombreAlumno.isBlank()) {
			throw new IllegalArgumentException("El nombre está vacío.");
		} else if (nombreAlumno.length() > 50) {
			throw new IllegalArgumentException("El nombre es demasiado largo.");
		}
		al.setNombre(nombreAlumno);

		// Para apellidos
		String apellidos = campos[1].trim();
		if (apellidos.isBlank()) {
			throw new IllegalArgumentException("Los apellidos están vacíos.");
		} else if (apellidos.length() > 100) {
			throw new IllegalArgumentException("Los apellidos son demasiado largos.");
		}
		al.setApellidos(apellidos);

		// Para email
		String email = campos[2].trim();
		if (email.isBlank()) {
			throw new IllegalArgumentException("El email está vacío");
		} else if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
			throw new IllegalArgumentException("El formato del email " + email + " no es válido");
		} else if (alumnoServicio.existePorEmail(email)) {
			throw new IllegalArgumentException("El email " + email + " ya está registrado en el sistema");
		}
		al.setEmail(email);

		// Para fechaNacimiento
		try {
			LocalDate fechaNacimiento = LocalDate.parse(campos[3].trim(), fmt);
			int edadMinima = 12;
			if (!fechaNacimiento.isBefore(LocalDate.now())) {
				throw new IllegalArgumentException("La fecha de nacimiento es posterior a hoy");
			} else if (fechaNacimiento.isAfter(LocalDate.now().minusYears(edadMinima))) {
				throw new IllegalArgumentException("El alumno debe tener al menos " + edadMinima + " años");
			}
			al.setFechaNacimiento(fechaNacimiento);
		} catch (DateTimeParseException e) {
			throw new IllegalArgumentException("La fecha " + campos[3] + " no tiene un formato válido");
		}

		// Para curso (se permite no tener curso al importar)
		String idCursoStr = campos[4].trim();
		if (!idCursoStr.isBlank()) {
			try {
				Integer idCurso = Integer.parseInt(idCursoStr);
				Curso curso = cursoServicio.obtenerPorId(idCurso);
				if (curso == null) {
					throw new IllegalArgumentException("El curso con ID " + idCurso + " no existe.");
				}
				al.setCurso(curso);

			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("El ID del curso debe ser un número.");
			}
		}

		// Si todo está bien, guardamos el alumno
		alumnoServicio.guardar(al);
	}
}
