package com.example.yulia.myapplication.myclass;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.widget.EditText;

import com.example.yulia.myapplication.R;

public class MyDatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        Dialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
        dialog.setTitle("Choose birth date");

        return dialog;
    }

    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
        EditText et = (EditText) getActivity().findViewById(R.id.etDate);
        et.setText(dayOfMonth + "." + month + "." + year);
    }
}
