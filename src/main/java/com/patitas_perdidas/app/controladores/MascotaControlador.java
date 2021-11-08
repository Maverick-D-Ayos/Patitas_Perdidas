package com.patitas_perdidas.app.controladores;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.patitas_perdidas.app.entidades.Mascota;
import com.patitas_perdidas.app.entidades.Persona;
import com.patitas_perdidas.app.excepciones.MascotaExcepcion;
import com.patitas_perdidas.app.excepciones.PersonaExcepcion;
import com.patitas_perdidas.app.servicios.MascotaServicio;
import com.patitas_perdidas.app.servicios.PersonaServicio;

@Controller
@RequestMapping("/mascota")
public class MascotaControlador {
	@Autowired
	private MascotaServicio ms;

	@Autowired
	private PersonaServicio personaServicio;

	// el metodo de encontrada y perdida podria ser uno solo pero cuando puse que
	// retorne al index se mostraba mal.
	//06-11 // puse el id de la persona para poder usarala
	@PreAuthorize("hasAnyRole('ROLE_USER')")
	@GetMapping("/registroencontrada/{id_persona}")
	public String registroencontrada(HttpSession session, ModelMap model, Model modelo, @PathVariable String id_persona)
			throws PersonaExcepcion {
		Persona person = (Persona) session.getAttribute("clientesession");
		if (person == null || !person.getId().equals(id_persona)) {
			return "redirect:/inicio";
		}

		Persona usuario = personaServicio.buscaPorId(id_persona);
		model.addAttribute("usuario", usuario);
		modelo.addAttribute("encontrada", "encontrada");
		return "registro-mascota";
	}

	@PreAuthorize("hasAnyRole('ROLE_USER')")
	@PostMapping("/registroencontrada/{id_persona}")
	public String registroencontrada(HttpSession session, RedirectAttributes redirAttrs, ModelMap modelo,
			@PathVariable String id_persona, @RequestParam String nombre, @RequestParam String descripcion,
			@RequestParam String color, @RequestParam String raza, @RequestParam String tamanio,
			@RequestParam Boolean encontrado, @RequestParam String fecha, @RequestParam String especie,
			@RequestParam String zona, @RequestParam MultipartFile archivo) throws PersonaExcepcion {
		try {
			Persona person = (Persona) session.getAttribute("clientesession");
			if (person == null || !person.getId().equals(id_persona)) {
				return "redirect:/inicio";
			}
			Date date = new SimpleDateFormat("yyyy-MM-dd").parse(fecha);
			personaServicio.aniadirMascota(id_persona, nombre, descripcion, color, raza, tamanio, encontrado, date,
					especie, zona, archivo);
			Persona usuario = personaServicio.buscaPorId(id_persona);
			modelo.addAttribute("usuario", usuario);
			redirAttrs.addFlashAttribute("exito", "Se añadio la mascota con exito");

			return "redirect:/mascota/registroencontrada/{id_persona}";
		} catch (MascotaExcepcion e) {
			redirAttrs.addFlashAttribute("error", e.getMessage());
			return ("redirect:/mascota/registroencontrada/{id_persona}");
		} catch (ParseException e) {
			redirAttrs.addFlashAttribute("error", "Revise la fecha añadida");
			return ("redirect:/mascota/registroencontrada/{id_persona}");
		} catch (IOException e) {
			redirAttrs.addFlashAttribute("error", "El archivo esta dañado.");
			return ("redirect:/mascota/registroencontrada/{id_persona}");
		}
	}
	
	@PreAuthorize("hasAnyRole('ROLE_USER')")
	@GetMapping("/registroperdida/{id_persona}")
	public String registroperdida(HttpSession session, ModelMap model, Model modelo, @PathVariable String id_persona)
			throws PersonaExcepcion {
		Persona person = (Persona) session.getAttribute("clientesession");
		if (person == null || !person.getId().equals(id_persona)) {
			return "redirect:/inicio";
		}

		Persona usuario = personaServicio.buscaPorId(id_persona);
		model.addAttribute("usuario", usuario);
		modelo.addAttribute("perdida", "perdida");
		return "registro-mascota";
	}

