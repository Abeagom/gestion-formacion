package com.daw.gestionformacion.seguridad;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import com.daw.gestionformacion.modelo.Profesor;
import com.daw.gestionformacion.repositorio.ProfesorRepositorio;

@Service
public class UsuarioServicio implements UserDetailsService {

    private final ProfesorRepositorio profesorRepositorio;

    public UsuarioServicio (ProfesorRepositorio profesorRepositorio) {
        this.profesorRepositorio = profesorRepositorio;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // Buscar profesor por email
        Profesor profesor = profesorRepositorio.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        //List<SimpleGrantedAuthority> roles = new ArrayList<>();
        //roles.add(new SimpleGrantedAuthority("ROLE_" + profesor.getTipo().name()));
        
        // Devolver un objeto User
        return new User(
            profesor.getEmail(), // email como username
            profesor.getPassword(),
            Collections.singleton( //Singleton es un set de tamaño 1
                new SimpleGrantedAuthority("ROLE_" + profesor.getTipo().name()) // rol
            )
        );
    }
}

