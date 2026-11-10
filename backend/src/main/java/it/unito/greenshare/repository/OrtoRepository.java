package it.unito.greenshare.repository;

import it.unito.greenshare.model.Orto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrtoRepository extends JpaRepository<Orto, Long> {
}