/*
  Import the openModal function to handle showing login modals
  Import the base API URL from the config file
*/
import { openModal } from "../components/modals.js";
import { API_BASE_URL } from "../config/config.js";
import { patientSignup, patientLogin } from "./patientServices.js";

/*
  Define API endpoints for admin and doctor login
*/
const ADMIN_API = `${API_BASE_URL}/admin/login`;
const DOCTOR_API = `${API_BASE_URL}/doctor/login`;

/*
  Use the window.onload event to attach listeners after DOM loads
*/
window.onload = function () {
    const adminLoginBtn = document.getElementById("adminLogin");
    const doctorLoginBtn = document.getElementById("doctorLogin");

    if (adminLoginBtn) {
        adminLoginBtn.addEventListener("click", () => openModal("adminLogin"));
    }

    if (doctorLoginBtn) {
        doctorLoginBtn.addEventListener("click", () => openModal("doctorLogin"));
    }
};

/*
  Define admin login handler globally
*/
window.adminLoginHandler = async function () {
    try {
        // Step 1: Get entered username & password
        const username = document.getElementById("username").value.trim();
        const password = document.getElementById("password").value.trim();

        // Step 2: Create admin object
        const admin = { username, password };

        // Step 3: Send POST request for admin login
        const response = await fetch(ADMIN_API, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(admin),
        });

        const data = await response.json();

        // Step 4: Success → store token, redirect role
        if (response.ok && data.token) {
            localStorage.setItem("token", data.token);
            selectRole("admin");
            return;
        }

        // Step 5: Invalid credentials
        alert(data.message || "Invalid admin credentials.");

    } catch (error) {
        console.error("Admin login error:", error);
        // Step 6: Generic error message
        alert("Something went wrong during admin login. Please try again.");
    }
};

/*
  Define doctor login handler globally
*/
window.doctorLoginHandler = async function () {
    try {
        // Step 1: Get entered email & password
        const email = document.getElementById("doctorEmail").value.trim();
        const password = document.getElementById("doctorPassword").value.trim();

        // Step 2: Create doctor login object
        const doctor = { email, password };

        // Step 3: Send POST request for doctor login
        const response = await fetch(DOCTOR_API, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(doctor),
        });

        const data = await response.json();

        // Step 4: Login success
        if (response.ok && data.token) {
            localStorage.setItem("token", data.token);
            selectRole("doctor");
            return;
        }

        // Step 5: Invalid credentials
        alert(data.message || "Invalid doctor credentials.");

    } catch (error) {
        console.error("Doctor login error:", error);
        // Step 6: Generic fallback message
        alert("Something went wrong during doctor login. Please try again.");
    }
};

window.signupPatient = async function () {
    try {
        const name = document.getElementById("name").value;
        const email = document.getElementById("email").value;
        const password = document.getElementById("password").value;
        const phone = document.getElementById("phone").value;
        const address = document.getElementById("address").value;

        const data = { name, email, password, phone, address };
        const { success, message } = await patientSignup(data);
        if (success) {
            alert(message);
            document.getElementById("modal").style.display = "none";
            window.location.reload();
        } else {
            alert(message);
        }
    } catch (error) {
        console.error("Signup failed:", error);
        alert("❌ An error occurred while signing up.");
    }
};

window.loginPatient = async function () {
    try {
        const email = document.getElementById("email").value;
        const password = document.getElementById("password").value;

        const data = { email, password };
        console.log("loginPatient :: ", data);
        const response = await patientLogin(data);

        if (response.ok) {
            const result = await response.json();
            localStorage.setItem("token", result.token);
            selectRole("loggedPatient");
            window.location.href = "/pages/loggedPatientDashboard.html";
        } else {
            alert("❌ Invalid credentials!");
        }
    } catch (error) {
        console.error("Error :: loginPatient :: ", error);
        alert("❌ Failed to Login.");
    }
};
