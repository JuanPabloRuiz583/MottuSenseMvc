package br.com.fiap.MottuSenseMvc.repository;

import br.com.fiap.MottuSenseMvc.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String username);
    boolean existsByEmail(String email);

}
