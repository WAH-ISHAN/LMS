```markdown
# Learning Management System (LMS)

A role-based **Learning Management System (LMS)** built with a **Java Spring Boot** backend and **React** frontend.

This system provides secure authentication, role-based access (Admin / Lecturer / Student), course management, enrollment, course content management, and course calendars.

---

## 🚀 Main Features

### 👤 User Roles

- **Admin**
  - Create and manage user accounts
  - Assign roles: Admin / Lecturer / Student
  - View and manage all courses

- **Lecturer**
  - Create and manage their own courses
  - Add / edit / delete course content
  - Create and manage course calendar events
  - Publish updates that are visible to enrolled students

- **Student**
  - Register and log in securely
  - Browse available courses
  - Enroll in courses
  - View course content and calendar events
  - See latest updates (e.g., modified content timestamps)

---

## 📚 Functional Overview

### 🔐 Authentication & Security

- User **registration** and **login**
- **JWT-based authentication** (Bearer tokens) using Spring Security
- Passwords stored as **hashed** values
- Role-based access control enforced on backend endpoints

### 🧾 Course Management

- Admin / Lecturer can:
  - Create courses
  - Edit course details (title, description)
  - Delete courses
- Each course has:
  - Course code (e.g., `DSA101`)
  - Title (e.g., *Data Structures & Algorithms*)
  - Description
  - Assigned Lecturer (User)

### 🎓 Enrollment

- Students can:
  - Enroll themselves into available courses
  - View **My Courses** (enrolled courses list)
- Enrollment stored via a dedicated `Enrollment` entity (linking `User` and `Course`)

### 📂 Course Content

- Lecturers can manage content per course:
  - Title
  - Description
  - Content type (Lecture, Assignment, Lab, etc.)
  - URL to resource (file storage / external link)
  - `createdAt` / `updatedAt` timestamps
- Students enrolled in a course can view downloadable / viewable content
- Updated content shows **last updated** time to students

### 🗓 Course Calendar

- Each course has its own calendar:
  - Lecture sessions
  - Assignment deadlines
  - Exams
  - Other important events
- Lecturers:
  - Add / edit / delete events
- Students:
  - View full course schedule

### 📘 Example Courses (as data)

The system supports generic courses. Example entries typically include:

- **Data Structures & Algorithms (DSA)**
- **Distributed Systems Basics**
- **Programming Fundamentals** (or any other subject required)

---

## 🛠 Technology Stack

### Backend

- **Java** (JDK 21+ / 25)
- **Spring Boot 3.x**
  - `spring-boot-starter-web` – REST API
  - `spring-boot-starter-security` – authentication & authorization
  - `spring-boot-starter-data-jpa` – database access via JPA/Hibernate
  - `spring-boot-starter-validation` – request validation
- **JWT**: [`jjwt`](https://github.com/jwtk/jjwt) library
- **Database**
  - **H2** (in-memory, development / testing)
  - **MySQL** (alternative for production)
- **Build Tool**: Maven

### Frontend

- **React**
- **Axios** – HTTP client for API requests
- **React Router** – client-side routing
- React **Context API** – store logged-in user and JWT (`AuthContext`)

---

## 🧱 High-Level Architecture

```text
[ React Frontend (SPA) ]
          |
          v
[ Spring Boot REST API (JWT + Spring Security) ]
          |
          v
