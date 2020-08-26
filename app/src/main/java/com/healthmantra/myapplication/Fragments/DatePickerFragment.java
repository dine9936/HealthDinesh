package com.healthmantra.myapplication.Fragments;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;


import com.healthmantra.myapplication.CommonClass;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DatePickerFragment extends DialogFragment {
   public static final int START_DATE = 1;
    public static final int END_DATE = 2;

    private int mChosenDate;

    public static int cur = 0;

    DatePickerDialog datePickerDialog;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);


       Calendar calendar = Calendar.getInstance();


//        try {
//            Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(CommonClass.date);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

      //  datePickerDialog.getDatePicker().setMaxDate(Long.parseLong(CommonClass.date));

        Bundle bundle = this.getArguments();
        if (bundle != null)
        {
            mChosenDate = bundle.getInt("DATE", 1);
        }

        switch (mChosenDate)
        {

            case START_DATE:
                cur = START_DATE;
                datePickerDialog = new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener) getActivity(), year, month, day);

                datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis()+86400000);

                datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis()+691200000);
                return datePickerDialog;

            case END_DATE:
                cur = END_DATE;
                datePickerDialog = new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener) getActivity(), year, month, day);


                String datenew = CommonClass.date;

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date date = null;
                try {
                    date = sdf.parse(datenew);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);


                datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis()+86400000);

                //end date ..
               //datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis()+691200000);

                return datePickerDialog;

        }
        return null;
    }
}
