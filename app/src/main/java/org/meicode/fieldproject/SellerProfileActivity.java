package org.meicode.fieldproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SellerProfileActivity extends AppCompatActivity {

    private ImageView shopBanner, locationIcon, chatIcon;
    private TextView shopName, shopDescription, shopAddress, shopPhoneNumber;
    private RecyclerView recyclerView;
    private WorksAdapter worksAdapter;
    private List<Work> worksList;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference userDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        // Initialize Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        userDatabaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // Initialize Views
        shopBanner = findViewById(R.id.shopBanner);
        locationIcon = findViewById(R.id.locationIcon);
        chatIcon = findViewById(R.id.chatIcon);
        shopName = findViewById(R.id.shopName);
        shopDescription = findViewById(R.id.shopDescription);
        shopAddress = findViewById(R.id.shopAddress);
        shopPhoneNumber = findViewById(R.id.shopPhoneNumber);
        recyclerView = findViewById(R.id.previousDesignsRecycler);

        worksList = new ArrayList<>();
        worksAdapter = new WorksAdapter(worksList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(worksAdapter);

        loadShopDetails();
        loadWorks();

        // OnClick for location icon (map)


        // OnClick for chat icon

    }

    // Load shop details (name, description, banner, address, phone)
    private void loadShopDetails() {
        String userId = firebaseAuth.getCurrentUser().getUid();

        userDatabaseReference.child(userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot dataSnapshot = task.getResult();
                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.child("shopName").getValue(String.class);
                    String description = dataSnapshot.child("shopDescription").getValue(String.class);
                    String address = dataSnapshot.child("shopAddress").getValue(String.class);
                    String phone = dataSnapshot.child("shopPhoneNumber").getValue(String.class);
                    String bannerUrl = dataSnapshot.child("shopBannerUrl").getValue(String.class);

                    shopName.setText(name);
                    shopDescription.setText(description);
                    shopAddress.setText(address);
                    shopPhoneNumber.setText(phone);

                    // Load banner image
                    Picasso.get().load(bannerUrl).into(shopBanner);
                }
            }
        });
    }

    // Load uploaded works (from Firebase)
    private void loadWorks() {
        String userId = firebaseAuth.getCurrentUser().getUid();
        DatabaseReference worksRef = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("works");

        worksRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot dataSnapshot = task.getResult();
                if (dataSnapshot.exists()) {
                    worksList.clear();
                    for (DataSnapshot workSnapshot : dataSnapshot.getChildren()) {
                        Work work = workSnapshot.getValue(Work.class);
                        worksList.add(work);
                    }
                    worksAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    // Navigate to the Map Activity


    // Navigate to Chat List


    // OnClick for add work button (FloatingActionButton)
    public void addWork(View view) {
        Intent intent = new Intent(SellerProfileActivity.this, AddWorkActivity.class);
        startActivity(intent);
    }
}
