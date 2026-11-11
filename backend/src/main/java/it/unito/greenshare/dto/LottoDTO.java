package it.unito.greenshare.dto;

public class LottoDTO {
    private Long id;
    private String codice;
    private Double dimensioneMq;
    private String stato;
    private Long ortoId;
    private String ortoNome;

    public LottoDTO() {}

    public LottoDTO(Long id, String codice, Double dimensioneMq, String stato, Long ortoId, String ortoNome) {
        this.id = id;
        this.codice = codice;
        this.dimensioneMq = dimensioneMq;
        this.stato = stato;
        this.ortoId = ortoId;
        this.ortoNome = ortoNome;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCodice() { return codice; }
    public void setCodice(String codice) { this.codice = codice; }
    public Double getDimensioneMq() { return dimensioneMq; }
    public void setDimensioneMq(Double dimensioneMq) { this.dimensioneMq = dimensioneMq; }
    public String getStato() { return stato; }
    public void setStato(String stato) { this.stato = stato; }
    public Long getOrtoId() { return ortoId; }
    public void setOrtoId(Long ortoId) { this.ortoId = ortoId; }
    public String getOrtoNome() { return ortoNome; }
    public void setOrtoNome(String ortoNome) { this.ortoNome = ortoNome; }
}