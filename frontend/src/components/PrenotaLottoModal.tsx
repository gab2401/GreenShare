import React, { useState } from 'react';
import type { LottoDTO } from '../types/types';

interface PrenotaLottoModalProps {
    lotto: LottoDTO;
    userId: number;
    onClose: () => void;
    onSuccess: (lottoId: number) => void;
}

export const PrenotaLottoModal: React.FC<PrenotaLottoModalProps> = ({
                                                                        lotto,
                                                                        userId,
                                                                        onClose,
                                                                        onSuccess
                                                                    }) => {
    const [dataInizio, setDataInizio] = useState<string>('2026-11-15');
    const [dataFine, setDataFine] = useState<string>('2027-05-15');
    const [loading, setLoading] = useState<boolean>(false);
    const [errorMsg, setErrorMsg] = useState<string | null>(null);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault(); // Blocca il ricaricamento pagina del browser
        setLoading(true);
        setErrorMsg(null);

        try {
            const response = await fetch('http://localhost:8080/api/prenotazioni', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                credentials: 'include', // Invia il cookie di sessione HttpOnly
                body: JSON.stringify({
                    userId,
                    lottoId: lotto.id,
                    dataInizio,
                    dataFine
                })
            });

            if (!response.ok) {
                const text = await response.text();
                throw new Error(text || 'Errore durante la prenotazione');
            }

            // Notifica al genitore App.tsx che la prenotazione è andata a buon fine
            onSuccess(lotto.id);
        } catch (err: any) {
            setErrorMsg(err.message || 'Errore di rete');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div style={{
            marginTop: '1.5rem',
            padding: '1.5rem',
            border: '2px solid #2e7d32',
            borderRadius: '8px',
            backgroundColor: '#ffffff'
        }}>
            <h3 style={{ marginTop: 0, color: '#1b5e20' }}>
                📅 Richiedi Assegnazione: {lotto.codice} ({lotto.dimensioneMq} mq)
            </h3>

            {errorMsg && (
                <div style={{ color: '#c62828', backgroundColor: '#ffebee', padding: '0.5rem', borderRadius: '4px', marginBottom: '1rem' }}>
                    {errorMsg}
                </div>
            )}

            <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
                <div>
                    <label htmlFor="dataInizio" style={{ display: 'block', fontWeight: 'bold', marginBottom: '0.3rem' }}>
                        Data Inizio Coltivazione:
                    </label>
                    <input
                        id="dataInizio"
                        type="date"
                        value={dataInizio}
                        onChange={(e) => setDataInizio(e.target.value)}
                        required
                        style={{ width: '100%', padding: '0.5rem', borderRadius: '4px', border: '1px solid #ccc' }}
                    />
                </div>

                <div>
                    <label htmlFor="dataFine" style={{ display: 'block', fontWeight: 'bold', marginBottom: '0.3rem' }}>
                        Data Fine Stagione:
                    </label>
                    <input
                        id="dataFine"
                        type="date"
                        value={dataFine}
                        onChange={(e) => setDataFine(e.target.value)}
                        required
                        style={{ width: '100%', padding: '0.5rem', borderRadius: '4px', border: '1px solid #ccc' }}
                    />
                </div>

                <div style={{ display: 'flex', gap: '1rem', marginTop: '0.5rem' }}>
                    <button
                        type="submit"
                        disabled={loading}
                        style={{
                            padding: '0.6rem 1.2rem',
                            backgroundColor: '#2e7d32',
                            color: '#fff',
                            border: 'none',
                            borderRadius: '4px',
                            cursor: 'pointer',
                            fontWeight: 'bold'
                        }}
                    >
                        {loading ? 'Invio in corso...' : 'Conferma Prenotazione'}
                    </button>

                    <button
                        type="button"
                        onClick={onClose}
                        style={{
                            padding: '0.6rem 1.2rem',
                            backgroundColor: '#9e9e9e',
                            color: '#fff',
                            border: 'none',
                            borderRadius: '4px',
                            cursor: 'pointer'
                        }}
                    >
                        Annulla
                    </button>
                </div>
            </form>
        </div>
    );
};