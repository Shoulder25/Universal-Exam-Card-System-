import { useState, useEffect, useRef } from "react";
import { useParams } from "react-router-dom";
import "../App.css";
import jsPDF from "jspdf";
import logo from "../assets/logo.png";
import QRCode from "react-qr-code";


function ExamCard({ label, title, date, seat, venue, index,CA ,status}) {
  return (
    <div className="card" style={{ animationDelay: `${index * 0.1}s` }}>
      <span className="card-label">{label}</span>
      <h3 className="card-title">{title}</h3>
      <ul className="card-meta">
        <li><span className="meta-key">Date</span><span className="meta-val">{date}</span></li>
        <li><span className="meta-key">Seat</span><span className="meta-val">{seat}</span></li>
        <li><span className="meta-key">Venue</span><span className="meta-val">{venue}</span></li>
        <li><span className="meta-key">CA</span><span className="meta-val">{CA}</span></li>
        <li><span className="meta-key">status</span><span className="meta-val">{status}</span></li>
      </ul>
    </div>
  );
}

export default function Dashboard() {

    
    const { studentId } = useParams();
    const [studentData, setStudentData] = useState(null);
    const [examCards, setExamCards] = useState([]);
    const [loading, setLoading] = useState(true);
    const [printing, setPrinting] = useState(false);
    const qrRef = useRef(null);

const API_URL = process.env.REACT_APP_API_URL || "http://localhost:5000";

useEffect(() => {
    if (!studentId) {
      console.log("NO STUDENT ID FOUND");
      return;
    }

    console.log("Fetching for studentId:", studentId);

   fetch(`${API_URL}/api/dashboard/${studentId}`)
      .then(async (res) => {
        const text = await res.text();
        if (!res.ok) {
          throw new Error(text || res.statusText);
        }
        try {
          return JSON.parse(text);
        } catch (error) {
          throw new Error(`Unexpected response: ${text.slice(0, 200)}`);
        }
      })
      .then((data) => {
        console.log("DATA RECEIVED:", data);
        setStudentData(data.student);
        setExamCards(data.courses);
        setLoading(false);
      })
      .catch((err) => {
        console.error("FETCH ERROR:", err);
        setLoading(false);
      });
  }, [studentId]);

  if (loading) return <div className="page">Loading...</div>;
  if (!studentData) return <div className="page">Student not found.</div>;


  const handlePrintExamCard = () => {
  const pdf = new jsPDF("p", "mm", "a4");
  const img = new Image();
  img.src = logo;

  img.onload = () => {
    // Background
    pdf.setFillColor(255, 255, 255);
    pdf.rect(0, 0, 210, 297, "F");

    // Card Border
    pdf.setDrawColor(26, 26, 46);
    pdf.setLineWidth(1);
    pdf.rect(15, 15, 180, 120, "S");

    // Header Background
    pdf.setFillColor(26, 26, 46);
    pdf.rect(15, 15, 180, 25, "F");

    // Logo in header
    pdf.addImage(img, "PNG", 18, 18, 18, 18);

    // University/Portal name in header
    pdf.setTextColor(255, 255, 255);
    pdf.setFontSize(14);
    pdf.setFont("helvetica", "bold");
    pdf.text("EXAM PORTAL", 105, 25, { align: "center" });
    pdf.setFontSize(9);
    pdf.setFont("helvetica", "normal");
    pdf.text("Official Student Exam Card", 105, 33, { align: "center" });

    // Student Details Section
    pdf.setTextColor(0, 0, 0);
    pdf.setFontSize(10);
    pdf.setFont("helvetica", "bold");
    pdf.setTextColor(100, 100, 100);
    pdf.text("STUDENT DETAILS", 20, 52);

    // Divider line
    pdf.setDrawColor(200, 200, 200);
    pdf.line(20, 54, 190, 54);

    // Student Info
    pdf.setFontSize(11);
    pdf.setFont("helvetica", "bold");
    pdf.setTextColor(0, 0, 0);
    pdf.text("First Name:", 20, 65);
    pdf.setFont("helvetica", "normal");
    pdf.text(`${studentData.name}`, 65, 65);

    pdf.setFont("helvetica", "bold");
    pdf.text("Last Name:", 20, 75);
    pdf.setFont("helvetica", "normal");
    pdf.text(`${studentData.surname}`, 65, 75);

    pdf.setFont("helvetica", "bold");
    pdf.text("Student ID:", 20, 85);
    pdf.setFont("helvetica", "normal");
    pdf.text(`${studentData.studentId}`, 65, 85);

    pdf.setFont("helvetica", "bold");
    pdf.text("Program:", 20, 95);
    pdf.setFont("helvetica", "normal");
    pdf.text(`${studentData.program}`, 65, 95);

    pdf.setFont("helvetica", "bold");
    pdf.text("Academic Year:", 20, 105);
    pdf.setFont("helvetica", "normal");
    pdf.text(
      examCards.length > 0 ? examCards[0].academic_year || "2026/2027" : "2026/2027",
      65, 105
    );

    // QR Code Section label
    pdf.setFont("helvetica", "bold");
    pdf.setTextColor(100, 100, 100);
    pdf.setFontSize(9);
    pdf.text("SCAN TO VERIFY", 148, 52, { align: "center" });

    
// Generate QR from SVG
const qrSvg = document.querySelector("#hiddenQR svg");
if (qrSvg) {
  const svgData = new XMLSerializer().serializeToString(qrSvg);
  const svgBlob = new Blob([svgData], { type: "image/svg+xml;charset=utf-8" });
  const svgUrl = URL.createObjectURL(svgBlob);
  const qrImg = new Image();
  qrImg.onload = () => {
    const canvas = document.createElement("canvas");
    canvas.width = 200;
    canvas.height = 200;
    const ctx = canvas.getContext("2d");
    ctx.drawImage(qrImg, 0, 0, 200, 200);
    const qrPng = canvas.toDataURL("image/png");
    pdf.addImage(qrPng, "PNG", 128, 54, 55, 55);
    URL.revokeObjectURL(svgUrl);
    pdf.save(`ExamCard_${studentData.studentId}.pdf`);
  };
  qrImg.src = svgUrl;
} else {
  pdf.setTextColor(200, 0, 0);
  pdf.text("QR unavailable", 148, 80, { align: "center" });
  pdf.save(`ExamCard_${studentData.studentId}.pdf`);
}

    // Student ID below QR
    pdf.setTextColor(0, 0, 0);
    pdf.setFontSize(8);
    pdf.setFont("helvetica", "normal");
    pdf.text(`${studentData.studentId}`, 155, 113, { align: "center" });

    // Footer inside card
    pdf.setFillColor(240, 240, 240);
    pdf.rect(15, 120, 180, 15, "F");
    pdf.setFontSize(8);
    pdf.setTextColor(100, 100, 100);
    pdf.text(
      "This card is valid for the current exam session only. Present to invigilator before entering exam venue.",
      105, 130,
      { align: "center" }
    );

    // Generated date outside card
    pdf.setFontSize(8);
    pdf.setTextColor(150, 150, 150);
    const today = new Date().toLocaleDateString();
    pdf.text(`Generated: ${today}`, 105, 145, { align: "center" });

    
  };
};


      const handleDownloadPDF = () => {
  setPrinting(true);
  const pdf = new jsPDF("p", "mm", "a4");
  const img = new Image();
  img.src = logo;

  img.onload = () => {
    // Logo
    pdf.addImage(img, "PNG", 10, 5, 25, 25);

    // Title
    pdf.setTextColor(46, 134, 171);
    pdf.setFontSize(16);
    pdf.text("Exam Report", 45, 15);
    pdf.setTextColor(0, 0, 0);

    // Student Info Box
    pdf.setFontSize(11);
    pdf.setFillColor(240, 240, 240);
    pdf.rect(10, 32, 190, 35, "F");

    pdf.setFontSize(10);
    pdf.setTextColor(100, 100, 100);
    pdf.text("STUDENT INFO", 14, 39);

    pdf.setTextColor(0, 0, 0);
    pdf.setFontSize(11);
    pdf.text(`Name:`, 14, 47);
    pdf.text(`${studentData.name}`, 35, 47);

    pdf.text(`Surname:`, 14, 54);
    pdf.text(`${studentData.surname}`, 40, 54);

    pdf.text(`Student ID:`, 110, 47);
    pdf.text(`${studentData.studentId}`, 135, 47);

    pdf.text(`Program:`, 110, 54);
    pdf.text(`${studentData.program}`, 132, 54);

    const today = new Date().toLocaleDateString();
    pdf.setFontSize(9);
    pdf.setTextColor(120, 120, 120);
    pdf.text(`Generated: ${today}`, 150, 62);
    pdf.setTextColor(0, 0, 0);

  // Table Header
let y = 80;
pdf.setFillColor(46, 134, 171);
pdf.rect(10, y - 6, 190, 10, "F");

pdf.setTextColor(255, 255, 255);
pdf.setFontSize(10);
pdf.text("Course", 12, y);
pdf.text("Date", 75, y);
pdf.text("Seat", 108, y);
pdf.text("Venue", 120, y);
pdf.text("CA", 158, y);
pdf.text("Status", 170, y);
pdf.setTextColor(0, 0, 0);

y += 10;

    // Rows
    examCards.forEach((course, index) => {
  if (y > 270) {
    pdf.addPage();
    y = 20;
  }

  // Alternate row color
  if (index % 2 === 0) {
    pdf.setFillColor(249, 249, 249);
    pdf.rect(10, y - 5, 190, 10, "F");
  }

  pdf.setFontSize(9);
  pdf.text(course.course_code || "-", 12, y);
  pdf.text(
    course.exam_date
      ? new Date(course.exam_date).toLocaleDateString()
      : "TBA",
    75,
    y
  );
  pdf.text(String(course.seat_number ?? "TBA"), 108, y);
  pdf.text(course.venue || "TBA", 120, y);
  pdf.text(String(course.ca_mark ?? 0), 158, y);
  pdf.text(course.exam_status || "Not Written", 170, y);

  pdf.setDrawColor(220, 220, 220);
  pdf.line(10, y + 4, 200, y + 4);

  y += 12;
});

    // Footer
    pdf.setFontSize(8);
    pdf.setTextColor(150, 150, 150);
    pdf.text("This document was generated automatically by the Exam Portal.", 10, 287);

    pdf.save(`Exam_Report_${studentData.studentId}.pdf`);
    setPrinting(false);
  };
};

  return (
    


    <div className="page">
      <div id="hiddenQR" ref={qrRef} style={{ position: "absolute", left: "-9999px" }}>
      <QRCode
        value={studentData.studentId}
        size={200}
      />
    </div>


      {/* Student Info Header */}
      <header className="student-header">
        <div className="header-accent" />
        <div className="header-content">
          <div className="header-label">Student Info</div>
          <div className="header-grid">
            <div className="info-item">
              <span className="info-key">Name</span>
              <span className="info-val">{studentData.name}</span>
            </div>
            <div className="info-item">
              <span className="info-key">Surname</span>
              <span className="info-val">{studentData.surname}</span>
            </div>
            <div className="info-item">
              <span className="info-key">Student ID</span>
              <span className="info-val">{studentData.studentId}</span>
            </div>
            <div className="info-item">
              <span className="info-key">Program</span>
              <span className="info-val">{studentData.program}</span>
            </div>
          </div>
        </div>
      </header>

      {/* Dashboard Body */}
      <main className="dashboard">
        <div className="dashboard-title">
          <span className="title-line" />
          <h1>Exam Dashboard</h1>
          <span className="title-line" />
        </div>

        {/* Course Exams */}
        <section className="section">
          <h2 className="section-heading">Course Exams</h2>
          <div className="cards-grid">
         {examCards.map((e, i) => (
        <ExamCard
          key={i}
          index={i}
          label="COURSE"
          title={e.course_code}
         date={e.exam_date
         ? new Date(e.exam_date).toLocaleDateString()
         : "TBA"}
         seat={e.seat_number || "TBA"}
         venue={e.venue || "TBA"}
         CA={e.ca_mark ?? 0}
         status={e.exam_status || "Not Written"}
  />
))}
          </div>
        </section>

        {/* Module Exams */}
    

        {/* Print Button */}
        <div className="print-bar" style={{ display: "flex", gap: "12px" }}>
  <button
    className={`print-btn ${printing ? "printing" : ""}`}
    onClick={handleDownloadPDF}
  >
    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.2">
      <polyline points="6 9 6 2 18 2 18 9" />
      <path d="M6 18H4a2 2 0 0 1-2-2v-5a2 2 0 0 1 2-2h16a2 2 0 0 1 2 2v5a2 2 0 0 1-2 2h-2" />
      <rect x="6" y="14" width="12" height="8" />
    </svg>
    {printing ? "Preparing…" : "Download Report"}
  </button>

  <button
    className="print-btn"
    onClick={handlePrintExamCard}
    style={{ backgroundColor: "#2E86AB" }}
  >
    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.2">
      <rect x="2" y="3" width="20" height="14" rx="2" ry="2" />
      <line x1="8" y1="21" x2="16" y2="21" />
      <line x1="12" y1="17" x2="12" y2="21" />
    </svg>
    Print Exam Card
  </button>
</div>
      </main>
    </div>
  );
}
