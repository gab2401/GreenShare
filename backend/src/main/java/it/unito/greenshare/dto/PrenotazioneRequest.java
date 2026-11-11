package it.unito.greenshare.dto;

import java.time.LocalDate;

public class PrenotazioneRequest {
    private Long userId;
    private Long lottoId;
    private LocalDate dataInizio;
    private LocalDate dataFine;

    public PrenotazioneRequest() {}

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getLottoId() { return lottoId; }
    public void setLottoId(Long lottoId) { this.lottoId = lottoId; }

    public LocalDate getDataInizio() { return dataInizio; }
    public void setDataInizio(LocalDate dataInizio) { this.dataInizio = dataInizio; }

    public LocalDate getDataFine() { return dataFine; }
    public void setDataFine(LocalDate dataFine) { this.dataFine = dataFine; }
}