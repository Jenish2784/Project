# DoTech Healthcare Platform — Complete Session & Chat Summary

**Project:** DoTech Telehealth & Healthcare Management System  
**Date:** 27 November 2026  
**Repository:** [https://github.com/Jenish2784/Project](https://github.com/Jenish2784/Project)  
**Academic Submission Email:** `snehbhattksfaculty@gmail.com`  

---

## 1. Academic & Team Information

- **Institution:** SEMESTER-9 / Mobile Application Software
- **Group ID:** 020
- **Team Members:**
  - **5006** — Devansh Chauhan
  - **5069** — Disha Parsaniya
  - **5035** — Jenish Pokiya
- **Project Report Reference:** `T:\JENISH\Msc.it\SEMESTER-9\Mobile Application Software\DOTECH_Group_id_020.pdf`
- **Faculty Guide:** Prof. Sneh Bhatt (`snehbhattksfaculty@gmail.com`)

---

## 2. Overview & Problem Resolutions

This session addressed critical architectural, functional, design, and code hygiene improvements across the two connected Android applications:
1. **`DoTechUser` (Patient App):** Native Android app for patients to browse doctors/hospitals, book appointments, make digital payments, consult via video, and view PDF prescriptions.
2. **`DoctorAppUi` (Doctor & Admin App):** Native Android app for independent doctors, hospital departments, and administrators to review patient queues, conduct video consultations, manage time slots, and verify registrations.

### Initial Issues Resolved:
1. **"Account not found in Doctor/Hospital records" Error:**
   - *Cause:* Users attempting to log into the Doctor/Admin app using Patient credentials (or vice versa).
   - *Resolution:* Added smart cross-collection validation and clear dialog guidance alerting users which app to use for their role.
2. **Broken VideoSDK Consultations:**
   - *Cause:* The obsolete VideoSDK integration had commented-out code, expired tokens, and failed session links.
   - *Resolution:* Replaced with high-performance, hardware-accelerated WebRTC via WebView using synchronized room IDs (`https://meet.jit.si/DoTech_<meetingId>`).
3. **Dead Email & Payment Receipt System (000webhost):**
   - *Cause:* Previous code relied on defunct `000webhostapp.com` PHP backend scripts that failed to deliver confirmation emails.
   - *Resolution:* Replaced with native in-app confirmation (Lottie animation), instant Firebase real-time notifications, and direct Gmail receipt sharing via `Intent.ACTION_SENDTO`.
4. **Missing PDF Prescriptions:**
   - *Resolution:* Built native Android A4 prescription generator (`android.graphics.pdf.PdfDocument`) with hospital/doctor header, patient vitals, diagnosis, and prescription details.

---

## 3. Work Completed by Priority

### Phase 1: High-Priority Deliverables
- **Hardware-Accelerated WebRTC Video Meeting:**
  - Configured `MeetingActivity.java` in both apps with Chrome DevTools permission handling (`onPermissionRequest`) for camera and microphone.
  - Synchronized `meetLink` generation between Doctor (`ConsultDataActivity`) and Patient (`ConsultationDetailsActivity`).
- **In-App Payment Receipts & Gmail Sharing:**
  - Revamped `EmailSendActivity.java` in both apps.
  - Eliminated external web hosting dependencies.
- **Doctor Registration Pending Dialog:**
  - Replaced confusing `warning_alert_dialog` ("You not verified by admin") in `SignUpVerifyActivity.java` with a friendly pending review dialog (`dialog_registration_success.xml`).
- **Admin Doctor Degree Full-Resolution Viewer:**
  - Added full-screen inspection dialog in `DoctorDataActivity.java` allowing administrators to inspect doctor medical degrees before approval.
- **Prescription PDF Generation:**
  - Implemented `PrescriptionPdfHelper.java` and `FileProvider` (`file_paths.xml`) for downloading and printing clinical prescriptions.

### Phase 2: Medium-Priority Deliverables
- **Empty State Illustrations Across All Lists:**
  - Created vector icons (`ic_empty_calendar.xml`, `ic_empty_requests.xml`).
  - Added illustrated empty states to:
    - Patient App: `InCompleteFragment`, `CompleteFragment`, `PaymentFailedFragment`, `AppointmentActivity`.
    - Doctor/Admin App: `NewRequestActivity`, `InCompleteFragment`, `CompleteFragment`.
- **Time-Slot Visual Indicators & Booking Protection:**
  - Color-coded slots in `SloteAdapter.java` and `item_slot.xml`:
    - **Selected:** Solid brand teal (`#11CFC5`), white text, confirmation `✓`.
    - **Available:** Subtle teal outline (`#11CFC5`), dark text (`#107163`).
    - **Booked:** Soft gray (`#F0F2F5`), disabled label `(Booked)`, click-protection alert.
- **Standard Action Bar Navigation:**
  - Added toolbar back arrows (`setDisplayHomeAsUpEnabled(true)` + `onSupportNavigateUp()`) across all secondary activities in both projects.
- **UI Typography Fixes:**
  - Corrected action bar titles (e.g., replaced placeholder `"Server Down!!"` with `"Consultations"`).

### Phase 3: Low-Priority Deliverables
- **Manifest Hygiene & Security:**
  - Cleaned up 350+ lines of redundant `<meta-data android:name="android.app.lib_name" android:value="" />` boilerplate across both manifests.
  - Hardened Android 12+ security by setting `android:exported="false"` on internal activities without intent-filters.
- **Dead Code & Legacy Library Elimination:**
  - Deleted obsolete `SendData.java` files from both projects.
  - Removed dead `Insert` AsyncTask and 000webhost constants from `DoctorDataActivity.java`.
  - Removed deprecated `useLibrary 'org.apache.http.legacy'` from `DoTechUser/app/build.gradle`.
- **Target SDK Modernization:**
  - Upgraded `DoctorAppUi` from old `compileSdk 32` / `targetSdk 32` to modern `compileSdk 34` / `targetSdk 34` (Android 14) matching `DoTechUser`.
- **Resource Hygiene:**
  - Removed leftover `hello_blank_fragment` placeholders from `strings.xml`.

---

## 4. Complete Firebase Login Credentials

Extracted directly from Firebase Realtime Database (`https://dotech-7a869-default-rtdb.firebaseio.com/`):

### A. Admin Accounts (Use in `DoctorAppUi`)
| Name | Email (User ID) | Password | Status | Role |
| :--- | :--- | :--- | :---: | :--- |
| **Jenish Pokiya** | `admin@dotech.com` | `Admin@123` | Active (`1`) | Main Administrator |
| **Jenish_Pokiya** | `pokiyaarvindbhai@gmail.com` | `Jenish@123` | Active (`1`) | Administrator |
| **DoTech Admin** | `consultation296@gmail.com` | `dotech@123` | Active (`1`) | System Administrator |

### B. Doctor Accounts (Use in `DoctorAppUi`)
| Doctor Name | Specialization | Email (User ID) | Password | Status |
| :--- | :--- | :--- | :--- | :---: |
| **Dr. Jensi Pokiya** | Cardiologist | `jensipokiya@gmail.com` | `Jenish@2784` | Verified Active (`1`) |
| **Chintan Patel** | Dentist | `patelchintan8358@gmail.com` | `123456` | Verified Active (`1`) |
| **Jevin** | Dentist | `jevinkivines@gmail.com` | `123456` | Verified Active (`1`) |
| **Hotel (Demo)** | Dentist | `hotel2989@gmail.com` | `123456` | `shutdown` |

### C. Hospital Accounts (Use in `DoctorAppUi`)
| Hospital Name | Specialization | Email (User ID) | Password | Status |
| :--- | :--- | :--- | :--- | :---: |
| **KD Hospital Ahmedabad** | Super Speciality | `kdhospital.ahmedabad@gmail.com` | `Jenish@2784` | Verified Active (`1`) |
| **Apollo Hospital Ahmedabad** | Multi Speciality | `apollo.ahmedabad@gmail.com` | `Jenish@2784` | Verified Active (`1`) |
| **Hospital (Patel)** | General Hospital | `patelchintan1278@gmail.com` | `123456` | Verified Active (`1`) |
| **Jenish Hospital** | General Hospital | `linux1271@gmail.com` | `Test@123` | Verified Active (`1`) |

### D. Patient Accounts (Use in `DoTechUser`)
| Patient Name | Mobile | Email (User ID) | Password | Status |
| :--- | :--- | :--- | :--- | :---: |
| **Jenish Pokiya** | `6355713445` | `2004jenishpokiya@gmail.com` | `Jenish@2784` | Active (`1`) |
| **Kelvin Bodar** | `8866433948` | `bodarkelvin2002@gmail.com` | `123456` | Active (`1`) |
| **KB** | `9188664339` | `kelvinbodar2002@gmail.com` | `123456` | Active (`1`) |

---

## 5. Build Artifacts & Git Commit History

### Generated Application Packages (APKs)
Both APKs are built using `build_all.bat` and copied to workspace root:
- **Patient Application:** `DoTech_Patient_App.apk` (35.2 MB)
- **Doctor & Admin Application:** `DoTech_Doctor_Admin_App.apk` (29.2 MB)

### GitHub Commits Log
- **`5faa163`** — *Implement low priority optimizations: clean manifests, dead code removal, SDK modernization and fresh APKs*
- **`7c97141`** — *Implement medium priority improvements: empty states, slot selectors, navigation and UI polish*
- **`33fe818`** — *Implement high priority improvements: WebRTC video consultation, in-app receipt share, doctor verification feedback, degree viewer dialog, and PDF prescriptions*
- **`810cb99`** — *Add automated build script build_all.bat for one-click compilation of both APKs*
- **`3a25bc2`** — *Initial commit of DoTech Mobile Application Software ecosystem*

---

## 6. Faculty Email Submission Draft

**To:** `snehbhattksfaculty@gmail.com`  
**Subject:** Project Progress Report & GitHub Submission — Group ID: 020 (DoTech Healthcare Platform)  
**Attachments:** `DOTECH_Group_id_020.pdf`, `CHAT_SUMMARY.md`  

```
Respected Sneh Ma'am,

We are submitting our project progress report and source code repository for the DoTech Telehealth Mobile Application Software project (Group ID: 020).

Team Members:
- Devansh Chauhan (Roll No: 5006)
- Disha Parsaniya (Roll No: 5069)
- Jenish Pokiya (Roll No: 5035)

Project Overview:
DoTech is a telehealth ecosystem comprising two synchronized native Android applications (Patient App & Doctor/Admin App) backed by Google Firebase Realtime Database and Cloud Storage.

Key Features & Recent Accomplishments:
1. WebRTC Hardware-Accelerated Video Consultation: Seamless patient-to-doctor virtual calls.
2. In-App Payment Receipts & Gmail Share: Clean confirmation receipt sharing without third-party webhost dependency.
3. Doctor Verification & Degree Inspection: Administrative verification workflow with full-resolution degree inspection dialog.
4. Native PDF Prescription Generation: Clinical A4 prescriptions downloadable and printable via FileProvider.
5. Modern UI Empty States & Color-Coded Time Slots: Selected, available, and booked slot indicators.
6. Target SDK Modernization: Both applications upgraded to target Android 14 (API 34).

GitHub Repository Link:
https://github.com/Jenish2784/Project

Please find attached our comprehensive Project Report PDF (DOTECH_Group_id_020.pdf) and project documentation for your review.

Thank you.

Sincerely,
Jenish Pokiya (5035)
Devansh Chauhan (5006)
Disha Parsaniya (5069)
```
