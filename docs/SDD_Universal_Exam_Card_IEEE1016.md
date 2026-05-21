UNIVERSAL EXAM CARD SYSTEM Software Design Document (SDD)
Project Title: Universal Exam Card System 
Names: Sharoon Nqobile Shoulder, Bandile Lokothwayo, Senamiso Masango, Gaute Banda
 Date: 14/05/2026

 
TABLE OF CONTENTS
1.	INTRODUCTION 
1.1 Purpose 
1.2 Scope 
1.3 Overview
1.4 Reference Material
1.5 Definitions and Acronyms 
2.	 SYSTEM OVERVIEW 
3.	DESIGN OVERVIEW
 3.1 Design Stakeholders
 3.2 Design Concerns 
 3.3 Selected Design Viewpoints 
 3.4 Design Languages and Notations 
 3.5 Design Constraints 
 3.6 Design Rationale
4.	SYSTEM ARCHITECTURE 
4.1 Architectural Design 
4.2 Decomposition Description
5.	DATA DESIGN
 5.1 Data Description
 5.2 Data Dictionary
 5.3 Interface Design (API Specification)
6.	COMPONENT DESIGN
 6.1 Pseudocode Specifications
 6.2Behavioral Design
    6.2.1 Sequence Diagram
    6.2.2 Activity diagram- scan verification and script collection
    6.2.3 Activity diagram- generate UEC 
7.	NON-FUNCTIONAL DESIGN CONSIDERATIONS
 7.1 Performance and Load Handling
 7.2 Security and Data Integrity
 7.3 Offline Mode Design and Synchronization
 7.4 Fault Tolerance and Availability
8.	HUMAN INTERFACE DESIGN
 8.1 Overview of User Interface
 8.2 Screen Images
 8.3 Screen Objects and Actions
9.	REQUIREMENTS MATRIX
10.	APPENDICES
 Appendix A: Detailed ER Diagram
 Appendix B: Architecture Diagram 
 Appendix C: Sequence Diagrams


 
1. INTRODUCTION
1.1 Purpose
This Software Design Document (SDD) describes the architecture, system design, components, interfaces, and data structures of the Universal Exam Card System. It translates the functional and non-functional requirements from the SRS into a detailed blueprint that will guide the implementation phase. The primary audience includes software developers, testers, and project maintainers.
1.2 Scope
The Universal Exam Card System automates student identity verification and exam script tracking at the University of Eswatini using QR code technology.
Goals and Objectives:
●	Generate a single digital Universal Exam Card consolidating all registered modules.
●	Enable real-time secure identity verification and attendance marking via Android app.
●	Digitally log script collection with invigilator accountability.
●	Provide real-time dashboards and reports for administrators and lead invigilators.
Benefits: Reduced impersonation, elimination of lost scripts, improved exam integrity, and better accountability.
In Scope: Backend API, React web dashboard, Android mobile app (with offline support), MySQL database. 
Out of Scope: Physical card printing, biometric authentication, integration with financial/HR systems.
1.3 Overview
This document follows the university SDD template (adapted from IEEE Std 1016-2009). This document is organized as follows:
●	Section 2 provides a high-level system overview.
●	Section 3 describes the system architecture and decomposition.
●	Section 4 details the data design.
●	Section 5 presents component-level design.
●	Section 6 covers the human interface design.
●	Section 7 provides traceability to SRS requirements.
●	Section 8 contains supporting appendices.

1.4 Reference Material
●	SRS Version 1.0 – Universal Exam Card System (01 May 2026)
●	IEEE Std 1016-2009-Recommended Practice for Software Design Descriptions
1.5 Definitions and Acronyms
 UEC: Universal Exam Card – Digital identity token with QR code. 
  QR Code: Quick Response code containing encrypted student verification data. 
  Scan Log: Record of verification or script collection event. 
  JWT: JSON Web Token – Used for authentication. 
  Invigilator: Examination supervisor using the Android app. 
  Lead Invigilator: Supervisor with dashboard access.

