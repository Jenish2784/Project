# DOTECH — Online Doctor Consultation & Healthcare Management System

## 1. Project Overview

**DOTECH** is a full-featured telehealth mobile application ecosystem developed for Android. It bridges the gap between healthcare seekers (patients) and healthcare providers (independent doctors and multi-department hospitals), enabling virtual medical consultations, appointment scheduling, real-time messaging, video calls, and digital payments.

### Academic Context
- **Institution**: Apollo Institute of Engineering and Technology / Gujarat Technological University (GTU)
- **Subject**: Mobile Application Software / Project-1
- **Project Members**:
  - Jenish Chovatiya (Enrollment: 204550307002)
  - Kelvin Bodar (Enrollment: 204550307008)
  - Chintan Patel (Enrollment: 204550307009)
  - Jevin Thakar (Enrollment: 204550307020)
  - Meet Panchal (Enrollment: 204550307043)
- **Internal Guide**: Prof. Richa Bhadauria
- **HOD**: Mr. Prashant Patel

---

## 2. System Architecture & Folder Organization

The system is split into two native Android applications sharing a unified Firebase cloud backend:

```
Mobile Application Software/
├── DoTechUser/                  # [Active] Patient / User Application
│   ├── app/                     # Package: com.example.dotechuser
│   ├── gradle/
│   ├── build.gradle
│   └── local.properties
│
├── DoctorAppUi/
│   └── DoctorAppUi/             # [Active] Doctor, Hospital & System Admin Application
│       ├── app/                 # Package: com.example.doctorappui
│       ├── gradle/
│       ├── build.gradle
│       └── local.properties
│
├── DoTechUserProject/           # [Archive] Earlier milestone of Patient app (SDK 32)
├── DoTech_1.0/                  # [Archive] Earlier milestone of Doctor app
├── Zip folder/                  # Backup archives (.zip)
├── run_dotech.bat               # Interactive CLI launcher & build script
├── run_dotech.ps1               # PowerShell launcher & build script
├── setup_environment.bat        # Automated environment configuration script
└── PROJECT_DETAILS.md           # This comprehensive guide
```

---

## 3. Application Modules & Role Features

### A. Patient App (`com.example.dotechuser`)
- **Authentication**: Sign up, sign in, email verification, password reset.
- **Home Dashboard (`HomeFragment`)**:
  - Medical categories / specialties carousel (Cardiology, Dermatology, Orthopedics, Pediatrics, etc.).
  - Top rated doctors and registered hospital lists.
  - Recent consultation shortcuts.
- **Doctor & Hospital Discovery (`DoctorFragment`, `HospitalFragment`)**:
  - Search by city, state, and specialty.
  - Detailed doctor profiles: qualifications, experience, consultation fees (voice/video/chat), hospital affiliation, patient reviews and ratings.
- **Appointment Booking & Slot Picker (`AppointmentTimeandDateActivity`)**:
  - Interactive calendar week view (`CalendarAdapter`).
  - Dynamic time slot selection (`TimeSlote` & `SloteAdapter`).
  - Health problem description and report/prescription image upload to Firebase Storage.
- **Payment Gateway Integration (`PaymentActivity`)**:
  - **Razorpay** checkout integration (`com.razorpay:checkout:1.6.36`).
  - Secure transaction processing with test key `rzp_test_mpXNBLeM9ZOuLN`.
  - Automatic appointment confirmation and receipt generation upon payment success.
- **Live Video Consultation (`MeetingActivity`)**:
  - Powered by **VideoSDK WebRTC** (`live.videosdk:rtc-android-sdk`).
  - Room token & meeting ID exchange for encrypted doctor-patient video consultations.
  - Mic/camera toggling and multi-participant stream management.
- **Real-Time In-App Chat (`ChatActivity`)**:
  - Instant text communication with the consulting physician.
- **Post-Consultation Review (`DoctorRatingActivity`)**:
  - Star rating and textual feedback stored in Firebase.

---

### B. Doctor / Hospital / Admin App (`com.example.doctorappui`)
A role-based application that dynamically launches into specific interfaces based on account role:

#### 1. Doctor Portal (`com.example.doctorappui.doctor`)
- **Registration & Verification**:
  - Doctor submits registration with medical degree certificates, license, specialization category, consultation fees, and bank account details (IFSC, Account Number).
  - Status starts at `"0"` (Pending Admin Review).
  - Once verified by Admin (`status = "1"`), the doctor receives full dashboard access.
- **Schedule Management (`ManageTimeSlotActivity`, `ScheduleFragment`)**:
  - Configure daily available consultation hours and time slots.
- **Appointment Queue & Patient Management (`PatientListActivity`, `AppointReportActivity`)**:
  - View upcoming, completed, and pending consultations.
  - Access patient symptoms, uploaded medical records, and previous consultation history.
