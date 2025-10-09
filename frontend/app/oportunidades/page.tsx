'use client';

import { useState, useEffect } from 'react';
import { useAuth } from '@/contexts/AuthContext';
import { useRouter } from 'next/navigation';
import Navbar from '@/components/Navbar';
import Card from '@/components/ui/Card';
import Input from '@/components/ui/Input';
import Button from '@/components/ui/Button';
import api from '@/lib/api';
import { Search, ExternalLink, Calendar, Building2, RefreshCw, Info, Users, DollarSign } from 'lucide-react';
import { format } from 'date-fns';
import { ptBR } from 'date-fns/locale';

interface Oportunidade {
  id: number;
  titulo: string;
  fonte: string;
  entidade: string;
  dataInicio: string | null;
  dataFim: string | null;
  link: string;
  categoria: string | null;
  codigo: string | null;
  descricao: string | null;
  tipoApoio: string | null;
  beneficiarios: string | null;
}

export default function OportunidadesPage() {
  const { user, loading: authLoading, isAdmin } = useAuth();
  const router = useRouter();
  const [oportunidades, setOportunidades] = useState<Oportunidade[]>([]);
  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState('');
  const [fonte, setFonte] = useState('');
  const [coletando, setColetando] = useState(false);
  const [expandedId, setExpandedId] = useState<number | null>(null);

  useEffect(() => {
    if (!authLoading && !user) {
      router.push('/login');
    }
  }, [user, authLoading, router]);

  useEffect(() => {
    if (user) {
      carregarOportunidades();
    }
  }, [user, fonte]);

  const carregarOportunidades = async () => {
    try {
      setLoading(true);
      const params: any = {};
      if (fonte) params.fonte = fonte;
      const response = await api.get('/oportunidades', { params });
      setOportunidades(response.data);
    } catch (error) {
      console.error('Erro ao carregar oportunidades', error);
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = async () => {
    if (!search.trim()) {
      carregarOportunidades();
      return;
    }
    try {
      setLoading(true);
      const response = await api.get('/oportunidades', { params: { search } });
      setOportunidades(response.data);
    } catch (error) {
      console.error('Erro na busca', error);
    } finally {
      setLoading(false);
    }
  };

  const handleColetar = async () => {
    try {
      setColetando(true);
      const response = await api.post('/oportunidades/coletar');
      alert(response.data.message);
      carregarOportunidades();
    } catch (error: any) {
      alert(error.response?.data?.message || 'Erro ao coletar oportunidades');
    } finally {
      setColetando(false);
    }
  };

  const formatarData = (data: string | null) => {
    if (!data) return 'N/A';
    try {
      return format(new Date(data), 'dd/MM/yyyy', { locale: ptBR });
    } catch {
      return 'N/A';
    }
  };

  const toggleExpand = (id: number) => {
    setExpandedId(expandedId === id ? null : id);
  };

  if (authLoading || !user) return null;

  return (
    <div className="min-h-screen bg-background">
      <Navbar />
      
      <div className="container mx-auto px-4 py-8">
        <div className="flex justify-between items-center mb-6">
          <h1 className="text-3xl font-bold text-foreground">Oportunidades de Negócio</h1>
          {isAdmin && (
            <Button onClick={handleColetar} disabled={coletando} className="flex items-center space-x-2">
              <RefreshCw size={18} className={coletando ? 'animate-spin' : ''} />
              <span>{coletando ? 'Coletando...' : 'Coletar Agora'}</span>
            </Button>
          )}
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-6">
          <div className="md:col-span-2">
            <div className="flex space-x-2">
              <Input
                placeholder="Buscar por título..."
                value={search}
                onChange={(e) => setSearch(e.target.value)}
                onKeyPress={(e) => e.key === 'Enter' && handleSearch()}
              />
              <Button onClick={handleSearch} className="flex items-center space-x-2">
                <Search size={18} />
                <span>Buscar</span>
              </Button>
            </div>
          </div>

          <div>
            <select
              className="w-full px-3 py-2 bg-input border border-border rounded-md text-foreground focus:outline-none focus:ring-2 focus:ring-ring"
              value={fonte}
              onChange={(e) => setFonte(e.target.value)}
            >
              <option value="">Todas as Fontes</option>
              <option value="PORTUGAL2030">Portugal 2030</option>
              <option value="COMPETE2030">Compete 2030</option>
            </select>
          </div>
        </div>

        {loading ? (
          <div className="flex justify-center py-12">
            <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-primary"></div>
          </div>
        ) : oportunidades.length === 0 ? (
          <Card>
            <p className="text-center text-muted-foreground py-8">Nenhuma oportunidade encontrada</p>
          </Card>
        ) : (
          <div className="grid grid-cols-1 gap-4">
            {oportunidades.map((op) => (
              <Card key={op.id} className="hover:border-primary transition-colors">
                <div className="flex justify-between items-start mb-3">
                  <div className="flex-1">
                    <div className="flex items-center space-x-2 mb-2">
                      <span className={`px-2 py-1 text-xs rounded ${
                        op.fonte === 'PORTUGAL2030' 
                          ? 'bg-blue-500/20 text-blue-400' 
                          : 'bg-green-500/20 text-green-400'
                      }`}>
                        {op.fonte === 'PORTUGAL2030' ? 'Portugal 2030' : 'Compete 2030'}
                      </span>
                      {op.codigo && (
                        <span className="text-xs text-muted-foreground">Cód: {op.codigo}</span>
                      )}
                    </div>
                    
                    <h3 className="text-lg font-semibold text-foreground mb-2">{op.titulo}</h3>
                    
                    <div className="flex flex-wrap gap-4 text-sm text-muted-foreground mb-3">
                      <div className="flex items-center space-x-1">
                        <Building2 size={16} />
                        <span>{op.entidade}</span>
                      </div>
                      {op.dataFim && (
                        <div className="flex items-center space-x-1">
                          <Calendar size={16} />
                          <span>Até {formatarData(op.dataFim)}</span>
                        </div>
                      )}
                      {op.categoria && (
                        <span className="px-2 py-1 bg-accent rounded text-xs">{op.categoria}</span>
                      )}
                      {op.tipoApoio && (
                        <div className="flex items-center space-x-1">
                          <DollarSign size={16} />
                          <span>{op.tipoApoio}</span>
                        </div>
                      )}
                    </div>

                    {expandedId === op.id && (
                      <div className="mt-4 pt-4 border-t border-border">
                        {op.descricao && (
                          <div className="mb-3">
                            <p className="text-sm font-medium text-foreground mb-1">Descrição:</p>
                            <p className="text-sm text-muted-foreground">{op.descricao}</p>
                          </div>
                        )}
                        {op.beneficiarios && (
                          <div className="flex items-start space-x-2 text-sm">
                            <Users size={16} className="mt-0.5 text-primary" />
                            <div>
                              <span className="font-medium text-foreground">Beneficiários: </span>
                              <span className="text-muted-foreground">{op.beneficiarios}</span>
                            </div>
                          </div>
                        )}
                      </div>
                    )}
                  </div>

                  <div className="ml-4 flex flex-col space-y-2">
                    <a
                      href={op.link}
                      target="_blank"
                      rel="noopener noreferrer"
                      className="flex items-center space-x-1 text-primary hover:text-primary/80 transition-colors text-sm"
                    >
                      <span>Ver Fonte</span>
                      <ExternalLink size={16} />
                    </a>
                    {(op.descricao || op.beneficiarios) && (
                      <button
                        onClick={() => toggleExpand(op.id)}
                        className="flex items-center space-x-1 text-muted-foreground hover:text-foreground transition-colors text-sm"
                      >
                        <Info size={16} />
                        <span>{expandedId === op.id ? 'Menos' : 'Mais'} Info</span>
                      </button>
                    )}
                  </div>
                </div>
              </Card>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}