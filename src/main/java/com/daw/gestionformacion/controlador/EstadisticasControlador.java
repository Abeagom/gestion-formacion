package com.daw.gestionformacion.controlador;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.daw.gestionformacion.servicio.PracticaServicio;


@Controller
@RequestMapping("/directiva/estadisticas")
public class EstadisticasControlador {

	private PracticaServicio practicaServicio;
	
	public EstadisticasControlador (PracticaServicio practicaServicio) {
		this.practicaServicio = practicaServicio;
	}
	
	@GetMapping
	public String obtenerEstadisticas(Model model) {
		// Mapa de alumnos con practicas - empresa
        Map<String, Integer> empresasMap = practicaServicio.obtenerAlumnosPorEmpresa();
        
        ArrayList<String> nombreEmpresas = new ArrayList<>(empresasMap.keySet());
        ArrayList<Integer> valoresEmpresas = new ArrayList<>(empresasMap.values());
        
     // Mapa de alumnos con práctica - curso
        Map<String, Integer> cursosMap = practicaServicio.obtenerAlumnosConPracticaPorCurso();
        
        ArrayList<String> nombreCursos= new ArrayList<>(cursosMap.keySet());
        ArrayList<Integer> valoresCursos = new ArrayList<>(cursosMap.values());

        model.addAttribute("nombresEmpresas", nombreEmpresas);
        model.addAttribute("valoresEmpresas", valoresEmpresas);
        model.addAttribute("nombresCursos", nombreCursos);
        model.addAttribute("valoresCursos", valoresCursos);
		return "estadisticas/estadisticas";
	}
}
