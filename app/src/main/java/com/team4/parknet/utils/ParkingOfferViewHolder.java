package com.team4.parknet.utils;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.team4.parknet.R;
import com.team4.parknet.entities.ParkingLotOffer;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class ParkingOfferViewHolder extends RecyclerView.ViewHolder
{
    private TextView mAddressField;
    private TextView mPriceField;
    private TextView mAvailabilityField;
    private Button mBookBtn;

    public ParkingOfferViewHolder(@NonNull View itemView) {
        super(itemView);
        mAddressField = itemView.findViewById(R.id.location);
        mPriceField = itemView.findViewById(R.id.pricePerHour);
        mAvailabilityField = itemView.findViewById(R.id.availableTime);
        mBookBtn = itemView.findViewById(R.id.bookBtn);
    }

    public void bind(@NonNull ParkingLotOffer offer, final OnBookClickCallBack cb){
        //mAddressField.setText(offer.getAddress());
        mPriceField.setText(String.valueOf(offer.getPrice()) + " $/Hr");

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm", Locale.ENGLISH);
        String avail = sdf.format(offer.getStartTime()) + " to " + sdf.format(offer.getEndTime());
        mAvailabilityField.setText(avail);

        mBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb.onBookClick();
            }
        });
    }

    public interface OnBookClickCallBack {
        void onBookClick();
    }
}
