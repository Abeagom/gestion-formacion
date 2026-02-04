package com.daw.gestionformacion.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daw.gestionformacion.modelo.Empresa;

public interface EmpresaRepository extends JpaRepository<Empresa, Integer>{

}