2. SYSTEM OVERVIEW
The Universal Exam Card System replaces error-prone manual processes with a secure digital solution. Students access their UEC via a web portal. Invigilators use an Android app to scan QR codes for real-time verification, attendance marking, and script logging. Administrators and lead invigilators monitor live attendance and generate reports through a web dashboard. The system supports offline functionality on mobile with later synchronization.
3. Design Overview
This section provides a high-level summary of the design approach, stakeholders, key design concerns, selected design viewpoints, notations used, and design constraints. It establishes the foundation for understanding the detailed design presented in subsequent sections.
3.1 Design Stakeholders
The following stakeholders have an interest in the design of the Universal Exam Card System:
Stakeholder	Role / Involvement	Key Interests / Concerns
Students
	End users of the Universal Exam Card
	Privacy, security of personal data, ease of accessing their digital exam card
Invigilators
	Primary users of the Android mobile application	Fast scanning, reliable offline functionality, simple and intuitive interface, quick feedback
Lead Invigilators
	Supervisors during examinations
	Real-time attendance visibility, anomaly detection, script collection monitoring
Administrators
	System administrators and exam coordinators
	Dashboard oversight, report generation, auditability, system reliability
Developers / Maintainers
	Implementation and future maintenance team
	Modularity, code maintainability, scalability, clear component interfaces
University Management
	Project sponsors and policy enforcers
	Exam integrity, reduction of impersonation and malpractice, accountability, data security

3.2 Design Concerns
The design addresses the following critical concerns derived from the SRS:
●	Security & Integrity: Prevention of impersonation through encrypted QR codes and strong authentication.
●	Reliability in Challenging Environments: Support for offline operation in examination venues with poor or no network connectivity.
●	Performance & Scalability: Ability to handle high concurrency (hundreds to thousands of simultaneous scans) during peak examination periods.
●	Real-time Monitoring: Provision of live attendance and script collection status to lead invigilators and administrators.
●	Usability: Simple, scanner-centric interface for invigilators who may not be technically proficient.
●	Auditability & Traceability: Complete logging of all verification and script collection actions for accountability.
●	Data Consistency: Proper synchronization between offline mobile operations and the central database.
●	Maintainability: Clear separation of concerns to support future enhancements within limited resources.
3.3 Selected Design Viewpoints
●	Context Viewpoint: Defines the system boundary and its interactions with external entities (students, invigilators, administrators, and external systems).
●	Composition Viewpoint: Describes the overall system structure, major subsystems, and their relationships.
●	Logical / Behavioural Viewpoint: Presents functional decomposition, workflows, and dynamic behaviour of the system.
●	Interface Viewpoint: Details the external interfaces, especially the RESTful API and WebSocket communication.
●	Information Viewpoint: Describes the data structures, entities, relationships, and storage mechanisms.
●	Deployment / Physical Viewpoint: (Implied) Describes how the system is deployed across web, mobile, and database tiers.
3.4 Design Languages and Notations
The following notations and languages are used to describe the design:
Design Aspect	Notation / Language Used
Architecture Diagrams
	UML Component, Deployment & Package diagrams

Behavioural Modelling
	UML Activity Diagrams, Sequence Diagrams

Data Modelling
	Entity-Relationship (ER) Diagrams

Data Dictionary
	Tabular format with attributes, keys, and descriptions

API Specification
	RESTful endpoint definitions with JSON examples

Algorithms & Logic
	Structured Pseudocode

User Interface
	Screen mock-ups and textual descriptions

Database Schema
	MySQL DDL conventions


3.5 Design Constraints
The following constraints significantly influenced the design decisions:
●	Academic Project Timeline: Must be completed within one semester by undergraduate students.
●	Technology Stack: Limited to technologies familiar to the development team (Node.js, React, Java for Android, MySQL).
●	Monolithic Architecture: Chosen over microservices due to time and complexity constraints.
●	Infrastructure: Must run on university-provided or low-cost cloud/server resources.
●	Mobile Platform: Android only, with mandatory offline support using Room SQLite and WorkManager.
●	No Biometrics: Physical card printing and biometric authentication are out of scope.
●	Security Limitations: Use of JWT, BCrypt, and TLS without advanced enterprise security tools.
3.6 Design Rationale Summary
A 3-tier Client-Server architecture (Presentation → Application → Data) was selected to achieve a clean separation of concerns, improve maintainability, and support multiple client types (Web + Mobile).
The combination of RESTful APIs for core operations and WebSockets for real-time updates provides the right balance between simplicity and responsiveness. A monolithic backend was preferred for faster development and easier deployment within project constraints, while still allowing future scaling through Docker containerization.
Offline support using local SQLite with intelligent synchronization was made a core design priority due to the unreliable network conditions common in large examination venues


