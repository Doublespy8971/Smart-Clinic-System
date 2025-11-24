🏥 Smart Clinic Management System

A complete clinic workflow solution for Admin, Doctor, and Patient, built with Spring Boot, MySQL, MongoDB, and JWT-based authentication.

🚀 Overview

The Smart Clinic Management System (SCMS) is designed to simplify daily clinical operations:

Admin can manage the system and users

Doctors can view, update, and filter their appointments

Patients can book appointments and manage their profiles

Secure authentication using JWT

MySQL used for relational data (Admin, Doctor, Patient, Appointment)

MongoDB used for non-relational storage (Prescriptions)

📚 User Stories
👨‍⚕️ Doctor Stories
✔️ Doctor Login

A doctor can securely log into the system using email and password.
After successful authentication, a JWT token is issued to access protected endpoints.

✔️ View All Appointments

A doctor can view all their scheduled appointments, including:

Patient name

Date & time

Status (Pending / Approved / Completed)

✔️ Update Appointment Status

Doctors can update appointment status (example: approve, cancel, complete).

✔️ Filter Appointments

Doctors can filter appointments based on:

Date

Status

Patient name

This helps doctors quickly focus on the most relevant appointments.

🧑‍🦱 Patient Stories
✔️ Patient Registration

New patients can register by providing:

Name

Email

Password

Phone number

Address

Unique email validation ensures no duplicate patients.

✔️ Patient Login

Authenticated via JWT token for secure access.

✔️ Book Appointment

Patients can book appointments with any available doctor by selecting:

Doctor

Date

Time

System checks for conflicts before confirming.

✔️ View Appointment History

Patients can view all their past and upcoming appointments.

✔️ Filter Appointment Records

Patients can filter appointments based on:

Status

Doctor Name

Date

🛠️ Admin Stories
✔️ Admin Login

Admin logs in securely with a username & password (stored in MySQL).

✔️ Manage Doctors

Admin can:

Add a new doctor

Remove a doctor

Update doctor details

View list of all doctors

✔️ Manage Patients

Admin can view registered patients and remove invalid accounts if needed.

✔️ Monitor Appointments

Admin can see all appointment activity happening across the system.

🔐 Authentication Flow
✔️ JWT Token Generation

When a Doctor, Patient, or Admin logs in successfully:

The server generates a JWT token using jwt.secret

Token includes:

User ID

Role (doctor, patient, admin)

Expiration time

✔️ Token Validation

Every protected endpoint requires:

Authorization: Bearer <token>


Token is validated before performing any action.

🗄️ Databases Used
🟦 MySQL (Relational Data)

Stores structured data:

Entity	Fields
Admin	id, username, password
Doctor	id, name, email, specialty, phone, password
Patient	id, name, email, phone, address, password
Appointment	id, doctor_id, patient_id, date, status
🟩 MongoDB (Non-Relational Data)

Stores prescriptions:

Field	Description
id	Unique Mongo document ID
patientId	Reference to MySQL patient
doctorId	Reference to doctor
medicines	List of prescribed medicines
📁 Project Structure (Simplified)
src/
└── main/
├── java/com/project/back_end/
│     ├── controllers/
│     ├── models/
│     ├── repository/
│     ├── services/
│     └── config/
└── resources/
├── application.properties
└── static/

⚙️ Features Implemented

Role-based authentication (Admin / Doctor / Patient)

JWT tokens

Appointment conflict handling

Filtering & searching

CRUD operations for all major entities

Robus exception & error handling

Validation on all DTOs

Lombok on all models

🧪 Future Enhancements

Email notification for appointment updates

Prescription upload/download

Doctor availability scheduling

Admin dashboard with analytics

Multi-language translation