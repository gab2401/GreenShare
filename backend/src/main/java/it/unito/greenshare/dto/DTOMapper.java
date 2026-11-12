package it.unito.greenshare.dto;

import it.unito.greenshare.model.*;

public class DTOMapper {

    public static UserDTO toUserDTO(User user) {
        if (user == null) return null;
        return new UserDTO(user.getId(), user.getUsername(), user.getNome(), user.getRuolo());
    }

    public static LottoDTO toLottoDTO(Lotto lotto) {
        if (lotto == null) return null;
        return new LottoDTO(
                lotto.getId(),
                lotto.getCodice(),
                lotto.getDimensioneMq(),
                lotto.getStato(),
                lotto.getOrto() != null ? lotto.getOrto().getId() : null,
                lotto.getOrto() != null ? lotto.getOrto().getNome() : null
        );
    }

    public static AttivitaDTO toAttivitaDTO(AttivitaRaccolto a) {
        if (a == null) return null;
        return new AttivitaDTO(
                a.getId(),
                a.getTipoColtura(),
                a.getQuantitaKg(),
                a.getDataRaccolto(),
                a.getAmbiente(),
                a.getFotoUrl(),
                a.getUser() != null ? a.getUser().getNome() : null,
                a.getLotto() != null ? a.getLotto().getCodice() : null
        );
    }

    public static PrenotazioneDTO toPrenotazioneDTO(Prenotazione p) {
        if (p == null) return null;
        return new PrenotazioneDTO(
                p.getId(),
                p.getDataInizio(),
                p.getDataFine(),
                p.getStato(),
                p.getUser() != null ? p.getUser().getId() : null,
                p.getUser() != null ? p.getUser().getUsername() : null,
                p.getLotto() != null ? p.getLotto().getId() : null,
                p.getLotto() != null ? p.getLotto().getCodice() : null
        );
    }

    public static OrtoDTO toOrtoDTO(Orto orto) {
        if (orto == null) return null;
        return new OrtoDTO(
                orto.getId(),
                orto.getNome(),
                orto.getIndirizzo(),
                orto.getDescrizione(),
                orto.getMqTotali()
        );
    }

    public static AttrezzoDTO toAttrezzoDTO(Attrezzo a) {
        if (a == null) return null;
        return new AttrezzoDTO(
                a.getId(),
                a.getNome(),
                a.isDisponibile(),
                a.getOrto() != null ? a.getOrto().getId() : null
        );
    }
}