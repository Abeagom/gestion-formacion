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

import com.daw.gestionformacion.modelo.Practica;
import com.daw.gestionformacion.servicio.AlumnoServicio;
import com.daw.gestionformacion.servicio.EmpresaServicio;
import com.daw.gestionformacion.servicio.PracticaServicio;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/practicas")
public class PracticaControlador {

	private PracticaServicio practicaServicio;
    private AlumnoServicio alumnoServicio;
    private EmpresaServicio empresaServicio;

    public PracticaControlador(PracticaServicio practicaServicio, 
                               AlumnoServicio alumnoServicio, 
                               EmpresaServicio empresaServicio) {
        this.practicaServicio = practicaServicio;
        this.alumnoServicio = alumnoServicio;
        this.empresaServicio = empresaServicio;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("practicas", practicaServicio.obtenerTodas());
        return "practicas/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("practica", new Practica());
        model.addAttribute("alumnos", practicaServicio.obtenerAlumnosDisponibles());
        model.addAttribute("empresas", empresaServicio.obtenerTodas());
        return "practicas/nuevo-practica"; 
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model, RedirectAttributes mensaje) {
        Practica practica = practicaServicio.obtenerPorId(id);
        
        if(practica == null) {
            mensaje.addFlashAttribute("error", "La práctica no existe.");
            return "redirect:/practicas";
        }
        
        model.addAttribute("practica", practica);
        // Para editar, permitimos ver todos los alumnos o al menos el asignado
        model.addAttribute("alumnos", alumnoServicio.obtenerTodos()); 
        model.addAttribute("empresas", empresaServicio.obtenerTodas());
        return "practicas/editar-practica";
    }

    @GetMapping("/detalles/{id}")
    public String verDetalles(@PathVariable Integer id, Model model, RedirectAttributes mensaje) {
        Practica practica = practicaServicio.obtenerPorId(id);
        
        if(practica == null) {
            mensaje.addFlashAttribute("error", "La práctica no existe.");
            return "redirect:/practicas";
        }
        model.addAttribute("practica", practica);
        return "practicas/detalles-practica";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, RedirectAttributes mensaje) {
        Practica practica = practicaServicio.obtenerPorId(id);

        if (practica == null) {
            mensaje.addFlashAttribute("error", "La práctica no existe.");
            return "redirect:/practicas";
        }

        practicaServicio.eliminar(id);
        mensaje.addFlashAttribute("exito", "Asignación de práctica eliminada correctamente.");
        return "redirect:/practicas";
    }

    @PostMapping("/guardar")
    public String guardarPractica(
            @Valid @ModelAttribute("practica") Practica practica, 
            BindingResult resultado, 
            RedirectAttributes mensaje, 
            Model model) {

        boolean esEdicion = practica.getId() != null;

        // Validación de fecha inicio anterior a fecha fin
        if (practica.getFechaInicio() != null && practica.getFechaFin() != null) {
            if (practica.getFechaFin().isBefore(practica.getFechaInicio())) {
                model.addAttribute("error", "La fecha de fin no puede ser anterior a la de inicio.");
                model.addAttribute("alumnos", esEdicion ? alumnoServicio.obtenerTodos() : practicaServicio.obtenerAlumnosDisponibles());
                model.addAttribute("empresas", empresaServicio.obtenerTodas());
                
                return esEdicion ? "practicas/editar-practica" : "practicas/nuevo-practica";
            }
        }

        // Validaciones de Bean Validation
        if (resultado.hasErrors()) {
            model.addAttribute("alumnos", esEdicion ? alumnoServicio.obtenerTodos() : practicaServicio.obtenerAlumnosDisponibles());
            model.addAttribute("empresas", empresaServicio.obtenerTodas());
            return esEdicion ? "practicas/editar-practica" : "practicas/nuevo-practica";
        }
        
        practicaServicio.guardar(practica);
        mensaje.addFlashAttribute("exito", esEdicion ? "Práctica actualizada correctamente." : "Práctica asignada correctamente.");
        
        return "redirect:/practicas";
    }
}
