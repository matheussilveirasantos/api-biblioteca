create database biblioteca;

-- create type sigla_enum as enum (
--     'ROM',  -- Romance
--     'DRM',  -- Drama
--     'FCC',  -- Ficção Científica
--     'TER',  -- Terror
--     'BIO',  -- Biografia
--     'TEC',  -- Técnico
--     'INF',  -- Infantil
--     'HQ',   -- História em Quadrinhos
--     'HIS',  -- História
--     'FIC',  -- Ficção
--     'CON',  -- Conto
--     'AVA',  -- Aventura
--     'FAN',  -- Fantasia
--     'POL',  -- Policial
--     'LIT',  -- Literatura
--     'CUL',  -- Cultura
--     'ART',  -- Arte
--     'EDI',  -- Editorial
--     'POE',  -- Poesia
--     'ENS',  -- Ensaio
--     'REL',  -- Religião
--     'FIL',  -- Filosofia
--     'AUT'   -- Autobiografia
-- );

-- create type estado_enum as enum (
--     'AC',  -- Acre
--     'AL',  -- Alagoas
--     'AP',  -- Amapá
--     'AM',  -- Amazonas
--     'BA',  -- Bahia
--     'CE',  -- Ceará
--     'DF',  -- Distrito Federal
--     'ES',  -- Espírito Santo
--     'GO',  -- Goiás
--     'MA',  -- Maranhão
--     'MT',  -- Mato Grosso
--     'MS',  -- Mato Grosso do Sul
--     'MG',  -- Minas Gerais
--     'PA',  -- Pará
--     'PB',  -- Paraíba
--     'PR',  -- Paraná
--     'PE',  -- Pernambuco
--     'PI',  -- Piauí
--     'RJ',  -- Rio de Janeiro
--     'RN',  -- Rio Grande do Norte
--     'RS',  -- Rio Grande do Sul
--     'RO',  -- Rondônia
--     'RR',  -- Roraima
--     'SC',  -- Santa Catarina
--     'SP',  -- São Paulo
--     'SE',  -- Sergipe
--     'TO'   -- Tocantins
-- );

create table genero (
    id      serial primary key,
    nome    varchar(50)  not null unique,
    sigla   varchar(3)   not null unique
);

create table editora (
    id      serial primary key,
    nome    varchar(100) not null,
    cnpj    varchar(14)  not null unique,
    estado  varchar(2)   not null unique
);

create table autor (
    id               serial primary key,
    nome             varchar(100) not null,
    nacionalidade    varchar(50)  not null,
    data_nascimento  date         not null
);

create table livro (
    id              serial primary key,
    titulo          varchar(200) not null,
    isbn            varchar(13)  not null,
    ano_publicacao  int          not null,
    id_autor        int          not null references autor(id),
    id_genero       int          not null references genero(id),
    id_editora      int          not null references editora(id)
);

create table usuario (
    id          SERIAL       PRIMARY KEY,
    nome        VARCHAR(100) NOT NULL,
    email       VARCHAR(150) NOT NULL UNIQUE,
    senha       VARCHAR(255) NOT NULL,
    role        VARCHAR(20)  NOT NULL DEFAULT 'USER',
    ativo       BOOLEAN      NOT NULL DEFAULT TRUE,
    criado_em   TIMESTAMP    NOT NULL DEFAULT NOW()
);
