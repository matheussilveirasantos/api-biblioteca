# Biblioteca API

API REST de catálogo de livros com autenticação JWT — pronta para deploy no [Render](https://render.com).

## Deploy no Render (GitHub → Render)

### 1. Subir o código no GitHub

O repositório remoto já aponta para:

`https://github.com/matheussilveirasantos/api-biblioteca`

```bash
git add .
git commit -m "Preparar API para deploy no Render"
git push origin main
```

> **Atenção:** nunca faça commit do arquivo `.env`. Use `.env.example` como referência.

### 2. Criar o Web Service no Render

1. Acesse [dashboard.render.com](https://dashboard.render.com/)
2. **New → Web Service**
3. Conecte o repositório `api-biblioteca` do GitHub
4. O Render detecta automaticamente o `Dockerfile`
5. Confirme:
   - **Runtime:** Docker
   - **Health Check Path:** `/actuator/health`
   - **Plan:** Free (se aplicável)

### 3. Banco PostgreSQL no Render (já criado)

Use o banco existente no [dashboard Render](https://dashboard.render.com/) com estes dados da aba **Connections**:

| Campo | Valor |
|-------|-------|
| Hostname (interno) | `dpg-d8kvr9ugvqtc73ad819g-a` |
| Port | `5432` |
| Database | `biblioteca_5ihl` |
| Username | `biblioteca` |
| Password | copie do Dashboard |

> Use sempre o **Internal Hostname** (termina em `-a`), pois a API roda na mesma rede do Render.

### 4. Variáveis de ambiente (Environment)

No Web Service → **Environment**, adicione:

| Variável | Valor |
|----------|-------|
| `DB_URL` | `jdbc:postgresql://dpg-d8kvr9ugvqtc73ad819g-a:5432/biblioteca_5ihl` |
| `DB_USERNAME` | `biblioteca` |
| `DB_PASSWORD` | *(senha da aba Connections)* |
| `JWT_SECRET` | Chave Base64URL (mín. 32 bytes) | *(segredo)* |
| `JWT_EXPIRATION` | Expiração do token em ms | `86400000` |
| `MAIL_USERNAME` | E-mail Gmail para envio | `seu@gmail.com` |
| `MAIL_PASSWORD` | Senha de app do Gmail | *(segredo)* |
| `EMAIL_DESTINATARIOS` | Destinatários (vírgula) | `admin@biblioteca.com` |
| `SPRING_PROFILES_ACTIVE` | Perfil Spring | `prod` |

O Render injeta automaticamente `PORT` e `RENDER_EXTERNAL_URL` (usado pelo Swagger).

### 5. Deploy

Clique em **Create Web Service** ou **Manual Deploy**. O build usa o `Dockerfile` multi-stage (Maven + JRE 17).

### Deploy via Blueprint (opcional)

O arquivo `render.yaml` permite criar o serviço via **New → Blueprint**. Os segredos devem ser preenchidos manualmente no Dashboard após a criação.

## Endpoints principais

| Método | Rota | Descrição |
|--------|------|-----------|
| `POST` | `/auth/login` | Autenticação |
| `POST` | `/auth/cadastrar` | Registro de usuário |
| `GET` | `/swagger-ui.html` | Documentação interativa |
| `GET` | `/actuator/health` | Health check |

## Execução local

```bash
# 1. Copie o exemplo de variáveis
cp .env.example .env
# Edite .env com suas credenciais

# 2. Suba com Docker
docker compose up --build
```

A API ficará em `http://localhost:8080`.

## Segurança

- Rotacione `DB_PASSWORD`, `JWT_SECRET` e `MAIL_PASSWORD` se o `.env` já foi commitado no passado
- O `.gitignore` impede novos commits de segredos e da pasta `target/`
