package it.unito.greenshare.service;

import it.unito.greenshare.dto.*;
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
        return ortoRepository.findAll().stream().map(DTOMapper::toOrtoDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LottoDTO> getLottiByOrto(Long ortoId, String stato) {
        List<Lotto> lotti = (stato != null && !stato.isBlank())
                ? lottoRepository.findByOrtoIdAndStato(ortoId, stato)
                : lottoRepository.findByOrtoId(ortoId);
        return lotti.stream().map(DTOMapper::toLottoDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PrenotazioneDTO> getPrenotazioni(Long userId) {
        List<Prenotazione> list = (userId != null)
                ? prenotazioneRepository.findByUserId(userId)
                : prenotazioneRepository.findAll();
        return list.stream().map(DTOMapper::toPrenotazioneDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(DTOMapper::toUserDTO).collect(Collectors.toList());
    }

    @Transactional
    public PrenotazioneDTO prenotaLotto(Long userId, Long lottoId, LocalDate dataInizio, LocalDate dataFine) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato: " + userId));
        Lotto lotto = lottoRepository.findById(lottoId)
                .orElseThrow(() -> new IllegalArgumentException("Lotto non trovato: " + lottoId));

        if (!"LIBERO".equalsIgnoreCase(lotto.getStato())) {
            throw new IllegalStateException("Il lotto " + lotto.getCodice() + " non è libero.");
        }

        lotto.setStato("IN_ATTESA");
        Prenotazione salvata = prenotazioneRepository.save(new Prenotazione(dataInizio, dataFine, "IN_ATTESA", user, lotto));
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

    @Transactional
    public void cancellaPrenotazione(Long prenotazioneId) {
        Prenotazione p = prenotazioneRepository.findById(prenotazioneId)
                .orElseThrow(() -> new IllegalArgumentException("Prenotazione non trovata: " + prenotazioneId));
        p.getLotto().setStato("LIBERO");
        prenotazioneRepository.delete(p);
    }
}