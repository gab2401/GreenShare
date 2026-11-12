import React, { useState, useEffect, useCallback } from 'react';
import type { AttivitaDTO, LottoDTO, UserDTO } from '../types/types';
import { DiarioRaccoltoForm } from './DiarioRaccoltoForm';

interface MioBalconeViewProps {
    currentUser: UserDTO | null;
    lotti: LottoDTO[];
    onOpenLogin: () => void;
}

export const MioBalconeView: React.FC<MioBalconeViewProps> = ({
                                                                  currentUser,
                                                                  lotti,
                                                                  onOpenLogin
                                                              }) => {
    const [piante, setPiante] = useState<AttivitaDTO[]>([]);
    const [filtroSoloMie, setFiltroSoloMie] = useState<boolean>(false);
    const [feedback, setFeedback] = useState<string | null>(null);

    // Chiama GET /api/attivita o GET /api/attivita?userId=... (Usa @RequestParam!)
    const caricaPiante = useCallback(() => {
        const url = (filtroSoloMie && currentUser)
            ? `http://localhost:8080/api/attivita?userId=${currentUser.id}`
            : 'http://localhost:8080/api/attivita';

        fetch(url, { credentials: 'include' })
            .then((res) => {
                if (!res.ok) {
                    throw new Error('Errore nel caricamento delle piante: ' + res.status);
                }
                return res.json();
            })
            .then((data) => {
                // Controllo di sicurezza: imposta lo stato solo se è davvero un Array!
                if (Array.isArray(data)) {
                    setPiante(data);
                } else {
                    setPiante([]);
                }
            })
            .catch((err) => {
                console.error('Errore caricamento piante:', err);
                setPiante([]); // Evita la schermata bianca in caso di errore!
            });
    }, [filtroSoloMie, currentUser]);

    useEffect(() => {
        caricaPiante();
    }, [caricaPiante]);

    return (
        <div>
            {/* Banner Introduttivo */}
            <div style={{
                backgroundColor: '#ffffff',
                padding: '1.5rem',
                borderRadius: '12px',
                border: '1px solid #bbf7d0',
                marginBottom: '1.5rem',
                display: 'flex',
                justifyContent: 'space-between',
                alignItems: 'center',
                flexWrap: 'wrap',
                gap: '1rem'
            }}>
                <div>
                    <h2 style={{ margin: '0 0 0.4rem 0', color: '#1b5e20' }}>
                        🏡 Il Mio Balcone & Galleria Piante della Comunità
                    </h2>
                    <p style={{ margin: 0, color: '#475569', fontSize: '0.95rem' }}>
                        Coltiva sul balcone di casa tua e, quando hai bisogno di più spazio, espanditi negli appezzamenti condivisi del quartiere!
                    </p>
                </div>

                {currentUser && (
                    <div style={{ display: 'flex', gap: '0.5rem' }}>
                        <button
                            onClick={() => setFiltroSoloMie(false)}
                            style={{
                                padding: '0.45rem 0.9rem',
                                borderRadius: '6px',
                                border: '1px solid #2e7d32',
                                backgroundColor: !filtroSoloMie ? '#2e7d32' : '#fff',
                                color: !filtroSoloMie ? '#fff' : '#2e7d32',
                                cursor: 'pointer',
                                fontWeight: 'bold'
                            }}
                        >
                            Tutte le Piante
                        </button>
                        <button
                            onClick={() => setFiltroSoloMie(true)}
                            style={{
                                padding: '0.45rem 0.9rem',
                                borderRadius: '6px',
                                border: '1px solid #2e7d32',
                                backgroundColor: filtroSoloMie ? '#2e7d32' : '#fff',
                                color: filtroSoloMie ? '#fff' : '#2e7d32',
                                cursor: 'pointer',
                                fontWeight: 'bold'
                            }}
                        >
                            Solo le Mie Piante
                        </button>
                    </div>
                )}
            </div>

            {feedback && (
                <div style={{ backgroundColor: '#dcfce7', color: '#166534', padding: '0.8rem 1rem', borderRadius: '8px', marginBottom: '1rem', fontWeight: 'bold' }}>
                    🌿 {feedback}
                </div>
            )}

            {/* Se loggato mostra il Form per aggiungere piante con foto */}
            {currentUser ? (
                <DiarioRaccoltoForm
                    lotti={lotti}
                    userId={currentUser.id}
                    onActivityAdded={(msg) => {
                        setFeedback(msg);
                        caricaPiante();
                    }}
                />
            ) : (
                <div style={{ backgroundColor: '#fffbeb', border: '1px solid #fde68a', padding: '1rem 1.5rem', borderRadius: '8px', marginBottom: '1.5rem', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                    <span>🔐 Effettua il login per caricare le foto delle piante del tuo balcone!</span>
                    <button
                        onClick={onOpenLogin}
                        style={{ padding: '0.45rem 1rem', backgroundColor: '#d97706', color: '#fff', border: 'none', borderRadius: '6px', cursor: 'pointer', fontWeight: 'bold' }}
                    >
                        Accedi Ora
                    </button>
                </div>
            )}

            {/* Griglia Fotografica delle Piante */}
            {piante.length === 0 ? (
                <p style={{ textAlign: 'center', color: '#64748b', padding: '2rem', backgroundColor: '#fff', borderRadius: '12px' }}>
                    Nessuna pianta ancora registrata. Usa il modulo qui sopra per aggiungere la prima pianta con foto!
                </p>
            ) : (
                <div style={{
                    display: 'grid',
                    gridTemplateColumns: 'repeat(auto-fill, minmax(260px, 1fr))',
                    gap: '1.5rem'
                }}>
                    {piante.map((p) => (
                        <div
                            key={p.id}
                            style={{
                                backgroundColor: '#fff',
                                borderRadius: '12px',
                                overflow: 'hidden',
                                border: '1px solid #e2e8f0',
                                boxShadow: '0 4px 10px rgba(0,0,0,0.05)'
                            }}
                        >
                            <img
                                src={p.fotoUrl || 'https://images.unsplash.com/photo-1592841200221-a6898f307baa?w=400'}
                                alt={p.tipoColtura}
                                style={{ width: '100%', height: '180px', objectFit: 'cover' }}
                            />
                            <div style={{ padding: '1rem' }}>
                <span style={{
                    display: 'inline-block',
                    fontSize: '0.72rem',
                    fontWeight: 'bold',
                    padding: '0.2rem 0.5rem',
                    borderRadius: '12px',
                    marginBottom: '0.5rem',
                    backgroundColor: p.ambiente === 'BALCONE_CASA' ? '#e0f2fe' : '#dcfce7',
                    color: p.ambiente === 'BALCONE_CASA' ? '#0369a1' : '#15803d'
                }}>
                  {p.ambiente === 'BALCONE_CASA' ? '🏡 BALCONE DI CASA' : '🌳 ORTO CONDIVISO'}
                </span>

                                <h4 style={{ margin: '0 0 0.4rem 0', fontSize: '1.1rem', color: '#1e293b' }}>
                                    {p.tipoColtura}
                                </h4>
                                <p style={{ margin: '0 0 0.3rem 0', fontSize: '0.85rem', color: '#475569' }}>
                                    📦 Raccolto/Stima: <strong>{p.quantitaKg} kg</strong> • Spazio: <strong>{p.lottoCodice}</strong>
                                </p>
                                <small style={{ color: '#94a3b8' }}>
                                    Coltivata da {p.username} il {p.dataRaccolto}
                                </small>
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};