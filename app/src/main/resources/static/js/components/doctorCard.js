// doctorCard.js

/* =======================
   IMPORT REQUIRED FUNCTIONS
   ======================= */

// Booking overlay for logged-in patients
import { showBookingOverlay } from "../pages/loggedPatient.js";

// Delete doctor API (Admin)
import { deleteDoctor } from "../services/doctorServices.js";

// Fetch patient details (Logged-in patient)
import { fetchPatientDetails } from "../services/patientServices.js";



/* =======================
   CREATE DOCTOR CARD COMPONENT
   ======================= */

export function createDoctorCard(doctor) {

    /* --- Main doctor card container --- */
    const card = document.createElement("div");
    card.classList.add("doctor-card");

    const role = localStorage.getItem("role");        // admin / patient / null
    const token = localStorage.getItem("token");      // authentication token


    /* ===========================
       DOCTOR INFORMATION SECTION
       =========================== */

    const info = document.createElement("div");
    info.classList.add("doctor-info");

    const name = document.createElement("h2");
    name.textContent = doctor.name;

    const specialization = document.createElement("p");
    specialization.textContent = `Specialization: ${doctor.specialty}`;

    const email = document.createElement("p");
    email.textContent = `Email: ${doctor.email}`;

    /* --- Available appointment timings --- */
    const timings = document.createElement("p");
    timings.textContent = `Available Timings: ${doctor.timings?.join(", ") || "N/A"}`;

    // Append info to the info container
    info.appendChild(name);
    info.appendChild(specialization);
    info.appendChild(email);
    info.appendChild(timings);



    /* ===========================
       ACTION BUTTONS SECTION
       =========================== */

    const actions = document.createElement("div");
    actions.classList.add("doctor-actions");



    /* =======================
       ADMIN ROLE — DELETE DOCTOR
       ======================= */

    if (role === "admin") {
        const deleteBtn = document.createElement("button");
        deleteBtn.classList.add("delete-btn");
        deleteBtn.textContent = "Delete Doctor";

        deleteBtn.addEventListener("click", async () => {
            const adminToken = localStorage.getItem("token");

            if (!adminToken) {
                alert("Admin not authenticated!");
                return;
            }

            const confirmDelete = confirm(`Are you sure you want to delete Dr. ${doctor.name}?`);
            if (!confirmDelete) return;

            try {
                const response = await deleteDoctor(doctor.id, adminToken);

                if (response.success) {
                    alert("Doctor deleted successfully!");
                    card.remove();
                } else {
                    alert("Failed to delete doctor.");
                }

            } catch (error) {
                console.error("Delete doctor failed:", error);
                alert("Error occurred while deleting doctor.");
            }
        });

        actions.appendChild(deleteBtn);
    }



    /* =======================
       PATIENT NOT LOGGED IN — SHOW LOGIN WARNING
       ======================= */

    else if (role === "patient" && !token) {
        const bookBtn = document.createElement("button");
        bookBtn.classList.add("book-btn");
        bookBtn.textContent = "Book Now";

        bookBtn.addEventListener("click", () => {
            alert("Please log in to book an appointment.");
        });

        actions.appendChild(bookBtn);
    }



    /* =======================
       LOGGED-IN PATIENT — BOOK APPOINTMENT
       ======================= */

    else if (role === "patient" && token) {
        const bookBtn = document.createElement("button");
        bookBtn.classList.add("book-btn");
        bookBtn.textContent = "Book Now";

        bookBtn.addEventListener("click", async () => {

            // Token must exist
            if (!token) {
                alert("Session expired. Please log in again.");
                window.location.href = "/login";
                return;
            }

            try {
                // Fetch logged-in patient details
                const patientData = await fetchPatientDetails(token);

                if (!patientData) {
                    alert("Unable to fetch patient data.");
                    return;
                }

                // Show booking popup overlay
                showBookingOverlay({
                    doctor,
                    patient: patientData
                });

            } catch (error) {
                console.error("Booking error:", error);
                alert("Unable to proceed with booking.");
            }
        });

        actions.appendChild(bookBtn);
    }



    /* --- Append info + actions to card --- */
    card.appendChild(info);
    card.appendChild(actions);

    return card;
}
