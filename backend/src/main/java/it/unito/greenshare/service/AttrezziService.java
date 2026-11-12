package it.unito.greenshare.service;

import it.unito.greenshare.dto.AttivitaDTO;
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
    private final OrtoRepository ortoRepository;

    public AttrezziService(AttrezzoRepository attrezzoRepository,
                           AttivitaRaccoltoRepository attivitaRaccoltoRepository,
                           UserRepository userRepository,
                           LottoRepository lottoRepository,
                           OrtoRepository ortoRepository) {
        this.attrezzoRepository = attrezzoRepository;
        this.attivitaRaccoltoRepository = attivitaRaccoltoRepository;
        this.userRepository = userRepository;
        this.lottoRepository = lottoRepository;
        this.ortoRepository = ortoRepository;
    }

    @Transactional(readOnly = true)
    public List<AttrezzoDTO> getAttrezziByOrto(Long ortoId, Boolean soloDisponibili) {
        List<Attrezzo> list = (Boolean.TRUE.equals(soloDisponibili))
                ? attrezzoRepository.findByOrtoIdAndDisponibileTrue(ortoId)
                : attrezzoRepository.findAll().stream().filter(a -> a.getOrto().getId().equals(ortoId)).toList();
        return list.stream().map(DTOMapper::toAttrezzoDTO).collect(Collectors.toList());
    }

    @Transactional
    public AttrezzoDTO aggiungiAttrezzo(Long ortoId, String nome) {
        Orto orto = ortoRepository.findById(ortoId)
                .orElseThrow(() -> new IllegalArgumentException("Orto non trovato"));
        Attrezzo nuovo = attrezzoRepository.save(new Attrezzo(nome, true, orto));
        return DTOMapper.toAttrezzoDTO(nuovo);
    }

    @Transactional
    public AttrezzoDTO prelevaAttrezzo(Long attrezzoId) {
        Attrezzo attrezzo = attrezzoRepository.findById(attrezzoId)
                .orElseThrow(() -> new IllegalArgumentException("Attrezzo non trovato: " + attrezzoId));
        if (!attrezzo.isDisponibile()) {
            throw new IllegalStateException("L'attrezzo selezionato è già in prestito.");
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
    public AttivitaDTO registraAttivita(Long userId, Long lottoId, String tipoColtura, Double quantitaKg, String ambiente, String fotoUrl) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));
        Lotto lotto = lottoRepository.findById(lottoId)
                .orElseThrow(() -> new IllegalArgumentException("Lotto non trovato"));

        String urlFinale = (fotoUrl == null || fotoUrl.isBlank())
                ? "https://images.unsplash.com/photo-1592841200221-a6898f307baa?w=400&auto=format&fit=crop&q=80"
                : fotoUrl;
        String ambFinale = (ambiente == null || ambiente.isBlank()) ? "BALCONE_CASA" : ambiente;

        AttivitaRaccolto salvata = attivitaRaccoltoRepository.save(
                new AttivitaRaccolto(tipoColtura, quantitaKg, LocalDate.now(), ambFinale, urlFinale, user, lotto)
        );
        return DTOMapper.toAttivitaDTO(salvata);
    }

    @Transactional(readOnly = true)
    public List<AttivitaDTO> getAttivita(Long userId) {
        List<AttivitaRaccolto> list = (userId != null)
                ? attivitaRaccoltoRepository.findByUserId(userId)
                : attivitaRaccoltoRepository.findAll();
        return list.stream().map(DTOMapper::toAttivitaDTO).collect(Collectors.toList());
    }

    @Transactional
    public void eliminaAttivita(Long id) {
        attivitaRaccoltoRepository.deleteById(id);
    }
}