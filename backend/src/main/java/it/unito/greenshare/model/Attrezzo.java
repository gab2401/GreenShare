package it.unito.greenshare.model;

import jakarta.persistence.*;

@Entity
@Table(name = "attrezzi")
public class Attrezzo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome; // es. "Vanga", "Annaffiatoio", "Forbici da pota"

    @Column(nullable = false)
    private boolean disponibile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orto_id", nullable = false)
    private Orto orto;

    public Attrezzo() {}

    public Attrezzo(String nome, boolean disponibile, Orto orto) {
        this.nome = nome;
        this.disponibile = disponibile;
        this.orto = orto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public boolean isDisponibile() { return disponibile; }
    public void setDisponibile(boolean disponibile) { this.disponibile = disponibile; }

    public Orto getOrto() { return orto; }
    public void setOrto(Orto orto) { this.orto = orto; }
}