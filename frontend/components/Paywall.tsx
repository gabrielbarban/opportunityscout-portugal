import { useRouter } from 'next/navigation';
import Card from './ui/Card';
import Button from './ui/Button';
import { Lock, Crown, Zap } from 'lucide-react';

interface PaywallProps {
  feature: string;
  description?: string;
  requiredPlan?: 'PREMIUM' | 'ENTERPRISE';
  showCTA?: boolean;
}

export default function Paywall({ 
  feature, 
  description,
  requiredPlan = 'PREMIUM',
  showCTA = true 
}: PaywallProps) {
  const router = useRouter();

  const getIcon = () => {
    switch (requiredPlan) {
      case 'ENTERPRISE':
        return <Crown size={48} className="text-purple-400" />;
      case 'PREMIUM':
      default:
        return <Lock size={48} className="text-primary" />;
    }
  };

  const getColor = () => {
    return requiredPlan === 'ENTERPRISE' 
      ? 'border-purple-500/50 bg-purple-500/5' 
      : 'border-primary/50 bg-primary/5';
  };

  return (
    <Card className={`${getColor()} border-2`}>
      <div className="text-center py-8 px-4">
        <div className="flex justify-center mb-4">
          {getIcon()}
        </div>
        
        <h3 className="text-xl font-bold mb-2 text-foreground">
          Recurso {requiredPlan}
        </h3>
        
        <p className="text-muted-foreground mb-1 font-medium">
          {feature}
        </p>
        
        {description && (
          <p className="text-sm text-muted-foreground mb-4">
            {description}
          </p>
        )}

        {showCTA && (
          <div className="mt-6 space-y-3">
            <Button 
              onClick={() => router.push('/pricing')} 
              className="w-full max-w-xs mx-auto flex items-center justify-center space-x-2"
            >
              <Zap size={18} />
              <span>Fazer Upgrade para {requiredPlan}</span>
            </Button>
            
            <p className="text-xs text-muted-foreground">
              A partir de €24.90/mês
            </p>
          </div>
        )}
      </div>
    </Card>
  );
}