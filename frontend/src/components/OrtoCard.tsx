import React from 'react';
import { type OrtoDTO } from '../types/types';

interface OrtoCardProps {
    orto: OrtoDTO;
    isSelected: boolean;
    onSelect: (orto: OrtoDTO) => void;
}

export const OrtoCard: React.FC<OrtoCardProps> = ({ orto, isSelected, onSelect }) => {
    return (
        <div
            onClick={() => onSelect(orto)}
            style={{
                border: isSelected ? '2px solid #2e7d32' : '1px solid #ccc',
                backgroundColor: isSelected ? '#e8f5e9' : '#fff',
                borderRadius: '8px',
                padding: '1rem',
                cursor: 'pointer',
                transition: 'box-shadow 0.2s',
                marginBottom: '1rem'
            }}
        >
            <h3 style={{ margin: '0 0 0.5rem 0', color: '#1b5e20' }}>{orto.nome}</h3>
            <p style={{ margin: '0 0 0.5rem 0', fontSize: '0.9rem', color: '#555' }}>📍 {orto.indirizzo}</p>
            <p style={{ margin: '0 0 0.5rem 0', fontSize: '0.85rem' }}>{orto.descrizione}</p>
            <span style={{ fontSize: '0.8rem', fontWeight: 'bold', color: '#333' }}>
        Estensione: {orto.mqTotali} mq
      </span>
        </div>
    );
};