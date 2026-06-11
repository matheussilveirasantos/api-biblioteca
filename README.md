# Biblioteca API

API REST para gerenciamento de catálogo de livros, construída com Spring Boot 3 + PostgreSQL + JWT.

## Endpoints principais

| Método | Rota | Descrição | Auth |
|--------|------|-----------|------|
| POST | `/auth/register` | Cadastrar novo usuário | ❌ |
| POST | `/auth/login` | Login → retorna JWT | ❌ |
| GET | `/livros` | Listar livros | ✅ |
| POST | `/livros` | Criar livro | ✅ |
| PUT | `/livros/{id}` | Atualizar livro | ✅ |
| DELETE | `/livros/{id}` | Remover livro | ✅ |
| GET | `/autores` | Listar autores | ✅ |
| GET | `/generos` | Listar gêneros | ✅ |
| GET | `/editoras` | Listar editoras | ✅ |

A documentação completa está disponível em `/swagger-ui.html`.

## Deploy no Render

### 1. Banco de Dados PostgreSQL

1. No [Render Dashboard](https://dashboard.render.com), clique em **New → PostgreSQL**
2. Defina um nome (ex: `biblioteca-db`) e crie
3. Copie a **External Database URL** (usada localmente) e o **Internal Database URL** (usado pelo serviço na mesma região)

### 2. Web Service (API)

1. Clique em **New → Web Service**
2. Conecte ao seu repositório GitHub
3. Configure:
   - **Runtime:** Docker
   - **Region:** mesma do banco

### 3. Variáveis de Ambiente no Render

Na aba **Environment** do Web Service, adicione:

| Variável | Valor |
|----------|-------|
| `DATABASE_URL` | Cole a **Internal Database URL** do Render, mas troque `postgresql://` por `jdbc:postgresql://` |
| `DB_USERNAME` | Username do banco (ex: `biblioteca`) |
| `DB_PASSWORD` | Password do banco |
| `JWT_SECRET` | Uma string longa e aleatória |
| `JWT_EXPIRATION` | `86400000` (24h em ms) |
| `CORS_ALLOWED_ORIGINS` | URL do seu frontend React (ex: `https://meu-app.vercel.app`) |
| `MAIL_USERNAME` | Seu email Gmail (opcional) |
| `MAIL_PASSWORD` | Senha de app Gmail (opcional) |

> **Dica:** O Render pode linkar o banco automaticamente via **Environment Groups** — ao linkar, ele injeta `DATABASE_URL` no formato correto (com `postgresql://`). Nesse caso, substitua no `application.properties` por `${DATABASE_URL}` diretamente usando o driver JDBC URL conforme documentado acima.

### 4. Como converter a DATABASE_URL do Render

O Render fornece: `postgresql://usuario:senha@host:5432/dbname`

Você precisa passar como: `jdbc:postgresql://host:5432/dbname`

Então crie as variáveis separadas:
- `DATABASE_URL` = `jdbc:postgresql://HOST:5432/DBNAME`
- `DB_USERNAME` = `usuario`
- `DB_PASSWORD` = `senha`

## Rodando localmente

```bash
# Copie e configure as variáveis
cp .env.example .env

# Com Docker Compose
docker-compose up

# Ou direto com Maven (requer PostgreSQL rodando)
./mvnw spring-boot:run
```

## Registro e Login

**Registrar:**
```json
POST /auth/register
{
  "nome": "João Silva",
  "username": "joao",
  "email": "joao@email.com",
  "password": "minhasenha123"
}
```

**Login:**
```json
POST /auth/login
{
  "username": "joao",
  "password": "minhasenha123"
}
```

**Resposta do login:**
```json
{
  "token": "eyJhbGci...",
  "tipo": "Bearer",
  "username": "joao"
}
```

Use o token nas requisições autenticadas:
```
Authorization: Bearer eyJhbGci...
```
