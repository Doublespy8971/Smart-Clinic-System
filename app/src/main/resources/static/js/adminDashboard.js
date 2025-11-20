/*
  Import required helper functions and service layer APIs
*/
import { openModal, closeModal } from "../utils/util.js";
import { getDoctors, filterDoctors, saveDoctor } from "../services/doctorServices.js";
import { createDoctorCard } from "../components/doctorCard.js";

/*
  Add click listener for "Add Doctor" button
*/
const addDoctorBtn = document.getElementById("addDocBtn");
if (addDoctorBtn) {
    addDoctorBtn.addEventListener("click", () => openModal("addDoctor"));
}

/*
  Load doctor cards when the page is fully loaded
*/
document.addEventListener("DOMContentLoaded", () => {
    loadDoctorCards();
});

/*
  Function: loadDoctorCards
  Purpose: Fetch and display all doctors as cards
*/
async function loadDoctorCards() {
    try {
        const doctors = await getDoctors();
        renderDoctorCards(doctors);
    } catch (error) {
        console.error("Error loading doctors:", error);
    }
}

/*
  Add listeners to search & filter fields
*/
const searchInput = document.getElementById("searchBar");
const timeFilter = document.getElementById("timeFilter");
const specialtyFilter = document.getElementById("specialtyFilter");

if (searchInput) searchInput.addEventListener("input", filterDoctorsOnChange);
if (timeFilter) timeFilter.addEventListener("change", filterDoctorsOnChange);
if (specialtyFilter) specialtyFilter.addEventListener("change", filterDoctorsOnChange);

/*
  Function: filterDoctorsOnChange
  Purpose: Filter doctors based on the user's search inputs
*/
async function filterDoctorsOnChange() {
    try {
        const name = searchInput.value.trim() || null;
        const time = timeFilter.value || null;
        const specialty = specialtyFilter.value || null;

        const filteredData = await filterDoctors(name, time, specialty);

        if (filteredData.doctors && filteredData.doctors.length > 0) {
            renderDoctorCards(filteredData.doctors);
        } else {
            document.getElementById("content").innerHTML =
                `<p class="no-result">No doctors found with the given filters.</p>`;
        }
    } catch (error) {
        console.error("Error filtering doctors:", error);
        alert("Failed to filter doctors. Please try again.");
    }
}

/*
  Function: renderDoctorCards
  Purpose: Clear and re-render the doctor_list UI
*/
function renderDoctorCards(doctors) {
    const contentDiv = document.getElementById("content");
    contentDiv.innerHTML = "";

    doctors.forEach((doctor) => {
        const card = createDoctorCard(doctor);
        contentDiv.appendChild(card);
    });
}

/*
  Function: adminAddDoctor
  Purpose: Collect form input and add a new doctor
*/
window.adminAddDoctor = async function () {
    // Collect values from modal form
    const name = document.getElementById("doctorName").value.trim();
    const email = document.getElementById("doctorEmail").value.trim();
    const phone = document.getElementById("doctorPhone").value.trim();
    const password = document.getElementById("doctorPassword").value.trim();
    const specialty = document.getElementById("doctorSpecialty").value.trim();

    // Collect all time slots
    const timeInputs = document.querySelectorAll(".timeSlot");
    const availableTimes = [...timeInputs].map((input) => input.value.trim()).filter(Boolean);

    const token = localStorage.getItem("token");
    if (!token) {
        alert("Not authorized. Please log in as admin.");
        return;
    }

    // Build doctor object
    const doctor = {
        name,
        email,
        phone,
        password,
        specialty,
        availableTimes,
    };

    try {
        const result = await saveDoctor(doctor, token);

        if (result.success) {
            alert("Doctor added successfully!");
            closeModal("addDoctor");
            loadDoctorCards();
        } else {
            alert(result.message || "Failed to add doctor.");
        }
    } catch (error) {
        console.error("Error adding doctor:", error);
        alert("Something went wrong. Please try again.");
    }
};
