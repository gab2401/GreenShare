package it.unito.greenshare.service;

import it.unito.greenshare.dto.DTOMapper;
import it.unito.greenshare.dto.LottoDTO;
import it.unito.greenshare.dto.OrtoDTO;
import it.unito.greenshare.dto.PrenotazioneDTO;
import it.unito.greenshare.model.*;
import it.unito.greenshare.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrtoService {

    private final OrtoRepository ortoRepository;
    private final LottoRepository lottoRepository;
    private final PrenotazioneRepository prenotazioneRepository;
    private final UserRepository userRepository;

    // Dependency Injection tramite costruttore
    public OrtoService(OrtoRepository ortoRepository,
                       LottoRepository lottoRepository,
                       PrenotazioneRepository prenotazioneRepository,
                       UserRepository userRepository) {
        this.ortoRepository = ortoRepository;
        this.lottoRepository = lottoRepository;
        this.prenotazioneRepository = prenotazioneRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<OrtoDTO> getAllOrti() {
        return ortoRepository.findAll()
                .stream()
                .map(DTOMapper::toOrtoDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LottoDTO> getLottiByOrto(Long ortoId) {
        return lottoRepository.findByOrtoId(ortoId)
                .stream()
                .map(DTOMapper::toLottoDTO)
                .collect(Collectors.toList());
    }

    // Metodo transazionale ACID: se fallisce qualcosa, fa rollback automatico
    @Transactional
    public PrenotazioneDTO prenotaLotto(Long userId, Long lottoId, LocalDate dataInizio, LocalDate dataFine) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato con id: " + userId));

        Lotto lotto = lottoRepository.findById(lottoId)
                .orElseThrow(() -> new IllegalArgumentException("Lotto non trovato con id: " + lottoId));

        if (!"LIBERO".equalsIgnoreCase(lotto.getStato())) {
            throw new IllegalStateException("Impossibile prenotare: il lotto " + lotto.getCodice() + " non è libero.");
        }

        // 1. Modifica lo stato dell'entità (Hibernate la rileva come "dirty" e la aggiorna sul DB)
        lotto.setStato("IN_ATTESA");

        // 2. Crea il record di prenotazione
        Prenotazione p = new Prenotazione(dataInizio, dataFine, "IN_ATTESA", user, lotto);
        Prenotazione salvata = prenotazioneRepository.save(p);

        return DTOMapper.toPrenotazioneDTO(salvata);
    }

    @Transactional
    public PrenotazioneDTO approvaPrenotazione(Long prenotazioneId) {
        Prenotazione p = prenotazioneRepository.findById(prenotazioneId)
                .orElseThrow(() -> new IllegalArgumentException("Prenotazione non trovata: " + prenotazioneId));

        p.setStato("APPROVATA");
        p.getLotto().setStato("OCCUPATO");

        return DTOMapper.toPrenotazioneDTO(p);
    }
}