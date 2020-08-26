package com.healthmantra.myapplication.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.healthmantra.myapplication.Adapters.HomeAd;
import com.healthmantra.myapplication.Adapters.SliderAdapterExample;
import com.healthmantra.myapplication.DialogLoading;
import com.healthmantra.myapplication.Models.HomeMo;
import com.healthmantra.myapplication.Models.SliderItem;
import com.healthmantra.myapplication.R;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    FirebaseDatabase database;
    DatabaseReference reference, refslider;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter homeadapter;

    private List<HomeMo> mHomeList;
    private List<String> imageList;
    SliderView sliderView;
    private SliderAdapterExample adapter;

    DialogLoading loading;

    public HomeFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        loading = new DialogLoading();

        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("productss");
        refslider = database.getReference().child("imageSlider");

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager linearLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        mHomeList = new ArrayList<>();
        imageList = new ArrayList<>();

        homeadapter = new HomeAd(getContext(), mHomeList);


        recyclerView.setAdapter(homeadapter);


        sliderView = view.findViewById(R.id.imageSlider);


        adapter = new SliderAdapterExample(getContext());
        sliderView.setSliderAdapter(adapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setIndicatorEnabled(true);
        sliderView.setIndicatorVisibility(true);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();


        sliderView.setOnIndicatorClickListener(new DrawController.ClickListener() {
            @Override
            public void onIndicatorClicked(int position) {
                Log.i("GGG", "onIndicatorClicked: " + sliderView.getCurrentPagePosition());
            }
        });
        List<SliderItem> sliderItemList = new ArrayList<>();



        for (int i = 0; i < 4; i++) {
            SliderItem sliderItem = new SliderItem();
            if (i == 0)
                sliderItem.setImageUrl("https://firebasestorage.googleapis.com/v0/b/newhealth-9d31d.appspot.com/o/slider%2Ffirst%20time%20offer.jpg?alt=media&token=944c450f-ff05-46ac-a2b7-6ddb84bef298");
            else if (i == 1)
                sliderItem.setImageUrl("https://firebasestorage.googleapis.com/v0/b/newhealth-9d31d.appspot.com/o/slider%2FCASHBACK%20BANNER.jpg?alt=media&token=2dd335ee-8c2f-45c6-8f2c-0369c7140da1");
            else if (i == 2)
                sliderItem.setImageUrl("https://firebasestorage.googleapis.com/v0/b/newhealth-9d31d.appspot.com/o/slider%2Freferal%20banner.jpg?alt=media&token=c299e7c6-a3a4-4e08-8826-a6c8b0b8ecd3");
            else if (i == 3)
                sliderItem.setImageUrl("https://firebasestorage.googleapis.com/v0/b/newhealth-9d31d.appspot.com/o/slider%2F14.jpg?alt=media&token=616f83db-4b15-4097-beda-695a48edef19");
            sliderItemList.add(sliderItem);
        }
        adapter.renewItems(sliderItemList);

        readPost();

        return view;
    }

    private void readPost() {

        reference.keepSynced(true);

        loading.show(getChildFragmentManager(), "Loading");

        reference.orderByChild("stock").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (isAdded()) {
                    if (dataSnapshot.exists()) {
                        loading.dismiss();
                        mHomeList = new ArrayList<>();
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            mHomeList.add(dataSnapshot1.getValue(HomeMo.class));
                        }
                        homeadapter = new HomeAd(getContext(), mHomeList);
                        recyclerView.setAdapter(homeadapter);

                    } else {
                        loading.dismiss();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.toString(), Toast.LENGTH_SHORT).show();

                loading.dismiss();
            }

        });
    }




}