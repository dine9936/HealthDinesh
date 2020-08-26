package com.healthmantra.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DialogTime extends DialogFragment {

    ImageButton imageButtonClose;
    Button buttonConfirm;

    RadioGroup radioGroup;
    RadioButton radioButton;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_choose_time, container, false);


        imageButtonClose = v.findViewById(R.id.close_dialog_time);
        imageButtonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });


        radioGroup = v.findViewById(R.id.rdgrouptime);

        buttonConfirm = v.findViewById(R.id.button_dialog_time);
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int rdid = radioGroup.getCheckedRadioButtonId();


               radioButton = v.findViewById(rdid);
               CommonClass.deliveryTime = radioButton.getText().toString();

                Toast.makeText(getContext(), CommonClass.deliveryTime, Toast.LENGTH_SHORT).show();

               getDialog().dismiss();



            }
        });

        return v;
    }
}
