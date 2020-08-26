package com.healthmantra.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.healthmantra.myapplication.Adapters.OrderAd;
import com.healthmantra.myapplication.Models.OrderInfoMo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class OrderFragment extends Fragment {

    RecyclerView recyclerView;

    DatabaseReference reference;
    private RecyclerView.Adapter homeadapter;

    private List<OrderInfoMo> mHomeList;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order, container, false);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        reference = FirebaseDatabase.getInstance().getReference().child("OrderList").child(Objects.requireNonNull(user.getPhoneNumber()));

        recyclerView = view.findViewById(R.id.recyclerview_order);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mHomeList = new ArrayList<>();

        homeadapter = new OrderAd(mHomeList,getContext());


        recyclerView.setAdapter(homeadapter);

        readPost();
        return view;
    }


    public void readPost() {
        reference.keepSynced(true);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    mHomeList = new ArrayList<>();
                    for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                        mHomeList.add(dataSnapshot1.getValue(OrderInfoMo.class));
                    }
                    homeadapter = new OrderAd(mHomeList,getContext());
                    recyclerView.setAdapter(homeadapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}