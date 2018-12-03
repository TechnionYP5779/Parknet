package com.team4.parknet.utils;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.team4.parknet.R;
import com.team4.parknet.entities.Order;
import com.team4.parknet.entities.ParkingLotOffer;

import static android.support.constraint.Constraints.TAG;


public class OrderViewHolder extends RecyclerView.ViewHolder {
    private TextView mAddressField;
    private TextView mPriceField;
    private Button mOrderDetailsBtn;
    private ParkingLotOffer mOfferOfOrder;

    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);
        mAddressField = itemView.findViewById(R.id.orderLocation);
        mPriceField = itemView.findViewById(R.id.orderPrice);
        mOrderDetailsBtn = itemView.findViewById(R.id.orderDetailsBtn);
    }

    public void bind(@NonNull final Order order, FirebaseFirestore mDb){
        DocumentReference docRef = mDb.collection("offers").document(order.getOfferId());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        mOfferOfOrder = task.getResult().toObject(ParkingLotOffer.class);
                        mAddressField.setText(mOfferOfOrder.getAddress());

                        Float price = order.getTimesOrdered().size() * mOfferOfOrder.getPrice();
                        mPriceField.setText(price.toString());
                    } else{
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }

            }
        });
    }
}
