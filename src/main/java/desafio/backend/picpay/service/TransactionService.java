package desafio.backend.picpay.service;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import desafio.backend.picpay.domain.Transaction;
import desafio.backend.picpay.dto.TransactionDTO;
import desafio.backend.picpay.repository.TransactionRepository;

@Service
public class TransactionService {

	@Autowired
	private UserService userService;
	
	@Autowired
	private NotificationService notificationService;
	
	@Autowired
	private TransactionRepository transactionRepository;
	
	@Autowired
	private RestTemplate restTemplate;
	
	public Transaction createTransaction(TransactionDTO transaction) throws Exception {
	
		var payer = this.userService.findUserById(transaction.payerId());
		var payee = this.userService.findUserById(transaction.payeeId());
		
		userService.validateUser(payer, transaction.amount());
		
		boolean isAuthorize = autorizeTransaction();
		
		if (!isAuthorize) {
			throw new Exception("Transação não autorizada!");
		}
		
		Transaction newTransaction = new Transaction();
		newTransaction.setAmount(transaction.amount());
		newTransaction.setPayer(payer);
		newTransaction.setPayee(payee);
		newTransaction.setTransactionTime(LocalDateTime.now());
		
		payer.setBalance(payer.getBalance().subtract(transaction.amount()));
		payee.setBalance(payee.getBalance().add(transaction.amount()));
		
		this.transactionRepository.save(newTransaction);
		this.userService.saveUser(payer);
		this.userService.saveUser(payee);
		
		notificationService.sendNotification(payer, "Transação realizada com sucesso!");
		notificationService.sendNotification(payee, "Transação recebida com sucesso!");
		
		return newTransaction;
	}
	
	public boolean autorizeTransaction() {
		var response = restTemplate.getForEntity(
				"https://run.mocky.io/v3/5794d450-d2e2-4412-8131-73d0293ac1cc", Map.class);
		
		if (response.getStatusCode() == HttpStatus.OK) {
			String message = (String) response.getBody().get("message");
			return "Autorizado".equalsIgnoreCase(message);
		} else return false;
	}
}