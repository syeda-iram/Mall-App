package com.example.madproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    EditText etName, etEmail, etPassword, etConfirmPassword;
    Button btnSignup;
    TextView loginBtn;
    FirebaseAuth auth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize Firebase
        try {
            auth = FirebaseAuth.getInstance();
            databaseReference = FirebaseDatabase.getInstance().getReference();
            Log.d("FIREBASE_INIT", "Firebase initialized successfully");
        } catch (Exception e) {
            Log.e("FIREBASE_INIT", "Failed to initialize Firebase", e);
            Toast.makeText(this, "Firebase initialization failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnSignup = findViewById(R.id.btnSignup);
        loginBtn = findViewById(R.id.login_btn);

        btnSignup.setOnClickListener(v -> registerUser());

        loginBtn.setOnClickListener(v -> {
            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void registerUser() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // Enhanced validation
        if (TextUtils.isEmpty(name)) {
            etName.setError("Name required");
            etName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email required");
            etEmail.requestFocus();
            return;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Enter a valid email address");
            etEmail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password required");
            etPassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            etPassword.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(confirmPassword)) {
            etConfirmPassword.setError("Confirm Password required");
            etConfirmPassword.requestFocus();
            return;
        }
        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            etConfirmPassword.requestFocus();
            return;
        }

        Log.d("SIGNUP_ATTEMPT", "Email: " + email + ", Password length: " + password.length());

        // Check if Firebase Auth is available
        if (auth == null) {
            Toast.makeText(this, "Firebase Auth not available. Please restart app.", Toast.LENGTH_LONG).show();
            return;
        }

        // Show loading
        btnSignup.setEnabled(false);
        btnSignup.setText("Creating Account...");

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    btnSignup.setEnabled(true);
                    btnSignup.setText("Sign Up");

                    if (task.isSuccessful()) {
                        Log.d("SIGNUP_SUCCESS", "User created successfully");
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            saveUserToRealtimeDB(user.getUid(), name, email);
                        } else {
                            Toast.makeText(this, "Registration failed: User is null", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e("SIGNUP_FAILED", "Error: ", task.getException());

                        // Detailed error handling
                        String errorMessage = "Registration Failed: ";
                        if (task.getException() instanceof FirebaseAuthException) {
                            FirebaseAuthException e = (FirebaseAuthException) task.getException();
                            String errorCode = e.getErrorCode();

                            switch (errorCode) {
                                case "ERROR_INVALID_EMAIL":
                                    errorMessage = "Invalid email address format";
                                    etEmail.setError("Invalid email format");
                                    etEmail.requestFocus();
                                    break;
                                case "ERROR_EMAIL_ALREADY_IN_USE":
                                    errorMessage = "Email already registered";
                                    etEmail.setError("Email already exists");
                                    etEmail.requestFocus();
                                    break;
                                case "ERROR_WEAK_PASSWORD":
                                    errorMessage = "Password is too weak";
                                    etPassword.setError("Password is too weak");
                                    etPassword.requestFocus();
                                    break;
                                case "ERROR_NETWORK_REQUEST_FAILED":
                                    errorMessage = "Network error. Check your internet connection";
                                    break;
                                default:
                                    errorMessage = "Error: " + e.getMessage();
                            }
                        } else {
                            errorMessage = "Error: " + task.getException().getMessage();
                        }

                        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void saveUserToRealtimeDB(String userId, String name, String email) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", name);
        userData.put("email", email);
        userData.put("createdAt", System.currentTimeMillis());

        databaseReference.child("users").child(userId)
                .setValue(userData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Log.e("SAVE_TO_DB", "Failed to save user data", e);
                    Toast.makeText(this, "Registration successful, but failed to save profile data", Toast.LENGTH_LONG).show();
                    // Still navigate to MainActivity
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                });
    }
}