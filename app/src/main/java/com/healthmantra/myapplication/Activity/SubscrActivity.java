package com.healthmantra.myapplication.Activity;


import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.healthmantra.myapplication.Adapters.SubscAd;
import com.healthmantra.myapplication.DialogSubInfo;
import com.healthmantra.myapplication.Models.OrderInfoMo;
import com.healthmantra.myapplication.R;
import com.healthmantra.myapplication.RecyclerViewClickInterface;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class SubscrActivity extends AppCompatActivity implements RecyclerViewClickInterface {

    Toolbar toolbar;
    RecyclerView recyclerView;

    DatabaseReference reference;
    private RecyclerView.Adapter homeadapter;

    private List<OrderInfoMo> mHomeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscr);

        toolbar = findViewById(R.id.toolbar_subscription);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        reference = FirebaseDatabase.getInstance().getReference().child("SubscriptionList").child(Objects.requireNonNull(user.getPhoneNumber()));

        recyclerView = findViewById(R.id.recyclerview_sub);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);
        mHomeList = new ArrayList<>();

        homeadapter = new SubscAd(mHomeList, this, this);


        recyclerView.setAdapter(homeadapter);

        readPost();
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
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if (user == null) {
//            Intent intent = new Intent(SubscrActivity.this, LoginActivity.class);
//            startActivity(intent);
//            finish();
//        }
    }


    public void readPost() {
        reference.keepSynced(true);

        reference.orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    mHomeList = new ArrayList<>();
                    for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                        mHomeList.add(dataSnapshot1.getValue(OrderInfoMo.class));
                    }
                    homeadapter = new SubscAd(mHomeList, getBaseContext(), SubscrActivity.this::onItemClick);
                    recyclerView.setAdapter(homeadapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void onItemClick(String type, String quantity, String startdate, String enddate) {
        DialogSubInfo dialogSubInfo = new DialogSubInfo();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        bundle.putString("qty", quantity);
        bundle.putString("sdate", startdate);
        bundle.putString("edate", enddate);
        dialogSubInfo.setArguments(bundle);
        dialogSubInfo.show(getSupportFragmentManager(), "hello");
    }
}

