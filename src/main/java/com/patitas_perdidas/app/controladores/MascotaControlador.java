package com.patitas_perdidas.app.controladores;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import com.patitas_perdidas.app.excepciones.MascotaExcepcion;
import com.patitas_perdidas.app.servicios.MascotaServicio;

@Controller
@RequestMapping("/mascota")
public class MascotaControlador {
	@Autowired
	private MascotaServicio ms;
	
	@GetMapping("/registro")
	public String muestraRegistro() {
		return " ";
	}
	
	@PostMapping("/registro")
	public String registra(ModelMap modelo,String nombre, String descripcion, String color, String raza, String tamaño,
			Boolean encontrado, String fecha, String especie, String zona, MultipartFile archivo) throws ParseException {
		Date date = new SimpleDateFormat("dd/MM/yyyy").parse(fecha);
		ms.crearMascota(nombre, descripcion, color, raza, tamaño, encontrado, date, especie, zona, archivo);
		modelo.put("Exito", "Registro exitoso");
		return " ";
	}
	
	@GetMapping("/actualizar/{id}")
	public String muestraActualiza(ModelMap modelo,@PathVariable String id) throws Exception {
		Mascota m=ms.buscaPorId(id);
		modelo.addAttribute(m);
		return " ";
	}
	
	@PostMapping("/actualizar/{id}")
	public String actualiza(ModelMap modelo,String id, String nombre, String descripcion, String color, String raza, String tamaño,
			Boolean encontrado, String fecha, String especie, String zona, MultipartFile archivo) throws ParseException, MascotaExcepcion {
		Date date = new SimpleDateFormat("dd/MM/yyyy").parse(fecha);
		ms.modificarMascota(id, nombre, descripcion, color, raza, tamaño, encontrado, date, especie, zona, archivo);
		modelo.put("Exito", "Actualoizacion exitosa");
		return " ";
	}
	
	@GetMapping("/eliminar/{id}")
	public String elimina(ModelMap modelo, @PathVariable String id) {
		ms.eliminarMascota(id);
		return "";
	}
	
	@GetMapping("/lista")
	public String listar(ModelMap modelo) {
		List<Mascota> muestraMascotas= ms.listarTodasMascotas();
		modelo.addAttribute("listaMascota", muestraMascotas);
		return "";
	}
	
	@GetMapping("/listar")
	public String listarActivos(ModelMap modelo) {
		List<Mascota> muestraMascotas=ms.listarMascotasActivasPerdidas();
		modelo.addAttribute("listaMascotasActivas", muestraMascotas);
		return "";
	}
	
	@GetMapping("/listar/{raza}")
	public String listarPorRaza(ModelMap modelo, @PathVariable String raza) {
		List<Mascota> muestraMascotas=ms.listarMascotasPorRaza(raza);
		modelo.addAttribute("listaMascotasxRaza", muestraMascotas);
		return "";
	}
	
	@GetMapping("/listar/{color}")
	public String listarPorColor(ModelMap modelo, @PathVariable String color) {
		List<Mascota> muestraMascotas=ms.listarMascotasColor(color);
		modelo.addAttribute("listaMascotasxColor", muestraMascotas);
		return "";
	}
}
