package com.healthmantra.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DialogSubInfo extends DialogFragment {
TextView type,sdate,edate,qty,remainingDay;
ImageButton imageButton;

   long diff,diff1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.dialog_info_sub,container,false);


        imageButton = view.findViewById(R.id.close_dialog);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });


        type = view.findViewById(R.id.type);
        sdate = view.findViewById(R.id.strat_date);
        edate = view.findViewById(R.id.end_date);
        qty = view.findViewById(R.id.quantity);

        remainingDay = view.findViewById(R.id.remaining_day);


        type.setText(getArguments().getString("type"));
        sdate.setText(getArguments().getString("sdate"));


        edate.setText(getArguments().getString("edate"));
        qty.setText(getArguments().getString("qty"));


        String dateStart = getArguments().getString("sdate");
        String dateEnd = getArguments().getString("edate");


        Calendar cal = Calendar.getInstance();



        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");



        try {
            Date date1 = sdf.parse(dateStart);
            Date date2 = sdf.parse(dateEnd);


            String current = sdf.format(cal.getTime());

            Date today = sdf.parse(current);
            diff = (date2.getTime() - date1.getTime()) +86400000;
            if (today.after(date1))
            {
                diff1 = (date2.getTime() - today.getTime())+86400000;

            }
            else {
                diff1 = diff;
            }









        }
        catch (Exception e){

        }
        remainingDay.setText (TimeUnit.DAYS.convert(diff1, TimeUnit.MILLISECONDS)+"/" + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));


        return view;



    }
}
