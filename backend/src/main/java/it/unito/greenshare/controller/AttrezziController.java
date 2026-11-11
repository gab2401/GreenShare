package it.unito.greenshare.controller;

import it.unito.greenshare.dto.AttivitaRequest;
import it.unito.greenshare.dto.AttrezzoDTO;
import it.unito.greenshare.model.AttivitaRaccolto;
import it.unito.greenshare.service.AttrezziService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AttrezziController {

    private final AttrezziService attrezziService;

    public AttrezziController(AttrezziService attrezziService) {
        this.attrezziService = attrezziService;
    }

    // 5. GET /api/orti/{ortoId}/attrezzi - Lista attrezzi e materiali disponibili
    @GetMapping("/orti/{ortoId}/attrezzi")
    public ResponseEntity<List<AttrezzoDTO>> getAttrezzi(@PathVariable Long ortoId) {
        return ResponseEntity.ok(attrezziService.getAttrezziDisponibili(ortoId));
    }

    // 6. PUT /api/attrezzi/{id}/preleva - Segna un attrezzo o risorsa come occupata
    @PutMapping("/attrezzi/{id}/preleva")
    public ResponseEntity<?> prelevaAttrezzo(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(attrezziService.prelevaAttrezzo(id));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // 7. PUT /api/attrezzi/{id}/restituisci - Rimette a disposizione l'attrezzo
    @PutMapping("/attrezzi/{id}/restituisci")
    public ResponseEntity<?> restituisciAttrezzo(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(attrezziService.restituisciAttrezzo(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // 8. POST /api/attivita - Form 2: registrazione del raccolto o semina
    @PostMapping("/attivita")
    public ResponseEntity<?> registraAttivita(@RequestBody AttivitaRequest req) {
        try {
            AttivitaRaccolto attivita = attrezziService.registraAttivita(
                    req.getUserId(),
                    req.getLottoId(),
                    req.getTipoColtura(),
                    req.getQuantitaKg()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(attivita);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}