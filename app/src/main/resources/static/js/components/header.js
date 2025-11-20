// header.js

/* ============================================================
   RENDER HEADER BASED ON USER ROLE & SESSION STATUS
   ============================================================ */

export function renderHeader() {
    const headerDiv = document.getElementById("header");

    /* ------------------------------------------------------------
       1. Check if current page is root → Clear session
       ------------------------------------------------------------ */
    if (window.location.pathname.endsWith("/")) {
        localStorage.removeItem("userRole");

        headerDiv.innerHTML = `
      <header class="header">
        <div class="logo-section">
          <img src="../assets/images/logo/logo.png" alt="Hospital CRM Logo" class="logo-img">
          <span class="logo-title">Hospital CMS</span>
        </div>
      </header>
    `;
        return;
    }

    /* ------------------------------------------------------------
       2. Retrieve session data
       ------------------------------------------------------------ */
    const role = localStorage.getItem("userRole");  // admin / doctor / patient / loggedPatient
    const token = localStorage.getItem("token");

    /* ------------------------------------------------------------
       3. Base header content
       ------------------------------------------------------------ */
    let headerContent = `
    <header class="header">
      <div class="logo-section">
        <img src="../assets/images/logo/logo.png" alt="Hospital CRM Logo" class="logo-img">
        <span class="logo-title">Hospital CMS</span>
      </div>
      <nav>
  `;

    /* ------------------------------------------------------------
       4. Handle invalid/expired session
       ------------------------------------------------------------ */
    if ((role === "loggedPatient" || role === "admin" || role === "doctor") && !token) {
        localStorage.removeItem("userRole");
        alert("Session expired or invalid login. Please log in again.");
        window.location.href = "/";
        return;
    }

    /* ------------------------------------------------------------
       5. Render role-specific header buttons
       ------------------------------------------------------------ */

    if (role === "admin") {
        headerContent += `
      <button id="addDocBtn" class="adminBtn" onclick="openModal('addDoctor')">Add Doctor</button>
      <a href="#" id="logoutBtn">Logout</a>
    `;
    }

    else if (role === "doctor") {
        headerContent += `
      <button class="adminBtn" onclick="selectRole('doctor')">Home</button>
      <a href="#" id="logoutBtn">Logout</a>
    `;
    }

    else if (role === "patient") {
        headerContent += `
      <button id="patientLogin" class="adminBtn">Login</button>
      <button id="patientSignup" class="adminBtn">Sign Up</button>
    `;
    }

    else if (role === "loggedPatient") {
        headerContent += `
      <button id="home" class="adminBtn" onclick="window.location.href='/pages/loggedPatientDashboard.html'">Home</button>
      <button id="patientAppointments" class="adminBtn" onclick="window.location.href='/pages/patientAppointments.html'">Appointments</button>
      <a href="#" id="logoutPatientBtn">Logout</a>
    `;
    }

    /* ------------------------------------------------------------
       6. Close header HTML
       ------------------------------------------------------------ */
    headerContent += `
      </nav>
    </header>
  `;

    /* ------------------------------------------------------------
       7. Render the header in the DOM
       ------------------------------------------------------------ */
    headerDiv.innerHTML = headerContent;

    /* ------------------------------------------------------------
       8. Attach event listeners to dynamic buttons
       ------------------------------------------------------------ */
    attachHeaderButtonListeners();
}



/* ============================================================
   Helper Function: Attach Event Listeners to Header Buttons
   ============================================================ */
function attachHeaderButtonListeners() {

    /* Patient login button (opens modal) */
    const patientLoginBtn = document.getElementById("patientLogin");
    if (patientLoginBtn) {
        patientLoginBtn.addEventListener("click", () => {
            openModal("patientLogin");
        });
    }

    /* Patient signup button (opens modal) */
    const patientSignupBtn = document.getElementById("patientSignup");
    if (patientSignupBtn) {
        patientSignupBtn.addEventListener("click", () => {
            openModal("signup");
        });
    }

    /* Admin/Doctor logout */
    const logoutBtn = document.getElementById("logoutBtn");
    if (logoutBtn) {
        logoutBtn.addEventListener("click", logout);
    }

    /* Patient logout */
    const logoutPatientBtn = document.getElementById("logoutPatientBtn");
    if (logoutPatientBtn) {
        logoutPatientBtn.addEventListener("click", logoutPatient);
    }
}



/* ============================================================
   Logout Functions
   ============================================================ */

function logout() {
    localStorage.removeItem("userRole");
    localStorage.removeItem("token");
    window.location.href = "/";
}

function logoutPatient() {
    localStorage.removeItem("token");
    window.location.href = "/pages/loggedPatientDashboard.html";
}



/* ============================================================
   Initialize Header Rendering
   ============================================================ */
document.addEventListener("DOMContentLoaded", renderHeader);
