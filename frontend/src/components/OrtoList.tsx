import React from 'react';
import type { OrtoDTO } from '../types/types';
import { OrtoCard } from './OrtoCard';

interface OrtoListProps {
    orti: OrtoDTO[];
    selectedOrto: OrtoDTO | null;
    onSelectOrto: (orto: OrtoDTO) => void;
}

export const OrtoList: React.FC<OrtoListProps> = ({
                                                      orti,
                                                      selectedOrto,
                                                      onSelectOrto
                                                  }) => {
    return (
        <section>
            <h2 style={{ fontSize: '1.2rem', marginBottom: '1rem', color: '#1b5e20' }}>
                Spazi Verdi & Comunità
            </h2>
            {orti.length === 0 ? (
                <p style={{ color: '#666' }}>Caricamento spazi in corso...</p>
            ) : (
                orti.map((orto) => (
                    <OrtoCard
                        key={orto.id}
                        orto={orto}
                        isSelected={selectedOrto?.id === orto.id}
                        onSelect={onSelectOrto}
                    />
                ))
            )}
        </section>
    );
};