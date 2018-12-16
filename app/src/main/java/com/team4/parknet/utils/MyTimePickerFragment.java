package com.team4.parknet.utils;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

public class MyTimePickerFragment extends DialogFragment {
    private String mStartOrEnd;
    OnCallbackReceived mCallback;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mCallback = (OnCallbackReceived) getActivity();

        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        mStartOrEnd = getArguments().getString("start-or-end");
        return new TimePickerDialog(getActivity(), timeSetListener, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    private TimePickerDialog.OnTimeSetListener timeSetListener =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    mCallback.UpdateTime(mStartOrEnd, hourOfDay);
                }
            };

    public interface OnCallbackReceived {
        void UpdateTime(String startOrEnd, Integer hour);
    }
}
