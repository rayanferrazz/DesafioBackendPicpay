package desafio.backend.picpay.dto;

import java.math.BigDecimal;

import desafio.backend.picpay.domain.UserType;

public record UserDTO(String name, String document, BigDecimal balance, String email, 
		String password, UserType userType) {
}