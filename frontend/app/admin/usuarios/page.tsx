'use client';

import { useState, useEffect } from 'react';
import { useAuth } from '@/contexts/AuthContext';
import { useRouter } from 'next/navigation';
import Navbar from '@/components/Navbar';
import Card from '@/components/ui/Card';
import Input from '@/components/ui/Input';
import Button from '@/components/ui/Button';
import api from '@/lib/api';
import { UserPlus, Trash2, Edit, X } from 'lucide-react';

interface Usuario {
  id: number;
  email: string;
  nome: string;
  role: string;
}

interface UsuarioForm {
  email: string;
  password: string;
  nome: string;
  role: string;
}

export default function UsuariosPage() {
  const { user, loading: authLoading, isAdmin } = useAuth();
  const router = useRouter();
  const [usuarios, setUsuarios] = useState<Usuario[]>([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [editingId, setEditingId] = useState<number | null>(null);
  const [formData, setFormData] = useState<UsuarioForm>({
    email: '',
    password: '',
    nome: '',
    role: 'USER',
  });

  useEffect(() => {
    if (!authLoading) {
      if (!user || !isAdmin) {
        router.push('/oportunidades');
      } else {
        carregarUsuarios();
      }
    }
  }, [user, authLoading, isAdmin, router]);

  const carregarUsuarios = async () => {
    try {
      setLoading(true);
      const response = await api.get('/usuarios');
      setUsuarios(response.data);
    } catch (error) {
      console.error('Erro ao carregar usuários', error);
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      if (editingId) {
        await api.put(`/usuarios/${editingId}`, formData);
      } else {
        await api.post('/usuarios', formData);
      }
      carregarUsuarios();
      fecharModal();
    } catch (error: any) {
      alert(error.response?.data?.message || 'Erro ao salvar usuário');
    }
  };

  const handleDelete = async (id: number) => {
    if (!confirm('Deseja realmente excluir este usuário?')) return;
    try {
      await api.delete(`/usuarios/${id}`);
      carregarUsuarios();
    } catch (error: any) {
      alert(error.response?.data?.message || 'Erro ao excluir usuário');
    }
  };

  const abrirModalNovo = () => {
    setEditingId(null);
    setFormData({ email: '', password: '', nome: '', role: 'USER' });
    setShowModal(true);
  };

  const abrirModalEditar = (usuario: Usuario) => {
    setEditingId(usuario.id);
    setFormData({ ...usuario, password: '' });
    setShowModal(true);
  };

  const fecharModal = () => {
    setShowModal(false);
    setEditingId(null);
    setFormData({ email: '', password: '', nome: '', role: 'USER' });
  };

  if (authLoading || !user || !isAdmin) return null;

  return (
    <div className="min-h-screen bg-background">
      <Navbar />
      
      <div className="container mx-auto px-4 py-8">
        <div className="flex justify-between items-center mb-6">
          <h1 className="text-3xl font-bold text-foreground">Gerenciar Usuários</h1>
          <Button onClick={abrirModalNovo} className="flex items-center space-x-2">
            <UserPlus size={18} />
            <span>Novo Usuário</span>
          </Button>
        </div>

        {loading ? (
          <div className="flex justify-center py-12">
            <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-primary"></div>
          </div>
        ) : (
          <div className="grid grid-cols-1 gap-4">
            {usuarios.map((usuario) => (
              <Card key={usuario.id}>
                <div className="flex justify-between items-center">
                  <div>
                    <h3 className="text-lg font-semibold text-foreground">{usuario.nome}</h3>
                    <p className="text-sm text-muted-foreground">{usuario.email}</p>
                    <span className={`inline-block mt-2 px-2 py-1 text-xs rounded ${
                      usuario.role === 'ADMIN'
                        ? 'bg-primary/20 text-primary'
                        : 'bg-secondary/20 text-secondary-foreground'
                    }`}>
                      {usuario.role}
                    </span>
                  </div>
                  <div className="flex space-x-2">
                    <Button variant="secondary" onClick={() => abrirModalEditar(usuario)}>
                      <Edit size={18} />
                    </Button>
                    <Button variant="danger" onClick={() => handleDelete(usuario.id)}>
                      <Trash2 size={18} />
                    </Button>
                  </div>
                </div>
              </Card>
            ))}
          </div>
        )}
      </div>

      {showModal && (
        <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
          <Card className="w-full max-w-md">
            <div className="flex justify-between items-center mb-4">
              <h2 className="text-xl font-bold text-foreground">
                {editingId ? 'Editar Usuário' : 'Novo Usuário'}
              </h2>
              <button onClick={fecharModal} className="text-muted-foreground hover:text-foreground">
                <X size={24} />
              </button>
            </div>

            <form onSubmit={handleSubmit} className="space-y-4">
              <Input
                label="Nome"
                value={formData.nome}
                onChange={(e) => setFormData({ ...formData, nome: e.target.value })}
                required
              />

              <Input
                label="Email"
                type="email"
                value={formData.email}
                onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                required
              />

              <Input
                label={editingId ? 'Senha (deixe em branco para não alterar)' : 'Senha'}
                type="password"
                value={formData.password}
                onChange={(e) => setFormData({ ...formData, password: e.target.value })}
                required={!editingId}
              />

              <div>
                <label className="block text-sm font-medium mb-2 text-foreground">Perfil</label>
                <select
                  className="w-full px-3 py-2 bg-input border border-border rounded-md text-foreground focus:outline-none focus:ring-2 focus:ring-ring"
                  value={formData.role}
                  onChange={(e) => setFormData({ ...formData, role: e.target.value })}
                >
                  <option value="USER">USER</option>
                  <option value="ADMIN">ADMIN</option>
                </select>
              </div>

              <div className="flex space-x-2 pt-4">
                <Button type="button" variant="secondary" onClick={fecharModal} fullWidth>
                  Cancelar
                </Button>
                <Button type="submit" fullWidth>
                  {editingId ? 'Salvar' : 'Criar'}
                </Button>
              </div>
            </form>
          </Card>
        </div>
      )}
    </div>
  );
}