package com.healthmantra.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DialogWallet extends DialogFragment {

    Button button1, button2;

    String amount;
    TextView textView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.dialog_wallet_alert,container,false);

        textView = view.findViewById(R.id.wallet_balance);
        if (CommonClass.type.equals("2")){
            textView.setText("Available balance : ₹"+CommonClass.walletmoney+"\nOrder price : ₹"+CommonClass.orderValue);
        }
           else {
            textView.setText("Available balance : ₹"+CommonClass.walletmoney+"\nYou Need Atleast : ₹500 in wallet");

        }

//        Bundle bundle = getArguments();
//        amount = bundle.getString("amount");


        //Toast.makeText(getContext(), amount, Toast.LENGTH_SHORT).show();
        button1 = view.findViewById(R.id.addamunt);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),Wallet.class);


                startActivity(intent);
                getDialog().dismiss();
            }
        });
        button2 = view.findViewById(R.id.cancel);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });
        return view;

    }
}
