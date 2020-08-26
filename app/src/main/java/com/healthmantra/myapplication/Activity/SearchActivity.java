package com.healthmantra.myapplication.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ferfalk.simplesearchview.SimpleSearchView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.healthmantra.myapplication.Adapters.HomeAd;
import com.healthmantra.myapplication.Adapters.SearchAd;
import com.healthmantra.myapplication.Models.HomeMo;
import com.healthmantra.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    DatabaseReference reference;
    SearchView searchView;
    Toolbar toolbar;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter searchAd;
    private List<HomeMo> mHomeList;

    SimpleSearchView simpleSearchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        reference = FirebaseDatabase.getInstance().getReference().child("productss");
        toolbar = findViewById(R.id.toolbar_search);
        toolbar.setTitle("");
        simpleSearchView = findViewById(R.id.searchView);

        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onBackPressed();
//            }
//        });

        recyclerView = findViewById(R.id.user_recycler_search);
        GridLayoutManager linearLayoutManager = new GridLayoutManager(this, 2);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        mHomeList = new ArrayList<>();

        searchAd = new SearchAd(getBaseContext(), mHomeList);

        simpleSearchView.setOnQueryTextListener(new SimpleSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String text = newText;

                searchFun(text);
                return false;
            }

            @Override
            public boolean onQueryTextCleared() {
                readPost();
                return false;
            }
        });

        simpleSearchView.setOnSearchViewListener(new SimpleSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {

                onBackPressed();
            }

            @Override
            public void onSearchViewShownAnimation() {

            }

            @Override
            public void onSearchViewClosedAnimation() {

            }
        });

        recyclerView.setAdapter(searchAd);
        readPost();
    }

    @Override
    protected void onStart() {
        super.onStart();


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null){
            Intent intent = new Intent(SearchActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            if (reference != null) {
                reference.orderByChild("stock").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {
                            mHomeList = new ArrayList<>();
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {

                                mHomeList.add(ds.getValue(HomeMo.class));
                            }
                            searchAd = new HomeAd(getBaseContext(), mHomeList);
                            recyclerView.setAdapter(searchAd);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

        }
    }

    private void searchFun(String newText) {
        ArrayList<HomeMo> myList = new ArrayList<>();
        for (HomeMo object : mHomeList) {
            if (object.getItemName().toLowerCase().contains(newText.toLowerCase())) {
                myList.add(object);
            }
        }
        HomeAd searchAd = new HomeAd(getApplicationContext(), myList);
        recyclerView.setAdapter(searchAd);
    }


//    @Override
//    public boolean onSupportNavigateUp() {
//        onBackPressed();
//        return true;
//    }

    @Override
    public void onBackPressed() {
        if (simpleSearchView.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        MenuItem item = menu.findItem(R.id.nav_search);
        simpleSearchView.setMenuItem(item);
simpleSearchView.showSearch(true);
        return true;
    }
    private void readPost() {

        reference.keepSynced(true);
        reference.orderByChild("stock").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    mHomeList = new ArrayList<>();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        mHomeList.add(dataSnapshot1.getValue(HomeMo.class));
                    }
                    searchAd = new HomeAd(getBaseContext(), mHomeList);
                    recyclerView.setAdapter(searchAd);

                } else {

                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getBaseContext(), databaseError.toString(), Toast.LENGTH_SHORT).show();
            }

        });

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.nav_noti) {
            Intent i = new Intent(SearchActivity.this,NotificationActivity.class);
            startActivity(i);
        } else if (item.getItemId() == R.id.nav_update) {
            Intent intent = new Intent(this, SubscrActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);

    }
}

