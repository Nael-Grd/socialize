# Socialize 🌐

Ce projet implémente un réseau social sous forme d'application web

[![Tech Stack](https://img.shields.io/badge/Backend-Java%20%7C%20Spring%20Security%20%7C%20JWT-blue)]()
[![Frontend](https://img.shields.io/badge/Frontend-React%20%7C%20Tailwind%20CSS-blue)]()
[![Database](https://img.shields.io/badge/Database-PostgreSQL-blue)]()

---

## *FR* Version Française (English below)

### Fonctionnalités

1. **Authentification Sécurisée :** Inscription et connexion gérées par **Spring Security** et **JSON Web Tokens (JWT)**.
2. **Fil d'Actualité (Feed) :** Affichage des publications (Posts) avec gestion de la pagination.
3. **Interactions Sociales :** Possibilité de liker et de commenter les publications en temps réel.
4. **Profils et Réseau :** Pages de profil individuelles avec gestion des abonnements.

### Backend (Java / Spring Boot)

* Java 21+, Spring Boot 3, Spring Security, Spring Data JPA, JJWT.
* Base de données : PostgreSQL.
* Architecture RESTful API avec gestion des politiques CORS.

### Frontend (React / Vite)

* React.js (Vite), React Router DOM.
* Interface responsive conçue avec Tailwind CSS.

## Lancement de l'application (Local)

### 1. Prérequis

* Node.js & npm (pour le Frontend).
* JDK 21+ & Maven (pour le Backend).
* Une instance PostgreSQL en cours d'exécution.

### 2. Démarrage du Backend (Spring Boot)

Assurez-vous que vos variables d'environnement ou votre fichier `application.properties` pointent vers votre base de données locale. Exécutez ces commandes depuis le répertoire racine du backend :

```bash
# Compiler et lancer l'application Spring Boot via Maven
./mvnw spring-boot:run
```

### 2. Démarrage du Frontend (React)

```bash
# Installer les dépendances (première fois uniquement)
npm install

# Lancer le serveur de développement React
npm run dev
```

## *ENG* English Version

### Features

1. **Secure Authentication:** Registration and login managed by **Spring Security** and **JSON Web Tokens (JWT)**.
2. **News Feed:** Display of posts with pagination support.
3. **Social Interactions:** Ability to like and comment on posts in real time.
4. **Profiles and Network:** Individual profile pages with subscription management.

### Backend (Java / Spring Boot)

* Java 21+, Spring Boot 3, Spring Security, Spring Data JPA, JWT.
* Database: PostgreSQL.
* RESTful API architecture with CORS policy management.

### Frontend (React / Vite)

* React.js (Vite), React Router DOM.
* Responsive interface built with Tailwind CSS.

## Launching the Application (Local)

### 1. Prerequisites

* Node.js & npm (for the Frontend).
* JDK 21+ & Maven (for the Backend).
* A running PostgreSQL instance.

### 2. Starting the Backend (Spring Boot)

Ensure that your environment variables or your `application.properties` file point to your local database. Run these commands from the backend’s root directory:

```bash
# Compile and run the Spring Boot application via Maven
./mvnw spring-boot:run
```

### 2. Starting the Frontend (React)

```bash
# Install dependencies (first time only)
npm install

# Start the React development server
npm run dev
```

