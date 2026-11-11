package it.unito.greenshare.dto;

import java.time.LocalDate;

public class PrenotazioneDTO {
    private Long id;
    private LocalDate dataInizio;
    private LocalDate dataFine;
    private String stato;
    private Long userId;
    private String username;
    private Long lottoId;
    private String lottoCodice;

    public PrenotazioneDTO() {}

    public PrenotazioneDTO(Long id, LocalDate dataInizio, LocalDate dataFine, String stato, Long userId, String username, Long lottoId, String lottoCodice) {
        this.id = id;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.stato = stato;
        this.userId = userId;
        this.username = username;
        this.lottoId = lottoId;
        this.lottoCodice = lottoCodice;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDate getDataInizio() { return dataInizio; }
    public void setDataInizio(LocalDate dataInizio) { this.dataInizio = dataInizio; }
    public LocalDate getDataFine() { return dataFine; }
    public void setDataFine(LocalDate dataFine) { this.dataFine = dataFine; }
    public String getStato() { return stato; }
    public void setStato(String stato) { this.stato = stato; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public Long getLottoId() { return lottoId; }
    public void setLottoId(Long lottoId) { this.lottoId = lottoId; }
    public String getLottoCodice() { return lottoCodice; }
    public void setLottoCodice(String lottoCodice) { this.lottoCodice = lottoCodice; }
}