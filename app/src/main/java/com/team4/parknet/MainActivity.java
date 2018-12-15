package com.team4.parknet;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private Button mOfferParkingBtn;
    private Button mRentParkingBtn;
    private Button mViewOrdersBtn;

    private static final int LOGIN_RETURN_CODE = 1;
    private static final int OFFER_PARKING_RETURN_CODE = 2;
    private static final int RENT_PARKING_RETURN_CODE = 3;
    private static final int VIEW_ORDERS_RETURN_CODE = 4;
    private Button mLogoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mLogoutButton = findViewById(R.id.logoutButton);

        mOfferParkingBtn = findViewById(R.id.offerParkingBtn);
        mRentParkingBtn = findViewById(R.id.rentParkingBtn);
        mViewOrdersBtn = findViewById(R.id.viewOrdersBtn);

        mOfferParkingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(MainActivity.this, OfferParkingActivity.class);
//                startActivityForResult(i, OFFER_PARKING_RETURN_CODE);
                Intent i = new Intent(MainActivity.this, MapsActivity.class);
                startActivityForResult(i, OFFER_PARKING_RETURN_CODE);
            }
        });

        mRentParkingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent i = new Intent(MainActivity.this, RentParkingActivity.class);
                Intent i = new Intent(MainActivity.this, SearchParkingsActivity.class);
                startActivityForResult(i, RENT_PARKING_RETURN_CODE);
            }
        });

        mViewOrdersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ViewOrdersActivity.class);
                startActivityForResult(i, VIEW_ORDERS_RETURN_CODE);
            }
        });

        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                promptLogin();
            }
        });

        if (mAuth.getCurrentUser() == null) {
            promptLogin();
        }

    }

    private void promptLogin() {
        Intent i = new Intent(MainActivity.this, LoginActivity.class);
        startActivityForResult(i, LOGIN_RETURN_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case LOGIN_RETURN_CODE:
                if (resultCode == RESULT_OK) {
                    afterLogin();
                } else {
                    promptLogin();
                }
        }
    }

    private void afterLogin() {
        Toast.makeText(this, "Welcome " + mAuth.getCurrentUser().getEmail(),Toast.LENGTH_LONG).show();
    }
}
