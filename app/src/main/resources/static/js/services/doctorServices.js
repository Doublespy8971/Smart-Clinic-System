/*
  Import the base API URL from the config file
*/
import { API_BASE_URL } from "../config/config.js";

/*
  Define a constant DOCTOR_API to hold the full endpoint
  for doctor-related actions
*/
const DOCTOR_API = `${API_BASE_URL}/doctor`;

/*
  Function: getDoctors
  Purpose: Fetch the list of all doctors from the API
*/
export async function getDoctors() {
    try {
        const response = await fetch(`${DOCTOR_API}/findAll`);

        const data = await response.json();
        return data.doctors || [];   // Return the list of doctors
    } catch (error) {
        console.error("Error fetching doctors:", error);
        return [];   // Return empty array on failure
    }
}

/*
  Function: deleteDoctor
  Purpose: Delete a specific doctor using their ID and an authentication token
*/
export async function deleteDoctor(doctorId, token) {
    try {
        const response = await fetch(`${DOCTOR_API}/delete/${doctorId}/${token}`, {
            method: "DELETE",
        });

        const data = await response.json();

        return {
            success: data.success || false,
            message: data.message || "Unable to delete doctor.",
        };
    } catch (error) {
        console.error("Error deleting doctor:", error);

        return {
            success: false,
            message: "An error occurred while deleting the doctor.",
        };
    }
}

/*
  Function: saveDoctor
  Purpose: Save (create) a new doctor using a POST request
*/
export async function saveDoctor(doctor, token) {
    try {
        const response = await fetch(`${DOCTOR_API}/save/${token}`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(doctor),
        });

        const data = await response.json();

        return {
            success: data.success || false,
            message: data.message || "Unable to save doctor.",
        };
    } catch (error) {
        console.error("Error saving doctor:", error);

        return {
            success: false,
            message: "An error occurred while saving the doctor.",
        };
    }
}

/*
  Function: filterDoctors
  Purpose: Fetch doctors based on filtering criteria (name, time, and specialty)
*/
export async function filterDoctors(name, time, specialty) {
    try {
        const response = await fetch(
            `${DOCTOR_API}/filter/${name}/${time}/${specialty}`
        );

        if (!response.ok) {
            console.error("Failed to filter doctors:", response.status);
            return { doctors: [] };
        }

        const data = await response.json();
        return data;
    } catch (error) {
        console.error("Error filtering doctors:", error);
        alert("Something went wrong while filtering doctors.");
        return { doctors: [] };
    }
}
