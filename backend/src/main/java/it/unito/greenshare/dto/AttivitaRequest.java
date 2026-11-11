package it.unito.greenshare.dto;

public class AttivitaRequest {
    private Long userId;
    private Long lottoId;
    private String tipoColtura;
    private Double quantitaKg;

    public AttivitaRequest() {}

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getLottoId() { return lottoId; }
    public void setLottoId(Long lottoId) { this.lottoId = lottoId; }

    public String getTipoColtura() { return tipoColtura; }
    public void setTipoColtura(String tipoColtura) { this.tipoColtura = tipoColtura; }

    public Double getQuantitaKg() { return quantitaKg; }
    public void setQuantitaKg(Double quantitaKg) { this.quantitaKg = quantitaKg; }
}