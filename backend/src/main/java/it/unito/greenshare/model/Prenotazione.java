package it.unito.greenshare.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "prenotazioni")
public class Prenotazione {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate dataInizio;

    @Column(nullable = false)
    private LocalDate dataFine;

    @Column(nullable = false)
    private String stato; // "IN_ATTESA", "APPROVATA", "RIFIUTATA"

    // Relazione: molte prenotazioni appartengono a un singolo utente
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Relazione: molte prenotazioni possono riferirsi a un singolo lotto (in periodi diversi)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lotto_id", nullable = false)
    private Lotto lotto;

    public Prenotazione() {}

    public Prenotazione(LocalDate dataInizio, LocalDate dataFine, String stato, User user, Lotto lotto) {
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.stato = stato;
        this.user = user;
        this.lotto = lotto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getDataInizio() { return dataInizio; }
    public void setDataInizio(LocalDate dataInizio) { this.dataInizio = dataInizio; }

    public LocalDate getDataFine() { return dataFine; }
    public void setDataFine(LocalDate dataFine) { this.dataFine = dataFine; }

    public String getStato() { return stato; }
    public void setStato(String stato) { this.stato = stato; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Lotto getLotto() { return lotto; }
    public void setLotto(Lotto lotto) { this.lotto = lotto; }
}