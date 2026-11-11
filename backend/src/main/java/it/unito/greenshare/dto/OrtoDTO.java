package it.unito.greenshare.dto;

public class OrtoDTO {
    private Long id;
    private String nome;
    private String indirizzo;
    private String descrizione;
    private int mqTotali;

    public OrtoDTO() {}

    public OrtoDTO(Long id, String nome, String indirizzo, String descrizione, int mqTotali) {
        this.id = id;
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