package com.patitas_perdidas.app.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.patitas_perdidas.app.entidades.Foto;

public interface FotoRepositorio extends JpaRepository<Foto, String> {

}
