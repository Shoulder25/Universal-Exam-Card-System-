 
Contents
Group Members	3
1. Introduction	4
1.1 Background	4
1.2. Aim	4
1.3. Objectives	4
1.4. Project Roles	4
1.5. Project Summary	5
2. User/Client Involvement	5
2.1. Information Provision	5
2.2. Resources and Facilities	5
2.3. Timing of Provision	5
3. Risks	6
3.1. Technical Risks	6
3.2. Schedule Risks	6
3.3. Resource Risks	6
3.4. Quality Risks	6
3.5. Operational Risks	7
4. Standards, Guidelines, and Procedures	7
5. Organization of the Project	7
5.1. Relations to Other Projects	7
5.2. Team Roles	7
5.3. Training Requirements	8
5.4. Training Schedule	8
6. Project Phases	9
6.1. Lifecycle Model	9
6.2. Phases and Tasks (Iterations)	9
6.3. Milestones	10
6.4. Critical Path	11
6.5. Effort Estimates (April–May)	11
7. Requirements Analysis and Design	11
7.1 Methods and Techniques	11
7.1.1. Requirements Gathering	11
7.1.2. Modeling Techniques	11
7.1.3 Validation and Refinement	11
7.2. Resources and Tools	11
7.3. Expected Deliverables	12
8. Implementation	12
8.1. Overview	12
8.2. Resources and Tools	12
8.3. Implementation Phases	13
8.4. Team Roles During Implementation	14
8.5. Cost and Estimation	14
9. Testing	14
9.1. Test Environment	14
9.2. Test Equipment	15
9.3. Testing Plan	15
9.4. Order of Integration and Testing	16
9.5. Testing Procedures	16
10. Resources	16
11.1. Organization	16
11.2. Procedures	16
11.3. Quality Metrics	17
11.4. Review and Feedback	17
12. Changes	18
12.1. Overview	18
12.2. Change Management Organization	18
12.3. Procedures for Handling Changes	18
12.3.1. Identification	18
12.3.2. Evaluation	18
12.3.3. Approval	18
12.3.4. Implementation	18
12.3.5. Verification	19
12.3.6. Tracking	19
12.4. Critical Principles	19
13. Project Timeline and Dependencies (Gantt Chart Representation)	19

