4. SYSTEM ARCHITECTURE
4.1 Architectural Design
The system follows a 3-tier Client-Server Architecture:
●	Presentation Tier: React.js Web Dashboard + Android Application (Java)
●	Application Tier: Node.js/Express RESTful API + WebSocket Server
●	Data Tier: MySQL Relational Database
Major Subsystems and Responsibilities:
●	Authentication Subsystem: User login and JWT management.
●	Exam Card Management Subsystem: Generation and QR encoding.
●	Verification & Logging Subsystem: QR scanning, validation, attendance & script logging.
●	Monitoring & Reporting Subsystem: Real-time dashboard and report generation.
Architecture Diagram:
 
The subsystems collaborate via REST APIs for standard operations and WebSockets for real-time updates.
4.2 Decomposition Description
Functional Decomposition:
1.	Generate Universal Exam Card
2.	Authenticate Users
3.	Scan & Verify Student
4.	Mark Attendance & Log Script Collection
5.	Monitor Dashboard (Real-time)
6.	Generate Reports
5. DATA DESIGN
5.1 Data Description
The information domain is transformed into a relational database model using MySQL. Data is normalized to 3NF to reduce redundancy and ensure integrity.
Storage: All persistent data is stored in a MySQL relational database. QR code data is stored as encrypted strings. Scan logs are append-only for auditability. The Android app uses local Room SQLite database for offline storage and synchronizes with the central MySQL when online using WorkManager.
Processing:
●	Student and exam data are joined during verification.
●	Timestamps and foreign keys maintain audit trails.
●	Indexes on student_id, session_id, and timestamp ensure fast queries during high-load exam periods.
Major Entities:
●	Students, Exam Cards, Exam Sessions, Scan Logs, Invigilators.	
5.2 Data Dictionary
Entity Attendance: Final record of student participation in an examination.
Attribute	Data Type	Key	Description
attendance_id	Integer	PK	Unique record identifier.
student_id	Integer	FK	Reference to the student.
course_code	String	FK	Reference to the course.
exam_date	Date		Date the attendance was recorded.
status	String		Participation status (e.g., "Present", "Absent").

Entity Courses: Defines the academic subjects available for examination
Attribute	Data Type	Key	Description
course_code	String	PK	Unique alphanumeric code identifying the course (e.g., CSC301).
course_name	String		The full descriptive title of the course.

Entity Enrolment: A dynamic table linking students to the specific courses they are registered for.
Attribute	Data Type	Key	Description
student_id	Integer	FK	Reference to the student being enrolled.
course_code	String	FK	Reference to the specific course

Entity Exam_Schedule: The master timetable linking courses to times and locations.
Attribute	Data Type	Key	Description
schedule_id	Integer	PK	Unique identifier for the scheduled slot.
course_code	String	FK	The course associated with this exam slot.
exam_date	Date		The calendar date of the exam.
start_time	Time		The scheduled start time.
end_time	Time		The scheduled end time.
venue_id	Integer	FK	Reference to where the exam is taking place.


Entity Invigilators: Stores details for staff members responsible for supervising examinations.
Attribute	Data Type	Key	Description
invigilator_id	Integer	PK	Unique identifier for the invigilator
name	String		Full name of the staff member.
email	String		Staff email address
password	String		Hashed credential for system access.

Entity Invigilation_Assignments: Links staff to their specific exam duties.
Attribute	Data Type	Key	Description
assignment_id	Integer	PK	The staff member assigned.
invigilator_id	Integer	FK	The staff member assigned.
course_code	String	FK	The course they are supervising.
exam_date	Date		The date of the assigned duty.

Entity Scan_Logs: Audit trail for QR code interactions during the invigilation process
Attribute	Data Type	Key	Description
log_id	Integer	PK	Unique identifier for the scan event.
student_id	Integer	FK	The student whose QR was scanned.
invigilator_id	Integer	FK	The staff member who performed the scan.
course_code	String	FK	The course for which the scan was performed.
scan_time	Timestamp		Exact date and time of the transaction.
venue	String		The location where the scan occurred.
action	String		Type of entry (e.g., "Check-in", "Check-out").

Entity students: Stores unique identification and credential information for students.
Attribute	Data Type	Key	Description
student_id	Integer	PK	Unique identifier for the student
name	String		Full name of the student.
email	String		Institutional email address
password	String		Hashed credential for system access.

