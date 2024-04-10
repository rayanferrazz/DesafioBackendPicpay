package desafio.backend.picpay.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import desafio.backend.picpay.domain.Transaction;
import desafio.backend.picpay.dto.TransactionDTO;
import desafio.backend.picpay.service.TransactionService;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

	@Autowired
	private TransactionService transactionService;
	
	@PostMapping
	public ResponseEntity<Transaction> createTransaction(@RequestBody TransactionDTO transaction) 
			throws Exception {
		var newTransaction = this.transactionService.createTransaction(transaction);
		return new ResponseEntity<>(newTransaction, HttpStatus.CREATED);
	}
}