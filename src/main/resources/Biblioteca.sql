-- Script de inicialização — o banco já existe no Render (não recria o database)

CREATE TABLE IF NOT EXISTS genero (
    id      SERIAL PRIMARY KEY,
    nome    VARCHAR(50)  NOT NULL UNIQUE,
    sigla   VARCHAR(3)   NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS editora (
    id      SERIAL PRIMARY KEY,
    nome    VARCHAR(100) NOT NULL,
    cnpj    VARCHAR(14)  NOT NULL UNIQUE,
    estado  VARCHAR(2)   NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS autor (
    id               SERIAL PRIMARY KEY,
    nome             VARCHAR(100) NOT NULL,
    nacionalidade    VARCHAR(50)  NOT NULL,
    data_nascimento  DATE         NOT NULL
);

CREATE TABLE IF NOT EXISTS livro (
    id              SERIAL PRIMARY KEY,
    titulo          VARCHAR(200) NOT NULL,
    isbn            VARCHAR(13)  NOT NULL,
    ano_publicacao  INT          NOT NULL,
    id_autor        INT          NOT NULL REFERENCES autor(id),
    id_genero       INT          NOT NULL REFERENCES genero(id),
    id_editora      INT          NOT NULL REFERENCES editora(id)
);
