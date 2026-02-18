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
import com.daw.gestionformacion.modelo.ImportacionResultado;
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
    public String listar(Model model) {
        model.addAttribute("alumnos", alumnoServicio.obtenerTodosOrdenados());
        return "alumnos/lista";
    }

    @PostMapping("/importar")
    public String importar (@RequestParam("ficheroCsv") MultipartFile fichero, RedirectAttributes mensaje) {
    	ImportacionResultado importacionResultado = csvServicio.cargarAlumnosDesdeCsv(fichero);
    	
    	if(importacionResultado.getExito() != null) {
    		mensaje.addFlashAttribute("exito", importacionResultado.getExito());
    	}
    	
    	if(importacionResultado.getError() != null || !importacionResultado.getError().isEmpty()) {
    		mensaje.addFlashAttribute("errores", importacionResultado.getError());
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

        boolean esEdicion = alumno.getId() != null; // Si tiene id es edición

        // Comprobar errores de validación del Bean
        if (resultado.hasErrors()) {
            model.addAttribute("cursos", cursoServicio.obtenerTodos());
            // Devolver la vista correspondiente según si es edición o creación
            return esEdicion ? "alumnos/editar-alumno" : "alumnos/nuevo-alumno";
        }

        // Comprobar si el email ya existe en otro alumno
        Alumno alumnoConMismoEmail = alumnoServicio.buscarPorEmail(alumno.getEmail());
        
        //Hay un alumno con ese email y (estoy creando uno nuevo o el email no le pertenece a este alumno)
        if (alumnoConMismoEmail != null && (!esEdicion || !alumnoConMismoEmail.getId().equals(alumno.getId()))) {
            model.addAttribute("error", "Error: Ya existe un alumno registrado con el email " + alumno.getEmail());
            model.addAttribute("cursos", cursoServicio.obtenerTodos());
            return esEdicion ? "alumnos/editar-alumno" : "alumnos/nuevo-alumno";
        }
        
        //Paso necesario para que el alumno no pierda la práctica (al no estar en el formulario de edición)
        if (esEdicion) {
            // Buscamos el alumno tal cual está en la base de datos ahora mismo
            Alumno alumnoOriginal = alumnoServicio.obtenerPorId(alumno.getId());
            
            // Le pasamos la práctica que ya tenía el alumno original al del formulario
            if (alumnoOriginal != null) {
                alumno.setPractica(alumnoOriginal.getPractica());
            }
        }

        // Guardar alumno (creación o actualización)
        alumnoServicio.guardar(alumno);

        // Mensaje y redirección
        mensaje.addFlashAttribute("exito", esEdicion ? "Alumno actualizado correctamente." : "Alumno creado correctamente.");
        return "redirect:/alumnos";
    }
}

