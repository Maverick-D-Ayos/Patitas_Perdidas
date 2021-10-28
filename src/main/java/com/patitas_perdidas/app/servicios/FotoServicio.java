package com.patitas_perdidas.app.servicios;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.patitas_perdidas.app.entidades.Foto;
import com.patitas_perdidas.app.repositorios.FotoRepositorio;

@Service
public class FotoServicio {
	
	@Autowired
	private FotoRepositorio fotoRepositorio;
	
	public Foto guardarFoto(MultipartFile archivo) {
		if(archivo != null) {
			try {
				Foto foto = new Foto();
				foto.setMime(archivo.getContentType());
				foto.setNombre(archivo.getName());
				foto.setContenido(archivo.getBytes());
				
				return fotoRepositorio.save(foto);
			} catch(Exception e) {
				System.err.println(e.getMessage());
			}
		}
	  return null;
	}
	
	public Foto actualizarFoto(String id, MultipartFile archivo) {
		if(archivo != null) {
			try {
				Foto foto = new Foto();
				
				if(id != null) {
					Optional<Foto> respuesta = fotoRepositorio.findById(id);
					if(respuesta.isPresent()) {
						foto = respuesta.get();
					}
					
				}
				foto.setMime(archivo.getContentType());
				foto.setNombre(archivo.getName());
				foto.setContenido(archivo.getBytes());
				
				return fotoRepositorio.save(foto);
			} catch(Exception e) {
				System.err.println(e.getMessage());
			}
		}
	  return null;
		
		
		
		
	}
}
