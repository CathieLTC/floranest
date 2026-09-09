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

Open `backend/src/main/resources/application.properties` and check the following:

| Property | Purpose | Default |
|----------|---------|---------|
| `spring.datasource.username` / `spring.datasource.password` | Your local MySQL credentials | `root` / `123456` |
| `app.cors.allowed-origins` | Frontend origin(s) allowed to call the API. Comma-separate multiple origins (e.g. a deployed domain) or login requests will be blocked by CORS. | `http://localhost:5173` |

> ⚠️ `application.properties` is shared in the repo. If you only change local
> database credentials for yourself, do **not** commit that file.

### Optional: AI Assistant

The `/ai` chat (floating assistant and the AI page) calls a
SambaNova-compatible chat API. To enable it, add these properties:

```properties
ai.api.key=your-api-key
ai.api.url=https://api.sambanova.ai/v1/chat/completions
ai.model=Meta-Llama-3.1-8B-Instruct
```

Without a key the rest of the app works normally; only the AI features fail.

## 3. Run the backend

```bash
cd backend
./mvnw spring-boot:run    
mvnw.cmd spring-boot:run  
```

The API starts at `http://localhost:8080`.

## 4. Run the frontend

```bash
cd frontend
npm install

```

Open `http://localhost:5173` in your browser.

There are no pre-seeded login accounts, so create one from the **Register**
page (`/register`) and then log in. Sample products and categories are already
seeded by `database/floranest_db.sql`.

## Team Members

- Cathie
- Leonel
- Alpha
