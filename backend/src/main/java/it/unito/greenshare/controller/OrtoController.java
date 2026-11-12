package it.unito.greenshare.controller;

import it.unito.greenshare.dto.*;
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

    // ROUTE 1: GET /api/orti
    @GetMapping("/orti")
    public ResponseEntity<List<OrtoDTO>> getOrti() {
        return ResponseEntity.ok(ortoService.getAllOrti());
    }

    // ROUTE 2: GET /api/orti/{ortoId}/lotti?stato=LIBERO (Usa sia @PathVariable sia @RequestParam!)
    @GetMapping("/orti/{ortoId}/lotti")
    public ResponseEntity<List<LottoDTO>> getLottiByOrto(
            @PathVariable Long ortoId,
            @RequestParam(required = false) String stato) {
        return ResponseEntity.ok(ortoService.getLottiByOrto(ortoId, stato));
    }

    // ROUTE 3: GET /api/prenotazioni?userId=1 (Usa @RequestParam nella Query String)
    @GetMapping("/prenotazioni")
    public ResponseEntity<List<PrenotazioneDTO>> getPrenotazioni(
            @RequestParam(required = false) Long userId) {
        return ResponseEntity.ok(ortoService.getPrenotazioni(userId));
    }

    // ROUTE 4: POST /api/prenotazioni (Usa @RequestBody)
    @PostMapping("/prenotazioni")
    public ResponseEntity<?> prenotaLotto(@RequestBody PrenotazioneRequest req) {
        try {
            PrenotazioneDTO dto = ortoService.prenotaLotto(req.getUserId(), req.getLottoId(), req.getDataInizio(), req.getDataFine());
            return ResponseEntity.status(HttpStatus.CREATED).body(dto);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // ROUTE 5: PUT /api/prenotazioni/{id}/approva (Solo Gestore)
    @PutMapping("/prenotazioni/{id}/approva")
    public ResponseEntity<?> approvaPrenotazione(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(ortoService.approvaPrenotazione(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // ROUTE 6: DELETE /api/prenotazioni/{id} (Rifiuta/Libera lotto)
    @DeleteMapping("/prenotazioni/{id}")
    public ResponseEntity<?> cancellaPrenotazione(@PathVariable Long id) {
        try {
            ortoService.cancellaPrenotazione(id);
            return ResponseEntity.ok("Prenotazione cancellata e lotto liberato");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // ROUTE 7: GET /api/utenti (Per controllo utenti nella dashboard Admin)
    @GetMapping("/utenti")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(ortoService.getAllUsers());
    }
}