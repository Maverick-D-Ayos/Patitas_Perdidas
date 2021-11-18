package com.patitas_perdidas.app.controladores;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.patitas_perdidas.app.entidades.ConfirmationToken;
import com.patitas_perdidas.app.entidades.Persona;
import com.patitas_perdidas.app.excepciones.PersonaExcepcion;
import com.patitas_perdidas.app.servicios.PersonaServicio;

@Controller
@RequestMapping("/persona")
public class PersonaControlador {
	
	@Autowired
	private PersonaServicio personaServicio;

	@GetMapping("/lista")
	public String lista(ModelMap modelo) {

		List<Persona> todos = personaServicio.listarTodos();
		modelo.addAttribute("personas", todos);

		return "list-persona";
	}

	@GetMapping("/registro")
	public String formulario() {
		return "registro.html";
	}

	@PostMapping("/registro")
	public String guardar(RedirectAttributes redirAttrs, @RequestParam String nombre, @RequestParam Long telefono,
			@RequestParam String mail, @RequestParam String clave) {

		try {
			personaServicio.guardar(nombre, telefono, mail, clave);
			personaServicio.sendMail(mail, nombre);
		} catch (PersonaExcepcion e) {
			redirAttrs.addFlashAttribute("nombre",nombre);
			redirAttrs.addFlashAttribute("telefono",telefono);
			redirAttrs.addFlashAttribute("mail",mail);
			redirAttrs.addFlashAttribute("errorReg", e.getMessage());
			return ("redirect:./registro");
		}
		redirAttrs.addFlashAttribute("exito", "Se ha registrado sastifactoriamente. Ahora puede ir a iniciar sesion.");
		return ("redirect:/");

	}


	@PreAuthorize("hasAnyRole('ROLE_USER')")
	@GetMapping("/baja/id")
	public String baja(@PathVariable String id) {
		try {
			personaServicio.baja(id);
			return "redirect:/persona/lista";
		} catch (Exception e) {
			return "redirect:/";
		}
	}

	@GetMapping("/alta/id")
	public String alta(@PathVariable String id) {
		try {
			personaServicio.alta(id);
			return "redirect:/persona/lista";
		} catch (Exception e) {
			return "redirect:/";
		}

	}

	@PreAuthorize("hasAnyRole('ROLE_USER')")
	@GetMapping("/perfil/{id}")
	public String perfilUsuario(HttpSession session, ModelMap modelo, @PathVariable String id) throws PersonaExcepcion {
		Persona person = (Persona) session.getAttribute("clientesession");
		if (person == null || !person.getId().equals(id)) {
			return "redirect:/inicio";
		}
		Persona usuario = personaServicio.buscaPorId(id);
		modelo.addAttribute("usuario", usuario);
		return "perfil.html";
	}

	@PreAuthorize("hasAnyRole('ROLE_USER')")
	@GetMapping("/modificar-pass/{id}")
	public String modificarPass(HttpSession session, ModelMap model, @PathVariable String id) throws PersonaExcepcion {
		Persona person = (Persona) session.getAttribute("clientesession");
		if (person == null || !person.getId().equals(id)) {
			return "redirect:/inicio";
		}

		Persona usuario = personaServicio.buscaPorId(id);
		model.addAttribute("usuario", usuario);
		model.put("pass", "pass");
		return "perfil.html";

	}
	@PreAuthorize("hasAnyRole('ROLE_USER')")
	@PostMapping("/modificar-pass/{id}")
	public String modificarPassPost(HttpSession session, ModelMap model, @PathVariable String id, @RequestParam String clave1, @RequestParam String clave2) throws PersonaExcepcion {
		Persona person = (Persona) session.getAttribute("clientesession");
		if (person == null || !person.getId().equals(id)) {
			return "redirect:/inicio";
		}
		try
		{
			personaServicio.modificarClave(id, clave1, clave2);
			model.put("cambio", "Su contraseña a sido cambiada con exito");
			model.put("pass", "pass");
			Persona usuario = personaServicio.buscaPorId(id);
			model.addAttribute("usuario", usuario);
			model.addAttribute("exito", "La clave a sido modificada exitosamente");
			session.setAttribute("clientesession", usuario);
			return "perfil.html";
		}
		catch(PersonaExcepcion pe)
		{
			model.put("error", pe.getMessage());
			Persona usuario = personaServicio.buscaPorId(id);
			model.addAttribute("usuario", usuario);
			session.setAttribute("clientesession", usuario);
			return "perfil.html";
		}


	}

