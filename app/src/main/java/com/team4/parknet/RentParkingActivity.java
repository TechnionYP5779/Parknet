package com.team4.parknet;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.team4.parknet.entities.ParkingLotOffer;
import com.team4.parknet.utils.ParkingOfferViewHolder;

public class RentParkingActivity extends AppCompatActivity {

    private static final String TAG = "RentParkingActivity";
    private static final int ORDER_RETURN_CODE = 1;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mDb;

    RecyclerView mOfferRecyclerView;
    FirestoreRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_parking);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);

        mOfferRecyclerView = findViewById(R.id.offerRecyclerView);

        mOfferRecyclerView.setHasFixedSize(true);
        mOfferRecyclerView.setLayoutManager(manager);

        mDb = FirebaseFirestore.getInstance();

        Query query = mDb.collection("offers").limit(10);

        FirestoreRecyclerOptions<ParkingLotOffer> options = new FirestoreRecyclerOptions.Builder<ParkingLotOffer>()
                .setQuery(query, ParkingLotOffer.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<ParkingLotOffer, ParkingOfferViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ParkingOfferViewHolder holder, final int position, @NonNull ParkingLotOffer model) {
                holder.bind(model, new ParkingOfferViewHolder.OnBookClickCallBack() {
                    @Override
                    public void onBookClick() {
                        String id = getSnapshots().getSnapshot(position).getId();
                        Intent i = new Intent(RentParkingActivity.this, OrderActivity.class);
                        i.putExtra("id", id);
                        startActivityForResult(i, ORDER_RETURN_CODE);
                    }
                });
            }

            @NonNull
            @Override
            public ParkingOfferViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
                View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.parking_offer_item, viewGroup, false);
                return new ParkingOfferViewHolder(v);
            }
        };

        mOfferRecyclerView.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
