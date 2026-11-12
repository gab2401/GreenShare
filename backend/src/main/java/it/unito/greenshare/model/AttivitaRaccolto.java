package it.unito.greenshare.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "attivita_raccolto")
public class AttivitaRaccolto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tipoColtura; // es. "Pomodori Ciliegino", "Pianta di Basilico"

    @Column(nullable = false)
    private Double quantitaKg;

    @Column(nullable = false)
    private LocalDate dataRaccolto;

    @Column(nullable = true)
    private String ambiente; // "BALCONE_CASA" oppure "ORTO_CONDIVISO"

    @Column(columnDefinition = "TEXT")
    private String fotoUrl; // Accetta sia link web sia foto caricate dal PC (Base64)!

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lotto_id", nullable = false)
    private Lotto lotto;

    public AttivitaRaccolto() {}

    public AttivitaRaccolto(String tipoColtura, Double quantitaKg, LocalDate dataRaccolto, String ambiente, String fotoUrl, User user, Lotto lotto) {
        this.tipoColtura = tipoColtura;
        this.quantitaKg = quantitaKg;
        this.dataRaccolto = dataRaccolto;
        this.ambiente = ambiente;
        this.fotoUrl = fotoUrl;
        this.user = user;
        this.lotto = lotto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTipoColtura() { return tipoColtura; }
    public void setTipoColtura(String tipoColtura) { this.tipoColtura = tipoColtura; }
    public Double getQuantitaKg() { return quantitaKg; }
    public void setQuantitaKg(Double quantitaKg) { this.quantitaKg = quantitaKg; }
    public LocalDate getDataRaccolto() { return dataRaccolto; }
    public void setDataRaccolto(LocalDate dataRaccolto) { this.dataRaccolto = dataRaccolto; }
    public String getAmbiente() { return ambiente; }
    public void setAmbiente(String ambiente) { this.ambiente = ambiente; }
    public String getFotoUrl() { return fotoUrl; }
    public void setFotoUrl(String fotoUrl) { this.fotoUrl = fotoUrl; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Lotto getLotto() { return lotto; }
    public void setLotto(Lotto lotto) { this.lotto = lotto; }
}