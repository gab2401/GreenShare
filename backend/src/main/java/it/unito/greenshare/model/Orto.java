package it.unito.greenshare.model;

import java.util.List;
import java.util.ArrayList;
import jakarta.persistence.*;

@Entity
@Table(name = "orti")
public class Orto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String indirizzo;

    @OneToMany(mappedBy = "orto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lotto> lotti = new ArrayList<>();

    public List<Lotto> getLotti() { return lotti; }
    public void setLotti(List<Lotto> lotti) { this.lotti = lotti; }

    private String descrizione;

    private int mqTotali;

    public Orto() {}

    public Orto(String nome, String indirizzo, String descrizione, int mqTotali) {
        this.nome = nome;
        this.indirizzo = indirizzo;
        this.descrizione = descrizione;
        this.mqTotali = mqTotali;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getIndirizzo() { return indirizzo; }
    public void setIndirizzo(String indirizzo) { this.indirizzo = indirizzo; }
    public String getDescrizione() { return descrizione; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }
    public int getMqTotali() { return mqTotali; }
    public void setMqTotali(int mqTotali) { this.mqTotali = mqTotali; }
}