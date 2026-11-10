package it.unito.greenshare.repository;

import it.unito.greenshare.model.Attrezzo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AttrezzoRepository extends JpaRepository<Attrezzo, Long> {
    List<Attrezzo> findByOrtoIdAndDisponibileTrue(Long ortoId);
}