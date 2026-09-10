# 🌿 FloraNest

FloraNest is an AI-powered plant e-commerce platform built as a full-stack web development course project.

## Technology Stack

| Layer    | Tech                                                           |
|----------|----------------------------------------------------------------|
| Backend  | Spring Boot 3.5 (Java 17), MyBatis, MySQL, BCrypt              |
| Frontend | Vue 3 (Composition API), Element Plus, Pinia, Vue Router, Axios |

## Project Structure

```
FloraNest/
├── backend/      # Spring Boot REST API (http://localhost:8080)
├── frontend/     # Vue 3 web app (http://localhost:5173)
├── database/     # MySQL schema + sample data (floranest_db.sql)
└── docs/         # Course documentation
```

## Prerequisites

- Java 17+
- Node.js 18+
- MySQL 8 (running locally)
- Maven is **not** required globally — the Maven wrapper (`mvnw`) is included in `backend/`.

## 1. Create and seed the MySQL database

1. Make sure your MySQL server is running.

2. Create the database (the dump file does not create it for you):

```bash
   mysql -u root -p -e "CREATE DATABASE floranest_db CHARACTER SET utf8mb4"
```

3. Import the schema and sample data:

```bash
   mysql -u root -p floranest_db < database/floranest_db.sql
```

   Alternatively, open `database/floranest_db.sql` in Navicat or MySQL Workbench and run it.

## 2. Configure the backend

Backend config is split into two files so that personal/secret values never end up in the repo:

- **`backend/src/main/resources/application.properties`** — committed, safe to be public. Server port, CORS origins, MyBatis paths, etc.
- **`backend/src/main/resources/application-local.properties`** — **not committed** (it's in `.gitignore`). Your MySQL credentials, AI API key, and admin access code go here.

### Set up your local file

1. Copy the template:

```bash
   cp backend/src/main/resources/application-local.properties.example \
      backend/src/main/resources/application-local.properties
```

2. Open the new file and fill in your own values:

   | Property | Purpose | Example |
   |----------|---------|---------|
   | `spring.datasource.username` / `spring.datasource.password` | Your local MySQL credentials | `root` / `yourpassword` |
   | `app.admin.access-code` | Required on the Register page to create an admin account | pick anything private |
   | `ai.api.key` / `ai.api.url` / `ai.model` | AI Assistant provider (optional — see below) | see below |

3. `application.properties` still holds `app.cors.allowed-origins` — update that if your frontend runs on a different port or deployed URL.

> ⚠️ **Never put real credentials in `application.properties`.** That file is committed and shared with everyone who clones the repo. `application-local.properties` is the only file that should ever contain a real password or API key — and it's git-ignored specifically so that a `git push` can't leak it.

### Optional: AI Assistant

The AI chat, smart search, recommendations, and disease-detection features call an OpenAI-compatible chat completions API. FloraNest is currently configured against **OpenRouter**, but any compatible provider (SambaNova, OpenAI, etc.) works — just point the URL/model at your provider of choice.

In `application-local.properties`:

```properties
ai.api.key=your-api-key
ai.api.url=https://openrouter.ai/api/v1/chat/completions
ai.model=openrouter/free
```

Without a key, the rest of the app works normally — only the AI features fail gracefully.

## 3. Run the backend

```bash
cd backend
./mvnw spring-boot:run     # macOS/Linux
mvnw.cmd spring-boot:run   # Windows
```

The API starts at `http://localhost:8080`.

## 4. Run the frontend

```bash
cd frontend
npm install
npm run dev
```

Open `http://localhost:5173` in your browser.

There are no pre-seeded login accounts, so create one from the **Register**
page (`/register`) and then log in. Sample products and categories are already
seeded by `database/floranest_db.sql`.

## Secrets & Local Configuration

This repo is public, so nothing secret can live in a tracked file. If you're contributing or forking:

- **Never commit real values** into `application.properties` — it's shared with everyone.
- All personal/secret config (DB password, AI key, admin access code) belongs in `application-local.properties`, which is listed in `.gitignore` and stays on your machine only.
- If you ever need to share what config keys exist (for teammates or graders), update `application-local.properties.example` with placeholder values — never real ones.
- If a real secret is ever accidentally committed, adding it to `.gitignore` afterward is **not enough** — it's still in the Git history. In that case, rotate the exposed credential (change the DB password, regenerate the API key) rather than relying on removing it from history.

## Team Members

- Cathie
- Leonel
- Alpha