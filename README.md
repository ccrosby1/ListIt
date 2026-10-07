# ListIT
 
ListIT is a web-based inventory management application that allows users to organize and manage multiple inventory catalogs, products, and categories through a centralized interface. The system supports secure authentication, role-based authorization, catalog sharing, and administrative user management.
 
## Features
 
- User registration and authentication
- Role-based security using Spring Security
- User profile management
- Catalog creation, editing, viewing, and deletion
- Product management within catalogs
- Parent-child category management
- Product search functionality
- Catalog sharing with view permissions
- Administrative dashboard and user management
- Persistent CSV application logging
- MySQL database persistence
 
## Technology Stack
 
- Java 17
- Spring Boot 3.4.4
- Spring Data JDBC
- Spring Security
- Thymeleaf
- MySQL 8.0
- Bootstrap
- HTML5
- CSS3
- JavaScript
 
## Architecture
 
ListIT follows a layered MVC architecture:
 
```
Presentation Layer
↓
Controllers
↓
Services
↓
Repositories
↓
MySQL Database
```
 
This design promotes separation of concerns, maintainability, scalability, and easier testing.
 
## Database
 
Major entities include:
 
- User Credentials
- User
- Roles
- User Roles
- Catalog
- Product
- Category
- Catalog Product
- Catalog Share
 
The database uses foreign key relationships and normalized design principles to maintain data integrity.
 
## Installation
 
### Prerequisites
 
- Java 17+
- Maven
- MySQL 8.x
- Spring Tool Suite 4 (optional)
 
### Setup
 
1. Clone the repository:
 
```bash
git clone https://github.com/ccrosby1/ListIt/tree/88b1fdec0b3894729ce45733bc2de1f08dda131c/Milestone
cd listit
```
 
2. Create a MySQL database.
 
3. Execute the provided database script.
 
4. Update `application.properties`:
 
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/listit
spring.datasource.username=your_username
spring.datasource.password=your_password
```
 
5. Build and run:
 
```bash
mvn clean install
mvn spring-boot:run
```
 
6. Open:
 
```
http://localhost:8080
```
 
## User Roles
 
### User
 
- Manage personal profile
- Create and manage catalogs
- Create and manage categories
- Create and manage products
- Share catalogs with other users
 
### Administrator
 
- Access admin dashboard
- View registered users
- Manage user information and permissions
 
## Testing
 
The application includes test coverage for:
 
- Authentication
- Registration
- User profile management
- Category management
- Catalog management
- Product management
- Catalog sharing
- Administrative functions
 
Documented test cases have been successfully executed and passed.
 
## Security
 
- BCrypt password hashing
- Role-based authorization
- Authentication through Spring Security
- Server-side validation
- HTTPS-ready architecture
 
## Project Status
 
Version: 2.0
 
Implemented Features:
 
- Registration
- Login
- User Management
- Category Management
- Catalog Management
- Product Management
- Search
- Catalog Sharing
- Administrative Dashboard
- Security Controls
 
## Author
 
Cody Crosby
 
Grand Canyon University
 
CST-452 Senior Project II