Entity Venues: Physical locations where examinations are held.
Attribute	Data Type	Key	Description
venue_id	Integer	PK	Unique identifier for the room or hall
venue_name	String		The name or number of the location (e.g., MPH).
capacity	Integer		Maximum number of students the venue can hold.
5.3 Interface Design (API Specification)
The system utilizes a RESTful API architecture to facilitate communication between the Mobile Client and the MySQL Database. All requests must be made over HTTPS.
5.3.1 Authentication Flow
The system uses JWT (JSON Web Tokens) for secure stateless authentication.
1.	Login: The user (Invigilator) sends credentials to /api/auth/login.
2.	Token Issuance: Upon validation, the server returns a signed JWT.
3.	Authorized Requests: The mobile app must include this token in the Authorization header as a Bearer token for all subsequent requests (e.g., QR verification).
5.3.2 Endpoint List & Definitions
Method
	Endpoint	Description	Authorisation Required
POST	/api/auth/login	Validates invigilator credentials and returns a JWT.	No
GET	/api/sync/pre-fetch	Downloads student/enrolment list for offline use.	Yes
POST	/api/verify-qr	Validates scanned student QR data against the schedule	Yes
POST	/api/sync/bulk-upload	Uploads locally stored scan logs once online	Yes

5.3.3 Request/Response Formats
5.3.3.1 POST /api/verify-qr
This is the primary endpoint used during the examination process.
Request Body (JSON):
{
  "student_id": 123456,
  "course_code": "STA301",
  "venue_id": 10,
  "scan_timestamp": "2026-05-15T09:00:00Z",
  "invigilator_id": 55
}
Success Response (200 OK):
{
  "status": "success",
  "message": "Student verified and attendance logged.",
  "data": {
    "student_name": "John Doe",
    "course_name": "Time Series Analysis"
  }
}
Error Response (403 Forbidden):
{
  "status": "error",
  "error_code": "NOT_ENROLLED",
  "message": "Student is not enrolled for this specific course exam."
}
5.3.3.2 POST /api/sync/bulk-upload
Used to synchronize data collected during Offline Mode.
Request Body (JSON):
{
  "batch_id": "uuid-789",
  "logs": [
    { "student_id": 123456, "scan_time": "...", "status": "check-in" },
    { "student_id": 654321, "scan_time": "...", "status": "check-in" }
  ]
}

6.Composite Design
6.1 Pseudocode Specifications
Pseudo code for verification component
function verifyStudent(qrData, invigilatorId, deviceLocation) {
    student = decodeQR(qrData);
    session = getCurrentExamSession(student, deviceLocation);
    if (!session) return {status: "FAIL", reason: "Not registered for this exam"};
    if (alreadyMarked(student, session)) 
        return {status: "WARNING", reason: "Already marked present"};
    log = createScanLog(student.id, invigilatorId, "verification", session.id);
    markAttendance(student.id, session.id);
    broadcastToDashboard({event: "new_attendance", student, log});
    return {status: "SUCCESS", studentInfo: student};
}
Pseudo code to generateExamCard
PROCEDURE generateExamCard(studentId: Integer)
    student ← getStudentById(studentId)
    IF student is null THEN RETURN error("Student not found") END IF
    modules ← getRegisteredModules(studentId)
    qrPayload ← {
        studentId: student.id,
        name: student.name,
        modules: modules,
        timestamp: currentTime()
    }
    qrCodeData ← encryptAndEncodeQR(qrPayload)   // Base64 + encryption
    card ← createExamCardRecord(studentId, qrCodeData)
    RETURN {cardId: card.id, qrCode: qrCodeData}
END PROCEDURE
Pseudocode for logScriptCollection()
PROCEDURE logScriptCollection(studentId: Integer, invigilatorId: Integer, sessionId: Integer)
    IF NOT isVerifiedToday(studentId, sessionId) THEN
        RETURN error("Student must be verified first")
    END IF
    log ← createScanLog(studentId, invigilatorId, "script_collection", sessionId)
    updateScanLog(log.id, {action: "script_collection", script_status: "collected"})
    broadcastRealTimeUpdate("script_logged", studentId, sessionId)
    RETURN success("Script collection logged")
END PROCEDURE
Pseudocode for generateAttendanceReport()
PROCEDURE generateAttendanceReport(sessionId: Integer, format: String)
    logs ← queryScanLogsBySession(sessionId)
    summary ← calculateStatistics(logs)   // attendance %, anomalies, etc.
    IF format = "PDF" THEN
        report ← generatePDFReport(summary)
    ELSE IF format = "Excel" THEN
        report ← generateExcelReport(summary)
    END IF
    saveReportToStorage(report)
    RETURN reportLink
