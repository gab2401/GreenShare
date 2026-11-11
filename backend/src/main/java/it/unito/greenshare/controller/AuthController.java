package it.unito.greenshare.controller;

import it.unito.greenshare.dto.DTOMapper;
import it.unito.greenshare.model.User;
import it.unito.greenshare.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AuthController {

    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 9. POST /api/auth/login - Autenticazione con salvataggio utente in sessione
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials, HttpSession session) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
            User user = userOpt.get();
            // Salva l'id utente nella sessione HTTP gestita da Tomcat
            session.setAttribute("LOGGED_USER_ID", user.getId());
            session.setAttribute("LOGGED_USER_ROLE", user.getRuolo());
            return ResponseEntity.ok(DTOMapper.toUserDTO(user));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenziali non valide");
    }

    // 10. GET /api/auth/me - Verifica se esiste una sessione attiva
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(HttpSession session) {
        Long userId = (Long) session.getAttribute("LOGGED_USER_ID");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Nessuna sessione attiva");
        }
        return userRepository.findById(userId)
                .map(u -> ResponseEntity.ok(DTOMapper.toUserDTO(u)))
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    // 11. POST /api/auth/logout - Invalida la sessione attiva
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Logout effettuato con successo");
    }
}