[ Database (H2 / MySQL via Spring Data JPA) ]
```

- React sends requests to `/api/**` endpoints.
- Each authenticated request includes `Authorization: Bearer <JWT>`.
- Backend validates JWT and checks role-based permissions.

---

## 📁 Backend Project Structure

```text
src/main/java/com/Backend/SecureAuth
 ├─ controller
 │   ├─ AuthController        # /api/auth: register, login
 │   ├─ CourseController      # /api/courses: course CRUD
 │   ├─ EnrollmentController  # /api/enrollments: student enrollment
 │   ├─ ContentController     # /api/courses/{id}/contents: course content
 │   └─ CalendarController    # /api/courses/{id}/events: course calendar
 ├─ model
 │   ├─ User                  # User entity (Admin/Lecturer/Student)
 │   ├─ Role                  # Enum: ADMIN, LECTURER, STUDENT
 │   ├─ Course                # Course entity
 │   ├─ Enrollment            # Student-course link
 │   ├─ CourseContent         # Content items for courses
 │   └─ CalendarEvent         # Calendar events for courses
 ├─ repository
 │   ├─ UserRepository
 │   ├─ CourseRepository
 │   ├─ EnrollmentRepository
 │   ├─ CourseContentRepository
 │   └─ CalendarEventRepository
 ├─ config
 │   ├─ SecurityConfig        # Spring Security, JWT filter, CORS
 │   ├─ JwtUtil               # JWT generation & validation
 │   └─ JwtAuthFilter         # Extract & validate JWT from requests
 └─ service
     └─ CustomUserDetailsService  # Loads users for Spring Security
```

### Key Entities (Summary)

- **User**
  - `id`, `fullName`, `email`, `password`, `role`, `active`
- **Course**
  - `id`, `code`, `title`, `description`, `lecturer` (User)
- **Enrollment**
  - `id`, `student` (User), `course` (Course)
- **CourseContent**
  - `id`, `course`, `title`, `description`, `type`, `contentUrl`, `createdAt`, `updatedAt`
- **CalendarEvent**
  - `id`, `course`, `title`, `description`, `startTime`, `endTime`, `type`

---

## 📂 Frontend Structure (React)

```text
src
 ├─ main.jsx                # App entry, Router + AuthProvider
 ├─ App.jsx                 # Routes definition
 ├─ api.js                  # Axios instance (baseURL + JWT interceptor)
 ├─ context
 │   └─ AuthContext.jsx     # Holds user object, token, login/logout logic
 ├─ components
 │   └─ PrivateRoute.jsx    # Protect routes that require login
 └─ pages
     ├─ LoginPage.jsx       # Login form
     ├─ RegisterPage.jsx    # Registration form
     ├─ CoursesPage.jsx     # List all courses
     ├─ MyCoursesPage.jsx   # List enrolled courses
     └─ CourseDetailPage.jsx# Course details, contents & calendar
```

- **AuthContext** stores:
  - `user` (id, name, email, role)
  - `token`
  - `login`, `register`, `logout` methods
- **PrivateRoute**:
  - Redirects to `/login` if user is not authenticated

---

## ⚙️ Getting Started

### ✅ Prerequisites

- **Java JDK** 21 or 25
- **Maven**
- **Node.js** and **npm**
- (Optional) **MySQL** server (if not using H2)

---

### 1️⃣ Backend Setup

1. Navigate to backend folder:

```bash
cd SecureAuth   # adjust if your backend folder name is different
```

2. Build the project:

```bash
mvn clean install
```

3. Run the Spring Boot application:

```bash
mvn spring-boot:run
```

4. Backend will be available at:  
   `http://localhost:8080`

#### Example `application.properties` (H2 DB)

```properties
spring.datasource.url=jdbc:h2:mem:lmsdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true

spring.h2.console.enabled=true

# JWT configuration
app.jwt.secret=ChangeThisSecretToSomethingStrong123456
app.jwt.expiration=86400000
```

- H2 console: `http://localhost:8080/h2-console`

---

### 2️⃣ Frontend Setup

1. Navigate to frontend folder:

```bash
cd lms-frontend    # or whatever your React project folder is
```

2. Install dependencies:

```bash
npm install
```

3. Start the dev server:

```bash
npm run dev
```

4. Frontend will be available at (Vite default):  
   `http://localhost:5173`

#### `src/api.js` – Axios Base URL

```js
import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:8080/api",
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export default api;
```

---

## 🔑 API Overview (Example)

### Auth

- `POST /api/auth/register`  
  Register a new user (default: Student)

- `POST /api/auth/login`  
  Log in and receive a JWT token

### Courses

- `GET  /api/courses`  
  List all courses

- `GET  /api/courses/{id}`  
  Get a single course

- `POST /api/courses` *(Lecturer/Admin only)*  
  Create a course

- `PUT  /api/courses/{id}` *(Owner Lecturer / Admin)*  
  Update course details

- `DELETE /api/courses/{id}` *(Owner Lecturer / Admin)*  
  Delete course

### Enrollments

- `POST /api/enrollments/enroll/{courseId}`  
  Enroll logged-in student into a course

- `GET  /api/enrollments/my`  
  Get all courses of the logged-in student

### Course Contents

- `GET  /api/courses/{courseId}/contents`  
  Get content list for a course

- `POST /api/courses/{courseId}/contents` *(Lecturer/Admin)*  
  Add course content

### Course Calendar

- `GET  /api/courses/{courseId}/events`  
  Get calendar events for a course

- `POST /api/courses/{courseId}/events` *(Lecturer/Admin)*  
  Add a calendar event

---

## 🔒 Security Highlights

- Passwords hashed (e.g., using BCrypt)
- JWT tokens for authentication
- Role-based authorization (Admin / Lecturer / Student)
- Spring Security filter chain with custom JWT filter

---

## 🌱 Future Enhancements

- File upload & storage for course materials
- Announcements & notifications (email / in-app)
- Discussion forums / comments per course
- Online quizzes and grading
- Advanced search and filtering
- Dockerization and deployment to cloud environment

---

## 📄 Notes

- Backend is fully implemented in **Java Spring Boot**.
- Frontend is implemented in **React**.
- The project is designed as an educational / academic LMS.

---
