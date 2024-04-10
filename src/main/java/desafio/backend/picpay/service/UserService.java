package desafio.backend.picpay.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import desafio.backend.picpay.domain.User;
import desafio.backend.picpay.domain.UserType;
import desafio.backend.picpay.dto.UserDTO;
import desafio.backend.picpay.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	public void saveUser(User user) {
		this.userRepository.save(user);
	}
	
	public User createUser(UserDTO user) {
		User newUser = new User(user);
		this.saveUser(newUser);
		return newUser;
	}

	public List<User> getAllUsers() {
		return this.userRepository.findAll();
	}
	
	public User findUserById(Long id) throws Exception {
		return this.userRepository.findById(id).orElseThrow( 
				() -> new Exception("Usuário não encontrado!"));
	}

	public boolean validateUser(User payer, BigDecimal amount) throws Exception {

		if (payer.getUserType() == UserType.MERCHANT) {
			throw new Exception("Um usuário lojista não pode realizar transações!");
		}
		
		if (payer.getBalance().compareTo(amount) < 0) {
			throw new Exception("Saldo insuficiente!");
		}
		return true;
	}
}