END PROCEDURE

6.2Behavioural Design

6.2.1Sequence diagram
 

6.2.2Activity diagram -scan verification and script collection 


6.2.3Activity diagram to generate UEC
 

7.NON-FUNCTIONAL DESIGN CONSIDERATIONS
7.1 Performance and Load Handling
To manage the high concurrency expected during peak examination windows (e.g., thousands of concurrent scans within a 15-minute window), the following strategies are implemented:
●	Horizontal Scaling: The Backend API will be containerized using Docker, allowing the system to scale instances dynamically based on CPU/Memory load.
●	Database Indexing: Composite indexes will be applied to the STUDENTS(student_id) and EXAM_SCHEDULE(course_code, exam_date) fields to ensure sub-second query response times during verification.
●	Connection Pooling: The MySQL database will utilize a connection pool (size: 50–100) to minimize the overhead of frequent handshakes between the API and the database.
7.2 Security and Data Integrity
●	Encryption Standards: * Data at Rest: All sensitive data, including student and invigilator passwords, must be hashed using the BCrypt algorithm with a salt factor of 12.
o	Data in Transit: All communication between the Invigilator App and Backend API will be secured via TLS 1.2/1.3 (HTTPS).
●	Authentication: API access requires a JSON Web Token (JWT). Tokens are issued upon successful login and must be included in the header of the POST /verify-qr request.
7.3 Offline Mode Design and Synchronization
To ensure the system remains functional in venues with poor connectivity (e.g., deep inside large halls), the following offline architecture is designed:
7.3.1 Local Caching Strategy
The Invigilator App will utilize an SQLite or Room database for local persistence.
●	Pre-fetching: 30 minutes before a scheduled exam, the app will automatically download the relevant ENROLLMENT and STUDENT data for that specific VENUE_ID and COURSE_CODE.
●	Local Logging: If the Backend API is unreachable, the app will store scan results locally in a PENDING_SYNC table with a timestamp.
7.3.2 Sync Conflict Resolution
When connectivity is restored, the app will initiate a background sync process:
●	LWW (Last Write Wins): For attendance status, the record with the most recent timestamp in the SCAN_LOGS will be considered the "truth."
●	Duplicate Prevention: The Backend API will enforce a unique constraint on the combination of student_id, course_code, and exam_date to prevent duplicate attendance entries during bulk sync.
7.4 Fault Tolerance and Availability
●	Retries: The mobile client will implement an Exponential Backoff retry strategy for failed API calls caused by transient network errors.
●	Health Monitoring: A /health endpoint will be implemented on the Backend API to allow load balancers to redirect traffic away from unhealthy instances.
8. HUMAN INTERFACE DESIGN
8.1 Overview of User Interface
Android App (Invigilator): Simple, scanner-centric interface with large buttons and immediate visual feedback (green/red). 
Web Dashboard: Real-time overview with charts showing attendance per venue/course, anomaly alerts, and report generation.
Users can complete all features: scan students, log scripts, view live statistics, and export reports.

8.2 Screen Images
1.	Android QR Scanner Screen
2.	Verification Success Screen
3.	Web Dashboard – Live Attendance Overview
4.	Report Generation Screen
8.3 Screen Objects and Actions
●	Scan Button: Activates camera and triggers verification flow.
●	Result Banner: Displays success/error with student details.
●	Dashboard Cards: Clickable widgets for detailed views.
●	Export Button: Generates downloadable reports.

9. REQUIREMENTS MATRIX
SRS ID	Requirement Description
	SDD Components / Modules
FR-01	Universal Exam Card with QR code	Exam Card Management + QR Service
FR-02	User Authentication (JWT)	Authentication Subsystem
FR-03	Identity Verification via Android	Verification & Logging Subsystem
FR-04	Automatic Attendance Marking	Scan Log + Attendance Module
FR-05	Script Collection Logging
	Scan Log (script_collection action)
FR-06	Real-time Dashboard	Monitoring Subsystem + WebSocket
FR-07	Reporting	Reporting Service

10. APPENDICES
Appendix A: Detailed ER Diagram
 
Appendix B: Architecture Diagram
 
Appendix C: Sequence Diagrams
 

