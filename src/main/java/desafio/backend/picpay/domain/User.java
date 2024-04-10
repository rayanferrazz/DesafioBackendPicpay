package desafio.backend.picpay.domain;

import java.math.BigDecimal;

import desafio.backend.picpay.dto.UserDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;

@Entity(name = "users")
@Table(name = "users")
@EqualsAndHashCode(of = "id")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	@Column(unique = true)
	private String document;
	
	@Column(unique = true)
	private String email;
	
	private String password;
	
	private UserType userType;
	
	private BigDecimal balance;

	public User() {
	}
	
	public User(UserDTO dto) {
		this.name = dto.name();
		this.document = dto.document();
		this.email = dto.email();
		this.password = dto.password();
		this.userType = dto.userType();
		this.balance = dto.balance();
	}
	
	public User(Long id, String name, String document, String email, String password, UserType userType,
			BigDecimal balance) {
		this.id = id;
		this.name = name;
		this.document = document;
		this.email = email;
		this.password = password;
		this.userType = userType;
		this.balance = balance;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
}