import React from 'react';
import type { UserDTO } from '../types/types';

export type ActiveTab = 'ESPLORA' | 'BALCONE' | 'ADMIN';

interface NavbarProps {
    currentUser: UserDTO | null;
    activeTab: ActiveTab;
    onTabChange: (tab: ActiveTab) => void;
    onOpenLogin: () => void;
    onLogout: () => void;
}

export const Navbar: React.FC<NavbarProps> = ({
                                                  currentUser,
                                                  activeTab,
                                                  onTabChange,
                                                  onOpenLogin,
                                                  onLogout
                                              }) => {
    const navBtnStyle = (tab: ActiveTab): React.CSSProperties => ({
        padding: '0.45rem 0.75rem',
        borderRadius: '6px',
        border: 'none',
        cursor: 'pointer',
        fontWeight: 'bold',
        fontSize: '0.85rem',
        backgroundColor: activeTab === tab ? '#ffffff' : 'rgba(255, 255, 255, 0.15)',
        color: activeTab === tab ? '#1b5e20' : '#ffffff'
    });

    return (
        <header style={{
            display: 'flex',
            justifyContent: 'space-between',
            alignItems: 'center',
            flexWrap: 'wrap',
            gap: '1rem',
            padding: '0.9rem 1.2rem',
            background: 'linear-gradient(135deg, #1b5e20 0%, #2e7d32 100%)',
            color: 'white',
            borderRadius: '12px',
            boxShadow: '0 4px 12px rgba(27, 94, 32, 0.2)',
            marginBottom: '1.5rem'
        }}>
            {/* Brand */}
            <div>
                <h1 style={{ margin: 0, fontSize: '1.5rem', color: '#fff', letterSpacing: '-0.5px' }}>
                    🌱 GreenShare
                </h1>
                <small style={{ color: '#c8e6c9' }}>
                    Dal tuo Balcone di Casa agli Orti Condivisi di Quartiere
                </small>
            </div>

            {/* Menu di Navigazione SPA */}
            <nav style={{ display: 'flex', gap: '0.6rem', alignItems: 'center' }}>
                <button style={navBtnStyle('ESPLORA')} onClick={() => onTabChange('ESPLORA')}>
                    🌍 Esplora Orti & Scambio
                </button>

                <button style={navBtnStyle('BALCONE')} onClick={() => onTabChange('BALCONE')}>
                    🏡 Il Mio Balcone & Piante
                </button>

                {currentUser?.ruolo === 'ROLE_GESTORE' && (
                    <button style={navBtnStyle('ADMIN')} onClick={() => onTabChange('ADMIN')}>
                        👑 Area Gestore
                    </button>
                )}
            </nav>

            {/* Stato Autenticazione */}
            <div style={{ display: 'flex', alignItems: 'center', gap: '0.8rem' }}>
                {currentUser ? (
                    <>
                        <div style={{ textAlign: 'right', fontSize: '0.85rem' }}>
                            <div><strong>{currentUser.nome}</strong></div>
                            <span style={{
                                backgroundColor: currentUser.ruolo === 'ROLE_GESTORE' ? '#ffb300' : '#81c784',
                                color: '#1b5e20',
                                padding: '0.1rem 0.4rem',
                                borderRadius: '4px',
                                fontWeight: 'bold',
                                fontSize: '0.7rem'
                            }}>
                {currentUser.ruolo === 'ROLE_GESTORE' ? 'AMMINISTRATORE' : 'CITTADINO'}
              </span>
                        </div>
                        <button
                            onClick={onLogout}
                            style={{
                                padding: '0.45rem 0.9rem',
                                backgroundColor: '#c62828',
                                color: '#fff',
                                border: 'none',
                                borderRadius: '6px',
                                cursor: 'pointer',
                                fontWeight: 'bold'
                            }}
                        >
                            Esci
                        </button>
                    </>
                ) : (
                    <button
                        onClick={onOpenLogin}
                        style={{
                            padding: '0.55rem 1.1rem',
                            backgroundColor: '#ffb300',
                            color: '#1b5e20',
                            border: 'none',
                            borderRadius: '6px',
                            cursor: 'pointer',
                            fontWeight: 'bold'
                        }}
                    >
                        🔐 Accedi con Password
                    </button>
                )}
            </div>
        </header>
    );
};