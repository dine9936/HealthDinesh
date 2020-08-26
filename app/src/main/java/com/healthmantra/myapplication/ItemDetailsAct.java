package com.healthmantra.myapplication;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class ItemDetailsAct extends AppCompatActivity implements DatePickerDialog.OnDateSetListener  {

    ///variables of views
    private TextView textViewItemName, textViewOneTimeCut, textViewItemPrice, textViewItemName2, textViewSub, textViewDiscount, textViewDetails, textViewPrevent, textViewBenefits,textViewDetailsHeading, textViewBenefitsHeading, textViewPreventHeading,textViewCalenderstart, textViewCalendarends,textViewSun, textViewMon, textViewTues, textViewWed, textViewThur, textViewFri, textViewSat,textViewQuantityplusmminus;



    ///variables of database



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details_acticity);

        intitializingElements();
    }



    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

    }




    /////initializing all elements......
    private void intitializingElements() {



    }
}
