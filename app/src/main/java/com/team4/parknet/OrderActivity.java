package com.team4.parknet;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.LogDescriptor;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.team4.parknet.entities.ParkingLotOffer;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class OrderActivity extends AppCompatActivity {

    private static final String TAG = "OrderActivity";
    private FirebaseFirestore mDb;
    private ParkingLotOffer mParkingLotOffer;
    private TextView mAddress;
    private TextView mPricePerHour;
    private TextView mStartTime;
    private TextView mEndTime;
    private TextView mTotalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        Bundle bundle = getIntent().getExtras();
        String id = bundle.getString("id");

        mDb = FirebaseFirestore.getInstance();

        DocumentReference docRef = mDb.collection("offers").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        mParkingLotOffer = task.getResult().toObject(ParkingLotOffer.class);

                        mAddress = findViewById(R.id.address);
                        mAddress.setText(mParkingLotOffer.getAddress());

                        mPricePerHour = findViewById(R.id.pricePerHour);
                        mPricePerHour.setText(mParkingLotOffer.getPrice() + " $/Hr");

                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm", Locale.ENGLISH);
                        String startTime = sdf.format(mParkingLotOffer.getStartTime());
                        mStartTime = findViewById(R.id.startTime);
                        mStartTime.setText(startTime);

                        String endTime = sdf.format(mParkingLotOffer.getEndTime());
                        mEndTime = findViewById(R.id.endTime);
                        mEndTime.setText(endTime);

                        long secs = (mParkingLotOffer.getEndTime().getTime() - mParkingLotOffer.getStartTime().getTime()) / 1000;
                        Double hours = secs / 3600.0;
                        mTotalPrice = findViewById(R.id.totalPrice);
                        mTotalPrice.setText(hours.toString() + " $");
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }
}
