package com.patitas_perdidas.app.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.patitas_perdidas.app.entidades.Mascota;

public interface MascotaRepositorio extends JpaRepository<Mascota, String> {

	// Busca una lista de mascota perdidas, retorna una lista de mascotas
	@Query("SELECT m FROM Mascota m WHERE m.alta = true and m.encontrado = false")
	public List<Mascota> buscarListaPerdidos();

	// Busca una lista de mascota encontradas, retorna una lista de mascotas
	@Query("SELECT m FROM Mascota m WHERE m.alta = true and m.encontrado = true")
	public List<Mascota> buscarListaEncontrados();

	// Busca una lista de mascota raza, retorna una lista de mascotas
	@Query("SELECT m FROM Mascota m WHERE m.alta = true and m.raza = :raza")
	public List<Mascota> buscarListaRaza(@Param("raza") String raza);

	// Busca una lista de mascota tamaño, retorna una lista de mascotas
	@Query("SELECT m FROM Mascota m WHERE m.alta = true and m.tamaño = :tamaño")
	public List<Mascota> buscarListaTamaño(@Param("tamaño") String tamaño);

	// Busca una lista de mascota color, retorna una lista de mascotas
	@Query("SELECT m FROM Mascota m WHERE m.alta = true and m.color = :color")
	public List<Mascota> buscarListaColor(@Param("color") String color);

	// Busca una lista de mascota especie, retorna una lista de mascotas
	@Query("SELECT m FROM Mascota m WHERE m.alta = true and m.especie = :especie")
	public List<Mascota> buscarListaEspecie(@Param("especie") String especie);
	
	// Busca una lista de mascota zona, retorna una lista de mascotas
	@Query("SELECT m FROM Mascota m WHERE m.alta = true and m.zona = :zona")
	public List<Mascota> buscarListaZona(@Param("zona") String zona);
	
	
}
