package it.unito.greenshare;

import it.unito.greenshare.model.*;
import it.unito.greenshare.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final OrtoRepository ortoRepository;
    private final LottoRepository lottoRepository;
    private final AttrezzoRepository attrezzoRepository;

    public DataLoader(UserRepository userRepository,
                      OrtoRepository ortoRepository,
                      LottoRepository lottoRepository,
                      AttrezzoRepository attrezzoRepository) {
        this.userRepository = userRepository;
        this.ortoRepository = ortoRepository;
        this.lottoRepository = lottoRepository;
        this.attrezzoRepository = attrezzoRepository;
    }

    @Override
    public void run(String... args) {
        // Popola il DB solo se vuoto
        if (userRepository.count() == 0) {
            // 1. Utenti di test richiesti dalle specifiche (1 Cittadino, 1 Gestore)
            User cittadino = userRepository.save(new User("mario", "pass123", "Mario Rossi", "ROLE_CITTADINO"));
            User gestore = userRepository.save(new User("admin", "admin123", "Laura Gestore", "ROLE_GESTORE"));

            // 2. Orti di quartiere
            Orto orto1 = ortoRepository.save(new Orto("Orti Condivisi San Salvario", "Via Madama Cristina, Torino", "Area verde urbana con appezzamenti e cassoni rialzati per orti da terrazzo", 500));
            Orto orto2 = ortoRepository.save(new Orto("Comunità Verde Vanchiglia", "Lungo Dora Siena, Torino", "Orto comunitario per scambio risorse e sementi", 350));

            // 3. Lotti / Cassoni
            lottoRepository.save(new Lotto("L-01", 15.0, "LIBERO", orto1));
            lottoRepository.save(new Lotto("L-02", 20.0, "OCCUPATO", orto1));
            lottoRepository.save(new Lotto("B-01 (Cassone Balcone)", 3.0, "LIBERO", orto1));
            lottoRepository.save(new Lotto("V-01", 12.0, "LIBERO", orto2));

            // 4. Attrezzi e materiali di scambio
            attrezzoRepository.save(new Attrezzo("Vanga ergonomica", true, orto1));
            attrezzoRepository.save(new Attrezzo("Sacco Terriccio Universale 50L", true, orto1));
            attrezzoRepository.save(new Attrezzo("Cesoie per potatura", false, orto1));
            attrezzoRepository.save(new Attrezzo("Set Palette e Vasi Balcone", true, orto2));

            System.out.println(">>> DataLoader completato: database inizializzato con successo! <<<");
        }
    }
}