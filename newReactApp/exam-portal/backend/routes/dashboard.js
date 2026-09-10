// routes/dashboard.js
const express = require("express");
const router  = express.Router();
const db      = require("../db");

router.get("/dashboard/:studentId", (req, res) => {
  const studentId = req.params.studentId;

  // Step 1: Get student info
  db.query(
    "SELECT * FROM students WHERE student_id = ?",
    [studentId],
    (err, students) => {
      if (err) return res.status(500).json(err);
      if (students.length === 0)
        return res.status(404).json({ message: "Student not found" });

      const student = students[0];

      // Step 2: Get enrolled courses with exam details
      db.query(
        `SELECT 
          c.course_name,
          c.course_code,
          e.seat_number,
          e.exam_status,
          e.ca_mark,
          es.exam_date,
          es.venue,
          es.start_time,
          es.end_time,
          es.academic_year,
          es.semester
         FROM enrollments e
         JOIN courses c ON e.course_code = c.course_code
         LEFT JOIN exam_schedules es ON es.course_code = c.course_code
         WHERE e.student_id = ?`,
        [studentId],
        (err, courses) => {
          if (err) return res.status(500).json(err);

          // Check for timetable clashes
          const examDates = courses
            .map(c => c.exam_date
              ? new Date(c.exam_date).toDateString()
              : null)
            .filter(Boolean);

          const uniqueDates   = new Set(examDates);
          const hasClash      = examDates.length !== uniqueDates.size;

          let clashingCourses = [];
          if (hasClash) {
            const seen = {};
            courses.forEach(c => {
              if (!c.exam_date) return;
              const dateStr = new Date(c.exam_date).toDateString();
              if (seen[dateStr]) {
                clashingCourses.push(c.course_name || c.course_code);
                clashingCourses.push(seen[dateStr]);
              } else {
                seen[dateStr] = c.course_name || c.course_code;
              }
            });
            clashingCourses = [...new Set(clashingCourses)];
          }

          res.json({
            student: {
              studentId: student.student_id,
              name:      student.first_name,
              surname:   student.last_name,
              program:   student.program,
              image_url: student.image_url || ""
            },
            courses:         courses,
            hasClash:        hasClash,
            clashingCourses: clashingCourses
          });
        }
      );
    }
  );
});

module.exports = router;