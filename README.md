# 🌿 FloraNest

## Introduction

FloraNest is an AI-powered plant e-commerce platform developed as a Full Stack Web Development course project.

## Technology Stack

### Backend
- Spring Boot
- MyBatis
- MySQL

### Frontend
- Vue 3
- Element Plus
- Axios
- Pinia
- Vue-router

### Development Tools
- IntelliJ IDEA
- VS Code
- Maven
- Git
- Postman

## Team Members

- Cathie
- Leonel
- Alpha

## Project Structure

```
FloraNest/
│
├── backend
├── frontend
├── docs
└── database
```
# FloraNest Database

This folder contains the official shared database for the FloraNest project.

## Files

- `floranest_db.sql` – Database structure and sample data.

## How to use

1. Open Navicat or MySQL Workbench.
2. Create a database named `floranest_db`.
3. Import `floranest_db.sql`.
4. Update `application.properties` with your local MySQL username and password if necessary.

Each team member should import this database before running the backend.
⚠️ Do not commit changes to `application.properties` if you only modified your local database credentials.

## Running the project
-Frontend
    Open a new terminal and run:
    ```
    cd frontend
    npm run dev
    ```
-Backend
    Open another new terminal and run:
    ```
    cd backend
    ./mvnw spring-boot:run
    ```
Then the frontend and backend will be running on `localhost:5173` and `localhost:8080` respectively.