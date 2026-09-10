const express = require("express");
const router = express.Router();
const db = require("../db");

router.post("/invigilator/login", (req, res) => {
  const { email, password } = req.body;

  if (!email || !password) {
    return res.status(400).json({ message: "Email and password required" });
  }

  db.query(
    "SELECT * FROM invigilators WHERE email = ?",
    [email],
    (err, results) => {
      if (err) return res.status(500).json({ message: "Server error" });

      if (results.length === 0) {
        return res.status(401).json({ message: "Invalid email or password" });
      }

      const invigilator = results[0];

      // Plain text password check for now
      if (password !== invigilator.password) {
        return res.status(401).json({ message: "Invalid email or password" });
      }

      res.json({
        invigilator: {
          invigilator_id: invigilator.invigilator_id,
          first_name: invigilator.first_name,
          middle_name: invigilator.middle_name,
          last_name: invigilator.last_name,
          email: invigilator.email,
          phone: invigilator.phone,
          department_id: invigilator.department_id,
          role:         invigilator.role
        }
      });
    }
  );
});

// Get all distinct venues from exam_schedules
router.get("/invigilator/venues", (req, res) => {
  db.query(
    "SELECT DISTINCT venue FROM exam_schedules ORDER BY venue",
    (err, results) => {
      if (err) return res.status(500).json({ message: "Server error" });
      const venues = results.map(r => r.venue);
      res.json({ venues });
    }
  );
});

// Get distinct dates for a selected venue
router.get("/invigilator/dates", (req, res) => {
  const { venue } = req.query;

  if (!venue) return res.status(400).json({ message: "Venue is required" });

  db.query(
    `SELECT DISTINCT exam_date 
     FROM exam_schedules 
     WHERE venue = ? 
     ORDER BY exam_date`,
    [venue],
    (err, results) => {
      if (err) return res.status(500).json({ message: "Server error" });
      const dates = results.map(r => {
        const d = new Date(r.exam_date);
        return d.toLocaleDateString("en-GB", {
          day: "2-digit",
          month: "short",
          year: "numeric"
        });
      });
      res.json({ dates });
    }
  );
});

//new


router.get("/invigilator/verify-venue/:studentId", (req, res) => {
  const { studentId } = req.params;
  const { venue, date } = req.query;

  if (!studentId || !venue || !date) {
    return res.status(400).json({ message: "Missing parameters" });
  }

  console.log("SCAN - studentId:", studentId, "venue:", venue, "date:", date);

  db.query(
    `SELECT 
      s.student_id,
      s.first_name,
      s.last_name,
      s.program,
      s.image_url,
      e.course_code,
      e.seat_number,
      e.exam_status,
      e.ca_mark,
      es.exam_date,
      es.start_time,
      es.end_time,
      es.venue,
      c.course_name
     FROM students s
     JOIN enrollments e ON s.student_id = e.student_id
     JOIN exam_schedules es ON e.course_code = es.course_code
     JOIN courses c ON e.course_code = c.course_code
     WHERE s.student_id = ?`,
    [studentId],
    (err, results) => {
      if (err) {
        console.error("DB ERROR:", err);
        return res.status(500).json({ message: err.message });
      }

      if (results.length === 0) {
        return res.status(404).json({ message: "Student not found" });
      }

      const studentInfo = {
        student_id: results[0].student_id,
        first_name: results[0].first_name,
        last_name:  results[0].last_name,
        program:    results[0].program,
        image_url:  results[0].image_url || ""
      };

      const exams = results.map(r => ({
        course_code:  r.course_code,
        course_name:  r.course_name,
        exam_date:    r.exam_date,
        start_time:   r.start_time   ? String(r.start_time).substring(0, 5) : "",
        end_time:     r.end_time     ? String(r.end_time).substring(0, 5)   : "",
        venue:        r.venue,
        seat_number:  r.seat_number,
        exam_status:  r.exam_status,
        ca_mark:      r.ca_mark
      }));

      // Parse incoming date "04 Jun 2026" → "2026-06-04"
const months = {
  Jan:"01", Feb:"02", Mar:"03", Apr:"04",
  May:"05", Jun:"06", Jul:"07", Aug:"08",
  Sep:"09", Oct:"10", Nov:"11", Dec:"12"
};
let parsedDate = date;
const dp = date.split(" ");
if (dp.length === 3) {
  parsedDate = `${dp[2]}-${months[dp[1]] || "01"}-${dp[0].padStart(2,"0")}`;
}

console.log("Parsed date:", parsedDate);

const matchedExam = exams.find(ex => {
  let examDateStr = "";

  if (ex.exam_date) {
    const d = new Date(ex.exam_date);

    // Build YYYY-MM-DD without timezone conversion
    examDateStr =
      d.getFullYear() +
      "-" +
      String(d.getMonth() + 1).padStart(2, "0") +
      "-" +
      String(d.getDate()).padStart(2, "0");
  }

  console.log(
    "Comparing:",
    `"${ex.venue}" === "${venue}"`,
    "|",
    `"${examDateStr}" === "${parsedDate}"`
  );

  return (
    ex.venue.trim().toUpperCase() === venue.trim().toUpperCase() &&
    examDateStr === parsedDate
  );
});

      console.log("Matched exam:", matchedExam);

      res.json({
        student: studentInfo,
        exam: matchedExam || null,
        venueVerification: {
          match:   !!matchedExam,
          message: matchedExam
            ? "Correct venue"
            : "Wrong venue or date"
        }
      });
    }
  );
});

