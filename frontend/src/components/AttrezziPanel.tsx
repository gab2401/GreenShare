import React from 'react';
import { type AttrezzoDTO } from '../types/types';

interface AttrezziPanelProps {
    attrezzi: AttrezzoDTO[];
    onPreleva: (attrezzoId: number) => void;
    onRestituisci: (attrezzoId: number) => void;
}

export const AttrezziPanel: React.FC<AttrezziPanelProps> = ({
                                                                attrezzi,
                                                                onPreleva,
                                                                onRestituisci
                                                            }) => {
    return (
        <div style={{
            marginTop: '2rem',
            backgroundColor: '#f1f8e9',
            padding: '1rem 1.5rem',
            borderRadius: '8px',
            border: '1px solid #c8e6c9'
        }}>
            <h3 style={{ margin: '0 0 1rem 0', color: '#2e7d32', fontSize: '1.1rem' }}>
                🛠️ Banca degli Attrezzi e Materiali Condivisi
            </h3>

            {attrezzi.length === 0 ? (
                <p style={{ color: '#666', fontSize: '0.9rem' }}>Nessun materiale disponibile al momento.</p>
            ) : (
                <ul style={{ listStyle: 'none', padding: 0, margin: 0 }}>
                    {attrezzi.map((a) => (
                        <li
                            key={a.id}
                            style={{
                                display: 'flex',
                                justifyContent: 'space-between',
                                alignItems: 'center',
                                padding: '0.6rem 0',
                                borderBottom: '1px solid #e0e0e0'
                            }}
                        >
              <span>
                <strong>{a.nome}</strong> — {a.disponibile ? (
                  <span style={{ color: '#2e7d32' }}>Disponibile</span>
              ) : (
                  <span style={{ color: '#d32f2f' }}>In uso</span>
              )}
              </span>

                            {a.disponibile ? (
                                <button
                                    onClick={() => onPreleva(a.id)}
                                    style={{
                                        padding: '0.3rem 0.6rem',
                                        backgroundColor: '#388e3c',
                                        color: '#fff',
                                        border: 'none',
                                        borderRadius: '4px',
                                        cursor: 'pointer'
                                    }}
                                >
                                    Preleva
                                </button>
                            ) : (
                                <button
                                    onClick={() => onRestituisci(a.id)}
                                    style={{
                                        padding: '0.3rem 0.6rem',
                                        backgroundColor: '#757575',
                                        color: '#fff',
                                        border: 'none',
                                        borderRadius: '4px',
                                        cursor: 'pointer'
                                    }}
                                >
                                    Restituisci
                                </button>
                            )}
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
};