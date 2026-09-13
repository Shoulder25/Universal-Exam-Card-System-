# Universal Exam Card System — Setup Guide

This guide walks through setting up the [Universal-Exam-Card-System-](https://github.com/Shoulder25/Universal-Exam-Card-System-) repo locally: the React frontend, the Node/Express backend, the MySQL database, and the Android app.

## 1. Project structure

```
Universal-Exam-Card-System-/
├── AndroidApp/              # Android (Kotlin/Gradle) invigilator/scanner app
├── docs/                    # Project plan, SRS, SDD documents
├── newReactApp/
│   └── exam-portal/
│       ├── backend/         # Express + MySQL API (server.js, routes/, db.js)
│       └── src/             # React frontend (student portal)
└── README.md
```

- **Frontend**: Create React App (React 19, react-router-dom) — the student-facing exam portal.
- **Backend**: Express 5 + `mysql2`, exposing `/api/login`, `/api/dashboard/:studentId`, and `/api/invigilator/*` routes.
- **Database**: MySQL, database name `exam_portal` by default.
- **AndroidApp**: Gradle-based Kotlin project (likely the invigilator/QR-scanning client).

## 2. Prerequisites

Install these before starting:

- **Node.js** 18+ and npm (comes bundled) 
- **MySQL Server** 8.x (or MariaDB equivalent) and a client such as MySQL Workbench or the `mysql` CLI
- **Git**
- **Android Studio** (only needed if you're building/running `AndroidApp`) — includes the Android SDK and a compatible JDK

## 3. Clone the repository

```bash
git clone https://github.com/Shoulder25/Universal-Exam-Card-System-.git
cd Universal-Exam-Card-System-
```

## 4. Set up the database

The repo doesn't ship a full schema file — `backend/database/setup.sql` only contains a sample data-fix query, not table definitions. Based on the queries used across `routes/auth.js`, `routes/dashboard.js`, and `routes/invigilator.js`, the backend expects a database named `exam_portal` with (at least) these tables:

```sql
CREATE DATABASE IF NOT EXISTS exam_portal;
USE exam_portal;

CREATE TABLE students (
  student_id   VARCHAR(20) PRIMARY KEY,
  first_name   VARCHAR(100),
  last_name    VARCHAR(100),
  password     VARCHAR(255),   -- stored/compared as plain text in the current code
  program      VARCHAR(150),
  image_url    VARCHAR(255)
);

CREATE TABLE courses (
  course_code  VARCHAR(20) PRIMARY KEY,
  course_name  VARCHAR(150)
);

CREATE TABLE enrollments (
  student_id   VARCHAR(20),
  course_code  VARCHAR(20),
  seat_number  VARCHAR(20),
  exam_status  VARCHAR(20) DEFAULT 'Not Written',
  ca_mark      DECIMAL(5,2),
  PRIMARY KEY (student_id, course_code),
  FOREIGN KEY (student_id) REFERENCES students(student_id),
  FOREIGN KEY (course_code) REFERENCES courses(course_code)
);

CREATE TABLE exam_schedules (
  course_code    VARCHAR(20),
  exam_date      DATE,
  venue          VARCHAR(100),
  start_time     TIME,
  end_time       TIME,
  academic_year  VARCHAR(20),
  semester       VARCHAR(20),
  FOREIGN KEY (course_code) REFERENCES courses(course_code)
);

CREATE TABLE invigilators (
  invigilator_id  INT AUTO_INCREMENT PRIMARY KEY,
  first_name      VARCHAR(100),
  middle_name     VARCHAR(100),
  last_name       VARCHAR(100),
  email           VARCHAR(150) UNIQUE,
  password        VARCHAR(255),  -- also compared as plain text currently
  phone           VARCHAR(30),
  department_id   INT,
  role            VARCHAR(50)
);

CREATE TABLE scan_logs (
  log_id          INT AUTO_INCREMENT PRIMARY KEY,
  student_id      VARCHAR(20),
  invigilator_id  INT,
  course_code     VARCHAR(20),
  venue           VARCHAR(100),
  script_qr_code  VARCHAR(100) DEFAULT NULL,
  scanned_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (student_id) REFERENCES students(student_id),
  FOREIGN KEY (invigilator_id) REFERENCES invigilators(invigilator_id),
  FOREIGN KEY (course_code) REFERENCES courses(course_code)
);
```

Run this against your MySQL server, then seed it with a few test rows (students, courses, an invigilator, and matching enrollments/exam_schedules) so you have something to log in with.

> This schema is inferred from the queries in the codebase, not an official file from the repo

## 5. Configure and run the backend

```bash
cd newReactApp/exam-portal/backend
npm install
```

`backend/db.js` reads connection settings from environment variables (falling back to hardcoded defaults if unset). Create a `.env` file in `backend/`:

```env
DB_HOST=localhost
DB_USER=root
DB_PASSWORD=your_mysql_password
DB_NAME=exam_portal
DB_PORT=3306
PORT=5000
```

Then start the API server:

```bash
npm start
```

You should see `Connected to exam_portal DB` and `Server running on port 5000` in the console. Visiting `http://localhost:5000` should show "Backend running...".

## 6. Configure and run the frontend

In a separate terminal:

```bash
cd newReactApp/exam-portal
npm install
npm start
```

This opens the app at `http://localhost:3000`. The frontend's `package.json` sets `"proxy": "http://localhost:5000"`, so API calls to `/api/...` are automatically forwarded to your backend — just make sure the backend is already running.

You can also start the backend via the frontend's own npm script from the `exam-portal` folder:

```bash
npm run backend
```

## 7. Set up the Android app (optional)

1. Open Android Studio → **Open** → select the `AndroidApp/` folder.
2. Let Gradle sync (it will pull dependencies via Google, Maven Central, and JitPack as configured in `settings.gradle.kts`).
3. If the app talks to the same backend API, update its base URL (check `AndroidApp/app/src/...` for a constants/config file) to point at your machine's IP and port `5000` — `localhost` won't resolve correctly from an emulator or physical device.
4. Run on an emulator or physical device via the ▶ Run button.

## 8.  checklist

- [ ] MySQL running, `exam_portal` database created and seeded
- [ ] Backend `.env` configured, `npm start` in `backend/` shows a successful DB connection
- [ ] Frontend `npm start` in `exam-portal/` loads at `localhost:3000`
- [ ] Logging in as a seeded student returns dashboard data
- [ ] (Optional) Android app builds and can reach the backend API

## Notes and known gaps

- **Passwords are stored and compared as plain text** in both `auth.js` and `invigilator.js` — fine for a class project demo, but flag this if the system is used with real student data.
- **No `.env.example` file exists** in the repo — the `.env` above is based on the variable names actually read in `db.js`.
- **`backend/database/setup.sql`** is not a schema/migration file as-is; it's a single `UPDATE` statement (and has a trailing comma that would fail to run). Use the `CREATE TABLE` statements above instead, or replace them with the team's real schema if one exists elsewhere.