Group Members 
Sharoon Nqobile Shoulder                       202304483                         Software Development Leader  
Dylan Senamiso Masango                     202301967                          Software Designer  
Bandile Irvin Lokothwayo                       157538                               Software Programmer  
Guate Banda                                            202301679                          Requirements Analyst  
 
 	  
1. Introduction 
1.1 Background  
The University of Eswatini currently relies on manual processes for exam identity verification and script collection. Students present physical exam cards and IDs, which are checked inconsistently, while exam scripts are collected at the end of sessions without a reliable tracking system. These practices create vulnerabilities such as impersonation, mismatched cards and scripts, and even loss of exam scripts. 
With increasing student numbers and the need for accountability, the university requires a secure, integrated solution that links student identity to exam scripts in real time. The Universal Exam Card System is proposed to address these challenges by combining physical and digital verification methods, ensuring transparency and reducing risks. 
This project builds on modern software engineering practices by integrating a node backend, a student services web extension, and a mobile app. The system will provide students with digital exam cards, invigilators with verification tools, and administrators with dashboards for monitoring exam integrity. 
1.2. Aim 
To develop a secure, digital examination management system that utilizes a universal identity token to automate student verification and script tracking, ensuring institutional accountability and preventing academic fraud at the University of Eswatini.  
1.3. Objectives 
•	Develop a universal exam card that consolidates all registered modules into a single identity token for each student. 
•	Enable secure identity verification at exam hall entry by linking the card to the student database, preventing impersonation. 
•	Digitally log script collection by pairing the universal exam card with exam script scans, ensuring every script is tied to a verified student. 
•	Establish invigilator accountability through row-level reconciliation and a lead invigilator dashboard that tracks attendance and script collection in real time. 
1.4. Project Roles  
•	Team Leader  
•	Requirements Analyst  
•	Software Designer  
•	Programmer  
1.5. Project Summary  
The Universal Exam Card System is a secure, cloud-enabled exam management solution developed using an iterative model to improve exam integrity and streamline student verification at the University of Eswatini. It integrates a backend API, web dashboard, and Android mobile app to provide digital exam cards with QR codes, invigilator scanning tools, and automated reporting. The system prevents fraud through identity checks and fallback mechanisms, ensures scalability with AWS deployment, and follows rigorous coding, testing, and quality assurance standards. Ultimately, it delivers a reliable, user-friendly platform that supports students, invigilators, and administrators in managing examinations efficiently 
2. User/Client Involvement 
The client for this project is the University of Eswatini, which will provide essential information, services, and facilities to support the development and implementation of the Universal Exam Card system. 
2.1. Information Provision 
•	The university will supply detailed insights into current examination processes, including identity verification, exam card distribution, and script collection workflows. 
•	This information will be gathered through consultations with lecturers, Heads of Department (HODs), and faculty administration staff. 
2.2. Resources and Facilities 
•	Network Access: School Wi-Fi will be made available to enable research, collaboration, and system testing. 
•	Physical Facilities: Classrooms and lecture halls will be used for team meetings, discussions, and project reviews. 
•	Computer Laboratories: In the event of personal device failure, the university’s computer labs will serve as backup facilities for development and research activities. 
2.3. Timing of Provision 
•	Information on exam workflows will be provided during the requirements analysis phase. 
•	Network access will be available during the implementation and testing phases. 
•	Physical facilities and computer labs will be accessible throughout the entire project lifecycle for collaboration and contingency support. 
3. Risks 
3.1. Technical Risks 
•	Risk: Real time exam session failure due to network outage or server downtime.  Mitigation: Implement offline functionality in the Android app with local data sync once   connectivity is restored. 
 
•	Risk: Database errors or data loss during student identity verification.  
Mitigation: Schedule regular backups, maintain transaction logs, and test with mock   datasets. 
 
•	Risk: Integration challenges between backend, web frontend, and mobile app.  Mitigation: Use Node.js APIs as a bridge, maintain clear API documentation, and conduct early integration testing. 
 
3.2. Schedule Risks 
•	Risk: Delays in team members uploading role documents or diagrams.  Mitigation: Set internal deadlines earlier than official submission dates and track progress using GitHub Issues. 
 
•	Risk: Disruptions due to student or staff protests.  
Mitigation: Coordinate work remotely using online collaboration tools. 
 
3.3. Resource Risks 
•	Risk: Limited access to licensed tools for example Visual Paradigm and IDEs.          Mitigation: Adopt free alternatives such as Draw.io, Lucidchart, and VS Code. 
 
•	Risk: Uneven workload distribution among team members.                 
        Mitigation: Assign tasks based on expertise and rotate responsibilities to balance effort. 
 
3.4. Quality Risks 
•	Risk: Inconsistent coding standards leading to errors and maintainability issues.  
        Mitigation: Enforce coding standards, use linting tools, and conduct peer reviews. 
 
•	Risk: Poor documentation hindering collaboration. 
Mitigation: Use Markdown templates in the docs/ directory and require updates with each commit. 
 
3.5. Operational Risks 
•	Risk: GitHub repository mismanagement that can result in accidental deletions, merge conflicts. 
Mitigation: Adopt a branching strategy like feature branches plus pull requests, maintain backups and hold weekly training meetings on how to use the platform.  
 
•	Risk: Miscommunication among teammates.  
Mitigation: Hold weekly check-ins, define clear role responsibilities, and use a shared progress board. 
 
