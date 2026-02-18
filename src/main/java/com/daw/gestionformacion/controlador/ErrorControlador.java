package com.daw.gestionformacion.controlador;


import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorControlador implements ErrorController {

    @GetMapping("/error")
    public String manejarError(HttpServletRequest request, Model model) {
        Object estadoObj = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object mensajeObj = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);

        int estado = estadoObj != null ? Integer.parseInt(estadoObj.toString()) : 500;
        String mensaje;

        switch (estado) {
            case 400:
                mensaje = "Solicitud incorrecta.";
                break;
            case 403:
                mensaje = "Acceso denegado.";
                break;
            case 404:
                mensaje = "Página no encontrada.";
                break;
            case 500:
                mensaje = "Error interno del servidor.";
                break;
            default:
                mensaje = mensajeObj != null ? mensajeObj.toString() : "Ha ocurrido un error inesperado.";
        }

        model.addAttribute("estado", estado);
        model.addAttribute("mensaje", mensaje);

        return "error"; 
    }
}
