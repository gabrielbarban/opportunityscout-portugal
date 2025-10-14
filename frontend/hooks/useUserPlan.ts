import { useAuth } from '@/contexts/AuthContext';

export function useUserPlan() {
  const { user, isPremium, isFree, isEnterprise, planName } = useAuth();

  return {
    planName,
    isPremium,
    isFree,
    isEnterprise,
    canAccessMunicipios: isPremium,
    canAccessEstatisticas: isPremium,
    canAccessTemplates: isPremium,
    canAccessAlertasIlimitados: isPremium,
    maxAlertas: isFree ? 1 : 999,
    maxMunicipios: isFree ? 0 : 10,
  };
}