// Mark student as written
router.put("/invigilator/mark-written", (req, res) => {
  const { studentId, invigilatorId, venue } = req.body;

  if (!studentId || !invigilatorId || !venue) {
    return res.status(400).json({ message: "Missing required fields" });
  }

  // Find the course for this student at this venue today
  db.query(
    `SELECT e.course_code 
     FROM enrollments e
     JOIN exam_schedules es ON e.course_code = es.course_code
     WHERE e.student_id = ?
     AND es.venue = ?
     AND DATE(es.exam_date) = CURDATE()`,
    [studentId, venue],
    (err, results) => {
      if (err) return res.status(500).json({ message: err.message });
      if (results.length === 0)
        return res.status(404).json({ 
          message: "No exam found for this student at this venue today" 
        });

      const courseCode = results[0].course_code;

      // Check if already written
      db.query(
        `SELECT exam_status FROM enrollments 
         WHERE student_id = ? AND course_code = ?`,
        [studentId, courseCode],
        (err, statusResult) => {
          if (err) return res.status(500).json({ message: err.message });

          if (statusResult[0].exam_status === "Written") {
            return res.status(400).json({ 
              message: "Student already marked as written" 
            });
          }

          // Update status
          db.query(
            `UPDATE enrollments SET exam_status = 'Written' 
             WHERE student_id = ? AND course_code = ?`,
            [studentId, courseCode],
            (err) => {
              if (err) return res.status(500).json({ message: err.message });

              // Log the scan
              db.query(
                `INSERT INTO scan_logs 
                 (student_id, invigilator_id, course_code, venue)
                 VALUES (?, ?, ?, ?)`,
                [studentId, invigilatorId, courseCode, venue],
                (err) => {
                  if (err) return res.status(500).json({ message: err.message });
                  res.json({ 
                    message: "Student marked as written successfully",
                    courseCode: courseCode
                  });
                }
              );
            }
          );
        }
      );
    }
  );
});


// GET COURSES FOR VENUE AND DATE (Script Collection)

router.get("/invigilator/script-courses", (req, res) => {
  const { venue, date } = req.query;

  if (!venue || !date) {
    return res.status(400).json({ message: "Venue and date required" });
  }

  // Parse date from "16 May 2026" format
  const months = {
    Jan:"01", Feb:"02", Mar:"03", Apr:"04",
    May:"05", Jun:"06", Jul:"07", Aug:"08",
    Sep:"09", Oct:"10", Nov:"11", Dec:"12"
  };
  let formattedDate = date;
  const dateParts = date.split(" ");
  if (dateParts.length === 3) {
    const day   = dateParts[0].padStart(2, "0");
    const month = months[dateParts[1]] || "01";
    const year  = dateParts[2];
    formattedDate = `${year}-${month}-${day}`;
  }

  db.query(
    `SELECT DISTINCT 
      c.course_code,
      c.course_name
     FROM exam_schedules es
     JOIN courses c ON es.course_code = c.course_code
     WHERE es.venue = ? 
     AND DATE(es.exam_date) = ?
     ORDER BY c.course_name`,
    [venue, formattedDate],
    (err, results) => {
      if (err) return res.status(500).json({ message: err.message });
      res.json({ courses: results });
    }
  );
});

// ── GET STUDENTS FROM SCAN_LOGS FOR COURSE AND VENUE ──────────
router.get("/invigilator/script-students", (req, res) => {
  const { courseCode, venue } = req.query;

  if (!courseCode || !venue) {
    return res.status(400).json({ 
      message: "Course code and venue required" 
    });
  }

  db.query(
    `SELECT 
      sl.log_id,
      sl.student_id,
      sl.course_code,
      sl.venue,
      sl.script_qr_code,
      s.first_name,
      s.last_name,
      s.program,
      s.image_url,
      e.seat_number,
      e.exam_status,
      c.course_name
     FROM scan_logs sl
     JOIN students s ON sl.student_id = s.student_id
     JOIN enrollments e ON sl.student_id = e.student_id 
       AND sl.course_code = e.course_code
     JOIN courses c ON sl.course_code = c.course_code
     WHERE sl.course_code = ? 
     AND sl.venue = ?
     ORDER BY s.last_name, s.first_name`,
    [courseCode, venue],
    (err, results) => {
      if (err) return res.status(500).json({ message: err.message });
      res.json({ students: results });
    }
  );
});

// ── SAVE SCRIPT NUMBER TO SCAN_LOGS ───────────────────────────
router.put("/invigilator/save-script", (req, res) => {
  const { studentId, courseCode, scriptNumber } = req.body;

  if (!studentId || !courseCode || !scriptNumber) {
    return res.status(400).json({ message: "Missing required fields" });
  }

  // Check if script already assigned
  db.query(
    `SELECT script_qr_code FROM scan_logs 
     WHERE student_id = ? AND course_code = ?`,
    [studentId, courseCode],
    (err, results) => {
      if (err) return res.status(500).json({ message: err.message });

      if (results.length === 0) {
        return res.status(404).json({ 
          message: "Student scan log not found" 
        });
      }

      if (results[0].script_qr_code !== null) {
        return res.status(400).json({ 
          message: "Script already assigned to this student" 
        });
      }

      // Save script number
      db.query(
        `UPDATE scan_logs 
         SET script_qr_code = ?
         WHERE student_id = ? AND course_code = ?`,
        [scriptNumber, studentId, courseCode],
        (err) => {
          if (err) return res.status(500).json({ message: err.message });
          res.json({ 
            message: "Script number saved successfully",
            scriptNumber: scriptNumber,
            studentId: studentId,
            courseCode: courseCode
          });
        }
      );
    }
  );
});

module.exports = router;