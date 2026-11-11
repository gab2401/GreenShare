package it.unito.greenshare.service;

import it.unito.greenshare.dto.AttrezzoDTO;
import it.unito.greenshare.dto.DTOMapper;
import it.unito.greenshare.model.*;
import it.unito.greenshare.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttrezziService {

    private final AttrezzoRepository attrezzoRepository;
    private final AttivitaRaccoltoRepository attivitaRaccoltoRepository;
    private final UserRepository userRepository;
    private final LottoRepository lottoRepository;

    public AttrezziService(AttrezzoRepository attrezzoRepository,
                           AttivitaRaccoltoRepository attivitaRaccoltoRepository,
                           UserRepository userRepository,
                           LottoRepository lottoRepository) {
        this.attrezzoRepository = attrezzoRepository;
        this.attivitaRaccoltoRepository = attivitaRaccoltoRepository;
        this.userRepository = userRepository;
        this.lottoRepository = lottoRepository;
    }

    @Transactional(readOnly = true)
    public List<AttrezzoDTO> getAttrezziDisponibili(Long ortoId) {
        return attrezzoRepository.findByOrtoIdAndDisponibileTrue(ortoId)
                .stream()
                .map(DTOMapper::toAttrezzoDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public AttrezzoDTO prelevaAttrezzo(Long attrezzoId) {
        Attrezzo attrezzo = attrezzoRepository.findById(attrezzoId)
                .orElseThrow(() -> new IllegalArgumentException("Attrezzo non trovato: " + attrezzoId));

        if (!attrezzo.isDisponibile()) {
            throw new IllegalStateException("L'attrezzo o materiale selezionato è già in uso.");
        }

        attrezzo.setDisponibile(false);
        return DTOMapper.toAttrezzoDTO(attrezzo);
    }

    @Transactional
    public AttrezzoDTO restituisciAttrezzo(Long attrezzoId) {
        Attrezzo attrezzo = attrezzoRepository.findById(attrezzoId)
                .orElseThrow(() -> new IllegalArgumentException("Attrezzo non trovato: " + attrezzoId));

        attrezzo.setDisponibile(true);
        return DTOMapper.toAttrezzoDTO(attrezzo);
    }

    @Transactional
    public AttivitaRaccolto registraAttivita(Long userId, Long lottoId, String tipoColtura, Double quantitaKg) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));
        Lotto lotto = lottoRepository.findById(lottoId)
                .orElseThrow(() -> new IllegalArgumentException("Lotto non trovato"));

        AttivitaRaccolto attivita = new AttivitaRaccolto(tipoColtura, quantitaKg, LocalDate.now(), user, lotto);
        return attivitaRaccoltoRepository.save(attivita);
    }
}