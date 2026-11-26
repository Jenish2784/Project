# HOW TO RUN DOTECH APPLICATIONS

This guide explains how to install, run, and test both DOTECH applications (**Patient App** and **Doctor & Admin App**) on your Android phone or laptop.

---

## 1. Ready-to-Install APK Files

Both APKs have been freshly compiled with all fixes and are located in the project root directory:

| Application | File Link | App Name on Phone | Description |
|---|---|---|---|
| **Patient App** | **[DoTech_Patient_App.apk](file:///T:/JENISH/Msc.it/SEMESTER-9/Mobile%20Application%20Software/DoTech_Patient_App.apk)** | **DoTech - Patient** | For patients to browse doctors by specialty, book consultations with dynamic time slots, and view Ahmedabad hospitals. |
| **Doctor & Admin App** | **[DoTech_Doctor_Admin_App.apk](file:///T:/JENISH/Msc.it/SEMESTER-9/Mobile%20Application%20Software/DoTech_Doctor_Admin_App.apk)** | **DoTech - Doctor & Admin** | For Doctors, Hospitals, and **Platform Administrators** to approve doctor registrations and manage operations. |

---

## 2. Distinct Portals & Automatic Smart Redirection

To eliminate any confusion when installing both apps on the same phone:

1. **Distinct Launcher Names**:
   - The Patient app icon is labeled **`DoTech - Patient`**.
   - The Doctor & Admin app icon is labeled **`DoTech - Doctor & Admin`**.
2. **On-Screen Portal Labels**:
   - The Patient sign-in screen clearly says: **`Patient Portal`**.
   - The Doctor & Admin sign-in screen clearly says: **`Doctor & Admin Portal`**.
3. **Automatic Cross-App Switching**:
   - If you ever enter an **Admin** or **Doctor** account into the Patient app, the app detects your account and **automatically launches the Doctor & Admin app** for you!
   - If you ever enter a **Patient** account into the Doctor & Admin app, the app detects your account and **automatically launches the Patient app** for you!
   - You will never encounter a dead-end error again.

---

## 3. Working Login Credentials

### A. Admin Accounts (Use in **DoTech - Doctor & Admin**)
| Admin Name | Email | Password | Role |
|---|---|---|---|
| **Arvind Pokiya** | `pokiyaarvindbhai@gmail.com` | `Jenish@123` | **Platform Administrator** |
| **Jensi Pokiya** | `admin@dotech.com` | `Admin@123` | **Platform Administrator** |
| **DoTech Admin** | `consultation296@gmail.com` | `Admin@123` | **Platform Administrator** |

* **Admin Capabilities**:
  - **New Request**: View pending doctor registrations.
  - Review submitted medical degree name and uploaded degree certificate.
  - Tap **Approve** to immediately verify doctors (changes `status: "0"` to `status: "1"`).
  - View registered doctors, hospitals, patients, appointments, and consultations.

### B. Patient Account (Use in **DoTech - Patient**)
| Name | Email | Password | Role |
|---|---|---|---|
| **Jenish Pokiya** | `2004jenishpokiya@gmail.com` | `Jenish@2784` | **Patient** |

* **Patient Capabilities**:
  - Browse doctors by category (Cardiologist, Dentist, Dermatologist, etc.).
  - Select doctor (e.g. Dr. Jensi Pokiya) and book appointment.
  - **Dynamic Time Slots**: 9 consultation slots (`09:00 AM` to `06:00 PM`) are automatically generated and saved for any date you pick!
  - Browse KD Hospital Ahmedabad and Apollo Hospital Ahmedabad.

### C. Pre-Approved Doctor & Tested Ahmedabad Hospitals
| Name | Email | Password | Role / Location |
|---|---|---|---|
| **Dr. Jensi Pokiya** | `jensipokiya@gmail.com` | `Jenish@2784` | Doctor (Cardiologist, Ahmedabad) |
| **KD Hospital Ahmedabad** | `kdhospital.ahmedabad@gmail.com` | `Jenish@2784` | S.G. Highway, Vaishnodevi Circle, Ahmedabad |
| **Apollo Hospital Ahmedabad** | `apollo.ahmedabad@gmail.com` | `Jenish@2784` | Bhat GIDC Estate, Gandhinagar - Ahmedabad |

---

## 4. Quick Launch Scripts (One-Click)

Inside `T:\JENISH\Msc.it\SEMESTER-9\Mobile Application Software`:

| Script | Purpose |
|---|---|
| **[run_patient_app.bat](file:///T:/JENISH/Msc.it/SEMESTER-9/Mobile%20Application%20Software/run_patient_app.bat)** | **1-Click Runner for Patient App**: Connect your phone via USB, double-click this script, and it installs and launches the Patient App. |
| **[run_doctor_app.bat](file:///T:/JENISH/Msc.it/SEMESTER-9/Mobile%20Application%20Software/run_doctor_app.bat)** | **1-Click Runner for Doctor & Admin App**: Installs and launches the Doctor & Admin App. |
| **[create_admin_user.bat](file:///T:/JENISH/Msc.it/SEMESTER-9/Mobile%20Application%20Software/create_admin_user.bat)** | **1-Click Admin Creator**: Prompts for Name, Email, and Password, and registers a fully-verified Admin account instantly in Firebase. |
| **[build_all.bat](file:///T:/JENISH/Msc.it/SEMESTER-9/Mobile%20Application%20Software/build_all.bat)** | Recompiles both APKs from source. |

---

## 5. Clean Workspace Structure
```
Mobile Application Software/
├── DoTech_Patient_App.apk         # Ready-to-install Patient APK
├── DoTech_Doctor_Admin_App.apk    # Ready-to-install Doctor & Admin APK
├── create_admin_user.bat          # 1-Click script to create new Admin users
├── create_admin.py                # Python backend for admin user creation
├── run_patient_app.bat            # 1-Click launcher for Patient App
├── run_doctor_app.bat             # 1-Click launcher for Doctor App
├── run_dotech.bat                 # Interactive management dashboard
├── build_all.bat                  # Clean build script for both apps
├── HOW_TO_RUN.md                  # Complete documentation and user manual
├── DoTechUser/                    # Patient Android App source project
├── DoctorAppUi/                   # Doctor, Hospital & Admin App source project
└── _archive_old_milestones/       # Archived old milestones & zip backups
```
