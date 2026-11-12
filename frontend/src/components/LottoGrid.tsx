import React from 'react';
import { type LottoDTO } from '../types/types';
import { LottoCard } from './LottoCards.tsx';

interface LottoGridProps {
    lotti: LottoDTO[];
    selectedLotto: LottoDTO | null;
    onSelectLotto: (lotto: LottoDTO) => void;
    ortoNome: string;
}

export const LottoGrid: React.FC<LottoGridProps> = ({
                                                        lotti,
                                                        selectedLotto,
                                                        onSelectLotto,
                                                        ortoNome
                                                    }) => {
    return (
        <div style={{ marginTop: '1.5rem' }}>
            <h3 style={{ fontSize: '1.1rem', color: '#333', marginBottom: '1rem' }}>
                Lotti e Cassoni disponibili in: <em>{ortoNome}</em>
            </h3>

            {lotti.length === 0 ? (
                <p style={{ color: '#888', fontStyle: 'italic' }}>
                    Nessun lotto registrato per questa area verde.
                </p>
            ) : (
                <div style={{
                    display: 'grid',
                    gridTemplateColumns: 'repeat(auto-fill, minmax(200px, 1fr))',
                    gap: '1rem'
                }}>
                    {lotti.map((lotto) => (
                        <LottoCard
                            key={lotto.id}
                            lotto={lotto}
                            isSelected={selectedLotto?.id === lotto.id}
                            onSelect={onSelectLotto}
                        />
                    ))}
                </div>
            )}
        </div>
    );
};