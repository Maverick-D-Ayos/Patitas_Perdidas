package com.patitas_perdidas.app.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.patitas_perdidas.app.entidades.Mascota;
import com.patitas_perdidas.app.servicios.MascotaServicio;

@Controller
@RequestMapping("/")
public class IndexControlador {
	@Autowired
	private MascotaServicio ms;
	@GetMapping()
	public String index(@RequestParam(required = false) String error, ModelMap model)
	{
		List<Mascota> ultimasEncontradas=this.ms.listarUltimasMascotasEncontradas();
		List<Mascota> ultimasPerdidas = this.ms.listarUltimasMascotasPerdidas();
		model.addAttribute("ultimasMascotasEncontradas",ultimasEncontradas);
		model.addAttribute("ultimasMascotasPerdidas",ultimasPerdidas);
		if(error != null)
		{
			System.out.println("usuario incorrecto");
			model.put("errorIndex", "Usuario o clave incorrectos");
		}
		return "index.html";
	}
	//List<Mascota> muestraMascotasP = ms.listarUltimasMascotasPerdidas();
		//model.addAttribute("listaMascotasActivas", muestraMascotasP);
		//List<Mascota> muestraMascotasE = ms.listarUltimasMascotasEncontradas();
		//model.addAttribute("listaMascotasEncontradas", muestraMascotasE);
	//a esto no le den bola, es una prueba de diseño
	@GetMapping("/index2")
	public String index2()
	{
		return "index2.html";
	}
	@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
	@GetMapping("/inicio")
	public String inicio(@RequestParam(required = false) String error, ModelMap model) {
		List<Mascota> ultimasMascotasEncontradas=this.ms.listarMascotasActivasEncontradas();
		List<Mascota> ultimasPerdidas = this.ms.listarUltimasMascotasPerdidas();
		model.addAttribute("ultimasMascotasEncontradas",ultimasMascotasEncontradas);
		model.addAttribute("ultimasMascotasPerdidas",ultimasPerdidas);
		if(error != null)
		{
			model.put("errorIndex", "Usuario o clave incorrectos");
			return "index.html";
		}
		else
		{
			return "inicio.html";
		}
		
	}
}


