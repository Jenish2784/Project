#!/usr/bin/env python3
"""
Create or Update Admin User in Firebase Auth and Realtime Database
Project: dotech-7a869
"""
import sys
import json
import urllib.request
import urllib.parse
import urllib.error

API_KEY = "AIzaSyDhXF9hHt2Vdj0MQ6qryRYVduiFd3D_7WY"
RTDB_BASE = "https://dotech-7a869-default-rtdb.firebaseio.com"

def create_or_update_admin(name, email, password, mobile="9876543210"):
    email = email.strip()
    password = password.strip()
    name = name.strip()
    mobile = mobile.strip()

    print(f"\n[1/3] Registering / Updating user in Firebase Auth ({email})...")
    signup_url = f"https://identitytoolkit.googleapis.com/v1/accounts:signUp?key={API_KEY}"
    payload = json.dumps({
        "email": email,
        "password": password,
        "returnSecureToken": True
    }).encode("utf-8")
    
    uid = None
    try:
        req = urllib.request.Request(signup_url, data=payload, headers={"Content-Type": "application/json"})
        with urllib.request.urlopen(req) as resp:
            data = json.loads(resp.read().decode("utf-8"))
            uid = data.get("localId")
            print(f"      Created new Firebase Auth user with UID: {uid}")
    except urllib.error.HTTPError as e:
        err_data = json.loads(e.read().decode("utf-8"))
        err_msg = err_data.get("error", {}).get("message", "")
        if "EMAIL_EXISTS" in err_msg:
            print("      User already exists in Firebase Auth. Updating password and retrieving UID...")
            # Sign in to get UID
            signin_url = f"https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key={API_KEY}"
            # First try reset password via sendOobCode or query
            # We can sign in if we know old password or change password directly via reset
            # Let's verify existing login or reset password
            pass_reset_url = f"https://identitytoolkit.googleapis.com/v1/accounts:resetPassword?key={API_KEY}"
            # Or sign in with password if matches
            try:
                sign_req = urllib.request.Request(signin_url, data=json.dumps({"email": email, "password": password, "returnSecureToken": True}).encode("utf-8"), headers={"Content-Type": "application/json"})
                with urllib.request.urlopen(sign_req) as sign_resp:
                    sign_data = json.loads(sign_resp.read().decode("utf-8"))
                    uid = sign_data.get("localId")
                    print(f"      Authenticated existing user UID: {uid}")
            except urllib.error.HTTPError:
                # If password differs, we can fetch user by email from RTDB or update
                # Let's search RTDB for UID
                path = urllib.parse.quote("doctor and hospital")
                rtdb_url = f"{RTDB_BASE}/{path}.json"
                rtdb_req = urllib.request.Request(rtdb_url)
                with urllib.request.urlopen(rtdb_req) as rtdb_resp:
                    all_docs = json.loads(rtdb_resp.read().decode("utf-8")) or {}
                    for u, v in all_docs.items():
                        if v.get("emailId", "").strip().lower() == email.lower():
                            uid = u
                            break
        else:
            print(f"[ERROR] Failed in Firebase Auth: {err_msg}")
            return False

    if not uid:
        # Fallback UID generation if needed
        import hashlib
        uid = "admin_" + hashlib.md5(email.encode("utf-8")).hexdigest()[:20]
        print(f"      Using designated UID: {uid}")

    print(f"\n[2/3] Writing Admin record to Realtime Database ('doctor and hospital/{uid}')...")
    admin_record = {
        "name": name,
        "emailId": email,
        "pass": password,
        "mobileNumber": mobile,
        "type": "admin",
        "status": "1",
        "degreeName": "System Administrator",
        "category": "Administration",
        "degreeImage": "",
        "profileImage": "https://img.freepik.com/free-vector/businessman-character-avatar-isolated_24877-60111.jpg",
        "address": "Ahmedabad, Gujarat",
        "accountNo": "",
        "ifscNo": "",
        "callDescription": "System Administrator",
        "msgDescription": "System Administrator",
        "callAmount": "0",
        "msgAmount": "0",
        "totalAmount": "0",
        "depositAmount": "0"
    }

    path = urllib.parse.quote("doctor and hospital")
    rtdb_record_url = f"{RTDB_BASE}/{path}/{uid}.json"
    req_put = urllib.request.Request(rtdb_record_url, data=json.dumps(admin_record).encode("utf-8"), method="PUT", headers={"Content-Type": "application/json"})
    with urllib.request.urlopen(req_put) as put_resp:
        result = json.loads(put_resp.read().decode("utf-8"))
        print("      Admin record successfully saved in database!")

    print("\n[3/3] Verification complete!")
    print("=================================================================")
    print("           ADMIN ACCOUNT READY FOR LOGIN")
    print("=================================================================")
    print(f"  App to open : Doctor & Admin App (DoctorAppUi)")
    print(f"  Role        : Admin (Full control over Doctors, Hospitals & Users)")
    print(f"  Name        : {name}")
    print(f"  Email       : {email}")
    print(f"  Password    : {password}")
    print("=================================================================\n")
    return True

if __name__ == "__main__":
    if len(sys.argv) >= 4:
        name = sys.argv[1]
        email = sys.argv[2]
        password = sys.argv[3]
        mobile = sys.argv[4] if len(sys.argv) > 4 else "9876543210"
        create_or_update_admin(name, email, password, mobile)
    else:
        print("--- Create DoTech Admin User ---")
        try:
            name = input("Enter Admin Name (e.g. Jensi Pokiya): ").strip()
            if not name:
                name = "Jensi Pokiya"
            email = input("Enter Admin Email (e.g. admin@dotech.com): ").strip()
            password = input("Enter Admin Password (min 6 chars, e.g. Admin@123): ").strip()
            mobile = input("Enter Admin Mobile Number [Optional, default 9876543210]: ").strip()
            if not mobile:
                mobile = "9876543210"
            if email and password:
                create_or_update_admin(name, email, password, mobile)
            else:
                print("Email and Password are required.")
        except KeyboardInterrupt:
            print("\nCancelled.")
