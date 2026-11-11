package it.unito.greenshare.controller;

import it.unito.greenshare.dto.LottoDTO;
import it.unito.greenshare.dto.PrenotazioneDTO;
import it.unito.greenshare.dto.OrtoDTO;
import it.unito.greenshare.dto.PrenotazioneRequest;
import it.unito.greenshare.model.Orto;
import it.unito.greenshare.service.OrtoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class OrtoController {

    private final OrtoService ortoService;

    public OrtoController(OrtoService ortoService) {
        this.ortoService = ortoService;
    }

    // 1. GET /api/orti - Lista di tutti gli orto
    @GetMapping("/orti")
    public ResponseEntity<List<OrtoDTO>> getOrti() {
        return ResponseEntity.ok(ortoService.getAllOrti());
    }

    // 2. GET /api/orti/{ortoId}/lotti - Lista lotti/cassoni appartenenti a un orto
    @GetMapping("/orti/{ortoId}/lotti")
    public ResponseEntity<List<LottoDTO>> getLottiByOrto(@PathVariable Long ortoId) {
        return ResponseEntity.ok(ortoService.getLottiByOrto(ortoId));
    }

    // 3. POST /api/prenotazioni - Form 1: creazione di una richiesta di prenotazione
    @PostMapping("/prenotazioni")
    public ResponseEntity<?> prenotaLotto(@RequestBody PrenotazioneRequest req) {
        try {
            PrenotazioneDTO nuovaPrenotazione = ortoService.prenotaLotto(
                    req.getUserId(),
                    req.getLottoId(),
                    req.getDataInizio(),
                    req.getDataFine()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(nuovaPrenotazione);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // 4. PUT /api/prenotazioni/{id}/approva - Funzionalità dedicata al Gestore
    @PutMapping("/prenotazioni/{id}/approva")
    public ResponseEntity<?> approvaPrenotazione(@PathVariable Long id) {
        try {
            PrenotazioneDTO approvata = ortoService.approvaPrenotazione(id);
            return ResponseEntity.ok(approvata);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}