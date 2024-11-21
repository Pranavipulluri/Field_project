package org.meicode.fieldproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddWorkActivity extends AppCompatActivity {

    private EditText titleEditText, descriptionEditText, priceEditText;
    private ImageView workImageView;
    private Uri imageUri;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference worksDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_work);

        // Initialize Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        worksDatabaseReference = FirebaseDatabase.getInstance().getReference("Users")
                .child(firebaseAuth.getCurrentUser().getUid()).child("works");

        titleEditText = findViewById(R.id.titleEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        priceEditText = findViewById(R.id.priceEditText); // Initialize price field
        workImageView = findViewById(R.id.workImageView);

        progressDialog = new ProgressDialog(this);

        // OnClick listener to select image
        workImageView.setOnClickListener(v -> selectImage());

        // OnClick listener for upload button
        findViewById(R.id.uploadButton).setOnClickListener(v -> uploadWork());
    }

    // Method to select image from gallery
    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            workImageView.setImageURI(imageUri);
        }
    }

    // Method to upload work (image, title, description, price)
    private void uploadWork() {
        String title = titleEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String priceString = priceEditText.getText().toString().trim();

        // Validate inputs
        if (title.isEmpty() || description.isEmpty() || imageUri == null || priceString.isEmpty()) {
            Toast.makeText(AddWorkActivity.this, "All fields must be filled", Toast.LENGTH_SHORT).show();
            return;
        }

        // Parse price
        double price;
        try {
            price = Double.parseDouble(priceString);
        } catch (NumberFormatException e) {
            Toast.makeText(AddWorkActivity.this, "Invalid price format", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Uploading...");
        progressDialog.show();

        // Upload work data to Firebase (including price)
        String workId = worksDatabaseReference.push().getKey();
        if (workId != null) {
            Work newWork = new Work(title, description, price, imageUri.toString());
            worksDatabaseReference.child(workId).setValue(newWork)
                    .addOnCompleteListener(task -> {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            Toast.makeText(AddWorkActivity.this, "Work uploaded successfully", Toast.LENGTH_SHORT).show();
                            finish(); // Close the activity after successful upload
                        } else {
                            Toast.makeText(AddWorkActivity.this, "Upload failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
