package desafio.backend.picpay.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import desafio.backend.picpay.domain.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}
