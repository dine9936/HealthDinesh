package com.healthmantra.myapplication.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.healthmantra.myapplication.Models.CustomerInfo;
import com.healthmantra.myapplication.OrderActivity;
import com.healthmantra.myapplication.R;
import com.healthmantra.myapplication.Wallet;


import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    DatabaseReference reference;

    ImageButton editProfile;
    CircleImageView profileImage;
    TextView textViewName, textViewPhone, textViewAddress, textViewSubNo, textViewWalletNo, textViewOrdersNo;


    Toolbar toolbar;
    Button button;

    LinearLayout llwallet, llsubs, llorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        reference = FirebaseDatabase.getInstance().getReference();

        profileImage = findViewById(R.id.image_view_profile);



        textViewName = findViewById(R.id.name_text);
        textViewPhone = findViewById(R.id.phone_text);

        textViewAddress = findViewById(R.id.address_text);

        llorder = findViewById(R.id.ll_orders);
        llorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, OrderActivity.class);
                startActivity(intent);            }
        });
        llsubs = findViewById(R.id.ll_subscribtion);
        llsubs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this,SubscrActivity.class);
                startActivity(intent);            }
        });
        llwallet = findViewById(R.id.ll_wallet);
        llwallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, Wallet.class);
                startActivity(intent);            }
        });


        toolbar = findViewById(R.id.toolbar_profile);

            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
        });







        button = findViewById(R.id.button_call_support);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + "+91 8874554433"));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);

                }
            }
        });
        textViewSubNo = findViewById(R.id.number_sub_text);
        textViewWalletNo = findViewById(R.id.number_wallet_text);
        textViewOrdersNo = findViewById(R.id.number_orders_text);
        editProfile = findViewById(R.id.ib_edit_profile);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this,EditProfileActivity.class);
                startActivity(intent);
            }
        });




        readData();

    }



    public void  readData(){
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    if (user!= null){

        reference.child("SubscriptionList").child(user.getPhoneNumber()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    textViewSubNo.setText(String.valueOf(snapshot.getChildrenCount()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reference.child("OrderList").child(user.getPhoneNumber()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    textViewOrdersNo.setText(String.valueOf(snapshot.getChildrenCount()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reference.child("customer").child(user.getPhoneNumber()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                CustomerInfo customerInfo = snapshot.getValue(CustomerInfo.class);
                textViewName.setText(customerInfo.getUserName());
                textViewPhone.setText(customerInfo.getUserPhone());
                if (!customerInfo.getUserAddress().equals("address")){
                    textViewAddress.setText(customerInfo.getUserAddress()+","+customerInfo.getUserLocation()+","+customerInfo.getUserRegion());

                }
                else {
                    textViewAddress.setText(customerInfo.getUserLocation()+","+customerInfo.getUserRegion());

                }
                textViewSubNo.setText(customerInfo.getUserSuscription());
                textViewOrdersNo.setText(customerInfo.getUserOrders());
                textViewWalletNo.setText("₹"+customerInfo.getUserWalletMoney());

                if (!customerInfo.getUserImage().equals("image")){
                    Glide.with(ProfileActivity.this).load(customerInfo.getUserImage())
                            .into(profileImage);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();

        readData();


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();



    }


}