4. Standards, Guidelines, and Procedures 
This project will adhere to internationally recognized software engineering standards to ensure quality and consistency:
•	IEEE 830 — for structuring the Software Requirements Specification (SRS).
•	IEEE 1016 — for preparing the Software Design Description (SDD).
In addition to these structural compliance frameworks, the team enforces strict collaboration and version control operations directly within the centralized project repository.

5. Organization of the Project 
5.1. Relations to Other Projects 
The Universal Exam Card System is grounded in the principles taught in CSC392: Practices in 
Software Engineering I. It is not designed as a standalone mini-project since its effectiveness relies on integration with the university’s existing systems and central database to validate student enrollments and exam schedules. By connecting directly to institutional data, the project ensures that exam verification and script tracking are seamlessly aligned with the university’s established academic processes 
5.2. Team Roles 
•	Project Manager (Team Leader) is responsible for guiding the overall direction of the project and ensuring smooth collaboration among team members. They oversee project planning, scheduling, and risk management, making sure milestones are achieved on time. The role involves coordinating communication, resolving conflicts, and keeping the team aligned with agreed objectives. The Team Leader also monitors progress through tools like GitHub Issues and project charts, ensuring that deliverables meet quality standards. By incorporating lecturer feedback and maintaining accountability, the Team Leader supports both the technical and organizational success of the project. 
•	Requirements Analyst confirms the team understands user needs and system goals, organizes requirements into functional and non-functional categories, documents them clearly, and supports designers and developers. Also helps verify that features match requirements to maintain quality and consistency. 
•	Software Designer translates requirements into architectural blueprints and design models. They create UML diagrams such as ERD, Sequence, and Activity diagrams to define system structure, behavior, and data flow. Ensure the system is scalable, maintainable, and aligned with requirements, providing a clear framework that guides consistent development across backend, frontend, and mobile components by applying design principles and patterns.  
•	Programmer/Developer is responsible for implementing system functionality across backend, frontend, and mobile components. They design and write code, integrate modules, and ensure proper interaction with the database. In addition to development, the Programmer prepares and executes test cases, runs unit and integration tests, and verifies that features meet documented requirements. They also document test results in docs/testing-report.md, helping maintain quality and consistency in the absence of a dedicated tester. 
 
  5.3. Training Requirements 
•	GitHub Workflow: Master version control, including branching, pull requests, and commit standards across web, backend, and Android repositories. 
•	Diagramming & Planning Tools: Be proficient in Visual Paradigm, Lucidchart, or Draw.io to create and interpret UML diagrams, Gantt charts, and PERT diagrams for critical path analysis. 
•	Coding & Security Standards: Familiarization with project guidelines such as JWT authentication, parameterized SQL queries, and Android background thread management. 
•	Project Management Tools: Understanding scheduling techniques and dependency tracking using Gantt or PERT charts. 
5.4. Training Schedule 
•	Week 1: GitHub basics, repository initialization, and AWS CLI setup. 
•	Week 2: UML diagramming workshop and MySQL Workbench schema design training. 
•	Week 3: Coding standards orientation (Node.js/React/Java) and security protocol briefing. 
•	Week 4: Testing methodology training, focusing on Jest, Supertest, and Android Emulator usage 
6. Project Phases 
6.1. Lifecycle Model 
We adopted the Iterative Model because the project has already progressed outside the traditional sequence of the System Development Life Cycle. A workflow and prototype were created before requirements were fully defined, making a strictly sequential approach unsuitable. The Iterative Model allows us to refine requirements, design, and implementation in cycles, incorporating feedback at each stage. This ensures flexibility, continuous improvement, and proper documentation while meeting fixed academic deadlines 
6.2. Phases and Tasks (Iterations) 
Iteration 1 (10–14 April) 
o	Proposal PDF which consisted of problem statement, solution, workflow and benefits created and Project approved.  
o	Initial prototype sketches shared in team discussions. 
o	 Unofficial GitHub repository initialized with first commit. 
o	Milestone: Concept note and initial prototype established. 
Iteration 2 (14–24 April) 
o	Consecutive commits refining dashboard, login routes, and exam card handling on unofficial prototype GitHub repository. 
o	Official GitHub repository created (Task 2 submission). 
o	Problem statement rewritten; aims and objectives documented. 
o	Milestone: Official repository and academic documentation completed. 

