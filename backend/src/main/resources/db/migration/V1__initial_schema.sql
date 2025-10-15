CREATE TABLE IF NOT EXISTS usuarios (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    nome VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS oportunidades (
    id BIGSERIAL PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    fonte VARCHAR(50) NOT NULL,
    entidade VARCHAR(255) NOT NULL,
    data_inicio DATE,
    data_fim DATE,
    link VARCHAR(1000) NOT NULL,
    categoria VARCHAR(255),
    codigo VARCHAR(255),
    descricao VARCHAR(2000),
    objetivos VARCHAR(500),
    valor_financiamento VARCHAR(255),
    tipo_apoio VARCHAR(255),
    beneficiarios VARCHAR(255),
    criado_em TIMESTAMP NOT NULL DEFAULT NOW(),
    atualizado_em TIMESTAMP NOT NULL DEFAULT NOW()
);