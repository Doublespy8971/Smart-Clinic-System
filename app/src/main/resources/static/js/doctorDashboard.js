/*
  Import required services and helper functions
*/
import { getAllAppointments } from "../services/patientServices.js";
import { createPatientRow } from "../components/patientTableRow.js";

/*
  Select DOM elements
*/
const tableBody = document.getElementById("patientTableBody");
const searchBar = document.getElementById("searchBar");
const todayButton = document.getElementById("todayButton");
const datePicker = document.getElementById("datePicker");

/*
  Initialize state variables
*/
let selectedDate = new Date().toISOString().split("T")[0]; // YYYY-MM-DD
let token = localStorage.getItem("token");
let patientName = null;

/*
  Search bar: filter by patient name on each input
*/
if (searchBar) {
    searchBar.addEventListener("input", () => {
        const value = searchBar.value.trim();
        patientName = value !== "" ? value : "null";
        loadAppointments();
    });
}

/*
  "Today" button: set date to today and reload appointments
*/
if (todayButton) {
    todayButton.addEventListener("click", () => {
        selectedDate = new Date().toISOString().split("T")[0];
        if (datePicker) datePicker.value = selectedDate;
        loadAppointments();
    });
}

/*
  Date picker: reload appointments when date changes
*/
if (datePicker) {
    datePicker.addEventListener("change", () => {
        selectedDate = datePicker.value;
        loadAppointments();
    });
}

/*
  Function: loadAppointments
  Purpose: Fetch and display appointments for selected date and optional patient name
*/
async function loadAppointments() {
    if (!token) {
        alert("Not authorized. Please log in again.");
        return;
    }

    try {
        const appointmentsData = await getAllAppointments(selectedDate, patientName, token);

        // Clear table body
        tableBody.innerHTML = "";

        if (!appointmentsData || appointmentsData.length === 0) {
            tableBody.innerHTML = `<tr><td colspan="5" class="no-appointments">No Appointments found for today.</td></tr>`;
            return;
        }

        // Populate table with appointment rows
        appointmentsData.forEach((appointment) => {
            const patient = {
                id: appointment.patientId,
                name: appointment.name,
                phone: appointment.phone,
                email: appointment.email,
            };

            const row = createPatientRow(patient, appointment);
            tableBody.appendChild(row);
        });

    } catch (error) {
        console.error("Error loading appointments:", error);
        tableBody.innerHTML = `<tr><td colspan="5" class="no-appointments">Error loading appointments. Try again later.</td></tr>`;
    }
}

/*
  Load default appointments and render layout on page load
*/
document.addEventListener("DOMContentLoaded", () => {
    if (typeof renderContent === "function") renderContent();
    loadAppointments();
});
