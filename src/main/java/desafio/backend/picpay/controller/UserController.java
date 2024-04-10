package desafio.backend.picpay.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import desafio.backend.picpay.domain.User;
import desafio.backend.picpay.dto.UserDTO;
import desafio.backend.picpay.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;
	
	@GetMapping
	public ResponseEntity<List<User>> getAllUsers() {
		var users = this.userService.getAllUsers();
		return new ResponseEntity<>(users, HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<User> createUser(@RequestBody UserDTO user) {
		User newUser = userService.createUser(user);
		return new ResponseEntity<>(newUser, HttpStatus.CREATED);
	}
}