package com.daw.gestionformacion.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.daw.gestionformacion.modelo.Curso;
import com.daw.gestionformacion.servicio.CursoServicio;

import jakarta.validation.Valid;

@Controller
@RequestMapping("directiva/cursos")
public class CursoControlador {

	private CursoServicio cursoServicio;

	public CursoControlador(CursoServicio cursoServicio) {
		this.cursoServicio = cursoServicio;
	}

	@GetMapping
	public String listar(Model model) {
		model.addAttribute("cursos", cursoServicio.obtenerTodos());
		return "cursos/lista";
	}

	@GetMapping("/nuevo")
	public String mostrarFormularioNuevo(Model model) {
		// Pasamos un objeto Curso vacío para que Thymeleaf lo rellene
		model.addAttribute("curso", new Curso());
		return "cursos/nuevo-curso";
	}

	@PostMapping("/guardar")
	public String guardarCurso(@Valid @ModelAttribute("curso") Curso curso, BindingResult resultado,
			RedirectAttributes mensaje, Model model) {

		boolean esEdicion = curso.getId() != null; // Detectamos si es edición

		// Comprobar errores de validación del Bean
		if (resultado.hasErrors()) {
			// Devolver la vista correspondiente según si es edición o creación
			return esEdicion ? "cursos/editar-curso" : "cursos/nuevo-curso";
		}

		// Guardar alumno (creación o actualización)
		cursoServicio.guardar(curso);

		// Mensaje y redirección
		mensaje.addFlashAttribute("exito",
				esEdicion ? "Curso actualizado correctamente." : "Curso creado correctamente.");
		return "redirect:/cursos";
	}
}
