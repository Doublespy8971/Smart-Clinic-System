📌 Smart Clinic Management System — Database Schema Design

This document describes the complete database design for the Smart Clinic Management System, covering all four major modules: Admin, Doctor, Patient, and Appointment, along with their relationships and constraints.

🗄️ 1. Overview

The system uses a merged database schema where all entities are stored in a unified MySQL database.
The design supports:

User authentication (admin, doctor, patient)

Appointment scheduling

Prescription handling (via MongoDB)

Data integrity through foreign keys

Scalable extension of roles and services

🧱 2. Entity Relationship Diagram (ERD)
+---------+        +-------------+        +-----------+
| Patient | 1    ∞ | Appointment | ∞    1 | Doctor    |
+---------+        +-------------+        +-----------+
↑                                         ↑
|                                         |
|                                         |
+--------------- Admin (manages users) ----+

🏥 3. Database Tables
3.1 Admin Table

Stores system administrators who manage doctors, patients, and appointments.

Column	Type	Description
id	BIGINT (PK)	Auto-increment admin ID
username	VARCHAR(255)	Unique admin username
password	VARCHAR(255)	Hashed admin password
Table DDL
CREATE TABLE admin (
id BIGINT NOT NULL AUTO_INCREMENT,
username VARCHAR(255) NOT NULL,
password VARCHAR(255) NOT NULL,
PRIMARY KEY (id),
UNIQUE KEY (username)
);

3.2 Doctor Table

Stores doctor details and authentication info.

Column	Type	Description
id	BIGINT (PK)	Unique doctor ID
name	VARCHAR(100)	Doctor’s full name
specialty	VARCHAR(50)	Medical specialization
email	VARCHAR(255)	Unique email
password	VARCHAR(255)	Hashed doctor password
phone	VARCHAR(10)	Contact number
Table DDL
CREATE TABLE doctor (
id BIGINT NOT NULL AUTO_INCREMENT,
name VARCHAR(100) NOT NULL,
specialty VARCHAR(50) NOT NULL,
email VARCHAR(255) NOT NULL,
password VARCHAR(255) NOT NULL,
phone VARCHAR(10) NOT NULL,
PRIMARY KEY (id),
UNIQUE KEY (email)
);

3.3 Patient Table

Stores patient details and login credentials.

Column	Type	Description
id	BIGINT (PK)	Unique patient ID
name	VARCHAR(100)	Full name
email	VARCHAR(255)	Unique email
password	VARCHAR(255)	Hashed password
phone	VARCHAR(10)	Contact number
address	VARCHAR(255)	Residential address
Table DDL
CREATE TABLE patient (
id BIGINT NOT NULL AUTO_INCREMENT,
name VARCHAR(100) NOT NULL,
email VARCHAR(255) NOT NULL,
password VARCHAR(255) NOT NULL,
phone VARCHAR(10) NOT NULL,
address VARCHAR(255) NOT NULL,
PRIMARY KEY (id),
UNIQUE KEY (email)
);

3.4 Appointment Table

Tracks every appointment between patients and doctors.

Column	Type	Description
id	BIGINT (PK)	Appointment ID
doctor_id	BIGINT (FK)	References doctor(id)
patient_id	BIGINT (FK)	References patient(id)
date	DATETIME	Appointment date & time
status	INT	0 = Pending, 1 = Completed, 2 = Cancelled
Relationship

A doctor can have multiple appointments

A patient can have multiple appointments

An appointment belongs to exactly one doctor and one patient

Table DDL
CREATE TABLE appointment (
id BIGINT NOT NULL AUTO_INCREMENT,
doctor_id BIGINT NOT NULL,
patient_id BIGINT NOT NULL,
date DATETIME NOT NULL,
status INT NOT NULL,
PRIMARY KEY (id),
FOREIGN KEY (doctor_id) REFERENCES doctor(id) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY (patient_id) REFERENCES patient(id) ON DELETE CASCADE ON UPDATE CASCADE
);

🍃 4. MongoDB: Prescription Collection

Prescriptions are stored in MongoDB for:

Flexible JSON structure

Quick updates

Separation from relational data

Document Structure
{
"id": "642fb2e9f1",
"appointmentId": 12,
"doctorNotes": "Take medicine twice daily.",
"createdAt": "2025-01-22T14:20:00Z"
}

🔗 5. Relationships Summary
Entity	Relationship Type
Doctor → Appointment	1 to Many
Patient → Appointment	1 to Many
Appointment → Prescription	1 to 1 (MongoDB)
🧩 6. Constraints & Rules

Unique emails for doctor & patient

Cascading delete ensures no orphan appointments

Token-based auth ensures only the correct role accesses data

Admin has system-wide access but stored separately

🚀 7. Scalability Notes

This database design supports:

Adding nurse roles

Adding hospital branches

Adding medical records

Multi-doctor specialty expansion

Integration with billing system