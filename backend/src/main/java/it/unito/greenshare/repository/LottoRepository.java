package it.unito.greenshare.repository;

import it.unito.greenshare.model.Lotto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LottoRepository extends JpaRepository<Lotto, Long> {
    // Query derivata per filtrare i lotti di un orto specifico in base allo stato (es. "LIBERO")
    List<Lotto> findByOrtoIdAndStato(Long ortoId, String stato);
    List<Lotto> findByOrtoId(Long ortoId);
}