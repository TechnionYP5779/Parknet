package com.team4.parknet;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.team4.parknet.entities.ParkingLotOffer;
import com.team4.parknet.utils.MyDatePickerFragment;
import com.team4.parknet.utils.MyTimePickerFragment;

import java.util.Calendar;
import java.util.Date;

public class OfferParkingActivity extends AppCompatActivity implements
        MyTimePickerFragment.OnCallbackReceived, MyDatePickerFragment.OnCallbackReceived {

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    //private EditText mAddressInput;
    private EditText mPriceInput;
    private Button mSendButton;
    private long time;
    private LatLng mLocation;

    private TextView mChooseStartTime;
    private TextView mChooseEndTime;

    private TextView mChooseStartDate;
    private TextView mChooseEndDate;

    private Integer mStartHour;
    private Integer mEndHour;

    private Integer mStartYear;
    private Integer mEndYear;

    private Integer mStartMonth;
    private Integer mEndMonth;

    private Integer mStartDay;
    private Integer mEndDay;

    public static Date getDate(int year, int month, int day, int hour) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_parking);

        mChooseStartTime = findViewById(R.id.chooseStartTime);
        mChooseEndTime = findViewById(R.id.chooseEndTime);

        mChooseStartDate = findViewById(R.id.chooseStartDate);
        mChooseEndDate = findViewById(R.id.chooseEndDate);

        mChooseStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog("start");
            }
        });

        mChooseEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog("end");
            }
        });


        mChooseStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog("start");
            }
        });

        mChooseEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog("end");
            }
        });

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() == null) {
            setResult(RESULT_CANCELED);
            finish();
        }

        Bundle bundle = getIntent().getExtras();
        mLocation = (LatLng) bundle.get("location");

        mFirestore = FirebaseFirestore.getInstance();

        //mAddressInput = findViewById(R.id.addressInput);
        mPriceInput = findViewById(R.id.priceInput);
        mSendButton = findViewById(R.id.sendButton);

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mStartDay == null || mStartHour == null) {
                    Toast.makeText(OfferParkingActivity.this, "You need to choose start date", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mEndDay == null || mEndHour == null) {
                    Toast.makeText(OfferParkingActivity.this, "You need to choose end date", Toast.LENGTH_SHORT).show();
                    return;
                }
                addParkingOffer();
            }
        });
    }

    private void addParkingOffer() {

        Date startTime = getDate(mStartYear, mStartMonth, mStartDay, mStartHour);
        Date endTime = getDate(mEndYear, mEndMonth, mEndDay, mEndHour);

        ParkingLotOffer offer = new ParkingLotOffer(mAuth.getCurrentUser().getUid(),
                new GeoPoint(mLocation.latitude, mLocation.longitude),
                Float.parseFloat(mPriceInput.getText().toString()),
                startTime, endTime);

        mFirestore.collection("offers").add(offer)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Toast.makeText(getApplicationContext(), "Parking Offer Added Successfully", Toast.LENGTH_LONG).show();
                        setResult(RESULT_OK);
                        finish();
                    }
                });
    }

    public void showTimePickerDialog(String startOrEnd) {
        DialogFragment newFragment = new MyTimePickerFragment();
        Bundle b = new Bundle();
        b.putString("start-or-end", startOrEnd);
        newFragment.setArguments(b);
        newFragment.show(getSupportFragmentManager(), "time picker");
    }

    public void showDatePickerDialog(String startOrEnd) {
        DialogFragment newFragment = new MyDatePickerFragment();
        Bundle b = new Bundle();
        b.putString("start-or-end", startOrEnd);
        newFragment.setArguments(b);
        newFragment.show(getSupportFragmentManager(), "date picker");
    }

    public void setStartTime(int startHour) {
        mStartHour = startHour;
    }

    public void setStartDate(int startYear, int startMonth, int startDay) {
        mStartYear = startYear;
        mStartMonth = startMonth;
        mStartDay = startDay;
    }

    public void setEndDate(int endYear, int endMonth, int endDay) {
        mEndYear = endYear;
        mEndMonth = endMonth;
        mEndDay = endDay;
    }

    public void setEndTime(int endHour) {
        mEndHour = endHour;
    }

    @Override
    public void UpdateTime(String startOrEnd, Integer hour) {
        if (startOrEnd.equals("start")) {
            setStartTime(hour);
            mChooseStartTime.setText(mStartHour.toString() + ":00");
        } else {
            setEndTime(hour);
            mChooseEndTime.setText(mEndHour.toString() + ":00");
        }
    }

    @Override
    public void UpdateDate(String startOrEnd, Integer year, Integer month, Integer dayOfMonth) {
        if (startOrEnd.equals("start")) {
            setStartDate(year, month, dayOfMonth);
            mChooseStartDate.setText(mStartDay.toString() + "/" + mStartMonth.toString() + "/" + mStartYear.toString());
        } else {
            setEndDate(year, month, dayOfMonth);
            mChooseEndDate.setText(mEndDay.toString() + "/" + mEndMonth.toString() + "/" + mEndYear.toString());
        }
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
        }
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
        }
    }
}
