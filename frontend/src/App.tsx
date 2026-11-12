import { useState, useEffect, useCallback } from 'react';
import type { OrtoDTO, LottoDTO, AttrezzoDTO, UserDTO } from './types/types';
import { Navbar, type ActiveTab } from './components/Navbar';
import { OrtoList } from './components/OrtoList';
import { LottoGrid } from './components/LottoGrid';
import { AttrezziPanel } from './components/AttrezziPanel';
import { PrenotaLottoModal } from './components/PrenotaLottoModal';
import { LoginModal } from './components/LoginModal';
import { MioBalconeView } from './components/MioBalcone';
import { AdminDashboard } from './components/AdminDashboard';

export function App() {
  const [currentUser, setCurrentUser] = useState<UserDTO | null>(null);
  const [activeTab, setActiveTab] = useState<ActiveTab>('ESPLORA');
  const [showLoginModal, setShowLoginModal] = useState<boolean>(false);

  const [orti, setOrti] = useState<OrtoDTO[]>([]);
  const [selectedOrto, setSelectedOrto] = useState<OrtoDTO | null>(null);

  const [lotti, setLotti] = useState<LottoDTO[]>([]);
  const [filtroStatoLotto, setFiltroStatoLotto] = useState<string>(''); // Usa @RequestParam ?stato=LIBERO
  const [attrezzi, setAttrezzi] = useState<AttrezzoDTO[]>([]);
  const [selectedLotto, setSelectedLotto] = useState<LottoDTO | null>(null);

  const [feedbackMsg, setFeedbackMsg] = useState<string | null>(null);
  const [errorMsg, setErrorMsg] = useState<string | null>(null);

  useEffect(() => {
    fetch('http://localhost:8080/api/auth/me', { credentials: 'include' })
        .then((res) => (res.ok ? res.json() : null))
        .then((user: UserDTO | null) => {
          if (user) setCurrentUser(user);
        })
        .catch(() => {});
  }, []);

  useEffect(() => {
    fetch('http://localhost:8080/api/orti')
        .then((res) => res.json())
        .then((data: OrtoDTO[]) => {
          setOrti(data);
          if (data.length > 0) setSelectedOrto(data[0]);
        })
        .catch(() => setErrorMsg('Impossibile contattare il server Spring Boot (porta 8080).'));
  }, []);

  const loadDettagliOrto = useCallback((ortoId: number, statoFiltro: string) => {
    const query = statoFiltro ? `?stato=${statoFiltro}` : '';
    fetch(`http://localhost:8080/api/orti/${ortoId}/lotti${query}`)
        .then((res) => res.json())
        .then((data: LottoDTO[]) => setLotti(data));

    fetch(`http://localhost:8080/api/orti/${ortoId}/attrezzi`)
        .then((res) => res.json())
        .then((data: AttrezzoDTO[]) => setAttrezzi(data));
  }, []);

  useEffect(() => {
    if (selectedOrto) {
      setSelectedLotto(null);
      loadDettagliOrto(selectedOrto.id, filtroStatoLotto);
    }
  }, [selectedOrto, filtroStatoLotto, loadDettagliOrto]);

  const handleLogout = async () => {
    await fetch('http://localhost:8080/api/auth/logout', { method: 'POST', credentials: 'include' });
    setCurrentUser(null);
    setSelectedLotto(null);
    setActiveTab('ESPLORA');
    setFeedbackMsg('Logout effettuato con successo.');
  };

  const handleToggleAttrezzo = async (attrezzoId: number, azione: 'preleva' | 'restituisci') => {
    if (!currentUser) {
      setShowLoginModal(true);
      return;
    }
    setErrorMsg(null);
    try {
      const res = await fetch(`http://localhost:8080/api/attrezzi/${attrezzoId}/${azione}`, {
        method: 'PUT',
        credentials: 'include'
      });
      if (!res.ok) throw new Error('Operazione non autorizzata o non valida.');
      if (selectedOrto) loadDettagliOrto(selectedOrto.id, filtroStatoLotto);
      setFeedbackMsg(`Materiale aggiornato (${azione}) con successo!`);
    } catch (err: any) {
      setErrorMsg(err.message);
    }
  };

  return (
      <div style={{ maxWidth: '1180px', margin: '0 auto', padding: '1.5rem' }}>
        <Navbar
            currentUser={currentUser}
            activeTab={activeTab}
            onTabChange={(t) => { setActiveTab(t); setFeedbackMsg(null); setErrorMsg(null); }}
            onOpenLogin={() => setShowLoginModal(true)}
            onLogout={handleLogout}
        />

        {showLoginModal && (
            <LoginModal
                onClose={() => setShowLoginModal(false)}
                onSuccess={(user) => {
                  setCurrentUser(user);
                  setShowLoginModal(false);
                  setFeedbackMsg(`Benvenuto ${user.nome}! Sessione attiva.`);
                }}
            />
        )}

        {errorMsg && (
            <div style={{ backgroundColor: '#ffebee', color: '#c62828', padding: '0.8rem 1rem', borderRadius: '8px', marginBottom: '1rem', border: '1px solid #ef9a9a' }}>
              ⚠️ {errorMsg}
            </div>
        )}
        {feedbackMsg && (
            <div style={{ backgroundColor: '#dcfce7', color: '#166534', padding: '0.8rem 1rem', borderRadius: '8px', marginBottom: '1rem', border: '1px solid #86efac' }}>
              🌿 {feedbackMsg}
            </div>
        )}

        {/* VISTA 1: ESPLORA ORTI & CONDIVISIONE */}
        {activeTab === 'ESPLORA' && (
            <main style={{ display: 'grid', gridTemplateColumns: '320px 1fr', gap: '2rem', alignItems: 'start' }}>
              <OrtoList
                  orti={orti}
                  selectedOrto={selectedOrto}
                  onSelectOrto={(o) => setSelectedOrto(o)}
              />

              <section style={{ backgroundColor: '#ffffff', padding: '1.8rem', borderRadius: '12px', border: '1px solid #e2e8f0', boxShadow: '0 4px 12px rgba(0,0,0,0.03)' }}>
                {selectedOrto ? (
                    <>
                      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', borderBottom: '1px solid #e2e8f0', paddingBottom: '1rem', flexWrap: 'wrap', gap: '1rem' }}>
                        <div>
                          <h2 style={{ margin: '0 0 0.3rem 0', color: '#1b5e20' }}>{selectedOrto.nome}</h2>
                          <p style={{ margin: '0 0 0.3rem 0', color: '#475569' }}>{selectedOrto.descrizione}</p>
                          <small style={{ color: '#64748b' }}>📍 {selectedOrto.indirizzo} • Superficie: {selectedOrto.mqTotali} mq</small>
                        </div>

                        {/* Filtro Lotti via Query String (@RequestParam) */}
                        <div>
                          <label style={{ fontSize: '0.8rem', fontWeight: 'bold', display: 'block', marginBottom: '0.2rem' }}>
                            Filtra per Stato:
                          </label>
                          <select
                              value={filtroStatoLotto}
                              onChange={(e) => setFiltroStatoLotto(e.target.value)}
                              style={{ padding: '0.45rem', borderRadius: '6px', border: '1px solid #cbd5e1' }}
                          >
                            <option value="">Tutti i Lotti</option>
                            <option value="LIBERO">Solo Liberi</option>
                            <option value="IN_ATTESA">In Attesa</option>
                            <option value="OCCUPATO">Occupati</option>
                          </select>
                        </div>
                      </div>

                      <LottoGrid
                          lotti={lotti}
                          selectedLotto={selectedLotto}
                          ortoNome={selectedOrto.nome}
                          onSelectLotto={(lotto) => {
                            if (!currentUser) {
                              setShowLoginModal(true);
                              return;
                            }
                            setSelectedLotto(lotto);
                          }}
                      />

                      {selectedLotto && currentUser && (
                          <PrenotaLottoModal
                              lotto={selectedLotto}
                              userId={currentUser.id}
                              onClose={() => setSelectedLotto(null)}
                              onSuccess={(lottoId) => {
                                setSelectedLotto(null);
                                setFeedbackMsg(`Richiesta inviata per il lotto #${lottoId}! Un gestore la approverà a breve.`);
                                loadDettagliOrto(selectedOrto.id, filtroStatoLotto);
                              }}
                          />
                      )}

                      <AttrezziPanel
                          attrezzi={attrezzi}
                          onPreleva={(id) => handleToggleAttrezzo(id, 'preleva')}
                          onRestituisci={(id) => handleToggleAttrezzo(id, 'restituisci')}
                      />
                    </>
                ) : (
                    <p>Seleziona uno spazio verde dalla lista a sinistra.</p>
                )}
              </section>
            </main>
        )}

        {/* VISTA 2: IL MIO BALCONE & GALLERIA PIANTE CON FOTO */}
        {activeTab === 'BALCONE' && (
            <MioBalconeView
                currentUser={currentUser}
                lotti={lotti}
                onOpenLogin={() => setShowLoginModal(true)}
            />
        )}

        {/* VISTA 3: AREA AMMINISTRAZIONE GESTORE */}
        {activeTab === 'ADMIN' && currentUser?.ruolo === 'ROLE_GESTORE' && (
            <AdminDashboard
                orti={orti}
                onDataUpdated={() => {
                  if (selectedOrto) loadDettagliOrto(selectedOrto.id, filtroStatoLotto);
                }}
            />
        )}
      </div>
  );
}

export default App;