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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.daw.gestionformacion.modelo.Alumno;
import com.daw.gestionformacion.servicio.AlumnoServicio;
import com.daw.gestionformacion.servicio.CsvServicio;
import com.daw.gestionformacion.servicio.CursoServicio;

import jakarta.validation.Valid;


@Controller
@RequestMapping("/alumnos")
public class AlumnoControlador {

    private final AlumnoServicio alumnoServicio;
    private final CursoServicio cursoServicio;
    private final CsvServicio csvServicio;

    public AlumnoControlador(AlumnoServicio alumnoServicio, CursoServicio cursoServicio, CsvServicio csvServicio) {
        this.alumnoServicio = alumnoServicio;
        this.cursoServicio=cursoServicio;
        this.csvServicio = csvServicio;
    }

    @GetMapping
    public String listar(@RequestParam(required = false) Integer cursoId, Model model) {
        if (cursoId != null) {
            model.addAttribute("alumnos", alumnoServicio.obtenerPorCurso(cursoId));
        } else {
            model.addAttribute("alumnos", alumnoServicio.obtenerTodos());
        }
        model.addAttribute("cursos", cursoServicio.obtenerTodos());
        return "alumnos/lista";
    }

    @PostMapping("/cargar")
    public String importar (@RequestParam("ficheroCsv") MultipartFile fichero, RedirectAttributes mensaje) {
        try {
            csvServicio.cargarAlumnosDesdeCsv(fichero);
            mensaje.addFlashAttribute("mensaje", "¡Importación exitosa!");
        } catch (Exception e) {
            mensaje.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        return "redirect:/alumnos";
    }
    
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        // Pasamos un objeto Alumno vacío para que Thymeleaf lo rellene
        model.addAttribute("alumno", new Alumno());
        // También necesitamos la lista de cursos para el desplegable (select)
        model.addAttribute("cursos", cursoServicio.obtenerTodos());
        return "alumnos/nuevo-alumno"; 
    }
    
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model, RedirectAttributes mensaje) {
    	Alumno alumno = alumnoServicio.obtenerPorId(id);
    	
    	//Controlamos que el usuario no ponga en la URL un id que no exista
    	if(alumno == null) {
    		mensaje.addFlashAttribute("error", "El alumno no existe.");
            return "redirect:/alumnos";
    	}
        model.addAttribute("alumno", alumno);
        model.addAttribute("cursos", cursoServicio.obtenerTodos());
        return "alumnos/editar-alumno";
    }
    
    @GetMapping("/detalles/{id}")
    public String verDetalles(@PathVariable Integer id, Model model, RedirectAttributes mensaje) {
    	Alumno alumno = alumnoServicio.obtenerPorId(id);
    	
    	//Controlamos que el usuario no ponga en la URL un id que no exista
    	if(alumno == null) {
    		mensaje.addFlashAttribute("error", "El alumno no existe.");
            return "redirect:/alumnos";
    	}
        model.addAttribute("alumno", alumno);
        model.addAttribute("cursos", cursoServicio.obtenerTodos());
        return "alumnos/detalles-alumno";
    }
    
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, RedirectAttributes mensaje) {

        Alumno alumno = alumnoServicio.obtenerPorId(id);

        if (alumno == null) {
            mensaje.addFlashAttribute("error", "El alumno no existe.");
            return "redirect:/alumnos";
        }

        alumnoServicio.eliminar(id);

        mensaje.addFlashAttribute("exito", "Alumno eliminado correctamente.");

        return "redirect:/alumnos";
    }

    
    @PostMapping("/guardar")
    public String guardarAlumno(
            @Valid @ModelAttribute("alumno") Alumno alumno, 
            BindingResult resultado, 
            RedirectAttributes mensaje, 
            Model model) {

        boolean esEdicion = alumno.getId() != null; // Detectamos si es edición

        // Comprobar errores de validación del Bean
        if (resultado.hasErrors()) {
            model.addAttribute("cursos", cursoServicio.obtenerTodos());
            // Devolver la vista correspondiente según si es edición o creación
            return esEdicion ? "alumnos/editar-alumno" : "alumnos/nuevo-alumno";
        }

        // Comprobar si el email ya existe en otro alumno
        Alumno existente = alumnoServicio.buscarPorEmail(alumno.getEmail());
        if (existente != null && (!esEdicion || !existente.getId().equals(alumno.getId()))) {
            model.addAttribute("error", "Error: Ya existe un alumno registrado con el email " + alumno.getEmail());
            model.addAttribute("cursos", cursoServicio.obtenerTodos());
            return esEdicion ? "alumnos/editar-alumno" : "alumnos/nuevo-alumno";
        }

        // Guardar alumno (creación o actualización)
        alumnoServicio.guardar(alumno);

        // Mensaje y redirección
        mensaje.addFlashAttribute("exito", esEdicion ? "Alumno actualizado correctamente." : "Alumno creado correctamente.");
        return "redirect:/alumnos";
    }
}

