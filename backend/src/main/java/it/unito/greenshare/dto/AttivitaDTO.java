package it.unito.greenshare.dto;

import java.time.LocalDate;

public class AttivitaDTO {
    private Long id;
    private String tipoColtura;
    private Double quantitaKg;
    private LocalDate dataRaccolto;
    private String ambiente;
    private String fotoUrl;
    private String username;
    private String lottoCodice;

    public AttivitaDTO() {}

    public AttivitaDTO(Long id, String tipoColtura, Double quantitaKg, LocalDate dataRaccolto, String ambiente, String fotoUrl, String username, String lottoCodice) {
        this.id = id;
        this.tipoColtura = tipoColtura;
        this.quantitaKg = quantitaKg;
        this.dataRaccolto = dataRaccolto;
        this.ambiente = ambiente;
        this.fotoUrl = fotoUrl;
        this.username = username;
        this.lottoCodice = lottoCodice;
    }

    public Long getId() { return id; }
    public String getTipoColtura() { return tipoColtura; }
    public Double getQuantitaKg() { return quantitaKg; }
    public LocalDate getDataRaccolto() { return dataRaccolto; }
    public String getAmbiente() { return ambiente; }
    public String getFotoUrl() { return fotoUrl; }
    public String getUsername() { return username; }
    public String getLottoCodice() { return lottoCodice; }
}