import React from 'react';
import { type LottoDTO } from '../types/types';

interface LottoCardProps {
    lotto: LottoDTO;
    onSelect: (lotto: LottoDTO) => void;
    isSelected: boolean;
}

export const LottoCard: React.FC<LottoCardProps> = ({ lotto, onSelect, isSelected }) => {
    // Configurazione cromatica in base allo stato
    const getBadgeStyle = (stato: string) => {
        switch (stato) {
            case 'LIBERO':
                return { backgroundColor: '#e8f5e9', color: '#2e7d32', border: '1px solid #81c784' };
            case 'OCCUPATO':
                return { backgroundColor: '#ffebee', color: '#c62828', border: '1px solid #e57373' };
            default:
                return { backgroundColor: '#fff8e1', color: '#f57f17', border: '1px solid #ffd54f' };
        }
    };

    return (
        <div
            style={{
                border: isSelected ? '2px solid #1b5e20' : '1px solid #ddd',
                borderRadius: '8px',
                padding: '1rem',
                backgroundColor: '#fff',
                boxShadow: isSelected ? '0 4px 8px rgba(0,0,0,0.1)' : 'none',
                display: 'flex',
                flexDirection: 'column',
                justifyContent: 'space-between'
            }}
        >
            <div>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                    <h4 style={{ margin: '0 0 0.5rem 0' }}>{lotto.codice}</h4>
                    <span style={{
                        fontSize: '0.75rem',
                        padding: '0.2rem 0.5rem',
                        borderRadius: '12px',
                        fontWeight: 'bold',
                        ...getBadgeStyle(lotto.stato)
                    }}>
            {lotto.stato}
          </span>
                </div>
                <p style={{ margin: '0.5rem 0', fontSize: '0.9rem', color: '#666' }}>
                    Superficie: <strong>{lotto.dimensioneMq} mq</strong>
                </p>
            </div>

            <button
                disabled={lotto.stato !== 'LIBERO'}
                onClick={() => onSelect(lotto)}
                style={{
                    marginTop: '1rem',
                    padding: '0.5rem 0.75rem',
                    backgroundColor: lotto.stato === 'LIBERO' ? '#2e7d32' : '#ccc',
                    color: '#fff',
                    border: 'none',
                    borderRadius: '4px',
                    cursor: lotto.stato === 'LIBERO' ? 'pointer' : 'not-allowed',
                    fontWeight: 'bold'
                }}
            >
                {lotto.stato === 'LIBERO' ? 'Richiedi Lotto' : 'Non Disponibile'}
            </button>
        </div>
    );
};