package com.daw.gestionformacion.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.daw.gestionformacion.modelo.Profesor;
import com.daw.gestionformacion.modelo.TipoProfesor;
import com.daw.gestionformacion.servicio.ProfesorServicio;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/directiva/profesores")
public class ProfesorControlador {
	
    private final ProfesorServicio profesorServicio;
    
    public ProfesorControlador(ProfesorServicio profesorServicio) {
        this.profesorServicio = profesorServicio;
    }
    
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("profesores", profesorServicio.obtenerTodosOrdenados());
        return "profesores/lista";
    }
    
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("profesor", new Profesor());
        model.addAttribute("tipoProfesor", TipoProfesor.values());
        return "profesores/nuevo-profesor"; 
    }
    
    @PostMapping("/guardar")
    public String guardarProfesor(
            @Valid @ModelAttribute("profesor") Profesor profesor, 
            BindingResult resultado,
            @RequestParam("confirmarPassword") String confirmar,
            RedirectAttributes mensaje, 
            Model model) {

        boolean esEdicion = profesor.getId() != null; // Detectamos si es edición

        //Comprobar que las contraseñas coincidan
        if (!profesor.getPassword().equals(confirmar)) {
            model.addAttribute("error", "Las contraseñas no coinciden.");
            model.addAttribute("tipoProfesor", TipoProfesor.values());
            return esEdicion ? "profesores/editar-profesor" : "profesores/nuevo-profesor";
        }
        
        // Comprobar errores de validación del Bean
        if (resultado.hasErrors()) {
            model.addAttribute("tipoProfesor", TipoProfesor.values());
            return esEdicion ? "profesores/editar-profesor" : "profesores/nuevo-profesor";
        }

        // Comprobar si el email ya existe en otro profesor
        Profesor existente = profesorServicio.buscarPorEmail(profesor.getEmail());
        if (existente != null && (!esEdicion || !existente.getId().equals(profesor.getId()))) {
            model.addAttribute("error", "Error: Ya existe un profesor registrado con el email " + profesor.getEmail());
            model.addAttribute("tipoProfesor", TipoProfesor.values());
            return esEdicion ? "profesores/editar-profesor" : "profesores/nuevo-profesor";
        }

        // Guardar profesor (creación o actualización)
        profesorServicio.guardar(profesor);

        // Mensaje y redirección
        mensaje.addFlashAttribute("exito", esEdicion ? "Profesor actualizado correctamente." : "Profesor creado correctamente.");
        return "redirect:/directiva/profesores";
    }
    
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model, RedirectAttributes mensaje) {
    	Profesor profesor = profesorServicio.obtenerPorId(id);
    	
    	//Controlamos que el usuario no ponga en la URL un id que no exista
    	if(profesor == null) {
    		mensaje.addFlashAttribute("error", "El profesor no existe.");
            return "redirect:/directiva/profesores";
    	}
        model.addAttribute("profesor", profesor);
        model.addAttribute("tipoProfesor", TipoProfesor.values());
        return "profesores/editar-profesor";
    }
    
    @GetMapping("/detalles/{id}")
    public String verDetalles(@PathVariable Integer id, Model model, RedirectAttributes mensaje) {
    	Profesor profesor = profesorServicio.obtenerPorId(id);
    	
    	//Controlamos que el usuario no ponga en la URL un id que no exista
    	if(profesor == null) {
    		mensaje.addFlashAttribute("error", "El profesor no existe.");
            return "redirect:/directiva/profesores";
    	}
        model.addAttribute("profesor", profesor);
        return "profesores/detalles-profesor";
    }
    
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, RedirectAttributes mensaje) {

        Profesor profesor = profesorServicio.obtenerPorId(id);

        if (profesor == null) {
            mensaje.addFlashAttribute("error", "El profesor no existe.");
            return "redirect:/directiva/profesores";
        }
        
        if(profesor.getCursos() != null) {
            mensaje.addFlashAttribute("error", "El profesor tiene cursos asociados.");
            return "redirect:/directiva/profesores";
        }

        profesorServicio.eliminar(id);

        mensaje.addFlashAttribute("exito", "Profesor eliminado correctamente.");

        return "redirect:/directiva/profesores";
    }

}
