package it.unito.greenshare.dto;

public class AttrezzoDTO {
    private Long id;
    private String nome;
    private boolean disponibile;
    private Long ortoId;

    public AttrezzoDTO() {}

    public AttrezzoDTO(Long id, String nome, boolean disponibile, Long ortoId) {
        this.id = id;
        this.nome = nome;
        this.disponibile = disponibile;
        this.ortoId = ortoId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public boolean isDisponibile() { return disponibile; }
    public void setDisponibile(boolean disponibile) { this.disponibile = disponibile; }
    public Long getOrtoId() { return ortoId; }
    public void setOrtoId(Long ortoId) { this.ortoId = ortoId; }
}