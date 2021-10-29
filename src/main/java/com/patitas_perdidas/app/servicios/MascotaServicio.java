package com.patitas_perdidas.app.servicios;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.mail.Multipart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.patitas_perdidas.app.entidades.Mascota;
import com.patitas_perdidas.app.repositorios.MascotaRepositorio;

@Service
public class MascotaServicio {
	
	@Autowired
	private MascotaRepositorio mr;
	@Autowired
	private FotoServicio fs;
	
	public void crearMascota(String nombre, String descripcion, String color, String raza, String tamaño,
			Boolean encontrado, Date fecha, String especie, String zona, MultipartFile archivo) {
		Mascota m=new Mascota();
		
		m.setNombre(nombre);
		m.setDescripcion(descripcion);
		m.setColor(color);
		m.setRaza(raza);
		m.setTamaño(tamaño);
		m.setEncontrado(encontrado);
		m.setFecha(fecha);
		m.setEspecie(especie);
		m.setAlta(true);
		m.setZona(zona);
		m.setFoto(fs.guardarFoto(archivo));
		
		mr.save(m);	
	}
	
	public void eliminarMascota(String id) {
		Mascota m=mr.getById(id);
		mr.delete(m);
	}
	
	public void modificarMascota(String id,String nombre, String descripcion, String color, String raza, String tamaño,
			Boolean encontrado, Date fecha, String especie, String zona, MultipartFile archivo) {
		Mascota m=mr.getById(id);
		
		m.setNombre(nombre);
		m.setDescripcion(descripcion);
		m.setColor(color);
		m.setRaza(raza);
		m.setTamaño(tamaño);
		m.setEncontrado(encontrado);
		m.setFecha(fecha);
		m.setEspecie(especie);
		m.setAlta(true);
		m.setZona(zona);
		m.setFoto(fs.guardarFoto(archivo));
		
		mr.save(m);	
	}
	
	public List<Mascota> listarMascotas(){
		List<Mascota> lm= mr.findAll();
		return lm;
	}
	
	public Mascota buscaPorId(String id) throws Exception{
		Optional<Mascota> oMascota=mr.findById(id);
		if(oMascota.isPresent()) {
			Mascota mascota = oMascota.get();
			return mascota;
		}else {
			throw new Exception("No se encuentra la mascota");
		}
	}
	
	
}
