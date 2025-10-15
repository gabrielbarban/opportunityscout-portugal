'use client';

import { useState, FormEvent } from 'react';
import { useRouter } from 'next/navigation';
import Input from '@/components/ui/Input';
import Button from '@/components/ui/Button';
import Card from '@/components/ui/Card';
import { Radar, ArrowLeft } from 'lucide-react';
import api from '@/lib/api';
import Link from 'next/link';

export default function CadastroPage() {
  const [nome, setNome] = useState('');
  const [email, setEmail] = useState('');
  const [telefone, setTelefone] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const router = useRouter();

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      await api.post('/auth/signup', {
        nome,
        email,
        telefone,
        password,
        confirmPassword
      });

      alert('✅ Cadastro realizado com sucesso! Faça login para continuar.');
      router.push('/login');
    } catch (err: any) {
      const message = err.response?.data?.message || err.response?.data?.error || 'Erro ao cadastrar';
      setError(message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-background px-4">
      <Card className="w-full max-w-md">
        <Link href="/login" className="inline-flex items-center space-x-1 text-sm text-primary hover:text-primary/80 mb-4">
          <ArrowLeft size={16} />
          <span>Voltar para login</span>
        </Link>

        <div className="text-center mb-8">
          <div className="flex justify-center mb-4">
            <div className="bg-primary/10 p-4 rounded-full">
              <Radar size={48} className="text-primary" />
            </div>
          </div>
          <h1 className="text-3xl font-bold text-foreground">Criar Conta</h1>
          <p className="text-primary font-medium mt-1">OpportunityScout Portugal</p>
          <p className="text-muted-foreground mt-3 text-sm">
            Cadastre-se grátis e comece a acompanhar oportunidades de financiamento
          </p>
        </div>

        <form onSubmit={handleSubmit} className="space-y-4">
          <Input
            label="Nome completo"
            type="text"
            placeholder="Seu nome"
            value={nome}
            onChange={(e) => setNome(e.target.value)}
            required
          />

          <Input
            label="Email"
            type="email"
            placeholder="seu@email.com"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />

          <Input
            label="Telefone (opcional)"
            type="tel"
            placeholder="+351 912 345 678"
            value={telefone}
            onChange={(e) => setTelefone(e.target.value)}
          />

          <Input
            label="Senha"
            type="password"
            placeholder="Mínimo 6 caracteres"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />

          <Input
            label="Confirmar senha"
            type="password"
            placeholder="Digite a senha novamente"
            value={confirmPassword}
            onChange={(e) => setConfirmPassword(e.target.value)}
            required
          />

          {error && (
            <div className="bg-destructive/10 border border-destructive text-destructive px-4 py-3 rounded-md text-sm">
              {error}
            </div>
          )}

          <Button type="submit" fullWidth disabled={loading}>
            {loading ? 'Cadastrando...' : 'Criar Conta Grátis'}
          </Button>
        </form>

        <div className="mt-6 text-center text-sm text-muted-foreground">
          <p>
            Já tem uma conta?{' '}
            <Link href="/login" className="text-primary hover:underline font-medium">
              Fazer login
            </Link>
          </p>
        </div>
      </Card>
    </div>
  );
}