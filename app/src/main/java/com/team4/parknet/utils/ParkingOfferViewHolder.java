package com.team4.parknet.utils;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.team4.parknet.OfferParkingActivity;
import com.team4.parknet.R;
import com.team4.parknet.entities.ParkingLotOffer;

import org.w3c.dom.Text;

public class ParkingOfferViewHolder extends RecyclerView.ViewHolder
{
    private TextView mAddressField;
    private TextView mPriceField;
    private TextView mAvailabilityField;

    public ParkingOfferViewHolder(@NonNull View itemView) {
        super(itemView);
        mAddressField = itemView.findViewById(R.id.address);
        mPriceField = itemView.findViewById(R.id.pricePerHour);
        mAvailabilityField = itemView.findViewById(R.id.availability);
    }

    public void bind(@NonNull ParkingLotOffer offer){
        mAddressField.setText(offer.getAddress());
        mPriceField.setText(String.valueOf(offer.getPrice()));

        String avail = "Available between " + offer.getStartTime().toString() + " to " + offer.getEndTime().toString();
        mAvailabilityField.setText(avail);
    }

}
