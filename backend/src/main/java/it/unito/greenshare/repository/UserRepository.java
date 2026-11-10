package it.unito.greenshare.repository;

import it.unito.greenshare.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Query Derivata: Spring genera automaticamente "SELECT * FROM utenti WHERE username = ?"
    Optional<User> findByUsername(String username);
}