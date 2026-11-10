package it.unito.greenshare.repository;

import it.unito.greenshare.model.AttivitaRaccolto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AttivitaRaccoltoRepository extends JpaRepository<AttivitaRaccolto, Long> {
    List<AttivitaRaccolto> findByUserId(Long userId);
}