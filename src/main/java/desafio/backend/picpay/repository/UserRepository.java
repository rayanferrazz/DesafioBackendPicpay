package desafio.backend.picpay.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import desafio.backend.picpay.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

}