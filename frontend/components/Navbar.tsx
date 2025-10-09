'use client';

import Link from 'next/link';
import { usePathname } from 'next/navigation';
import { useAuth } from '@/contexts/AuthContext';
import { LogOut, Users, Briefcase, Radar } from 'lucide-react';
import Button from './ui/Button';

export default function Navbar() {
  const { user, logout, isAdmin } = useAuth();
  const pathname = usePathname();

  if (!user) return null;

  return (
    <nav className="bg-card border-b border-border">
      <div className="container mx-auto px-4">
        <div className="flex items-center justify-between h-16">
          <div className="flex items-center space-x-8">
            <Link href="/oportunidades" className="flex items-center space-x-2">
              <Radar size={28} className="text-primary" />
              <div>
                <div className="text-lg font-bold text-primary">OpportunityScout</div>
                <div className="text-xs text-muted-foreground -mt-1">Portugal</div>
              </div>
            </Link>
            
            <div className="flex space-x-4">
              <Link
                href="/oportunidades"
                className={`flex items-center space-x-2 px-3 py-2 rounded-md transition-colors ${
                  pathname === '/oportunidades'
                    ? 'bg-primary text-primary-foreground'
                    : 'text-muted-foreground hover:text-foreground hover:bg-accent'
                }`}
              >
                <Briefcase size={18} />
                <span>Oportunidades</span>
              </Link>

              {isAdmin && (
                <Link
                  href="/admin/usuarios"
                  className={`flex items-center space-x-2 px-3 py-2 rounded-md transition-colors ${
                    pathname === '/admin/usuarios'
                      ? 'bg-primary text-primary-foreground'
                      : 'text-muted-foreground hover:text-foreground hover:bg-accent'
                  }`}
                >
                  <Users size={18} />
                  <span>Usuários</span>
                </Link>
              )}
            </div>
          </div>

          <div className="flex items-center space-x-4">
            <div className="text-sm">
              <p className="text-foreground font-medium">{user.nome}</p>
              <p className="text-muted-foreground text-xs">{user.role}</p>
            </div>
            <Button variant="secondary" onClick={logout} className="flex items-center space-x-2">
              <LogOut size={18} />
              <span>Sair</span>
            </Button>
          </div>
        </div>
      </div>
    </nav>
  );
}