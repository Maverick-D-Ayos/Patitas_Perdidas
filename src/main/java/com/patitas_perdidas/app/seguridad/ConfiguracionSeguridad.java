package com.patitas_perdidas.app.seguridad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.patitas_perdidas.app.servicios.PersonaServicio;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)

public class ConfiguracionSeguridad extends WebSecurityConfigurerAdapter{
	@Autowired
	public PersonaServicio personaServicio;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(personaServicio).passwordEncoder(new BCryptPasswordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()

				.antMatchers("/css/*", "/js/*", "/img/*", "/**").permitAll().and().formLogin().loginPage("/")
				.loginProcessingUrl("/logincheck").usernameParameter("mail").passwordParameter("clave")
				.defaultSuccessUrl("/").permitAll().and().logout().logoutUrl("/logout")
				.logoutSuccessUrl("/").permitAll().and().csrf().disable();
	}

}
