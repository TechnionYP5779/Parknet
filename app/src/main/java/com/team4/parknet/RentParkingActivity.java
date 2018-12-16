package com.team4.parknet;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.team4.parknet.entities.ParkingLotOffer;
import com.team4.parknet.utils.ParkingOfferViewHolder;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RentParkingActivity extends AppCompatActivity {

    private static final String TAG = "RentParkingActivity";
    private static final int ORDER_RETURN_CODE = 1;
    public static final int MILLISECS_TO_HOURS = 3600000;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mDb;
    private FirebaseFunctions mFunctions;

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

        mFunctions = FirebaseFunctions.getInstance();
        //testCloudFunction();

        Bundle bundle = getIntent().getExtras();
        Date startDate = (Date)bundle.get("start-date");
        Date endDate = (Date)bundle.get("end-date");
        String address = bundle.getString("address");

        Map<String, Object> data = new HashMap<>();
        data.put("startDate", startDate);
        data.put("endDate", endDate);
        data.put("address", address);

        testCloudFunction(data);
//        This is some test to prove that we can query based on vacant timeslots
        Query query = mDb.collection("offers");

//        HashMap<String, Object> temp = new HashMap<>();
//        Date currDate1 = startDate;
//        Date currDate2;
//        for(int i = 0; i <= (endDate.getTime() - startDate.getTime())/ MILLISECS_TO_HOURS; ++i){
//            Calendar cal = Calendar.getInstance(); // creates calendar
//            cal.setTime(currDate1);
//            cal.add(Calendar.HOUR_OF_DAY, 1); // adds one hour
//            currDate2 = cal.getTime();
//            temp.put("startTime", currDate1);
//            temp.put("endTime", currDate2);
//            temp.put("available", true);
//            query = query.whereArrayContains("availability", temp);
//            currDate1 = currDate2;
//        }

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

    private Task<String> helloWorld(){
        Map<String, Object> data = new HashMap<>();

        return mFunctions
                .getHttpsCallable("helloWorld")
                .call(data)
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) {
                        // This continuation runs on either success or failure, but if the task
                        // has failed then getResult() will throw an Exception which will be
                        // propagated down.
                        String result = (String)((Map)task.getResult().getData()).get("text");
                        return result;
                    }
                });
    }

    private Task<Date> queryTest(Map<String, Object> data){

        return mFunctions
                .getHttpsCallable("startDate")
                .call(data)
                .continueWith(new Continuation<HttpsCallableResult, Date>() {
                    @Override
                    public Date then(@NonNull Task<HttpsCallableResult> task) {
                        // This continuation runs on either success or failure, but if the task
                        // has failed then getResult() will throw an Exception which will be
                        // propagated down.
                        Date result = (Date)((Map)task.getResult().getData()).get("text");
                        return result;
                    }
                });
    }



    private void testCloudFunction(Map<String, Object> data){
        queryTest(data).addOnCompleteListener(new OnCompleteListener<Date>() {
            @Override
            public void onComplete(@NonNull Task<Date> task) {
                if(task.isSuccessful()){
                    Date date = task.getResult();
                    Log.d(TAG, "onComplete: " + date.toString());
                }
                else{
                    Exception e = task.getException();
                    Log.d(TAG, "onComplete: " + e.getMessage());
                }
            }
        });
    }
}
