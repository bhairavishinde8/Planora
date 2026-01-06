# Planora
planning the flow of our life

📌** Planora — Smart Task Management & Productivity Backend**

Planora is a secure backend system designed to manage tasks, track productivity, and send automated reminders.
The project focuses on backend architecture, security, scheduling, and analytics, built using Java and Spring Boot.

🚀 Features
🔐 Authentication & Security

JWT-based authentication

User-specific data isolation

Secure access to protected APIs

🗂 Task Management

Create, update, delete, and view tasks

Task priorities (LOW / MEDIUM / HIGH)

Status tracking (Pending / Completed)

Automatic overdue task detection

📅 Smart Scheduling

Daily agenda generation

Automatic sorting by due date

Overdue task handling

📧 Email Notifications

Daily task summary emails

Due-date reminders

Overdue alerts

SMTP-based email integration

📊 Productivity Analytics

Task completion percentage

Weekly productivity trends

Focus score calculation

Backend analytics APIs for dashboard integration

🛠 Tech Stack
Layer	Technology
Language	Java
Framework	Spring Boot
Security	Spring Security, JWT
Database	MySQL
ORM	Spring Data JPA
Email	SMTP (JavaMailSender)
Build Tool	Maven
Testing	Postman
📂 Project Structure
Planora/
│── backend/
│   ├── src/
│   │   ├── main/java/com/planora/backend
│   │   └── main/resources/
│   │       └── application.properties.example
│   ├── pom.xml
│── .gitignore
│── README.md


🔒 The frontend code is intentionally excluded as the primary focus of this project is backend development.

⚙️ Configuration

Sensitive configuration files are not committed for security reasons.

❗ Important

application.properties is ignored via .gitignore

Use application.properties.example as a reference

📄 Example Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/planora
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD

spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=YOUR_EMAIL
spring.mail.password=YOUR_APP_PASSWORD

jwt.secret=YOUR_JWT_SECRET

▶️ How to Run

Clone the repository:

git clone https://github.com/bhairavishinde8/Planora.git


Navigate to backend:

cd Planora/backend


Create application.properties using the example file.

Run the application:

mvn spring-boot:run


Server will start at:

http://localhost:8080

🔎 API Testing

All APIs were tested using Postman:

Authentication endpoints

Task management endpoints

Analytics endpoints

Email notification triggers

🎯 Project Highlights

Clean layered architecture (Controller → Service → Repository)

Secure authentication using JWT

Scheduled background jobs

Real-world backend use cases

Focused on scalability and maintainability

👩‍💻 Author

Bhairavi Shinde
Backend Developer
📧 bhairavishinde8@gmail.com

🔗 LinkedIn

💻 GitHub

📌 Note

This project was developed as part of backend skill enhancement and demonstrates practical implementation of real-world backend concepts.
