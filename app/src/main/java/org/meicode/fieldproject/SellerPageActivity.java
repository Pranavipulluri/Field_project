package org.meicode.fieldproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

public class SellerPageActivity extends AppCompatActivity {
    private RecyclerView productList;
    private Button messageButton, viewLocationButton;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_page);

        productList = findViewById(R.id.productList);
        messageButton = findViewById(R.id.messageButton);
        viewLocationButton = findViewById(R.id.viewLocationButton);
        firestore = FirebaseFirestore.getInstance();

        loadShopDetails();

        messageButton.setOnClickListener(v -> navigateToMessageScreen());
        viewLocationButton.setOnClickListener(v -> showLocationOnMap());
    }

    private void loadShopDetails() {
        // Fetch shop data from Firestore and display it
    }

    private void navigateToMessageScreen() {
        Intent intent = new Intent(SellerPageActivity.this, ChatActivity.class);
        startActivity(intent);
    }

    private void showLocationOnMap() {
        // Open Google Maps with shop location
    }
}
