package com.example.movieexplorer.service;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.movieexplorer.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AuthService {
    private static final String TAG = "AuthService";
    private static AuthService instance;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    private AuthService() {
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
    }

    public static synchronized AuthService getInstance() {
        if (instance == null) {
            instance = new AuthService();
        }
        return instance;
    }

    public interface AuthCallback {
        void onSuccess(User user);
        void onError(String error);
    }

    public boolean isUserLoggedIn() {
        return mAuth.getCurrentUser() != null;
    }

    public User getCurrentUser() {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null) {
            return new User(
                firebaseUser.getUid(),
                firebaseUser.getEmail(),
                firebaseUser.getDisplayName(),
                firebaseUser.getPhotoUrl() != null ? firebaseUser.getPhotoUrl().toString() : null
            );
        }
        return null;
    }

    public void signUp(String email, String password, String displayName, AuthCallback callback) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            // Update display name
                            firebaseUser.updateProfile(
                                new com.google.firebase.auth.UserProfileChangeRequest.Builder()
                                    .setDisplayName(displayName)
                                    .build()
                            );

                            // Create user document in Firestore
                            User user = new User(firebaseUser.getUid(), email, displayName, null);
                            saveUserToFirestore(user, callback);
                        }
                    } else {
                        Log.e(TAG, "Sign up failed", task.getException());
                        callback.onError(task.getException() != null ? 
                            task.getException().getMessage() : "Sign up failed");
                    }
                }
            });
    }

    public void signIn(String email, String password, AuthCallback callback) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            // Update last login time
                            updateLastLoginTime(firebaseUser.getUid());
                            
                            User user = new User(
                                firebaseUser.getUid(),
                                firebaseUser.getEmail(),
                                firebaseUser.getDisplayName(),
                                firebaseUser.getPhotoUrl() != null ? firebaseUser.getPhotoUrl().toString() : null
                            );
                            callback.onSuccess(user);
                        }
                    } else {
                        Log.e(TAG, "Sign in failed", task.getException());
                        callback.onError(task.getException() != null ? 
                            task.getException().getMessage() : "Sign in failed");
                    }
                }
            });
    }

    public void signOut() {
        mAuth.signOut();
    }

    private void saveUserToFirestore(User user, AuthCallback callback) {
        try {
            Map<String, Object> userData = new HashMap<>();
            userData.put("uid", user.getUid());
            userData.put("email", user.getEmail());
            userData.put("displayName", user.getDisplayName());
            userData.put("photoUrl", user.getPhotoUrl());
            userData.put("createdAt", user.getCreatedAt());
            userData.put("lastLoginAt", user.getLastLoginAt());

            mFirestore.collection("users")
                .document(user.getUid())
                .set(userData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            callback.onSuccess(user);
                        } else {
                            Log.e(TAG, "Error saving user to Firestore", task.getException());
                            // Still call success since user is authenticated, just Firestore failed
                            callback.onSuccess(user);
                        }
                    }
                });
        } catch (Exception e) {
            Log.e(TAG, "Exception in saveUserToFirestore", e);
            // Still call success since user is authenticated
            callback.onSuccess(user);
        }
    }

    private void updateLastLoginTime(String uid) {
        try {
            Map<String, Object> updateData = new HashMap<>();
            updateData.put("lastLoginAt", new Date());

            mFirestore.collection("users")
                .document(uid)
                .update(updateData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            Log.e(TAG, "Error updating last login time", task.getException());
                        }
                    }
                });
        } catch (Exception e) {
            Log.e(TAG, "Exception in updateLastLoginTime", e);
        }
    }
}
