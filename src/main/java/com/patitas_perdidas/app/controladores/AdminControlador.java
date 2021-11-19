package com.patitas_perdidas.app.controladores;


import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
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
import com.patitas_perdidas.app.enums.Rol;
import com.patitas_perdidas.app.excepciones.MascotaExcepcion;
import com.patitas_perdidas.app.excepciones.PersonaExcepcion;
import com.patitas_perdidas.app.servicios.MascotaServicio;
import com.patitas_perdidas.app.servicios.PersonaServicio;

@Controller
@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
@RequestMapping("/dashboard")
public class AdminControlador {

	@Autowired
	private PersonaServicio personaServicio;
	
	@Autowired
	private MascotaServicio ms;

	@GetMapping("")
	public String lista(ModelMap model) throws ParseException {
		
		model.addAttribute("countMasc", ms.countMascotasActivas());
		model.addAttribute("countPers", personaServicio.countPersonasActivas());
		model.addAttribute("countAdmin", personaServicio.countAdmin());
		model.addAttribute("countUserWeek", personaServicio.userlastWeek());
		model.addAttribute("countPersBaja", personaServicio.countPersonasBaja());
		model.addAttribute("countMascBaja", ms.countMascotasBaja());
		
		List<Date> fechas = personaServicio.dia6();
		Map<String, Integer> graphData = new TreeMap<>();
		for (Date fecha : fechas) {
			SimpleDateFormat format = new SimpleDateFormat("dd/MM");
			String dateString = format.format(fecha);
			if(graphData.containsKey(dateString))
			{
				int n = graphData.get(dateString) + 1;
				graphData.put(dateString, n);
			}
			else
				graphData.put(dateString, personaServicio.getUsuarioFecha(fecha));			
		}
		model.addAttribute("chartData", graphData);
		
		Map<String, Integer> graphData2 = new TreeMap<>();
        graphData2.put("Perro", ms.countMascotasPerro());
        graphData2.put("Gato", ms.countMascotasGatos());
        graphData2.put("Otros", ms.countMascotasOtros());

        model.addAttribute("chartData2", graphData2);
        
        List<Date> fechasMasc = ms.dia6();
        Map<String, Integer> graphData3 = new TreeMap<>();
        for (Date fecha : fechasMasc) {
			SimpleDateFormat format = new SimpleDateFormat("dd/MM");
			String dateString = format.format(fecha);
			if(graphData3.containsKey(dateString))
			{
				int n = graphData3.get(dateString) + 1;
				graphData3.put(dateString, n);
			}
			else
				graphData3.put(dateString, ms.getMascotaFecha(fecha));			
		}
        model.addAttribute("chartData3", graphData3);
        		
		return "dashboard.html";
	}
	@GetMapping("/usuarios")
	public String getUsuarios(ModelMap model, @RequestParam(value = "search", required = false) String search) throws PersonaExcepcion
	{
		try
		{			
			model.addAttribute("usuarios", personaServicio.listarPersonasBusqueda(search));
		}
		catch(PersonaExcepcion p)
		{
			model.put("error", p.getMessage());
		}
		
		return "dashboard-usuarios.html";
	}
	
	@GetMapping("/usuarios/baja")
	public String baja(@RequestParam String id) {
		try {
			personaServicio.baja(id);
			return "redirect:/dashboard/usuarios";
		} catch (Exception e) {
			return "redirect:/dashboard/usuarios";
		}
	}
	@GetMapping("/usuarios/editar")
	public String editar(HttpSession session, ModelMap model, @RequestParam String id) throws PersonaExcepcion 
	{		
		Persona usuario = personaServicio.buscaPorId(id);
		model.addAttribute("usuario", usuario);
		model.addAttribute("clientesession", session);
		return "dashboard-usuarios-editar";
	}
	@PostMapping("/usuarios/editar")
	public String editarPost(HttpSession session, RedirectAttributes redirAttrs, ModelMap model,
			@RequestParam String id, @RequestParam String nombre, @RequestParam Long telefono,
			@RequestParam String mail, @RequestParam(required = false) Rol rol) throws PersonaExcepcion {
		try {
			personaServicio.modificarAdmin(id, nombre, telefono, mail, rol);
			model.put("exito", "Su perfil ha sido modificado exitosamente");
			return "redirect:/dashboard/usuarios";
		} catch(PersonaExcepcion e) {
			model.put("error", e.getMessage());
			return "dashboard-usuarios.html";
		} catch (Exception e) {
			model.put("error", "Falto ingresar el nombre");
			
			return "redirect:/dashboard/usuarios";
		} 
	}
//	@GetMapping("/mascotas")
//	public String getMascotas(ModelMap model) throws MascotaExcepcion
//	{
//		try
//		{
//			model.addAttribute("mascotas", ms.listarTodasMascotasActivas());
//		}
//		catch(MascotaExcepcion m)
//		{
//			model.put("error", m.getMessage());
//		}
//		
//		return "dashboard-mascotas.html";
//	}
	@GetMapping("/mascotas")
	public String getMascotasSearch(ModelMap model, @RequestParam(required = false, value = "search") String search) throws MascotaExcepcion
	{
		model.addAttribute("mascotas", ms.listarMascotasBusqueda(search, "todas"));		
		return "dashboard-mascotas.html";
	}
	@GetMapping("/mascota/baja")
	public String mascotaBaja(@RequestParam String id) {
		try {
			ms.eliminarMascota(id);
		} catch (Exception e) {			
		}
		return "redirect:/dashboard/mascotas";
	}
	@GetMapping("/mascota/editar")
	public String mascotaEditar(HttpSession session, ModelMap model, @RequestParam String id) throws MascotaExcepcion 
	{		
		Mascota mascota = ms.buscaPorId(id);
		model.addAttribute("mascota", mascota);
		return "dashboard-mascota-editar.html";
	}
	@PostMapping("/mascota/editar")
	public String actualiza(HttpSession session, ModelMap model, @RequestParam String id, @RequestParam(required = false) String nombre, @RequestParam String descripcion, @RequestParam String color, @RequestParam String raza,
			@RequestParam String tamanio, @RequestParam Boolean encontrado, @RequestParam String fecha, @RequestParam String especie, @RequestParam String zona, @RequestParam(required = false) MultipartFile archivo)
			throws ParseException, MascotaExcepcion, IOException 
	{
		Date date = new SimpleDateFormat("yyyy-MM-dd").parse(fecha);
		ms.modificarMascota(id, nombre, descripcion, color, raza, tamanio, encontrado, date, especie, zona, archivo);
		model.put("Exito", "Actualizacion exitosa");
		return "redirect:/dashboard/mascotas";
	}

}