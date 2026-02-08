package com.daw.gestionformacion.seguridad;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ConfiguracionSeguridad {

	private UsuarioServicio usuarioServicio;

	public ConfiguracionSeguridad (UsuarioServicio usuarioServicio) {
		this.usuarioServicio = usuarioServicio;
	}

	@Bean
	public PasswordEncoder codificadorContraseña() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain cadenaSeguridad(HttpSecurity http) throws Exception {

		http.authorizeHttpRequests(auth -> auth.requestMatchers("/login").permitAll() // login para todos
				.requestMatchers("/directiva/**").hasRole("DIRECTIVA") // solo directiva
				.anyRequest().authenticated() // resto profesores logueados
		).formLogin(form -> form.loginPage("/login") // página de login
				.usernameParameter("email")// usamos email como username
				.defaultSuccessUrl("/", true).permitAll())
				.logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/login?logout").permitAll()
				);

		return http.build();
	}

	@Bean
	public AuthenticationManager gestorAutentificacion (AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

}
