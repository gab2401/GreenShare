import React, { useState } from 'react';
import type { UserDTO } from '../types/types';

interface LoginModalProps {
    onSuccess: (user: UserDTO) => void;
    onClose: () => void;
}

export const LoginModal: React.FC<LoginModalProps> = ({ onSuccess, onClose }) => {
    const [username, setUsername] = useState<string>('');
    const [password, setPassword] = useState<string>('');
    const [error, setError] = useState<string | null>(null);
    const [loading, setLoading] = useState<boolean>(false);

    const handleLogin = async (e: React.FormEvent) => {
        e.preventDefault();
        setError(null);
        setLoading(true);

        try {
            const res = await fetch('http://localhost:8080/api/auth/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                credentials: 'include',
                body: JSON.stringify({ username, password })
            });

            if (!res.ok) {
                throw new Error('Username o password errati (401 Unauthorized)');
            }

            const user: UserDTO = await res.json();
            onSuccess(user);
        } catch (err: any) {
            setError(err.message || 'Errore di autenticazione');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div style={{
            position: 'fixed',
            top: 0, left: 0, right: 0, bottom: 0,
            backgroundColor: 'rgba(0, 0, 0, 0.55)',
            display: 'flex',
            justifyContent: 'center',
            alignItems: 'center',
            zIndex: 1000
        }} onClick={onClose}>
            <div
                onClick={(e) => e.stopPropagation()} // Blocca il bubbling dell'evento click!
                style={{
                    backgroundColor: '#fff',
                    padding: '2rem',
                    borderRadius: '12px',
                    width: '100%',
                    maxWidth: '420px',
                    boxShadow: '0 10px 25px rgba(0,0,0,0.2)'
                }}
            >
                <h2 style={{ marginTop: 0, color: '#1b5e20' }}>🔐 Accesso Utente</h2>
                <p style={{ fontSize: '0.9rem', color: '#555' }}>
                    Inserisci le tue credenziali per gestire il tuo balcone o prenotare lotti condivisi.
                </p>

                {error && (
                    <div style={{ backgroundColor: '#ffebee', color: '#c62828', padding: '0.6rem', borderRadius: '6px', marginBottom: '1rem', fontSize: '0.85rem' }}>
                        ⚠️ {error}
                    </div>
                )}

                <form onSubmit={handleLogin} style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
                    <div>
                        <label htmlFor="loginUser" style={{ display: 'block', fontWeight: 'bold', fontSize: '0.85rem', marginBottom: '0.3rem' }}>
                            Username:
                        </label>
                        <input
                            id="loginUser"
                            type="text"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            placeholder="es. mario oppure admin"
                            required
                            style={{ width: '100%', padding: '0.6rem', borderRadius: '6px', border: '1px solid #cbd5e1' }}
                        />
                    </div>

                    <div>
                        <label htmlFor="loginPass" style={{ display: 'block', fontWeight: 'bold', fontSize: '0.85rem', marginBottom: '0.3rem' }}>
                            Password:
                        </label>
                        <input
                            id="loginPass"
                            type="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            placeholder="Inserisci la password"
                            required
                            style={{ width: '100%', padding: '0.6rem', borderRadius: '6px', border: '1px solid #cbd5e1' }}
                        />
                    </div>

                    <div style={{ display: 'flex', gap: '0.8rem', marginTop: '0.5rem' }}>
                        <button
                            type="submit"
                            disabled={loading}
                            style={{ flex: 1, padding: '0.7rem', backgroundColor: '#2e7d32', color: '#fff', border: 'none', borderRadius: '6px', fontWeight: 'bold', cursor: 'pointer' }}
                        >
                            {loading ? 'Verifica...' : 'Entra'}
                        </button>
                        <button
                            type="button"
                            onClick={onClose}
                            style={{ padding: '0.7rem 1rem', backgroundColor: '#e2e8f0', color: '#334155', border: 'none', borderRadius: '6px', cursor: 'pointer' }}
                        >
                            Chiudi
                        </button>
                    </div>
                </form>

                {/* Scorciatoia rapida per la demo d'esame */}
                <div style={{ marginTop: '1.5rem', paddingTop: '1rem', borderTop: '1px dashed #cbd5e1', fontSize: '0.8rem', color: '#64748b' }}>
                    <div style={{ marginBottom: '0.4rem' }}>💡 <strong>Auto-compila credenziali per Demo Esame:</strong></div>
                    <div style={{ display: 'flex', gap: '0.5rem' }}>
                        <button
                            type="button"
                            onClick={() => { setUsername('mario'); setPassword('pass123'); }}
                            style={{ padding: '0.3rem 0.6rem', backgroundColor: '#e0f2fe', color: '#0369a1', border: '1px solid #bae6fd', borderRadius: '4px', cursor: 'pointer' }}
                        >
                            Cittadino (mario / pass123)
                        </button>
                        <button
                            type="button"
                            onClick={() => { setUsername('admin'); setPassword('admin123'); }}
                            style={{ padding: '0.3rem 0.6rem', backgroundColor: '#fef3c7', color: '#b45309', border: '1px solid #fde68a', borderRadius: '4px', cursor: 'pointer' }}
                        >
                            Gestore (admin / admin123)
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
};