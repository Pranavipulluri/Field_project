package org.meicode.fieldproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button customerLoginButton, sellerLoginButton, registerButton;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference userDatabaseReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();
        userDatabaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // Initialize views
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        customerLoginButton = findViewById(R.id.customerLoginButton);
        sellerLoginButton = findViewById(R.id.sellerLoginButton);
        registerButton = findViewById(R.id.registerButton);
        progressBar = findViewById(R.id.progressBar);

        // Check if the user is already logged in
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            checkUserTypeAndRedirect(currentUser.getUid());
        }

        // Set OnClickListener for Customer Login
        customerLoginButton.setOnClickListener(v -> loginUser("customer"));

        // Set OnClickListener for Seller Login
        sellerLoginButton.setOnClickListener(v -> loginUser("seller"));

        // Set OnClickListener for Register Button
        registerButton.setOnClickListener(v -> {
            // Redirect to Registration Activity
            Intent registerIntent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(registerIntent);
        });
    }

    private void loginUser(String userType) {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show progress bar while logging in
        progressBar.setVisibility(View.VISIBLE);

        // Firebase sign in
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            progressBar.setVisibility(View.GONE);

            if (task.isSuccessful()) {
                // Login successful, check user type and navigate accordingly
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    checkUserTypeAndRedirect(user.getUid());
                }
            } else {
                // Login failed
                Toast.makeText(LoginActivity.this, "Login failed. Please check your credentials.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkUserTypeAndRedirect(String userId) {
        // Check in the database to see if the user is a Customer or Seller
        userDatabaseReference.child(userId).child("userType").get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                String userType = task.getResult().getValue(String.class);
                if ("customer".equals(userType)) {
                    // Redirect to Customer Dashboard
                    Intent customerIntent = new Intent(LoginActivity.this, CustomerDashboard.class);
                    startActivity(customerIntent);
                    finish();
                } else if ("seller".equals(userType)) {
                    // Redirect to Seller Profile Setup if not completed, or Seller Dashboard if already setup
                    checkSellerProfileSetup(userId);
                }
            } else {
                Toast.makeText(LoginActivity.this, "Failed to determine user type", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkSellerProfileSetup(String userId) {
        // Check if the seller's profile is set up
        userDatabaseReference.child(userId).child("shopDetails").get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null && task.getResult().hasChildren()) {
                // If shop details exist, go to Seller Dashboard
                Intent sellerIntent = new Intent(LoginActivity.this, SellerProfileActivity.class);
                startActivity(sellerIntent);
            } else {
                // If no shop details, go to Seller Profile Setup Activity
                Intent setupIntent = new Intent(LoginActivity.this, ShopProfileSetupActivity.class);
                startActivity(setupIntent);
            }
            finish();
        });
    }
}