Iteration 3 (24–25 April) 
o	Project plan drafted, including lifecycle model, phases, milestones, critical path, and effort estimates. 
o	Milestone: Project plan uploaded to documentation. 

Iteration 4 (26 April – 3 May) 
o	Requirements Analyst: Begin gathering and documenting functional and non-functional requirements; draft use cases and diagrams.
o	Designer: Continue UX/UI prototype contributions; start workflow and UML model creation. 
o	Programmer: Extend prototype functionality; run initial unit tests; document results in docs/testing-report.md. 
o	Milestone: Draft requirements and design models completed. 

Iteration 5 (4–10 May) 
o	Refine requirements and design models based on feedback. 
o	Programmer integrates backend logic with database schema; frontend and mobile components expanded. 
o	Designer improves UX/UI and aligns models with requirements. 
o	Milestone: Prototype refined with functional requirements integrated. 

Iteration 6 (11–17 May) 
o	Requirements Analyst finalizes documentation of requirements. 
o	Programmer continues coding and testing in parallel (backend, web, mobile)
o	Designer completes UML and workflow models. 
o	Team Leader approves environment setup for AWS server. 
o	Milestone: Requirements finalized, design package completed, AWS environment prepared. 
 
Iteration 7 (18–27 May) 
      o Programmer runs integration and acceptance tests with mock student data. 
o	Designer polishes UX/UI and ensures consistency with models. 
o	Requirements Analyst validates system against documented requirements. 
o	Team Leader coordinates deployment package and final submission. 
o	Milestone: Final submission delivered (27 May). 
6.3. Milestones 
•	Proposal and concept note completed (10 April). 
•	Initial prototype and GitHub setup (14 April). 
•	Official repository, rewritten problem statement, aims and objectives (24 April). 
•	Project plan drafted (25 April). 
•	Requirements and design models drafted (3 May). 
•	Prototype refined with requirements integrated (10 May). 
•	Requirements finalized, design package completed, AWS environment prepared (17 May). 
•	Final submission delivered (27 May). 
6.4. Critical Path 
•	Requirements → Design → Implementation → Testing → Deployment. 
•	Each iteration builds on the previous one; delays in requirements or design directly affect later cycles. 
•	Parallel tasks reduce time but depend on requirements and design completion. 
6.5. Effort Estimates (April–May) 
•	Requirements: ~20 person-hours. 
•	Design: ~25 person-hours. 
•	Implementation: ~40 person-hours. 
•	Testing: ~20 person-hours. 
•	Deployment: ~15 person-hours. 
Total estimated effort: ~120 person-hours across 7 iterations (10 April – 27 May). 
 
7. Requirements Analysis and Design 
7.1 Methods and Techniques
Requirements will be gathered through stakeholder consultations, workflow observations, and team discussions. Both functional and non‑functional requirements will be documented following IEEE 830 guidelines to ensure clarity, completeness, and traceability.
7.2 Modeling Techniques
System behavior and structure will be represented using UML diagrams like the Use Case, and Activity Sequence Diagram. These models will be prepared in line with IEEE 1016 viewpoints to capture context, composition, logical behavior, and information flow.
7.3 Validation and Refinement
Iterative reviews with the team and stakeholders will be conducted to verify correctness and consistency. Feedback loops will be used to refine requirements and design before implementation.

