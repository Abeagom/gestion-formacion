package com.daw.gestionformacion.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.daw.gestionformacion.modelo.Curso;
import com.daw.gestionformacion.servicio.CursoServicio;
import com.daw.gestionformacion.servicio.ProfesorServicio;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/directiva/cursos")
public class CursoControlador {

	private CursoServicio cursoServicio;
	private ProfesorServicio profesorServicio;

	public CursoControlador(CursoServicio cursoServicio, ProfesorServicio profesorServicio) {
		this.cursoServicio = cursoServicio;
		this.profesorServicio = profesorServicio;
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
		//Lista de profesores para asignar tutores
        model.addAttribute("profesores", profesorServicio.obtenerTodos());
		return "cursos/nuevo-curso";
	}
	
	@GetMapping("/editar/{id}")
	public String mostrarFormularioEditar(@PathVariable Integer id, Model model) {
	    Curso curso = cursoServicio.obtenerPorId(id);
	    if (curso == null) {
	        return "redirect:/directiva/cursos";
	    }
        model.addAttribute("profesores", profesorServicio.obtenerTodos());
	    model.addAttribute("curso", curso);
	    return "cursos/editar-curso"; 
	}

	@PostMapping("/guardar")
	public String guardarCurso(@Valid @ModelAttribute("curso") Curso curso, BindingResult resultado,
			RedirectAttributes mensaje, Model model) {

		boolean esEdicion = curso.getId() != null; // Detectamos si es edición

		// Comprobar errores de validación del Bean
		if (resultado.hasErrors()) {
			model.addAttribute("profesores", profesorServicio.obtenerTodos());
			// Devolver la vista correspondiente según si es edición o creación
			return esEdicion ? "cursos/editar-curso" : "cursos/nuevo-curso";
		}
		
		// Comprobar si el nombre ya existe
	    Curso existente = cursoServicio.buscarPorNombre(curso.getNombre());
	    
	    // Si existe y (es creación O es edición de un curso distinto)
	    if (existente != null && (!esEdicion || !existente.getId().equals(curso.getId()))) {
			model.addAttribute("profesores", profesorServicio.obtenerTodos());
	        model.addAttribute("error", "Error: Ya existe un curso con el nombre '" + curso.getNombre() + "'");
	        return esEdicion ? "cursos/editar-curso" : "cursos/nuevo-curso";
	    }

		// Guardar alumno (creación o actualización)
		cursoServicio.guardar(curso);

		// Mensaje y redirección
		mensaje.addFlashAttribute("exito",
				esEdicion ? "Curso actualizado correctamente." : "Curso creado correctamente.");
		return "redirect:/directiva/cursos";
	}
	
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, RedirectAttributes mensaje) {

        Curso curso = cursoServicio.obtenerPorId(id);

        if (curso == null) {
            mensaje.addFlashAttribute("error", "El curso no existe.");
            return "redirect:/directiva/cursos";
        }

        if(!curso.getAlumnos().isEmpty()) {
            mensaje.addFlashAttribute("error", "El curso tiene alumnos inscritos. No puede eliminarse.");
            return "redirect:/directiva/cursos";
        }
        cursoServicio.eliminar(id);

        mensaje.addFlashAttribute("exito", "Curso eliminado correctamente.");

        return "redirect:/directiva/cursos";
    }
    
    @GetMapping("/detalles/{id}")
    public String verDetalles(@PathVariable Integer id, Model model, RedirectAttributes mensaje) {
    	Curso curso = cursoServicio.obtenerPorId(id);
    	
    	//Controlamos que el usuario no ponga en la URL un id que no exista
    	if(curso == null) {
    		mensaje.addFlashAttribute("error", "El curso no existe.");
            return "redirect:/directiva/cursos";
    	}
        model.addAttribute("curso", curso);
        return "cursos/detalles-curso";
    }
}
