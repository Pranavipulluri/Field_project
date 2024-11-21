package org.meicode.fieldproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CustomerDashboard extends AppCompatActivity {

    private EditText searchShopsEditText;
    private RecyclerView productsRecyclerView;
    private ImageButton imageUploadButton, messageButton;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private ProductAdapter productsAdapter;
    private List<Product> productList;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dashboard);

        searchShopsEditText = findViewById(R.id.searchShopsEditText);
        productsRecyclerView = findViewById(R.id.productsRecyclerView);
        imageUploadButton = findViewById(R.id.imageUploadButton);
        messageButton = findViewById(R.id.messageButton);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        productList = new ArrayList<>();

        // Initialize RecyclerView and Adapter
        productsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        productsAdapter = new ProductAdapter(productList);
        productsRecyclerView.setAdapter(productsAdapter);

        // Load all products initially
        loadProducts("");

        // Set up Search functionality
        searchShopsEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String searchQuery = charSequence.toString().trim();
                loadProducts(searchQuery);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        // Image Upload Button to navigate to the Image Upload Activity
        imageUploadButton.setOnClickListener(v -> {
            Intent intent = new Intent(CustomerDashboard.this, ImageUploadActivity.class);
            startActivity(intent);
        });

        // Message Button to navigate to the Message Activity
        messageButton.setOnClickListener(v -> {
            Intent intent = new Intent(CustomerDashboard.this, ChatActivity.class);
            startActivity(intent);
        });
    }

    private void loadProducts(String searchQuery) {
        // Get products from Firestore (filter by description or shop name if searchQuery is not empty)
        firestore.collection("products")
                .orderBy("productName")
                .startAt(searchQuery)
                .endAt(searchQuery + "\uf8ff")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        productList.clear();
                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                            Product product = document.toObject(Product.class);
                            productList.add(product);
                        }
                        productsAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(CustomerDashboard.this, "No products found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(CustomerDashboard.this, "Error fetching products", Toast.LENGTH_SHORT).show();
                });
        addMockProducts();
    }
    private void addMockProducts() {
        productList.add(new Product("Product 1", "Category 1", "https://example.com/product1.jpg", "$10"));
        productList.add(new Product("Product 2", "Category 2", "https://example.com/product2.jpg", "$20"));
        productList.add(new Product("Product 3", "Category 3", "https://example.com/product3.jpg", "$30"));
        productList.add(new Product("Product 4", "Category 4", "https://example.com/product4.jpg", "$40"));

        // Notify adapter that data has changed
        productsAdapter.notifyDataSetChanged();
    }
}
