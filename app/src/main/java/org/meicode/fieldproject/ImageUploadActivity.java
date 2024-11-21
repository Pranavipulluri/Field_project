package org.meicode.fieldproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ImageUploadActivity extends AppCompatActivity {

    private EditText descriptionInput;
    private Button selectImageButton, uploadImageButton, searchShopsButton;
    private ImageView imagePreview;
    private Uri imageUri;
    private FirebaseStorage firebaseStorage;
    private FirebaseFirestore firestore;
    private StorageReference storageReference;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload);

        descriptionInput = findViewById(R.id.descriptionInput);
        selectImageButton = findViewById(R.id.selectImageButton);
        uploadImageButton = findViewById(R.id.uploadImageButton);
        searchShopsButton = findViewById(R.id.searchShopsButton);
        imagePreview = findViewById(R.id.imagePreview);

        firebaseStorage = FirebaseStorage.getInstance();
        firestore = FirebaseFirestore.getInstance();
        storageReference = firebaseStorage.getReference("uploaded_images");

        selectImageButton.setOnClickListener(v -> openGallery());
        uploadImageButton.setOnClickListener(v -> uploadImageToFirebase());
        searchShopsButton.setOnClickListener(v -> searchShops());

        // Initially disable upload and search buttons
        uploadImageButton.setEnabled(false);
        searchShopsButton.setEnabled(false);
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imagePreview.setImageURI(imageUri);

            // Enable the upload button when an image is selected
            uploadImageButton.setEnabled(true);
        }
    }

    private void uploadImageToFirebase() {
        if (imageUri != null) {
            String description = descriptionInput.getText().toString().trim();
            if (!description.isEmpty()) {
                // Create a unique image name
                String imageName = UUID.randomUUID().toString();
                StorageReference imageRef = storageReference.child(imageName);

                // Upload the image
                imageRef.putFile(imageUri)
                        .addOnSuccessListener(taskSnapshot -> {
                            imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                // Once the image is uploaded, save description and URL to Firestore
                                saveImageDetailsToFirestore(uri.toString(), description);
                            });
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(ImageUploadActivity.this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            } else {
                Toast.makeText(this, "Please enter a description", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveImageDetailsToFirestore(String imageUrl, String description) {
        // Save image and description to Firestore
        Map<String, Object> imageDetails = new HashMap<>();
        imageDetails.put("imageUrl", imageUrl);
        imageDetails.put("description", description);

        firestore.collection("uploads")
                .add(imageDetails)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();

                    // Enable the search button after successful upload
                    searchShopsButton.setEnabled(true);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error uploading image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void searchShops() {
        String searchQuery = descriptionInput.getText().toString().trim();

        if (!searchQuery.isEmpty()) {
            // Perform Firestore search based on the description entered by the user
            firestore.collection("shops")
                    .whereArrayContains("keywords", searchQuery)  // Assuming shops have a 'keywords' field for easy search
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> shopList = queryDocumentSnapshots.getDocuments();
                            displayShops(shopList);
                        } else {
                            Toast.makeText(this, "No shops found", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Error searching shops: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void displayShops(List<DocumentSnapshot> shopList) {
        // Display matching shops in a RecyclerView or other UI components
        // Example: Populate a list of shops found in Firestore
    }
}
