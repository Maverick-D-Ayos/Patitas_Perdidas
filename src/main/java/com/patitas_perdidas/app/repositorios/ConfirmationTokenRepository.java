package com.patitas_perdidas.app.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.patitas_perdidas.app.entidades.ConfirmationToken;



public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, String> {
	ConfirmationToken findByConfirmationToken(String confirmationToken);
}
