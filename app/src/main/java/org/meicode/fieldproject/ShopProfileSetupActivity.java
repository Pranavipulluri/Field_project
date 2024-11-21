package org.meicode.fieldproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class ShopProfileSetupActivity extends AppCompatActivity {

    private EditText etShopName, etPhoneNumber, etShopAddress, etShopDescription;
    private Button btnUploadBanner, btnSubmitProfile;
    private Uri bannerUri;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private StorageReference storageReference;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_profile);

        // Initialize Firebase Firestore and Storage
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        // Initialize UI elements
        etShopName = findViewById(R.id.editTextShopName);
        etShopAddress = findViewById(R.id.editTextShopAddress);
        etShopDescription = findViewById(R.id.editTextShopAddress);
        btnUploadBanner = findViewById(R.id.imageViewBanner);
        btnSubmitProfile = findViewById(R.id.btnSaveProfile);

        // Upload Banner Button Click
        btnUploadBanner.setOnClickListener(v -> openImagePicker());

        // Submit Button Click
        btnSubmitProfile.setOnClickListener(v -> submitProfile());
    }

    private void openImagePicker() {
        // Open Image Picker to choose banner
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1001);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == RESULT_OK && data != null) {
            bannerUri = data.getData();
        }
    }

    private void submitProfile() {
        String shopName = etShopName.getText().toString().trim();
        String phoneNumber = etPhoneNumber.getText().toString().trim();
        String shopAddress = etShopAddress.getText().toString().trim();
        String shopDescription = etShopDescription.getText().toString().trim();

        if (shopName.isEmpty() || phoneNumber.isEmpty() || shopAddress.isEmpty() || shopDescription.isEmpty() || bannerUri == null) {
            Toast.makeText(this, "Please fill all fields and upload a banner", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save banner image to Firebase Storage
        String bannerPath = "shop_banners/" + auth.getCurrentUser().getUid() + ".jpg";
        storageReference.child(bannerPath).putFile(bannerUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Get download URL of the banner
                    storageReference.child(bannerPath).getDownloadUrl().addOnSuccessListener(uri -> {
                        // Save shop details to Firestore
                        saveShopDetails(shopName, phoneNumber, shopAddress, shopDescription, uri.toString());
                    });
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to upload banner", Toast.LENGTH_SHORT).show());
    }

    private void saveShopDetails(String name, String phone, String address, String description, String bannerUrl) {
        // Create shop data object
        Map<String, Object> shopData = new HashMap<>();
        shopData.put("shopName", name);
        shopData.put("phoneNumber", phone);
        shopData.put("address", address);
        shopData.put("description", description);
        shopData.put("bannerUrl", bannerUrl);

        // Save to Firestore
        db.collection("shops").document(auth.getCurrentUser().getUid())
                .set(shopData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Shop Profile Created", Toast.LENGTH_SHORT).show();
                    // Redirect to Seller Profile Activity
                    Intent intent = new Intent(ShopProfileSetupActivity.this, ShopProfileSetupActivity.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to create shop profile", Toast.LENGTH_SHORT).show());
    }
}
