package org.meicode.fieldproject;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnCustomer, btnSeller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCustomer = findViewById(R.id.btnCustomer);
        btnSeller = findViewById(R.id.btnSeller);

        btnCustomer.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CustomerDashboard.class);
            startActivity(intent);
        });

        btnSeller.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SellerProfileActivity.class);
            startActivity(intent);
        });
    }
}
