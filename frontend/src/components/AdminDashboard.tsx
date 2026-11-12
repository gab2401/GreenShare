import React, { useState, useEffect, useCallback } from 'react';
import type { PrenotazioneDTO, UserDTO, OrtoDTO } from '../types/types';

interface AdminDashboardProps {
    orti: OrtoDTO[];
    onDataUpdated: () => void;
}

export const AdminDashboard: React.FC<AdminDashboardProps> = ({ orti, onDataUpdated }) => {
    const [prenotazioni, setPrenotazioni] = useState<PrenotazioneDTO[]>([]);
    const [utenti, setUtenti] = useState<UserDTO[]>([]);
    const [nuovoAttrezzoNome, setNuovoAttrezzoNome] = useState<string>('');
    const [ortoTargetId, setOrtoTargetId] = useState<number>(orti.length > 0 ? orti[0].id : 1);
    const [msg, setMsg] = useState<string | null>(null);

    const caricaDatiAdmin = useCallback(() => {
        fetch('http://localhost:8080/api/prenotazioni', { credentials: 'include' })
            .then((res) => res.json())
            .then((data: PrenotazioneDTO[]) => setPrenotazioni(data));

        fetch('http://localhost:8080/api/utenti', { credentials: 'include' })
            .then((res) => res.json())
            .then((data: UserDTO[]) => setUtenti(data));
    }, []);

    useEffect(() => {
        caricaDatiAdmin();
    }, [caricaDatiAdmin]);

    const handleApprova = async (id: number) => {
        await fetch(`http://localhost:8080/api/prenotazioni/${id}/approva`, {
            method: 'PUT',
            credentials: 'include'
        });
        setMsg(`Prenotazione #${id} approvata! Il lotto ora è OCCUPATO.`);
        caricaDatiAdmin();
        onDataUpdated();
    };

    const handleRifiuta = async (id: number) => {
        await fetch(`http://localhost:8080/api/prenotazioni/${id}`, {
            method: 'DELETE',
            credentials: 'include'
        });
        setMsg(`Prenotazione #${id} rifiutata/cancellata. Il lotto è tornato LIBERO.`);
        caricaDatiAdmin();
        onDataUpdated();
    };

    const handleAggiungiAttrezzo = async (e: React.FormEvent) => {
        e.preventDefault();
        await fetch(`http://localhost:8080/api/orti/${ortoTargetId}/attrezzi`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            credentials: 'include',
            body: JSON.stringify({ nome: nuovoAttrezzoNome })
        });
        setMsg(`Materiale "${nuovoAttrezzoNome}" aggiunto con successo alla comunità!`);
        setNuovoAttrezzoNome('');
        onDataUpdated();
    };

    return (
        <div style={{ display: 'flex', flexDirection: 'column', gap: '1.5rem' }}>
            <div style={{ backgroundColor: '#fff', padding: '1.5rem', borderRadius: '12px', border: '2px solid #ffb300' }}>
                <h2 style={{ margin: '0 0 0.4rem 0', color: '#b45309' }}>
                    👑 Pannello di Controllo Gestore (`ROLE_GESTORE`)
                </h2>
                <p style={{ margin: 0, color: '#555' }}>
                    Approva o rifiuta le richieste di lotti, controlla gli utenti iscritti e aggiungi nuovi attrezzi o sacchi di terriccio.
                </p>
            </div>

            {msg && (
                <div style={{ backgroundColor: '#dcfce7', color: '#166534', padding: '0.8rem 1rem', borderRadius: '8px', fontWeight: 'bold' }}>
                    ✅ {msg}
                </div>
            )}

            {/* Sezione 1: Gestione Richieste Prenotazione Lotti */}
            <div style={{ backgroundColor: '#fff', padding: '1.5rem', borderRadius: '12px', border: '1px solid #e2e8f0' }}>
                <h3 style={{ marginTop: 0 }}>📋 Richieste di Assegnazione Lotti</h3>
                {prenotazioni.length === 0 ? (
                    <p style={{ color: '#64748b' }}>Nessuna richiesta di prenotazione presente.</p>
                ) : (
                    <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: '0.9rem' }}>
                        <thead>
                        <tr style={{ backgroundColor: '#f8fafc', textAlign: 'left', borderBottom: '2px solid #e2e8f0' }}>
                            <th style={{ padding: '0.6rem' }}>ID</th>
                            <th style={{ padding: '0.6rem' }}>Utente</th>
                            <th style={{ padding: '0.6rem' }}>Lotto</th>
                            <th style={{ padding: '0.6rem' }}>Periodo</th>
                            <th style={{ padding: '0.6rem' }}>Stato</th>
                            <th style={{ padding: '0.6rem' }}>Azioni Gestore</th>
                        </tr>
                        </thead>
                        <tbody>
                        {prenotazioni.map((p) => (
                            <tr key={p.id} style={{ borderBottom: '1px solid #e2e8f0' }}>
                                <td style={{ padding: '0.6rem' }}>#{p.id}</td>
                                <td style={{ padding: '0.6rem' }}><strong>{p.username}</strong></td>
                                <td style={{ padding: '0.6rem' }}>{p.lottoCodice}</td>
                                <td style={{ padding: '0.6rem' }}>{p.dataInizio} → {p.dataFine}</td>
                                <td style={{ padding: '0.6rem' }}>
                    <span style={{
                        padding: '0.2rem 0.5rem',
                        borderRadius: '10px',
                        fontWeight: 'bold',
                        fontSize: '0.75rem',
                        backgroundColor: p.stato === 'APPROVATA' ? '#dcfce7' : '#fef3c7',
                        color: p.stato === 'APPROVATA' ? '#166534' : '#b45309'
                    }}>
                      {p.stato}
                    </span>
                                </td>
                                <td style={{ padding: '0.6rem', display: 'flex', gap: '0.5rem' }}>
                                    {p.stato === 'IN_ATTESA' && (
                                        <button
                                            onClick={() => handleApprova(p.id)}
                                            style={{ padding: '0.35rem 0.7rem', backgroundColor: '#16a34a', color: '#fff', border: 'none', borderRadius: '4px', cursor: 'pointer', fontWeight: 'bold' }}
                                        >
                                            ✓ Approva
                                        </button>
                                    )}
                                    <button
                                        onClick={() => handleRifiuta(p.id)}
                                        style={{ padding: '0.35rem 0.7rem', backgroundColor: '#dc2626', color: '#fff', border: 'none', borderRadius: '4px', cursor: 'pointer' }}
                                    >
                                        ✕ Rifiuta / Libera Lotto
                                    </button>
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                )}
            </div>

            {/* Sezione 2: Aggiungi Nuovo Materiale / Terriccio Condiviso */}
            <div style={{ backgroundColor: '#fff', padding: '1.5rem', borderRadius: '12px', border: '1px solid #e2e8f0' }}>
                <h3 style={{ marginTop: 0 }}>🛠️ Aggiungi Nuovo Attrezzo o Sacco di Terriccio alla Banca</h3>
                <form onSubmit={handleAggiungiAttrezzo} style={{ display: 'flex', gap: '1rem', flexWrap: 'wrap' }}>
                    <select
                        value={ortoTargetId}
                        onChange={(e) => setOrtoTargetId(Number(e.target.value))}
                        style={{ padding: '0.5rem', borderRadius: '6px', border: '1px solid #cbd5e1' }}
                    >
                        {orti.map((o) => (
                            <option key={o.id} value={o.id}>{o.nome}</option>
                        ))}
                    </select>
                    <input
                        type="text"
                        placeholder="Es. Sacco Compost Bio 25L, Forbici Pota..."
                        value={nuovoAttrezzoNome}
                        onChange={(e) => setNuovoAttrezzoNome(e.target.value)}
                        required
                        style={{ flex: 1, padding: '0.5rem', borderRadius: '6px', border: '1px solid #cbd5e1' }}
                    />
                    <button
                        type="submit"
                        style={{ padding: '0.5rem 1.2rem', backgroundColor: '#2e7d32', color: '#fff', border: 'none', borderRadius: '6px', fontWeight: 'bold', cursor: 'pointer' }}
                    >
                        ➕ Aggiungi Materiale
                    </button>
                </form>
            </div>

            {/* Sezione 3: Controllo Utenti Registrati */}
            <div style={{ backgroundColor: '#fff', padding: '1.5rem', borderRadius: '12px', border: '1px solid #e2e8f0' }}>
                <h3 style={{ marginTop: 0 }}>👥 Utenti Registrati nella Piattaforma</h3>
                <div style={{ display: 'flex', gap: '1rem', flexWrap: 'wrap' }}>
                    {utenti.map((u) => (
                        <div key={u.id} style={{ padding: '0.8rem 1.2rem', backgroundColor: '#f8fafc', borderRadius: '8px', border: '1px solid #cbd5e1' }}>
                            <strong>{u.nome}</strong> (@{u.username}) — <span style={{ color: '#2e7d32', fontWeight: 'bold' }}>{u.ruolo}</span>
                        </div>
                    ))}
                </div>
            </div>
        </div>
    );
};