# Dashboard de Chamados

Dashboard administrativo integrado a um sistema de chamados técnicos, com API própria em Spring Boot.

## Pré-requisitos

- JDK 17
- Maven (ou o wrapper do seu IDE)
- PostgreSQL rodando localmente, com um banco chamado `dashboard_chamados` (guarda os chamados)
- MongoDB (local ou Atlas) configurado via `MONGODB_URI` — guarda os usuários de login e as sessões

## Variáveis de ambiente

A aplicação usa variáveis de ambiente para credenciais sensíveis (não ficam fixas no código):

| Variável | Obrigatória | Padrão | Descrição |
|---|---|---|---|
| `DB_USERNAME` | Não | `postgres` | Usuário do PostgreSQL |
| `DB_PASSWORD` | Sim | `postgres` | Senha do PostgreSQL |
| `MONGODB_URI` | Sim | `mongodb://localhost:27017/dashboard_chamados` | String de conexão do MongoDB (Atlas ou local) |
| `ADMIN_USERNAME` | Não | `admin` | Usuário do admin criado na primeira execução |
| `ADMIN_EMAIL` | Não | `admin@dashboard.local` | E-mail do admin |
| `ADMIN_PASSWORD` | Não | `TrocarSenha123!` | Senha do admin |

Exemplo (PowerShell), antes de rodar `mvn spring-boot:run`:

```powershell
$env:DB_PASSWORD="sua_senha_postgres"
$env:MONGODB_URI="mongodb+srv://usuario:senha@seu-cluster.mongodb.net/dashboard_chamados?retryWrites=true&w=majority"
```

Crie o banco Postgres antes de rodar a aplicação:

```sql
CREATE DATABASE dashboard_chamados;
```

Se seu usuário/senha do Postgres forem diferentes de `postgres`/`postgres`, ajuste em `src/main/resources/application.properties`. O Mongo não precisa de criação manual de banco/coleção — a aplicação cria tudo na primeira execução.

## Como rodar

No VSCode (ou outro IDE com suporte a Maven/Java), abra esta pasta como projeto e execute a classe `DashboardChamadosApplication`, ou pelo terminal:

```bash
mvn spring-boot:run
```

A aplicação sobe em `http://localhost:8080`. Na primeira execução, o Hibernate cria a tabela `chamado` e o arquivo `data.sql` popula alguns chamados de exemplo, e o `AdminInitializer` cria o usuário administrador inicial no Mongo.

## Login

O acesso ao dashboard e à API é restrito a um usuário com perfil `ADMIN`. As credenciais do admin criado automaticamente na primeira execução são:

- usuário: `admin`
- senha: `TrocarSenha123!`

Você pode sobrescrever esses valores com as variáveis de ambiente `ADMIN_USERNAME`, `ADMIN_EMAIL` e `ADMIN_PASSWORD` antes de rodar a aplicação. Troque a senha padrão antes de gravar evidências/vídeo de entrega.

Para testar os endpoints da API via `curl` depois de logado, é preciso manter o cookie de sessão entre as chamadas:

```bash
curl -c cookies.txt -d "login=admin&password=TrocarSenha123!" http://localhost:8080/login
curl -b cookies.txt http://localhost:8080/api/chamados/indicadores
```

Ou, mais simples: abra `http://localhost:8080/login` no navegador, entre com o admin, e navegue para os endpoints — o navegador guarda o cookie de sessão automaticamente.

## Endpoints da API

- `GET /api/chamados` — lista chamados (filtros: `dataInicio`, `dataFim`, `status`, `tipo`, `prioridade`)
- `GET /api/chamados/{id}` — busca um chamado
- `POST /api/chamados` — cria um chamado
- `PUT /api/chamados/{id}` — atualiza um chamado
- `PATCH /api/chamados/{id}/status` — atualiza apenas o status
- `GET /api/chamados/indicadores` — indicadores gerais (RF01)
- `GET /api/chamados/por-periodo?agrupamento=DIA|SEMANA|MES` — série temporal (RF02)
- `GET /api/chamados/situacao` — distribuição por status (RF03)
- `GET /api/chamados/tipos-mais-atendidos?topN=5` — ranking de tipos (RF04)

Todos os endpoints de leitura aceitam os filtros de `dataInicio`, `dataFim`, `status`, `tipo` e `prioridade` como query params (RF05).

## Dashboard

Acesse `http://localhost:8080/dashboard` para ver o painel com os indicadores, os filtros e os três gráficos (período, situação e tipos mais atendidos).
