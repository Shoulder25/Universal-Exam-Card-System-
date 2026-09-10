// db.js
require('dotenv').config();
const mysql = require("mysql2");

const db = mysql.createConnection({
  host:     process.env.DB_HOST     || "localhost",
  user:     process.env.DB_USER     || "root",
  password: process.env.DB_PASSWORD || "12345",
  database: process.env.DB_NAME     || "exam_portal",
  port:     process.env.DB_PORT     || 3306
});

db.connect((err) => {
  if (err) {
    console.error("Database connection failed:", err);
    return;
  }
  console.log("Connected to exam_portal DB");
});

module.exports = db;