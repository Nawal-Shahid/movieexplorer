# Firebase Setup Instructions

## ⚠️ **IMPORTANT: Fix Firebase Configuration**

Your current Firebase project has the wrong package name configuration. Follow these steps to fix it:

## 1. Create New Firebase Project

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Click "Create a project" or "Add project"
3. Enter project name: `movieexplorer` (or any name you prefer)
4. Enable Google Analytics (optional)
5. Click "Create project"

## 2. Add Android App to Firebase Project

1. In your Firebase project, click "Add app" and select Android
2. **CRITICAL**: Enter your package name: `com.example.movieexplorer`
3. Enter app nickname: `Movie Explorer`
4. Enter SHA-1 fingerprint (optional for now)
5. Click "Register app"

## 3. Download Configuration File

1. Download the `google-services.json` file
2. **REPLACE** the current `google-services.json` in the `app/` directory with the downloaded file
3. **VERIFY** the package name in the JSON file matches `com.example.movieexplorer`

## 4. Enable Authentication

1. In Firebase Console, go to "Authentication" in the left sidebar
2. Click "Get started"
3. Go to "Sign-in method" tab
4. Enable "Email/Password" provider
5. Click "Save"

## 5. Enable Firestore Database

1. In Firebase Console, go to "Firestore Database" in the left sidebar
2. Click "Create database"
3. Choose "Start in test mode" (for development)
4. Select a location for your database
5. Click "Done"

## 6. Configure Firestore Security Rules

1. In Firestore Database, go to "Rules" tab
2. Replace the default rules with these:

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    // Allow users to read and write their own user documents
    match /users/{userId} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
    }
    
    // Allow authenticated users to read and write favorites
    match /favorites/{document} {
      allow read, write: if request.auth != null;
    }
  }
}
```

3. Click "Publish"

## 6. Update API Key (if needed)

If you need to update the API key in the code, it's located in:
- `app/src/main/java/com/example/movieexplorer/api/ApiConstants.java`

## 7. Build and Run

1. Sync your project with Gradle files
2. Build and run the app
3. Test the login/signup functionality

## Features Implemented

✅ **Firebase Authentication**
- Email/Password login and signup
- User session management
- Automatic login persistence

✅ **Firebase Firestore**
- User profile storage
- User data persistence

✅ **Authentication Guards**
- Favorites only work when logged in
- Login prompt for unauthenticated users
- Automatic redirect to login screen

✅ **Enhanced Profile**
- User information display
- Logout functionality
- Better UI design

✅ **Fixed Search**
- Improved search stability
- Better error handling
- Loading states

## Notes

- The app will automatically redirect to login if user is not authenticated
- Favorites feature requires authentication
- User data is stored in Firestore for persistence
- All authentication is handled through Firebase Auth
