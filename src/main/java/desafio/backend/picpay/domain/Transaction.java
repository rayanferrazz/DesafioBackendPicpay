package desafio.backend.picpay.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;

@Entity(name = "transactions")
@Table(name = "transactions")
@EqualsAndHashCode(of = "id")
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private BigDecimal amount;
	
	@ManyToOne
	@JoinColumn(name = "payer_id")
	private User payer;
	
	@ManyToOne
	@JoinColumn(name = "payee_id")
	private User payee;
	
	private LocalDateTime transactionTime;

	public Transaction() {
	}
	
	public Transaction(Long id, BigDecimal amount, User payer, User payee, LocalDateTime transactionTime) {
		this.id = id;
		this.amount = amount;
		this.payer = payer;
		this.payee = payee;
		this.transactionTime = transactionTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public User getPayer() {
		return payer;
	}

	public void setPayer(User payer) {
		this.payer = payer;
	}

	public User getPayee() {
		return payee;
	}

	public void setPayee(User payee) {
		this.payee = payee;
	}

	public LocalDateTime getTransactionTime() {
		return transactionTime;
	}

	public void setTransactionTime(LocalDateTime transactionTime) {
		this.transactionTime = transactionTime;
	}
}