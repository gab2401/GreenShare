export interface UserDTO {
    id: number;
    username: string;
    nome: string;
    ruolo: "ROLE_CITTADINO" | "ROLE_GESTORE";
}

export interface OrtoDTO {
    id: number;
    nome: string;
    indirizzo: string;
    descrizione: string;
    mqTotali: number;
}

export interface LottoDTO {
    id: number;
    codice: string;
    dimensioneMq: number;
    stato: "LIBERO" | "OCCUPATO" | "IN_ATTESA";
    ortoId: number;
    ortoNome: string;
}

export interface AttrezzoDTO {
    id: number;
    nome: string;
    disponibile: boolean;
    ortoId: number;
}

export interface PrenotazioneDTO {
    id: number;
    dataInizio: string;
    dataFine: string;
    stato: "IN_ATTESA" | "APPROVATA" | "RIFIUTATA";
    userId: number;
    username: string;
    lottoId: number;
    lottoCodice: string;
}

export interface AttivitaDTO {
    id: number;
    tipoColtura: string;
    quantitaKg: number;
    dataRaccolto: string;
    ambiente: "BALCONE_CASA" | "ORTO_CONDIVISO";
    fotoUrl: string;
    username: string;
    lottoCodice: string;
}