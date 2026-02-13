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

import com.daw.gestionformacion.modelo.Empresa;
import com.daw.gestionformacion.servicio.EmpresaServicio;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/empresas")
public class EmpresaControlador {

	private final EmpresaServicio empresaServicio;

    public EmpresaControlador(EmpresaServicio empresaServicio) {
        this.empresaServicio = empresaServicio;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("empresas", empresaServicio.obtenerTodas());
        return "empresas/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("empresa", new Empresa());
        return "empresas/nuevo-empresa"; 
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model, RedirectAttributes mensaje) {
        Empresa empresa = empresaServicio.obtenerPorId(id);
        
        if(empresa == null) {
            mensaje.addFlashAttribute("error", "La empresa no existe.");
            return "redirect:/empresas";
        }
        model.addAttribute("empresa", empresa);
        return "empresas/editar-empresa";
    }

    @GetMapping("/detalles/{id}")
    public String verDetalles(@PathVariable Integer id, Model model, RedirectAttributes mensaje) {
        Empresa empresa = empresaServicio.obtenerPorId(id);
        
        if(empresa == null) {
            mensaje.addFlashAttribute("error", "La empresa no existe.");
            return "redirect:/empresas";
        }
        model.addAttribute("empresa", empresa);
        return "empresas/detalles-empresa";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, RedirectAttributes mensaje) {
        Empresa empresa = empresaServicio.obtenerPorId(id);

        if (empresa == null) {
            mensaje.addFlashAttribute("error", "La empresa no existe.");
            return "redirect:/empresas";
        }

        // Una empresa con prácticas no puede ser borrada
        if (empresa.getPracticas() != null && !empresa.getPracticas().isEmpty()) {
            mensaje.addFlashAttribute("error", "La empresa " + empresa.getNombre() + " tiene prácticas asociadas");
            return "redirect:/empresas";
        }

        empresaServicio.eliminar(id);
        mensaje.addFlashAttribute("exito", "Empresa eliminada correctamente.");
        return "redirect:/empresas";
    }

    @PostMapping("/guardar")
    public String guardarEmpresa(
            @Valid @ModelAttribute("empresa") Empresa empresa, 
            BindingResult resultado, 
            RedirectAttributes mensaje, 
            Model model) {

        boolean esEdicion = empresa.getId() != null;

        if (resultado.hasErrors()) {
            return esEdicion ? "empresas/editar-empresa" : "empresas/nuevo-empresa";
        }

        // Comprobar si el nombre ya existe
        Empresa existente = empresaServicio.buscarPorNombre(empresa.getNombre());
        if (existente != null && (!esEdicion || !existente.getId().equals(empresa.getId()))) {
            model.addAttribute("error", "Ya existe una empresa registrada con el nombre " + empresa.getNombre());
            return esEdicion ? "empresas/editar-empresa" : "empresas/nuevo-empresa";
        }

        empresaServicio.guardar(empresa);
        mensaje.addFlashAttribute("exito", esEdicion ? "Empresa actualizada correctamente." : "Empresa creada correctamente.");
        return "redirect:/empresas";
    }
}
