// routes/auth.js


const express = require("express");
const router = express.Router();
const db = require("../db");

router.post("/login", (req, res) => {
  const { studentId, password } = req.body;

  console.log("LOGIN REQUEST:", req.body);

  db.query(
    "SELECT * FROM students WHERE student_id = ?",
    [studentId],
    (err, results) => {

      if (err) {
        console.log("DB ERROR:", err);
        return res.status(500).json(err);
      }

      console.log("QUERY RESULTS:", results);

      if (results.length === 0) {
        return res.status(401).json({
          message: "Student not found"
        });
      }

      const student = results[0];

      if (password.trim() !== student.password.trim()) {
        return res.status(401).json({
          message: "Wrong password"
        });
      }

      return res.json({
        success: true,
        student: {
        student_id: student.student_id,
        first_name: student.first_name,
        last_name: student.last_name,
        program: student.program,
        image_url: student.image_url
  }
 });
    }
  );
});

module.exports = router;