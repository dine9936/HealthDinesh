package com.healthmantra.myapplication.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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
import com.healthmantra.myapplication.Adapters.TransactAd;
import com.healthmantra.myapplication.R;
import com.healthmantra.myapplication.TransactionMo;

import java.util.ArrayList;
import java.util.List;

public class TransactionHistory extends AppCompatActivity {
Toolbar toolbar;

    FirebaseDatabase database;
    DatabaseReference reference;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private List<TransactionMo> transactionMoList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("TransactionHistory");

        toolbar = findViewById(R.id.toolbar_transaction);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });




        recyclerView = findViewById(R.id.rv_trsaction_history);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);

        transactionMoList = new ArrayList<>();

        adapter = new TransactAd( transactionMoList,this);


        recyclerView.setAdapter(adapter);


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


    private void readPost() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        reference.child(user.getPhoneNumber()).keepSynced(true);

       // loading.show(getChildFragmentManager(), "Loading");

        reference.child(user.getPhoneNumber()).orderByChild("txnid").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {
                        //loading.dismiss();
                        transactionMoList = new ArrayList<>();
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            transactionMoList.add(dataSnapshot1.getValue(TransactionMo.class));
                        }
                        adapter = new TransactAd( transactionMoList,TransactionHistory.this);
                        recyclerView.setAdapter(adapter);

                    } else {
                        //loading.dismiss();
                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(TransactionHistory.this, databaseError.toString(), Toast.LENGTH_SHORT).show();

                //loading.dismiss();
            }

        });
    }
}