	@PreAuthorize("hasAnyRole('ROLE_USER')")
	@PostMapping("/registroperdida/{id_persona}")
	public String perdida(HttpSession session, RedirectAttributes redirAttrs, ModelMap modelo,
			@PathVariable String id_persona, @RequestParam String nombre, @RequestParam String descripcion,
			@RequestParam String color, @RequestParam String raza, @RequestParam String tamanio,
			@RequestParam Boolean encontrado, @RequestParam String fecha, @RequestParam String especie,
			@RequestParam String zona, @RequestParam MultipartFile archivo) throws PersonaExcepcion {
		try {
			Persona person = (Persona) session.getAttribute("clientesession");
			if (person == null || !person.getId().equals(id_persona)) {
				return "redirect:/inicio";
			}
			Date date = new SimpleDateFormat("yyyy-MM-dd").parse(fecha);
			personaServicio.aniadirMascota(id_persona, nombre, descripcion, color, raza, tamanio, encontrado, date,
					especie, zona, archivo);
			Persona usuario = personaServicio.buscaPorId(id_persona);
			modelo.addAttribute("usuario", usuario);
			redirAttrs.addFlashAttribute("exito", "Se añadio la mascota con exito");

			return "redirect:/mascota/registroperdida/{id_persona}";
		} catch (MascotaExcepcion e) {
			redirAttrs.addFlashAttribute("error", e.getMessage());
			return ("redirect:/mascota/registroperdida/{id_persona}");
		} catch (ParseException e) {
			redirAttrs.addFlashAttribute("error", "Revise la fecha añadida");
			return ("redirect:/mascota/registroperdida/{id_persona}");
		} catch (IOException e) {
			redirAttrs.addFlashAttribute("error", "El archivo esta dañado.");
			return ("redirect:/mascota/registroperdida/{id_persona}");
		}
	}
	
	@GetMapping("/actualizar/{id}")
	public String muestraActualiza(ModelMap modelo, @PathVariable String id) throws Exception {
		Mascota m = ms.buscaPorId(id);
		modelo.addAttribute(m);
		return " ";
	}

	@PostMapping("/actualizar/{id}")
	public String actualiza(ModelMap modelo, String id, String nombre, String descripcion, String color, String raza,
			String tamanio, Boolean encontrado, String fecha, String especie, String zona, MultipartFile archivo)
			throws ParseException, MascotaExcepcion, IOException {
		Date date = new SimpleDateFormat("yyyy-MM-dd").parse(fecha);
		ms.modificarMascota(id, nombre, descripcion, color, raza, tamanio, encontrado, date, especie, zona, archivo);
		modelo.put("Exito", "Actualizacion exitosa");
		return " ";
	}

	@GetMapping("/eliminar/{id}")
	public String elimina(ModelMap modelo, @PathVariable String id) {
		ms.eliminarMascota(id);
		return "";
	}

	@GetMapping("/lista")
	public String listar(ModelMap modelo) {
		List<Mascota> muestraMascotas = ms.listarTodasMascotas();
		modelo.addAttribute("listaMascota", muestraMascotas);
		return "";
	}

	@GetMapping("/listar")
	public String listarActivos(ModelMap modelo) {
		List<Mascota> muestraMascotas = ms.listarMascotasActivasPerdidas();
		modelo.addAttribute("listaMascotasActivas", muestraMascotas);
		return "mascotasPerdidas.html";
	}

	@GetMapping("/listarE")
	public String listarActivos1(ModelMap modelo) {
		List<Mascota> muestraMascotas = ms.listarMascotasActivasEncontradas();
		modelo.addAttribute("listaMascotasEncontradas", muestraMascotas);
		return "mascotasEncontradas.html";
	}

	@GetMapping("/listar/{raza}")
	public String listarPorRaza(ModelMap modelo, @PathVariable String raza) {
		List<Mascota> muestraMascotas = ms.listarMascotasPorRaza(raza);
		modelo.addAttribute("listaMascotasxRaza", muestraMascotas);
		return "";
	}

	@GetMapping("/listar/{color}")
	public String listarPorColor(ModelMap modelo, @PathVariable String color) {
		List<Mascota> muestraMascotas = ms.listarMascotasColor(color);
		modelo.addAttribute("listaMascotasxColor", muestraMascotas);
		return "";
	}
	@GetMapping("/mis-mascotas")
	public String getMascotasPersona(ModelMap model, @RequestParam String id) throws PersonaExcepcion
	{
		List<Mascota> lista = ms.getMascotasPersona(id);
		model.put("listaMascotas", lista);
		return "mis-mascotas.html";
	}
}
