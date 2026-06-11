# Biblioteca API

API REST de catálogo de livros com autenticação JWT — deployada no [Render](https://render.com).

## Deploy no Render

### Pré-requisitos
- Conta no [Render](https://dashboard.render.com)
- Banco PostgreSQL já criado (as credenciais estão na imagem da pergunta)
- Repositório no GitHub com este código

### Variáveis de ambiente (configurar no Dashboard do Render)

| Variável             | Descrição                                   | Exemplo                          |
|----------------------|---------------------------------------------|----------------------------------|
| `DB_URL`             | URL JDBC do banco (hostname interno)        | `jdbc:postgresql://dpg-xxx:5432/biblioteca_5ihl` |
| `DB_USERNAME`        | Usuário do banco                            | `biblioteca`                     |
| `DB_PASSWORD`        | Senha do banco                              | *(segredo)*                      |
| `JWT_SECRET`         | Chave Base64URL para assinar tokens JWT     | *(segredo, mín. 32 bytes)*       |
| `JWT_EXPIRATION`     | Expiração do token em ms                    | `86400000` (24h)                 |
| `MAIL_USERNAME`      | E-mail Gmail para envio                     | `seu@gmail.com`                  |
| `MAIL_PASSWORD`      | Senha de app do Gmail (não a senha normal)  | *(segredo)*                      |
| `EMAIL_DESTINATARIOS`| E-mail(s) de destino separados por vírgula  | `admin@biblioteca.com`           |

> **Importante:** `DB_URL` deve usar o **Internal Hostname** do Render (ex: `dpg-xxx-a`), pois app e banco estão na mesma rede interna.

### Passos
1. Faça push deste projeto para um repositório GitHub
2. No Render: **New → Web Service → Connect Repository**
3. Render detecta automaticamente o `Dockerfile`
4. Preencha as variáveis de ambiente acima em **Environment**
5. Clique em **Deploy**

### Endpoints principais
- `POST /auth/login` — autenticação
- `POST /auth/cadastrar` — registro de usuário
- `GET /swagger-ui.html` — documentação interativa
- `GET /actuator/health` — health check

## Execução local (Docker)

```bash
# Crie um .env com as variáveis acima, depois:
docker compose up --build
```
