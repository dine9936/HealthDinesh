package com.healthmantra.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class DialogAddress extends DialogFragment {

    TextView location, status;

    EditText editText;

    Button button;
    ImageButton imageButton;

    ArrayList<String> regionList = new ArrayList<>();
    ArrayList<String> regionListNotDeliverable = new ArrayList<>();
    SpinnerDialog spinnerDialog;

    LocationList locationList;


DatabaseReference reference;
FirebaseUser user;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_address,container,false);


        location = view.findViewById(R.id.tv_delivery_location);
        location.setText(CommonClass.location);
        status = view.findViewById(R.id.tv_delivery_status);


        reference = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();


        ///////////////////
 locationList = new LocationList();
regionList = locationList.getRegionList();



        if (locationList.isDeliverable(CommonClass.location)){
            status.setTextColor(Color.GREEN);

            status.setText("Delivery Available");
        }
        else {
            status.setTextColor(Color.RED);
            status.setText("Delivery Not Available");
        }



        button = view.findViewById(R.id.button_delivery);

        imageButton = view.findViewById(R.id.ib_delivery_close);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

        editText = view.findViewById(R.id.et_delivery_address);
        if (!CommonClass.address.equals("address")){
            editText.setText(CommonClass.address);
        }
        else {
            editText.setText("");
        }


        spinnerDialog = new SpinnerDialog(getActivity(), regionList, "Select Location");
        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {

                location.setText(item);
                CommonClass.location = item;

                if (locationList.isDeliverable(item)){
                    status.setTextColor(Color.GREEN);

                    status.setText("Delivery Available");
                }
                else {
                    status.setTextColor(Color.RED);
                    status.setText("Delivery Not Available");
                }

            }
        });
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerDialog.showSpinerDialog();
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.getText().toString().trim().equals("")){
                    CommonClass.address = "address";
                    reference.child("customer").child(user.getPhoneNumber()).child("userAddress").setValue(CommonClass.address);
                    reference.child("customer").child(user.getPhoneNumber()).child("userLocation").setValue(CommonClass.location);

                }
                else {
                    CommonClass.address = editText.getText().toString().trim();
                    reference.child("customer").child(user.getPhoneNumber()).child("userAddress").setValue(CommonClass.address);

                    reference.child("customer").child(user.getPhoneNumber()).child("userLocation").setValue(CommonClass.location);

                }

                getDialog().dismiss();
            }
        });

        return view;
    }


}
