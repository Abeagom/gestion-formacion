# Práctica sobre Gestión de Formación (FCT)

Aplicación web desarrollada con **Spring Boot** para la gestión de la Fase de Formación en Empresa (FCT) de los alumnos.
![Estado](https://img.shields.io/badge/Estado-En%20Desarrollo-yellow?style=flat-square)
![Java](https://img.shields.io/badge/Java-17-orange?style=flat-square&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.10-brightgreen?style=flat-square&logo=springboot)

## Tecnologías
- **Lenguaje:** Java 17
- **Framework:** Spring Boot 3.5.10
- **Persistencia:** Spring Data JPA (Hibernate)
- **Base de Datos:** MySQL

## Modelo de Datos (Entidades)
- **Alumno:** Cada alumno pertenece a un curso y tiene una **única** práctica asignada (Relación 1:1).
- **Profesor:** Sistema de usuarios con roles diferenciados mediante `TipoProfesor` (DIRECTIVA y PROFESOR).
- **Curso:** Gestión de grupos de alumnos supervisados por un profesor.
- **Empresa:** Catálogo de centros de trabajo con información del tutor laboral.
- **Práctica:** Entidad central que vincula Alumno y Empresa, gestionando fechas y seguimiento.
