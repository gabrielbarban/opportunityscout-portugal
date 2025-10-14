interface PlanBadgeProps {
  plan: string;
  size?: 'sm' | 'md' | 'lg';
  showIcon?: boolean;
}

export default function PlanBadge({ 
  plan, 
  size = 'md',
  showIcon = false 
}: PlanBadgeProps) {
  const getStyles = () => {
    switch (plan.toUpperCase()) {
      case 'FREE':
        return {
          bg: 'bg-gray-500/20',
          text: 'text-gray-400',
          border: 'border-gray-500/30'
        };
      case 'PREMIUM':
        return {
          bg: 'bg-primary/20',
          text: 'text-primary',
          border: 'border-primary/30'
        };
      case 'ENTERPRISE':
        return {
          bg: 'bg-gradient-to-r from-purple-500/20 to-pink-500/20',
          text: 'text-purple-400',
          border: 'border-purple-500/30'
        };
      default:
        return {
          bg: 'bg-gray-500/20',
          text: 'text-gray-400',
          border: 'border-gray-500/30'
        };
    }
  };

  const getSizeClasses = () => {
    switch (size) {
      case 'sm':
        return 'px-2 py-0.5 text-xs';
      case 'md':
        return 'px-3 py-1 text-sm';
      case 'lg':
        return 'px-4 py-1.5 text-base';
      default:
        return 'px-3 py-1 text-sm';
    }
  };

  const getIcon = () => {
    switch (plan.toUpperCase()) {
      case 'FREE':
        return '🆓';
      case 'PREMIUM':
        return '⭐';
      case 'ENTERPRISE':
        return '👑';
      default:
        return '';
    }
  };

  const styles = getStyles();
  const sizeClasses = getSizeClasses();

  return (
    <span 
      className={`inline-flex items-center space-x-1 ${styles.bg} ${styles.text} ${styles.border} border rounded-full font-semibold ${sizeClasses}`}
    >
      {showIcon && <span>{getIcon()}</span>}
      <span>{plan.toUpperCase()}</span>
    </span>
  );
}