	@PreAuthorize("hasAnyRole('ROLE_USER')")
	@PostMapping("/modificar/{id}")
	public String modificar(HttpSession session, RedirectAttributes redirAttrs, ModelMap model,
			@PathVariable String id, @RequestParam String nombre, @RequestParam Long telefono,
			@RequestParam String mail) throws PersonaExcepcion {
		try {
			Persona person = (Persona) session.getAttribute("clientesession");
			if (person == null || !person.getId().equals(id)) {
				return "redirect:/inicio";
			}
			personaServicio.modificar(id, nombre, telefono, mail);
			Persona usuario = personaServicio.buscaPorId(id);
			model.addAttribute("usuario", usuario);
			session.setAttribute("clientesession", usuario);
			model.put("exito", "Su perfil ha sido modificado exitosamente");
			redirAttrs.addAttribute("id", id);
			return "perfil.html";
		} catch(PersonaExcepcion e) {
			model.put("error", e.getMessage());
			Persona usuario = personaServicio.buscaPorId(id);
			model.addAttribute("usuario", usuario);
			session.setAttribute("clientesession", usuario);
			return "perfil.html";
		} catch (Exception e) {
			model.put("error", "Falto ingresar el nombre");
			redirAttrs.addAttribute("id", id);

			return "redirect:/persona/perfil/{id}";
		} 
	}
	
	@RequestMapping(value="/forgot-password", method=RequestMethod.GET)
    public ModelAndView displayResetPassword(ModelAndView modelAndView, Persona user) {
        modelAndView.addObject("user", user);
        modelAndView.setViewName("passwordOlvidada");
        return modelAndView;
    }

    // Receive the address and send an email
    @RequestMapping(value="persona/forgot-password", method=RequestMethod.POST)
    public ModelAndView forgotUserPassword(ModelAndView modelAndView, Persona user) {
        Persona existingUser = personaServicio.buscarPorEmail2(user.getMail());
        System.out.println("Entramos sin saber porque");
        if (existingUser != null) {
        	
            // Create token
            ConfirmationToken confirmationToken = new ConfirmationToken(existingUser);

            // Save it
            personaServicio.guardarToken(confirmationToken);

            // Create the email
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(existingUser.getMail());
            mailMessage.setSubject("Complete Password Reset!");
            mailMessage.setFrom("patitasperdidas.egg@gmail.com");
            mailMessage.setText("To complete the password reset process, please click here: "
              + "http://localhost:8080/persona/confirm-reset/"+confirmationToken.getConfirmationToken());

            // Send the email
            personaServicio.sendMailContraseña(mailMessage);

            modelAndView.addObject("message", "Request to reset password received. Check your inbox for the reset link.");
            modelAndView.setViewName("successForgotPassword");

        } else {
            modelAndView.addObject("message", "This email address does not exist!");
            modelAndView.setViewName("error");
        }
        return modelAndView;
    }
    
    // Endpoint to confirm the token
    @RequestMapping(value="confirm-reset/{token}", method= {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView validateResetToken(ModelAndView modelAndView, @PathVariable String token) {
       System.out.println(token+"Tendria que aparecer");
    	ConfirmationToken token1 = personaServicio.confirmarToken(token);
        System.out.println(token);
        if (token != null) {
            Persona user =personaServicio.buscarPorEmail2(token1.getUser().getMail());
            try {
				personaServicio.alta(user.getId());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            modelAndView.addObject("user", user);
            modelAndView.addObject("mail", user.getMail());
            modelAndView.setViewName("resetPassword");
        } else {
            modelAndView.addObject("message", "The link is invalid or broken!");
            modelAndView.setViewName("error");
        }
        return modelAndView;
    }
    
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    // Endpoint to update a user's password
    @RequestMapping(value = "confirm-reset/persona/reset-password", method = RequestMethod.POST)
    public ModelAndView resetUserPassword(ModelAndView modelAndView, Persona user) {
        System.out.println("Entro a la funcion");
    	if (user.getMail() != null) {
            // Use email to find user
            Persona tokenUser = personaServicio.buscarPorEmail(user.getMail());
            tokenUser.setClave(encoder.encode(user.getClave()));
            personaServicio.guardarPersona(tokenUser);
            modelAndView.addObject("message", "Password successfully reset. You can now log in with the new credentials.");
            modelAndView.setViewName("successResetPassword");
        } else {
            modelAndView.addObject("message","The link is invalid or broken!");
            modelAndView.setViewName("error");
        }
        return modelAndView;
    }
}