package desafio.backend.picpay.service;

import org.springframework.stereotype.Service;

import desafio.backend.picpay.domain.User;

@Service
public class NotificationService {

	public void sendNotification(User user, String message) {
		
		String name = user.getName();
		
		System.out.println(name + " - " + message);
	}
}