package it.unito.greenshare.model;

import jakarta.persistence.*;

@Entity
@Table(name = "lotti")
// Definizione della classe Lotto come entità JPA
public class Lotto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String codice; // es: "L-01", "L-02"

    @Column(nullable = false)
    private Double dimensioneMq;

    @Column(nullable = false)
    private String stato; // es: "LIBERO", "OCCUPATO", "IN_MANUTENZIONE"

    // Relazione Molti-a-1: molti Lotti appartengono a un Orto.
    // È il lato proprietario che definisce la Foreign Key (orto_id)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orto_id", nullable = false)
    private Orto orto;

    // Costruttore di default obbligatorio per JPA
    public Lotto() {
    }

    public Lotto(String codice, Double dimensioneMq, String stato, Orto orto) {
        this.codice = codice;
        this.dimensioneMq = dimensioneMq;
        this.stato = stato;
        this.orto = orto;
    }

    // Getter e Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public Double getDimensioneMq() {
        return dimensioneMq;
    }

    public void setDimensioneMq(Double dimensioneMq) {
        this.dimensioneMq = dimensioneMq;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    public Orto getOrto() {
        return orto;
    }

    public void setOrto(Orto orto) {
        this.orto = orto;
    }
}