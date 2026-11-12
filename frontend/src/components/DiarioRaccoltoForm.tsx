import React, { useState } from 'react';
import type { LottoDTO } from '../types/types';

interface DiarioRaccoltoFormProps {
    lotti: LottoDTO[];
    userId: number;
    onActivityAdded: (messaggio: string) => void;
}

const FOTO_PRESET = [
    { label: '🍅 Pomodori in vaso', url: 'https://images.unsplash.com/photo-1592841200221-a6898f307baa?w=500&auto=format&fit=crop&q=80' },
    { label: '🌿 Basilico & Aromi', url: 'https://images.unsplash.com/photo-1618375569909-3c8616cf7733?w=500&auto=format&fit=crop&q=80' },
    { label: '🍓 Fragole da balcone', url: 'https://images.unsplash.com/photo-1464965911861-746a04b4bca6?w=500&auto=format&fit=crop&q=80' },
    { label: '🌶️ Peperoncini', url: 'https://images.unsplash.com/photo-1588252303782-cb80119abd6d?w=500&auto=format&fit=crop&q=80' },
    { label: '🥬 Ortaggi condivisi', url: 'https://images.unsplash.com/photo-1540420773420-3366772f4999?w=500&auto=format&fit=crop&q=80' }
];

export const DiarioRaccoltoForm: React.FC<DiarioRaccoltoFormProps> = ({
                                                                          lotti,
                                                                          userId,
                                                                          onActivityAdded
                                                                      }) => {
    const [lottoId, setLottoId] = useState<number>(lotti.length > 0 ? lotti[0].id : 1);
    const [tipoColtura, setTipoColtura] = useState<string>('');
    const [quantitaKg, setQuantitaKg] = useState<string>('1.0');
    const [ambiente, setAmbiente] = useState<string>('BALCONE_CASA');
    const [fotoUrl, setFotoUrl] = useState<string>(FOTO_PRESET[0].url);
    const [submitting, setSubmitting] = useState<boolean>(false);
    const [error, setError] = useState<string | null>(null);

    // Funzione per caricare una vera immagine dal computer (Mac/PC) tramite FileReader
    const handleFileUpload = (e: React.ChangeEvent<HTMLInputElement>) => {
        const file = e.target.files?.[0];
        if (!file) return;

        const reader = new FileReader();
        reader.onloadend = () => {
            if (typeof reader.result === 'string') {
                setFotoUrl(reader.result); // Salva l'immagine convertita in Base64 nello stato!
            }
        };
        reader.readAsDataURL(file);
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setSubmitting(true);
        setError(null);

        // Se lotti non fosse ancora caricato, usa l'id 1 di default per non bloccare mai la richiesta
        const targetLottoId = lotti.length > 0 ? Number(lottoId) : 1;

        try {
            const response = await fetch('http://localhost:8080/api/attivita', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                credentials: 'include',
                body: JSON.stringify({
                    userId,
                    lottoId: targetLottoId,
                    tipoColtura,
                    quantitaKg: parseFloat(quantitaKg),
                    ambiente,
                    fotoUrl
                })
            });

            if (!response.ok) {
                const errText = await response.text();
                throw new Error(errText || 'Errore durante il salvataggio sul server');
            }

            onActivityAdded(`Aggiunta pianta "${tipoColtura}" (${ambiente === 'BALCONE_CASA' ? 'Mio Balcone' : 'Orto Condiviso'})!`);
            setTipoColtura('');
        } catch (err: any) {
            setError(err.message || 'Errore durante il salvataggio');
        } finally {
            setSubmitting(false);
        }
    };

    return (
        <div style={{
            backgroundColor: '#ffffff',
            padding: '1.5rem',
            borderRadius: '12px',
            border: '1px solid #cbd5e1',
            boxShadow: '0 2px 6px rgba(0,0,0,0.04)',
            marginBottom: '2rem'
        }}>
            <h3 style={{ margin: '0 0 1rem 0', color: '#1b5e20' }}>
                🪴 Aggiungi una Pianta al tuo Balcone o Registra un Raccolto
            </h3>

            {error && (
                <div style={{ backgroundColor: '#ffebee', color: '#c62828', padding: '0.7rem', borderRadius: '6px', marginBottom: '1rem', fontSize: '0.9rem' }}>
                    ⚠️ {error}
                </div>
            )}

            <form onSubmit={handleSubmit} style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: '1rem' }}>
                <div>
                    <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: 'bold', marginBottom: '0.3rem' }}>
                        Nome Pianta / Coltura:
                    </label>
                    <input
                        type="text"
                        placeholder="Es. Rosmarino, Monstera, Pomodori..."
                        value={tipoColtura}
                        onChange={(e) => setTipoColtura(e.target.value)}
                        required
                        style={{ width: '100%', padding: '0.5rem', borderRadius: '6px', border: '1px solid #cbd5e1' }}
                    />
                </div>

                <div>
                    <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: 'bold', marginBottom: '0.3rem' }}>
                        Dove si trova:
                    </label>
                    <select
                        value={ambiente}
                        onChange={(e) => setAmbiente(e.target.value)}
                        style={{ width: '100%', padding: '0.5rem', borderRadius: '6px', border: '1px solid #cbd5e1' }}
                    >
                        <option value="BALCONE_CASA">🏡 Il Mio Balcone di Casa</option>
                        <option value="ORTO_CONDIVISO">🌳 Espansione in Orto Condiviso</option>
                    </select>
                </div>

                <div>
                    <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: 'bold', marginBottom: '0.3rem' }}>
                        Spazio / Lotto collegato:
                    </label>
                    <select
                        value={lottoId}
                        onChange={(e) => setLottoId(Number(e.target.value))}
                        style={{ width: '100%', padding: '0.5rem', borderRadius: '6px', border: '1px solid #cbd5e1' }}
                    >
                        {lotti.length > 0 ? (
                            lotti.map((l) => (
                                <option key={l.id} value={l.id}>{l.codice}</option>
                            ))
                        ) : (
                            <option value={1}>B-01 (Balcone / Default)</option>
                        )}
                    </select>
                </div>

                <div>
                    <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: 'bold', marginBottom: '0.3rem' }}>
                        Stima Raccolto / Peso (Kg):
                    </label>
                    <input
                        type="number"
                        step="0.1"
                        min="0.1"
                        value={quantitaKg}
                        onChange={(e) => setQuantitaKg(e.target.value)}
                        required
                        style={{ width: '100%', padding: '0.5rem', borderRadius: '6px', border: '1px solid #cbd5e1' }}
                    />
                </div>

                {/* Sezione Caricamento Foto: Dal Computer + Preset + Anteprima Live */}
                <div style={{ gridColumn: '1 / -1', backgroundColor: '#f8fafc', padding: '1rem', borderRadius: '8px', border: '1px dashed #94a3b8' }}>
                    <div style={{ display: 'flex', gap: '1.5rem', alignItems: 'center', flexWrap: 'wrap' }}>

                        {/* Anteprima Immagine */}
                        <img
                            src={fotoUrl}
                            alt="Anteprima pianta"
                            style={{ width: '90px', height: '90px', objectFit: 'cover', borderRadius: '8px', border: '2px solid #2e7d32' }}
                        />

                        <div style={{ flex: 1 }}>
                            <label style={{ display: 'block', fontSize: '0.9rem', fontWeight: 'bold', color: '#1b5e20', marginBottom: '0.4rem' }}>
                                📸 1. Carica una foto dal tuo Mac/PC:
                            </label>
                            <input
                                type="file"
                                accept="image/*"
                                onChange={handleFileUpload}
                                style={{ marginBottom: '0.8rem', fontSize: '0.85rem' }}
                            />

                            <label style={{ display: 'block', fontSize: '0.8rem', fontWeight: 'bold', color: '#475569', marginBottom: '0.3rem' }}>
                                Oppure scegli una foto rapida / incolla URL:
                            </label>
                            <div style={{ display: 'flex', gap: '0.4rem', flexWrap: 'wrap' }}>
                                {FOTO_PRESET.map((preset) => (
                                    <button
                                        key={preset.label}
                                        type="button"
                                        onClick={() => setFotoUrl(preset.url)}
                                        style={{
                                            padding: '0.25rem 0.6rem',
                                            fontSize: '0.75rem',
                                            borderRadius: '14px',
                                            border: fotoUrl === preset.url ? '2px solid #1b5e20' : '1px solid #cbd5e1',
                                            backgroundColor: fotoUrl === preset.url ? '#dcfce7' : '#fff',
                                            cursor: 'pointer'
                                        }}
                                    >
                                        {preset.label}
                                    </button>
                                ))}
                            </div>
                        </div>
                    </div>
                </div>

                <div style={{ gridColumn: '1 / -1', textAlign: 'right' }}>
                    <button
                        type="submit"
                        disabled={submitting}
                        style={{
                            padding: '0.65rem 1.5rem',
                            backgroundColor: '#2e7d32',
                            color: '#fff',
                            border: 'none',
                            borderRadius: '6px',
                            fontWeight: 'bold',
                            cursor: 'pointer'
                        }}
                    >
                        {submitting ? 'Salvataggio...' : '➕ Pubblica Pianta con Foto'}
                    </button>
                </div>
            </form>
        </div>
    );
};