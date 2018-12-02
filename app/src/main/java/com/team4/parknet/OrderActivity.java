package com.team4.parknet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.api.LogDescriptor;

public class OrderActivity extends AppCompatActivity {

    private static final String TAG = "OrderActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        Bundle bundle = getIntent().getExtras();
        String id = bundle.getString("id");
        Log.d(TAG, "onCreate: " + id);
    }
}
