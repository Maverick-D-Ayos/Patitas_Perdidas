package com.patitas_perdidas.app.controladores;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.patitas_perdidas.app.entidades.Mascota;
import com.patitas_perdidas.app.servicios.MascotaServicio;

@Controller
@RequestMapping("/mascota")
public class MascotaControlador {
	@Autowired
	private MascotaServicio ms;
	
	@GetMapping("/mascota/registro")
	public String muestraRegistro() {
		return " ";
	}
	
	@PostMapping("/mascota/registro")
	public String registra(ModelMap modelo,String nombre, String descripcion, String color, String raza, String tamaño,
			Boolean encontrado, Date fecha, String especie, String zona, MultipartFile archivo) {
		ms.crearMascota(nombre, descripcion, color, raza, tamaño, encontrado, fecha, especie, zona, archivo);
		modelo.put("Exito", "Registro exitoso");
		return " ";
	}
	
	@GetMapping("/mascota/actualizar/{id}")
	public String muestraActualiza(ModelMap modelo,@PathVariable String id) throws Exception {
		Mascota m=ms.buscaPorId(id);
		modelo.addAttribute(m);
		return " ";
	}
	
	@PostMapping("/mascota/actualizar/{id}")
	public String actualiza(ModelMap modelo,String id, String nombre, String descripcion, String color, String raza, String tamaño,
			Boolean encontrado, Date fecha, String especie, String zona, MultipartFile archivo) {
		ms.modificarMascota(id, nombre, descripcion, color, raza, tamaño, encontrado, fecha, especie, zona, archivo);
		modelo.put("Exito", "Actualizacion exitosa");
		return " ";
	}
	
	@GetMapping("/mascota/eliminar/{id}")
	public String elimina(ModelMap modelo, @PathVariable String id) {
		ms.eliminarMascota(id);
		return "";
	}
	
	@GetMapping("/mascota/lista")
	public String listar(ModelMap modelo) {
		List<Mascota> muestraMascotas= ms.listarMascotas();
		modelo.addAttribute("listaMascota", muestraMascotas);
		return "";
	}
	
}