7. 4 Resources and Tools
Visual Paradigm, Lucidchart, or Draw.io will be used for diagramming; GitHub will serve as the collaboration and documentation platform; Markdown will be used for academic deliverables.
7.5 Expected Deliverables:
IEEE 830‑compliant Requirements Specification Document
Approved UML and structured diagrams (Use Case, ERD, Sequence, Activity, DFD)
Requirements Traceability Matrix linking requirements to design elements.

8. Implementation 
8.1. Overview 
The implementation phase focuses on building, integrating, and deploying the Universal Exam Card System across its three main components; backend, web frontend, and mobile app. This phase uses the iterative model, allowing continuous testing, prototyping, and refinement while maintaining alignment with requirements and design outputs. 
8.2. Resources and Tools 
Resource 	Specification 
Development PC 	Minimum 8 GB RAM, 256 GB SSD, Windows 10/11 or macOS 
Android Device 	Android 7.0 (API 24) or higher with camera for QR scanning 
Network 	Stable internet connection for cloud deployment and API calls 
AWS Account 	Free tier account sufficient for development and small-scale production 
Table 1. Hardware Requirements 
 
Software 	Version / Notes 
Node.js 	v18 or higher 
MySQL Server 	v8.0 or higher 
Android Studio 	Hedgehog or later with Gradle 9.3.1 
React 	v18 with Create React App 
Java JDK 	Version 11 for Android development 
AWS Services 	RDS, Elastic Beanstalk, S3, Amplify, EC2 
Table 2. Software Requirements 
 
Tool 	Component 	Purpose 
Visual Studio Code 	React Frontend + Node.js 
Backend  	Primary code editor with JS/Node extensions 
Android Studio 	Android App 	Official IDE for Java Android development 
MySQL Workbench 	Database 	Schema design, query execution, and data management 
Git + GitHub 	All components 	Version control and collaboration 
Postman 	Backend + API 	Endpoint testing and documentation 
Figma 	UI/UX 	Design mockups before implementation 
AWS CLI 	Cloud 	Deploy and manage AWS services 
Table 3. Implementation: Development tools 
8.3. Implementation Phases  
Phase Milestone 	Description 
1 	Database Setup 	Design and implement MySQL schema, insert test data, verify relationships. 
2 	Backend API 	Implement Express routes, connect to MySQL, test endpoints with Postman. 
3 	React Dashboard 	Build student login, dashboard, PDF report, and QR exam card. 
4 	Android App 	Develop invigilator login, home screen, QR scanner, and manual entry. 
Phase Milestone 	Description 
5 	Integration Testing 	Test all components end-to-end with mock student data. 
6 	Cloud Deployment 	Deploy database to AWS RDS, backend to Elastic Beanstalk, frontend to Amplify. 
7 	User Acceptance 
Testing (UAT) 	Conduct UAT with real invigilators and mock student data. 
8 	Production Launch 	Publish Android app and go live with all cloud services. 
Table 4. Implementation Phases 
 
8.4. Team Roles During Implementation 
•	Requirements Analyst: Continues refining requirements and validating system behavior. 
•	Designer: Enhances UX/UI and ensures design consistency across platforms. 
•	Programmer: Develops, tests, and deploys backend, web, and mobile components. 
•	Team Leader: Oversees environment setup (AWS), approves milestones, and coordinates integration. 
8.5. Cost and Estimation 
	Resource 	Estimated Cost 	Notes 
AWS Free Tier (12 months) 	$0 	EC2, Amplify within limits. 
Total Effort 	~120 person-hours 	Distributed across design, coding, testing, and deployment. 
Table 5. Implementation: Cost and Estimation 
9. Testing 
9.1. Test Environment 
Testing will be conducted across all four system components, both local and cloud environments to ensure reliability and scalability. 
 
 
Component 	Environment 	Description 
Backend 	Local (Node.js v18 + MySQL v8.0) and AWS Elastic Beanstalk 	API routes tested locally and deployed to cloud for integration. 
Web Frontend 	Local React development server a nd AWS Amplify 	Browser-based testing on Chrome, Firefox and Edge. 
Mobile App 	Android Studio emulator and physi cal Android devices (API 24+) 	Tests, QR scanning, login, and venue  verification. 
Database 	MySQL Workbench and AWS  
RDS 	Schema validation, data integrity checks,  and mock student data insertion. 
Table 6. Test Environments for System Components 
 
