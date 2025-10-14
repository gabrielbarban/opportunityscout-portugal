'use client';

import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import { useAuth } from '@/contexts/AuthContext';
import Navbar from '@/components/Navbar';
import Card from '@/components/ui/Card';
import Button from '@/components/ui/Button';
import PlanBadge from '@/components/ui/PlanBadge';
import api from '@/lib/api';
import { Check, X, Zap, Crown, Gift } from 'lucide-react';

interface Plano {
  id: number;
  nome: string;
  precoMensal: number;
  precoAnual: number;
  maxAlertas: number;
  maxMunicipios: number;
  acessoTemplates: boolean;
  acessoEstatisticas: boolean;
  descricao: string;
  economiaAnual: number;
}

export default function PricingPage() {
  const { user, loading: authLoading, planName } = useAuth();
  const router = useRouter();
  const [planos, setPlanos] = useState<Plano[]>([]);
  const [loading, setLoading] = useState(true);
  const [periodo, setPeriodo] = useState<'mensal' | 'anual'>('mensal');

  useEffect(() => {
    carregarPlanos();
  }, []);

  const carregarPlanos = async () => {
    try {
      const response = await api.get('/planos');
      setPlanos(response.data);
    } catch (error) {
      console.error('Erro ao carregar planos', error);
    } finally {
      setLoading(false);
    }
  };

  const getPreco = (plano: Plano) => {
    if (plano.nome === 'FREE') return 0;
    return periodo === 'mensal' ? plano.precoMensal : plano.precoAnual / 12;
  };

  const getPrecoTotal = (plano: Plano) => {
    if (plano.nome === 'FREE') return 0;
    return periodo === 'mensal' ? plano.precoMensal : plano.precoAnual;
  };

  const handleSelectPlan = (plano: Plano) => {
    if (plano.nome === 'FREE') {
      router.push('/login');
      return;
    }
    
    // Por enquanto, só redireciona para oportunidades
    // Na próxima etapa vamos integrar Stripe
    alert(`Em breve: Checkout para ${plano.nome} - ${periodo}`);
  };

  const features = [
    {
      name: 'Oportunidades Nacionais',
      free: true,
      premium: true,
      enterprise: true
    },
    {
      name: 'Oportunidades Municipais (10 cidades)',
      free: false,
      premium: true,
      enterprise: true
    },
    {
      name: 'Alertas por Email',
      free: '1 alerta',
      premium: 'Ilimitado',
      enterprise: 'Ilimitado'
    },
    {
      name: 'Calendário de Prazos',
      free: false,
      premium: true,
      enterprise: true
    },
    {
      name: 'Templates de Candidatura',
      free: false,
      premium: true,
      enterprise: true
    },
    {
      name: 'Estatísticas e Histórico',
      free: false,
      premium: true,
      enterprise: true
    },
    {
      name: 'Análise de Compatibilidade',
      free: false,
      premium: true,
      enterprise: true
    },
    {
      name: 'Export PDF/Excel',
      free: false,
      premium: true,
      enterprise: true
    },
    {
      name: 'API de Integração',
      free: false,
      premium: false,
      enterprise: true
    },
    {
      name: 'Multi-usuários',
      free: false,
      premium: false,
      enterprise: '10 usuários'
    },
    {
      name: 'Suporte Prioritário',
      free: false,
      premium: false,
      enterprise: true
    }
  ];

  if (loading) {
    return (
      <div className="min-h-screen bg-background flex items-center justify-center">
        <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-primary"></div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-background">
      {user && <Navbar />}
      
      <div className="container mx-auto px-4 py-12">
        {/* Header */}
        <div className="text-center mb-12">
          <h1 className="text-4xl md:text-5xl font-bold text-foreground mb-4">
            Escolha o Plano Ideal
          </h1>
          <p className="text-xl text-muted-foreground mb-8">
            Não perca nenhuma oportunidade de financiamento em Portugal
          </p>

          {/* Toggle Mensal/Anual */}
          <div className="flex items-center justify-center space-x-4">
            <button
              onClick={() => setPeriodo('mensal')}
              className={`px-6 py-2 rounded-lg font-medium transition-colors ${
                periodo === 'mensal'
                  ? 'bg-primary text-primary-foreground'
                  : 'bg-secondary text-secondary-foreground hover:bg-secondary/80'
              }`}
            >
              Mensal
            </button>
            <button
              onClick={() => setPeriodo('anual')}
              className={`px-6 py-2 rounded-lg font-medium transition-colors flex items-center space-x-2 ${
                periodo === 'anual'
                  ? 'bg-primary text-primary-foreground'
                  : 'bg-secondary text-secondary-foreground hover:bg-secondary/80'
              }`}
            >
              <span>Anual</span>
              <span className="text-xs bg-green-500 text-white px-2 py-0.5 rounded-full">
                -17% OFF
              </span>
            </button>
          </div>
        </div>

        {/* Cards de Planos */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-8 mb-16">
          {planos.map((plano) => {
            const isPlanoPremium = plano.nome === 'FREE';
            const isPlanoAtual = !!(user && planName === plano.nome); // CORRIGIDO: forçar boolean
            
            return (
              <Card 
                key={plano.id}
                className={`relative ${
                  isPlanoPremium 
                    ? 'border-2 border-primary shadow-lg shadow-primary/20' 
                    : 'border border-border'
                }`}
              >
                {/* Badge Recommended */}
                {isPlanoPremium && (
                  <div className="absolute -top-4 left-1/2 -translate-x-1/2">
                    <div className="bg-primary text-primary-foreground px-4 py-1 rounded-full text-sm font-bold flex items-center space-x-1">
                      <Zap size={14} />
                      <span>RECOMENDADO</span>
                    </div>
                  </div>
                )}

                {/* Badge Plano Atual */}
                {isPlanoAtual && (
                  <div className="absolute top-4 right-4">
                    <PlanBadge plan={plano.nome} size="sm" showIcon />
                  </div>
                )}

                <div className="p-6">
                  {/* Nome do Plano */}
                  <div className="flex items-center space-x-2 mb-2">
                    {plano.nome === 'FREE' && <Gift size={24} className="text-gray-400" />}
                    {plano.nome === 'PREMIUM' && <Zap size={24} className="text-primary" />}
                    {plano.nome === 'ENTERPRISE' && <Crown size={24} className="text-purple-400" />}
                    <h3 className="text-2xl font-bold text-foreground">{plano.nome}</h3>
                  </div>

                  {/* Descrição */}
                  <p className="text-sm text-muted-foreground mb-6 h-12">
                    {plano.descricao}
                  </p>

                  {/* Preço */}
                  <div className="mb-6">
                    <div className="flex items-baseline space-x-2">
                      <span className="text-4xl font-bold text-foreground">
                        €{getPreco(plano).toFixed(2)}
                      </span>
                      <span className="text-muted-foreground">/mês</span>
                    </div>
                    {periodo === 'anual' && plano.nome !== 'FREE' && (
                      <div className="mt-2">
                        <p className="text-sm text-muted-foreground">
                          €{getPrecoTotal(plano).toFixed(2)} cobrado anualmente
                        </p>
                        <p className="text-xs text-green-500 font-medium">
                          Economize {plano.economiaAnual.toFixed(0)}% vs mensal
                        </p>
                      </div>
                    )}
                  </div>

                  {/* Botão */}
                  <Button
                    onClick={() => handleSelectPlan(plano)}
                    variant={isPlanoPremium ? 'primary' : 'secondary'}
                    fullWidth
                    disabled={isPlanoAtual}
                    className="mb-6"
                  >
                    {isPlanoAtual ? 'Plano Atual' : plano.nome === 'FREE' ? 'Começar Grátis' : 'Assinar Agora'}
                  </Button>

                  {/* Features principais */}
                  <div className="space-y-3">
                    <div className="flex items-center space-x-2 text-sm">
                      <Check size={16} className="text-primary" />
                      <span className="text-foreground">
                        {plano.maxAlertas === 999 ? 'Alertas ilimitados' : `${plano.maxAlertas} alerta`}
                      </span>
                    </div>
                    
                    {plano.maxMunicipios > 0 && (
                      <div className="flex items-center space-x-2 text-sm">
                        <Check size={16} className="text-primary" />
                        <span className="text-foreground">
                          {plano.maxMunicipios} municípios cobertos
                        </span>
                      </div>
                    )}
                    
                    {plano.acessoTemplates && (
                      <div className="flex items-center space-x-2 text-sm">
                        <Check size={16} className="text-primary" />
                        <span className="text-foreground">Templates de candidatura</span>
                      </div>
                    )}
                    
                    {plano.acessoEstatisticas && (
                      <div className="flex items-center space-x-2 text-sm">
                        <Check size={16} className="text-primary" />
                        <span className="text-foreground">Estatísticas avançadas</span>
                      </div>
                    )}

                    {plano.nome === 'ENTERPRISE' && (
                      <>
                        <div className="flex items-center space-x-2 text-sm">
                          <Check size={16} className="text-primary" />
                          <span className="text-foreground">API de integração</span>
                        </div>
                        <div className="flex items-center space-x-2 text-sm">
                          <Check size={16} className="text-primary" />
                          <span className="text-foreground">10 usuários</span>
                        </div>
                      </>
                    )}
                  </div>
                </div>
              </Card>
            );
          })}
        </div>

        {/* Tabela Comparativa */}
        <div className="mb-16">
          <h2 className="text-3xl font-bold text-center text-foreground mb-8">
            Comparativo Completo
          </h2>
          
          <Card className="overflow-x-auto">
            <table className="w-full">
              <thead>
                <tr className="border-b border-border">
                  <th className="text-left py-4 px-4 text-foreground font-semibold">
                    Funcionalidades
                  </th>
                  <th className="text-center py-4 px-4 text-foreground font-semibold">
                    FREE
                  </th>
                  <th className="text-center py-4 px-4 text-foreground font-semibold">
                    PREMIUM
                  </th>
                  <th className="text-center py-4 px-4 text-foreground font-semibold">
                    ENTERPRISE
                  </th>
                </tr>
              </thead>
              <tbody>
                {features.map((feature, index) => (
                  <tr key={index} className="border-b border-border hover:bg-accent/50">
                    <td className="py-3 px-4 text-foreground">{feature.name}</td>
                    <td className="py-3 px-4 text-center">
                      {typeof feature.free === 'boolean' ? (
                        feature.free ? (
                          <Check size={20} className="text-primary mx-auto" />
                        ) : (
                          <X size={20} className="text-muted-foreground mx-auto" />
                        )
                      ) : (
                        <span className="text-sm text-foreground">{feature.free}</span>
                      )}
                    </td>
                    <td className="py-3 px-4 text-center">
                      {typeof feature.premium === 'boolean' ? (
                        feature.premium ? (
                          <Check size={20} className="text-primary mx-auto" />
                        ) : (
                          <X size={20} className="text-muted-foreground mx-auto" />
                        )
                      ) : (
                        <span className="text-sm text-foreground">{feature.premium}</span>
                      )}
                    </td>
                    <td className="py-3 px-4 text-center">
                      {typeof feature.enterprise === 'boolean' ? (
                        feature.enterprise ? (
                          <Check size={20} className="text-primary mx-auto" />
                        ) : (
                          <X size={20} className="text-muted-foreground mx-auto" />
                        )
                      ) : (
                        <span className="text-sm text-foreground">{feature.enterprise}</span>
                      )}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </Card>
        </div>

        {/* FAQ */}
        <div className="max-w-3xl mx-auto">
          <h2 className="text-3xl font-bold text-center text-foreground mb-8">
            Perguntas Frequentes
          </h2>
          
          <div className="space-y-4">
            <Card>
              <h3 className="font-bold text-foreground mb-2">Posso trocar de plano depois?</h3>
              <p className="text-sm text-muted-foreground">
                Sim! Você pode fazer upgrade ou downgrade a qualquer momento. O valor será ajustado proporcionalmente.
              </p>
            </Card>

            <Card>
              <h3 className="font-bold text-foreground mb-2">Como funciona o plano anual?</h3>
              <p className="text-sm text-muted-foreground">
                O plano anual é cobrado uma vez por ano e oferece 17% de desconto em relação ao mensal. Você economiza quase 2 meses!
              </p>
            </Card>

            <Card>
              <h3 className="font-bold text-foreground mb-2">O plano FREE tem alguma limitação de tempo?</h3>
              <p className="text-sm text-muted-foreground">
                Não! O plano FREE é gratuito para sempre. Você terá acesso às oportunidades nacionais sem nenhum custo.
              </p>
            </Card>

            <Card>
              <h3 className="font-bold text-foreground mb-2">Quais municípios estão cobertos?</h3>
              <p className="text-sm text-muted-foreground">
                Atualmente cobrimos as 5 maiores cidades de Portugal: Lisboa, Porto, Braga, Sintra e Cascais. Estamos expandindo para mais municípios.
              </p>
            </Card>

            <Card>
              <h3 className="font-bold text-foreground mb-2">Como funciona o pagamento?</h3>
              <p className="text-sm text-muted-foreground">
                Aceitamos pagamentos via cartão de crédito/débito através do Stripe. Todos os dados são processados de forma segura e criptografada.
              </p>
            </Card>
          </div>
        </div>
      </div>
    </div>
  );
}