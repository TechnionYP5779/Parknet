package com.team4.parknet.utils;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

public class MyDatePickerFragment extends DialogFragment {
    private String mStartOrEnd;
    OnCallbackReceived mCallback;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mCallback = (OnCallbackReceived) getActivity();

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        mStartOrEnd = getArguments().getString("start-or-end");
        return new DatePickerDialog(getActivity(), dateSetListener, year, month, dayOfMonth);
    }

    private DatePickerDialog.OnDateSetListener dateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    mCallback.UpdateDate(mStartOrEnd, year, month, dayOfMonth);
                }
            };

    public interface OnCallbackReceived {
        public void UpdateDate(String startOrEnd, Integer year, Integer month, Integer dayOfMonth);
    }
}
