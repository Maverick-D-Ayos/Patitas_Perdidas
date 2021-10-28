package com.patitas_perdidas.app.repositorios;

import java.util.List;

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

	public Persona insert(Persona entidad);

}
