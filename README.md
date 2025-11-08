# Mini-ProjectMgmt
An assigment from The Growth Hive
A complete backend application to manage users, projects, and tasks using **Spring Boot 3**, **Spring Security (JWT)**, and **MySQL**.

## 📦 Features


- User Registration & Login with JWT Authentication
- CRUD Operations for Projects
- CRUD Operations for Tasks (linked to Projects)
- Search and Sort Tasks by `title`, `description`, `dueDate`, or `priority`
- Global Exception Handling
- Field Validation (using `jakarta.validation`)
- MySQL Integration with Spring Data JPA
- Optional Docker Support


---


## 🧰 Tech Stack


| Layer | Technology |
|--------|-------------|
| Language | Java 17 |
| Framework | Spring Boot 3.x |
| Security | Spring Security + JWT |
| ORM | Spring Data JPA (Hibernate) |
| Database | MySQL |
| Build Tool | Maven |

---


## ⚙️ Setup Instructions


### 1️⃣ Clone the Repository
```bash
git clone https://github.com/<your-username>/project-management-api.git
cd project-management-api
```

###Configure Database

Make sure MySQL is running and update credentials in application.properties:
spring.datasource.username=your_username
spring.datasource.password=your_password

Create A Database and tables:
Create Database with name and make sure same name should be pass to application.properties file
create tables
CREATE TABLE IF NOT EXISTS users (
id BIGINT AUTO_INCREMENT PRIMARY KEY,
username VARCHAR(100) UNIQUE NOT NULL,
password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS project (
id BIGINT AUTO_INCREMENT PRIMARY KEY,
name VARCHAR(255) NOT NULL,
description TEXT,
created_at DATETIME,
updated_at DATETIME,
user_id BIGINT,
CONSTRAINT fk_project_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS task (
id BIGINT AUTO_INCREMENT PRIMARY KEY,
title VARCHAR(255) NOT NULL,
description TEXT,
status VARCHAR(50),
priority VARCHAR(50),
due_date DATE,
created_at DATETIME,
updated_at DATETIME,
project_id BIGINT,
CONSTRAINT fk_task_project FOREIGN KEY (project_id) REFERENCES project(id) ON DELETE CASCADE
);


###Build and Run
mvn clean install
mvn spring-boot:run

###API Documentation

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/auth/register | Register new user |
| POST | /api/auth/login | Login and get JWT token |
| POST | /api/projects | Create project (auth required) |
| GET | /api/projects | List all projects for user |
| PUT | /api/projects/{id} | Update project |
| DELETE | /api/projects/{id} | Delete project |
| POST | /api/tasks/{projectId} | Create task under project |
| GET | /api/tasks/project/{projectId} | Get tasks by project |
| PUT | /api/tasks/{taskId} | Update task |
| DELETE | /api/tasks/{taskId} | Delete task |
| GET | /api/tasks/search?query= | Search tasks |
| GET | /api/tasks/sort?sortBy= | Sort tasks |


🔑 Authentication

All endpoints except /api/auth/** require a Bearer Token.

Example Header: Authorization: Bearer <your-jwt-token>
