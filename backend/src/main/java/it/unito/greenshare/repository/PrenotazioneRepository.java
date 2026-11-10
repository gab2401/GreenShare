package it.unito.greenshare.repository;

import it.unito.greenshare.model.Prenotazione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PrenotazioneRepository extends JpaRepository<Prenotazione, Long> {
    List<Prenotazione> findByUserId(Long userId);
    List<Prenotazione> findByLottoId(Long lottoId);
}