9.2. Test Equipment 
•	Development laptops with VS Code and Android Studio. 
•	Android devices for real-world QR scanning tests. 
•	Stable internet connection for cloud deployment and API calls. 
•	AWS RDS and Elastic Beanstalk instances for live integration tests. 
•	Postman for API testing and Jest + Supertest for automated backend tests. 
 
9.3. Testing Plan 
Testing follows an iterative approach, ensuring each component is validated before integration. 
•	Unit Testing 
 Backend functions and route handlers tested with Jest and Supertest. React components tested with Jest and React Testing Library. 
Android activities tested using JUnit and instrumentation tests. 
•	Integration Testing 
 Verify communication between backend APIs and web/mobile clients. 
Ensure data consistency across databases and UI components. 
•	System Testing 
End-toend workflow: student card scan → verification → script logging → dashboard update. 
Test concurrency for multiple students entering simultaneously. 
•	User Acceptance Testing (UAT)
 Conducted with mock student data and invigilator accounts. 
Validate real-world scenarios such as wrong venue, duplicate scan, and manual entry. 
 
9.4. Order of Integration and Testing 
1.	Test backend modules independently.    
2.	Integrate backend with web frontend → run integration tests. 
3.	Integrate backend with mobile app → run integration tests. 
4.	Combine all components → perform system testing. 
5.	Conduct UAT before deployment to AWS. 
9.5. Testing Procedures 
•	Define test cases in docs/testing-report.md. 
•	Record expected vs. actual results for each test case. 
•	Document bugs and issues in GitHub Issues for tracking. 
•	Retest after fixes to confirm resolution. 
•	Generate final test report summarizing coverage and outcomes. 
10. Resources 
11. Quality Assurance 
11.1. Organization 
•	Team Leader: Oversees quality assurance activities, ensures standards are followed, and coordinates reviews. 
•	Requirements Analyst: Validates that the system meets documented functional and non-functional requirements. 
•	Designer: Reviews UX/UI consistency and ensures design models align with requirements. 
•	Programmer: Implements coding standards, runs automated tests, and documents results. 
•	Peer Review: All code and documentation must be reviewed by at least one teammate before merging into the main branch. 
 
11.2. Procedures 
11.2.1. Coding Standards: 
o	Backend follows Node.js/Express guidelines (parameterized queries, error handling, bcrypt hashing). o Frontend follows React conventions (component separation, error handling, environment variables). o Android app follows Java/Android best practices (background threads, runtime permissions, secure storage). o Database follows schema rules (primary keys, foreign keys, snake_case, bcrypt password storage). 
11.2.2. Documentation Standards: 
o	All documents stored in docs/ folder in Markdown format. 
o	Updates must accompany commits to keep repository synchronized. 
11.2.3. Testing Procedures: 
o	Unit tests for backend functions and route handlers. o Integration tests for web and mobile components. 
o	System testing of end-to-end workflows. 
o	User acceptance testing with mock student data. 
11.2.4. Continuous Integration: 
o	GitHub Actions runs automated tests on every commit. o Builds must pass before merging into the main branch. 
11.3. Quality Metrics 
•	Reliability: No critical bugs in final submission. 
•	Usability: Clear user interface for students, invigilators, and administrators. 
•	Performance: System responds to card scans and script logging within 2 seconds. 
•	Maintainability: Code is modular, documented, and reusable. 
•	Security: JWT authentication, HTTPS enforcement, and AWS security groups applied. 
 
