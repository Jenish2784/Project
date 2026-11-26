# DoTech - Doctor & Patient Healthcare Appointment System

An integrated mobile healthcare platform consisting of two Android applications designed to streamline doctor appointments, hospital consultations, video calls, and administrative management.

---

## 📱 Applications Overview

### 1. **DoTech - Patient App** (`DoTechUser`)
- **Package Name**: `com.example.dotechuser`
- **Key Features**:
  - Doctor and hospital search and filtering by specialty and category.
  - Interactive appointment booking with dynamic date and time slot selection.
  - In-app messaging and real-time chat with doctors.
  - Telemedicine video consultations powered by VideoSDK.
  - Patient health history, past appointment records, and doctor ratings/reviews.

### 2. **DoTech - Doctor & Admin Portal** (`DoctorAppUi`)
- **Package Name**: `com.example.doctorappui`
- **Key Features**:
  - **Doctor Role**: View upcoming consultations, accept/complete appointments, prescribe medication, and video chat with patients.
  - **Hospital Role**: Manage department doctors, schedule availability, and monitor hospital appointments.
  - **Super Admin Role**: Verify and approve doctor/hospital registrations, review degree certificates, manage users, and audit platform activity.

---

## 🛠️ Technology Stack

- **Platform**: Android (Native Java, Minimum SDK: 24, Target SDK: 34)
- **Database**: Google Firebase Realtime Database (`dotech-7a869-default-rtdb`)
- **Authentication**: Firebase Authentication (Email & Password)
- **Cloud Storage**: Firebase Cloud Storage for profile photos and degree verification
- **Video Calling**: VideoSDK WebRTC
- **Build System**: Gradle 8.2+ / AGP 8.2+

---

## 📁 Repository Structure

```
├── DoTechUser/                          # Patient Application Android Source Code
│   ├── app/src/main/java/              # Java source files
│   └── app/src/main/res/               # Layouts, XML drawables, strings
├── DoctorAppUi/                         # Doctor & Admin Android Source Code
│   └── DoctorAppUi/app/src/main/       # Java source and resources
├── DoTech_Doctor_Admin_App.apk         # Pre-built APK for Doctor & Admin
├── DoTech_Patient_App.apk              # Pre-built APK for Patient
├── HOW_TO_RUN.md                       # Comprehensive guide to compile and run
├── PROJECT_DETAILS.md                  # Architecture, schema, and API documentation
├── build_all.bat                       # Automated one-click build script
├── run_patient_app.bat                 # One-click patient app launcher
├── run_doctor_app.bat                  # One-click doctor app launcher
└── dotech-23012-default-rtdb-export.json # Seed database export backup
```

---

## 🚀 Quick Start Guide

### Prerequisites
1. **Android Studio** (Hedgehog / Iguana / Jellyfish or newer)
2. **Java Development Kit**: JDK 17 or JDK 21
3. **Android Device or Emulator** with USB Debugging enabled

### Build using Terminal
```cmd
# Build both APKs simultaneously
build_all.bat
```

### Install directly to connected device
```cmd
# Install Patient App
adb install -r DoTech_Patient_App.apk

# Install Doctor & Admin App
adb install -r DoTech_Doctor_Admin_App.apk
```

---

## 🔐 Tested Credentials

| Role | Application | Email | Password | Access Level |
| :--- | :--- | :--- | :--- | :--- |
| **Super Admin** | Doctor & Admin App | `pokiyaarvindbhai@gmail.com` | `Jenish@123` | Full Admin Dashboard |
| **Admin** | Doctor & Admin App | `admin@dotech.com` | `Admin@123` | Platform Admin |
| **Doctor** | Doctor & Admin App | `jensipokiya@gmail.com` | `Jenish@2784` | Doctor Dashboard |
| **Patient** | Patient App | `2004jenishpokiya@gmail.com` | `Jenish@2784` | Patient Appointments |

---

## 👨‍💻 Author
- **Jenish Pokiya** - [GitHub Profile](https://github.com/Jenish2784)
- M.Sc. (Information Technology) - Semester 9