- **Teleconsultation (`MeetingActivity`, `ChatActivity`)**:
  - Launch or join video consultations with patients.
  - Messaging and medical advice delivery.
- **Earnings & Bank Management (`BankAccountActivity`)**:
  - View consultation earnings and registered banking credentials.

#### 2. Hospital Portal (`com.example.doctorappui.hospital`)
- Hospital registration and verification.
- **Department Management (`DepartmentActivity`)**: Add and manage hospital wings/departments (e.g., ICU, OPD, Surgery, ENT).
- **Doctor Allocation (`AddDepartmentDoctorActivity`, `DepartmentDoctorActivity`)**: Assign affiliated doctors to specific hospital departments.
- View hospital-wide appointment analytics and patient logs.

#### 3. System Admin Portal (`com.example.doctorappui.admin`)
- Accessible only with `type == "admin"` credentials.
- **Admin Dashboard (`AdminMainActivity`)**:
  - Real-time KPI counter widgets (Pending Approvals, Registered Doctors, Registered Hospitals, Total Users).
  - Platform emergency shutdown / maintenance mode switch.
- **Doctor & Hospital Approval Engine (`NewRequestActivity`, `DoctorDataActivity`)**:
  - Review submitted medical licenses and certificates.
  - One-click actions: Approve (`status = "1"`), Reject (`status = "reject"`), or Block (`status = "block"`).
- **Specialty Category Management (`AddCategory`)**: Add, update, or remove medical specialties with category icons.
- **User & Payment Oversight (`RegisteredUserActivity`, `AdminPaymentActivity`)**: View total transactions, user queries (`AdminContactUsActivity`), and ratings (`RatingActivity`).

---

## 4. Backend & Cloud Integration

- **Firebase Realtime Database**: `https://dotech-23012-default-rtdb.firebaseio.com`
  - Core database nodes:
    - `/doctor and hospital`: Physician and facility records, credentials, status flags (`0` pending, `1` approved, `reject`, `block`).
    - `/users`: Registered patient profiles, contact info, personal health stats.
    - `/category`: Medical specializations.
    - `/appointment`: Booking records, dates, time slots.
    - `/consultation`: Transaction records, symptoms, medical notes.
    - `/rating`: Ratings and written feedback.
    - `/contact_us`: Inquiries and customer support messages.
- **Firebase Authentication**: Email and password authentication with role verification.
- **Firebase Cloud Storage**: `dotech-23012.appspot.com` (Medical certificates, profile pictures, patient report uploads).
- **VideoSDK RTC**: Real-time interactive audio/video calling.
- **Razorpay**: In-app checkout for consultation fees.

---

## 5. Development & Build Specifications

| Component | Doctor App (`DoctorAppUi`) | Patient App (`DoTechUser`) |
|---|---|---|
| **Namespace / App ID** | `com.example.doctorappui` | `com.example.dotechuser` |
| **Compile SDK** | 32 (Android 12L) | 34 (Android 14) |
| **Target SDK** | 32 | 34 |
| **Min SDK** | 21 (Android 5.0 Lollipop) | 21 (Android 5.0 Lollipop) |
| **Android Gradle Plugin** | 8.2.1 | 8.3.1 |
| **Gradle Wrapper** | 8.2 | 8.4 |
| **Required Java Version** | **JDK 17 or JDK 21** (`C:\Program Files\Java\jdk-21`) | **JDK 17 or JDK 21** |
| **View Binding** | Enabled | Enabled |

---

## 6. How to Run the Applications

### Method 1: Using the Automated Run Script (Recommended)
Double-click `run_dotech.bat` in the project root or run from PowerShell:
```powershell
.\run_dotech.bat
```
The script will present an interactive menu:
1. **Build Patient App APK (`DoTechUser`)**
2. **Build Doctor & Admin App APK (`DoctorAppUi`)**
3. **Build Both Apps**
4. **Install & Launch Patient App on Connected Device / Emulator**
5. **Install & Launch Doctor App on Connected Device / Emulator**
6. **Verify Environment & Repair `local.properties`**
7. **Clean Build Caches**

### Method 2: Opening in Android Studio
1. Launch **Android Studio**.
2. Select **Open** and choose either:
   - `T:\JENISH\Msc.it\SEMESTER-9\Mobile Application Software\DoTechUser` (for Patient app)
   - `T:\JENISH\Msc.it\SEMESTER-9\Mobile Application Software\DoctorAppUi\DoctorAppUi` (for Doctor app)
3. In Android Studio settings:
   - Go to **File > Settings > Build, Execution, Deployment > Build Tools > Gradle**.
   - Ensure **Gradle JDK** is set to **JDK 17 or JDK 21** (`C:\Program Files\Java\jdk-21`).
4. Wait for Gradle Sync to complete.
5. Click **Run ('app')** with your preferred Android emulator or USB-connected device.