11.4. Review and Feedback 
•	Weekly team check-ins to review progress and catch errors early. 
•	Lecturer feedback incorporated into revisions. 
•	Issues tracked in GitHub for transparency and accountability. 
12. Changes 
12.1. Overview 
Changes during the software development process are inevitable due to evolving requirements, design refinements, and testing feedback. To ensure these changes are managed systematically, the team will follow a structured change management procedure. A more detailed version may be documented separately in a Configuration Management Plan. 
12.2. Change Management Organization 
•	Team Leader: Approves or rejects major changes, ensures alignment with project goals. 
•	Requirements Analyst: Documents change to functional and non-functional requirements. 
•	Designer: Updates design models and UX/UI mockups to reflect approved changes. 
•	Programmer: Implements code changes, ensures backward compatibility, and updates tests. 
•	All Team Members: Record changes in GitHub Issues and update documentation accordingly. 
 
12.3. Procedures for Handling Changes 
12.3.1. Identification 
o	Any team member who discovers a need for change (bug fix, new requirement, design adjustment) must log it in GitHub Issues. 
o	Each change request must include a description, rationale, and potential impact. 
12.3.2. Evaluation 
o	The team leader and analyst assess the impact on scope, schedule, and resources. 
o	Changes are categorized as minor (UI tweaks, bug fixes) or major (new features, database schema updates). 
12.3.3. Approval 
o	Minor changes may be approved by the team leader directly. 
o	Major changes require team discussion and consensus before implementation. 
12.3.4. Implementation 
o	Programmer applies changes in a separate branch. o Designer updates models and mockups if applicable. 
o	Documentation in docs/change-log.md is updated to reflect the change. 
12.3.5. Verification 
o Changes undergo unit and integration testing. o Results are logged in docs/testing-report.md. 
12.3.6. Tracking 
o	All changes are tracked in GitHub Projects (Kanban board: To Do → In Progress → Done). 
o	Weekly reviews ensure no undocumented changes are introduced. 
12.4. Critical Principles 
•	Transparency: Every change must be documented and visible to the team. 
•	Traceability: Each change links back to its origin (requirement, bug report, feedback). 
•	Accountability: Only approved changes are merged into the main branch. 
•	Consistency: Documentation, code, and design models must remain synchronized. 
 
13. Project Timeline and Dependencies (Gantt Chart Representation) 
 
 
Iteration Dates 	Tasks 	Milestones 	Dependencies 
1 	10–14 
April 	Project approval; proposal PDF created; initial prototype sketches; 
GitHub repo initialized 	Concept note and initial prototype established 	None (start) 
2 	14–24 
April 	Consecutive commits refining dashboard, login routes, exam card handling; official repo created; problem statement rewritten; aims 
& objectives documented 	Official repository and academic documentation completed 	Iteration 1 
3 	24–25 
April 	Draft project plan (lifecycle model, phases, milestones, critical path, effort estimates) 	Project plan uploaded to docs/ 	Iteration 2 
4 	26 Apr –
3 May 	Requirements Analyst drafts functional & non-functional requirements, use cases, diagrams; Designer continues UX/UI prototype and workflow models; Programmer extends 	Draft requirements and design models completed 	Iteration 3 
		prototype functionality and runs initial unit tests 		
5 	4–10 
May 	Refine requirements and design models; Programmer integrates backend logic with database schema; frontend and mobile expanded; Designer improves 
UX/UI 	Prototype refined with functional requirements integrated 	Iteration 4 
6 	11–17 
May 	Programmer continues coding and testing; Requirements Analyst finalizes documentation; Designer completes UML and workflow models; Team Leader approves 
AWS environment setup 	Requirements 
finalized, design package completed, AWS environment prepared 	Iteration 5 
7 	18–27 
May 	Programmer runs integration and acceptance tests; Designer polishes UX/UI; Requirements Analyst validates system against requirements; Team Leader coordinates deployment package 	Final submission delivered (27 May) 	Iteration 6 
Table 11. Gantt Chart 
 
 
 
 
 
