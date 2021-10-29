package com.patitas_perdidas.app.repositorios;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.patitas_perdidas.app.entidades.Persona;

public interface PersonaRepositorio extends JpaRepository<Persona, String> {
	// Busca una lista de personas activas, retorna una lista de personas
	@Query("SELECT p FROM Persona p WHERE p.alta = true")
	public List<Persona> buscarListaPersonas();

	// Busca una lista de personas popr nombre, retorna una lista de personas
	@Query("SELECT p FROM Persona p WHERE p.alta = true and p.nombre LIKE %:nombre%")
	public List<Persona> buscarListaPersonasNombre(@Param("nombre") String nombre);

	// Devuelve una persona por su nombre.
	@Query("SELECT p FROM Persona p WHERE p.nombre = :nombre")
	public Optional<Persona> buscarPorNombre(@Param("nombre") String nombre);
	
	// Devuelve una persona por su email.
	@Query("SELECT p FROM Persona p WHERE p.mail = :mail")
	public Optional<Persona> buscarPorMail(@Param("mail") String mail);
}
