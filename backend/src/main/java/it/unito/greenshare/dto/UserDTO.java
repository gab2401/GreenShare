package it.unito.greenshare.dto;

public class UserDTO {
    private Long id;
    private String username;
    private String nome;
    private String ruolo;

    public UserDTO() {}

    public UserDTO(Long id, String username, String nome, String ruolo) {
        this.id = id;
        this.username = username;
        this.nome = nome;
        this.ruolo = ruolo;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getRuolo() { return ruolo; }
    public void setRuolo(String ruolo) { this.ruolo = ruolo; }
}