package it.unito.greenshare.controller;

import it.unito.greenshare.dto.AttivitaDTO;
import it.unito.greenshare.dto.AttivitaRequest;
import it.unito.greenshare.dto.AttrezzoDTO;
import it.unito.greenshare.service.AttrezziService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AttrezziController {

    private final AttrezziService attrezziService;

    public AttrezziController(AttrezziService attrezziService) {
        this.attrezziService = attrezziService;
    }

    // ROUTE 8: GET /api/orti/{ortoId}/attrezzi?soloDisponibili=true
    @GetMapping("/orti/{ortoId}/attrezzi")
    public ResponseEntity<List<AttrezzoDTO>> getAttrezzi(
            @PathVariable Long ortoId,
            @RequestParam(required = false) Boolean soloDisponibili) {
        return ResponseEntity.ok(attrezziService.getAttrezziByOrto(ortoId, soloDisponibili));
    }

    // ROUTE 9: POST /api/orti/{ortoId}/attrezzi (Solo Gestore)
    @PostMapping("/orti/{ortoId}/attrezzi")
    public ResponseEntity<?> aggiungiAttrezzo(
            @PathVariable Long ortoId,
            @RequestBody Map<String, String> body) {
        String nome = body.get("nome");
        if (nome == null || nome.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Il nome del materiale è obbligatorio.");
        }
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(attrezziService.aggiungiAttrezzo(ortoId, nome.trim()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // ROUTE 10: PUT /api/attrezzi/{id}/preleva
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

    // ROUTE 11: PUT /api/attrezzi/{id}/restituisci
    @PutMapping("/attrezzi/{id}/restituisci")
    public ResponseEntity<?> restituisciAttrezzo(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(attrezziService.restituisciAttrezzo(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // ROUTE 12: GET /api/attivita?userId=1
    @GetMapping("/attivita")
    public ResponseEntity<List<AttivitaDTO>> getAttivita(
            @RequestParam(required = false) Long userId) {
        return ResponseEntity.ok(attrezziService.getAttivita(userId));
    }

    // ROUTE 13: POST /api/attivita
    @PostMapping("/attivita")
    public ResponseEntity<?> registraAttivita(@RequestBody AttivitaRequest req) {
        try {
            AttivitaDTO dto = attrezziService.registraAttivita(
                    req.getUserId(),
                    req.getLottoId(),
                    req.getTipoColtura(),
                    req.getQuantitaKg(),
                    req.getAmbiente(),
                    req.getFotoUrl()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}