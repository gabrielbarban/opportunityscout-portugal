-- ==========================
-- MIGRATION V2: Planos e Fontes Municipais
-- ==========================

-- 1. Criar tabela de planos
CREATE TABLE IF NOT EXISTS planos (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(50) NOT NULL UNIQUE,
    preco_mensal DECIMAL(10,2),
    preco_anual DECIMAL(10,2),
    max_alertas INT DEFAULT 0,
    max_municipios INT DEFAULT 0,
    acesso_templates BOOLEAN DEFAULT false,
    acesso_estatisticas BOOLEAN DEFAULT false,
    descricao TEXT
);

-- 2. Criar tabela de fontes municipais
CREATE TABLE IF NOT EXISTS fontes_municipais (
    id BIGSERIAL PRIMARY KEY,
    municipio VARCHAR(100) NOT NULL,
    distrito VARCHAR(100),
    url VARCHAR(500),
    codigo_fonte VARCHAR(50) UNIQUE NOT NULL,
    ativo BOOLEAN DEFAULT true,
    disponivel_free BOOLEAN DEFAULT false,
    populacao INT
);

-- 3. Adicionar colunas na tabela usuarios (SEM constraint ainda)
ALTER TABLE usuarios ADD COLUMN IF NOT EXISTS plano_id BIGINT;
ALTER TABLE usuarios ADD COLUMN IF NOT EXISTS data_assinatura TIMESTAMP;
ALTER TABLE usuarios ADD COLUMN IF NOT EXISTS data_expiracao TIMESTAMP;
ALTER TABLE usuarios ADD COLUMN IF NOT EXISTS stripe_customer_id VARCHAR(255);
ALTER TABLE usuarios ADD COLUMN IF NOT EXISTS stripe_subscription_id VARCHAR(255);

-- 4. Inserir planos (ANTES da constraint)
INSERT INTO planos (id, nome, preco_mensal, preco_anual, max_alertas, max_municipios, acesso_templates, acesso_estatisticas, descricao) VALUES
(1, 'FREE', 0.00, 0.00, 1, 0, false, false, 'Acesso básico às oportunidades nacionais'),
(2, 'PREMIUM', 24.90, 249.00, 999, 10, true, true, 'Acesso completo incluindo oportunidades municipais'),
(3, 'ENTERPRISE', 99.90, 999.00, 999, 999, true, true, 'Solução completa para empresas')
ON CONFLICT (id) DO NOTHING;

-- 5. Inserir fontes municipais (5 maiores cidades de Portugal)
INSERT INTO fontes_municipais (municipio, distrito, url, codigo_fonte, ativo, disponivel_free, populacao) VALUES
('Lisboa', 'Lisboa', 'https://www.cm-lisboa.pt', 'MUNICIPAL_LISBOA', true, false, 504718),
('Porto', 'Porto', 'https://www.cm-porto.pt', 'MUNICIPAL_PORTO', true, false, 231962),
('Braga', 'Braga', 'https://www.cm-braga.pt', 'MUNICIPAL_BRAGA', true, false, 193333),
('Sintra', 'Lisboa', 'https://www.cm-sintra.pt', 'MUNICIPAL_SINTRA', true, false, 385654),
('Cascais', 'Lisboa', 'https://www.cm-cascais.pt', 'MUNICIPAL_CASCAIS', true, false, 206479)
ON CONFLICT (codigo_fonte) DO NOTHING;

-- 6. Atualizar usuários existentes para plano FREE
UPDATE usuarios SET plano_id = 1 WHERE plano_id IS NULL;

-- 7. AGORA SIM criar a constraint FK (dados já existem)
DO $$ 
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint WHERE conname = 'fk_usuario_plano'
    ) THEN
        ALTER TABLE usuarios ADD CONSTRAINT fk_usuario_plano 
            FOREIGN KEY (plano_id) REFERENCES planos(id);
    END IF;
END $$;

-- 8. Definir default para novos usuarios
ALTER TABLE usuarios ALTER COLUMN plano_id SET DEFAULT 1;