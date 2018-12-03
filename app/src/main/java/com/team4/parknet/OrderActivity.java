package com.team4.parknet;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.LogDescriptor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.team4.parknet.entities.Order;
import com.team4.parknet.entities.ParkingLotOffer;
import com.team4.parknet.entities.TimeSlot;

import java.text.SimpleDateFormat;
import java.util.List;
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
    private Button mOrderButton;
    private ListView mTimeSlotsList;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        Bundle bundle = getIntent().getExtras();
        final String id = bundle.getString("id");

        mDb = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

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

                        Float hours = mParkingLotOffer.getDurationInHours();
                        Float total_price = hours * mParkingLotOffer.getPrice();
                        mTotalPrice = findViewById(R.id.totalPrice);
                        mTotalPrice.setText(total_price.toString() + " $");

                        mTimeSlotsList = findViewById(R.id.time_slots);

                        TimeSlotListAdapter adapter = new TimeSlotListAdapter(OrderActivity.this, R.layout.time_slot_item, mParkingLotOffer.getAvailability());

                        mTimeSlotsList.setAdapter(adapter);

                        mOrderButton = findViewById(R.id.orderButton);
                        mOrderButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Order order = new Order(id, mParkingLotOffer.getStartTime(),
                                        mParkingLotOffer.getEndTime(), mAuth.getCurrentUser().getUid());

                                mDb.collection("orders").add(order)
                                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                                Toast.makeText(getApplicationContext(), "Order Added Successfully", Toast.LENGTH_LONG).show();
                                                setResult(RESULT_OK);
                                                finish();
                                            }
                                        });
                            }
                        });
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    private class TimeSlotListAdapter extends ArrayAdapter<TimeSlot> {

        private Context context;

        public TimeSlotListAdapter(@NonNull Context context, int resource, @NonNull List<TimeSlot> objects) {
            super(context, resource, objects);
            this.context = context;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.time_slot_item, parent, false);

            TimeSlot timeSlot = getItem(position);

            TextView startTime = rowView.findViewById(R.id.startTime);
            TextView endTime = rowView.findViewById(R.id.endTime);
            Button bookBtn = rowView.findViewById(R.id.bookBtn);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm", Locale.ENGLISH);
            startTime.setText(sdf.format(timeSlot.getStartTime()));
            endTime.setText(sdf.format(timeSlot.getEndTime()));

            if (timeSlot.isAvailable()) {
                bookBtn.setVisibility(View.VISIBLE);
                rowView.setBackgroundColor(getResources().getColor(R.color.vacantBg));
            } else {
                bookBtn.setVisibility(View.INVISIBLE);
                rowView.setBackgroundColor(getResources().getColor(R.color.busyBg));
            }

            return rowView;
        }
    }
}
