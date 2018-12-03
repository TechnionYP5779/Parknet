package com.team4.parknet;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.team4.parknet.entities.Order;
import com.team4.parknet.entities.ParkingLotOffer;
import com.team4.parknet.utils.OrderViewHolder;
import com.team4.parknet.utils.ParkingOfferViewHolder;

public class ViewOrdersActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore mDb;

    RecyclerView mOrderRecyclerView;
    FirestoreRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_orders);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);

        mOrderRecyclerView = findViewById(R.id.offerRecyclerView);

        mOrderRecyclerView.setHasFixedSize(true);
        mOrderRecyclerView.setLayoutManager(manager);

        mDb = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        Query query = mDb.collection("orders").whereEqualTo("uid", mAuth.getCurrentUser().getUid());

        FirestoreRecyclerOptions<Order> options = new FirestoreRecyclerOptions.Builder<Order>()
                .setQuery(query, Order.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Order, OrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder holder, final int position, @NonNull Order model) {
                holder.bind(model, mDb);
            }

            @NonNull
            @Override
            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
                View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.order_item, viewGroup, false);
                return new OrderViewHolder(v);
            }
        };

        mOrderRecyclerView.setAdapter(adapter);
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
