package com.healthmantra.myapplication.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.healthmantra.myapplication.CommonClass;
import com.healthmantra.myapplication.R;


import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;

    Toolbar toolbar;
    NavigationView navigationView;
    DrawerLayout drawer;

    TextView name, location, phone;
    CircleImageView image;
    View header;

    DatabaseReference reference;

    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // reference = FirebaseDatabase.getInstance().getReference();


        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Health Mantra");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);







        navigationView = findViewById(R.id.navigation);
        drawer = findViewById(R.id.drawer_layout);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,
                R.id.nav_about,
                R.id.nav_contact,
                R.id.nav_profile,
                R.id.nav_wallet,
                R.id.nav_refer,
                R.id.nav_order
                )
                .setDrawerLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navigationView, navController);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);

        NavigationUI.setupWithNavController(toolbar, navController, mAppBarConfiguration);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                //it's possible to do more actions on several items, if there is a large amount of items I prefer switch(){case} instead of if()
                if (id == R.id.nav_logout) {
                    showConfirDialog();
                }

                if (id == R.id.nav_term) {
                    Intent intent = new Intent(MainActivity.this, TermsCondition.class);
                    startActivity(intent);
                }
                if (id == R.id.nav_privacy) {
                    Intent intent = new Intent(MainActivity.this, PrivacyPolicy.class);
                    startActivity(intent);
                }


                navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
                    @Override
                    public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                        if (destination.getId() == R.id.nav_refer){

                            //toolbar.getMenu().getItem(R.id.nav_search).setVisible(false);

                        }
                    }
                });


                //This is for maintaining the behavior of the Navigation view
                NavigationUI.onNavDestinationSelected(menuItem, navController);
                //This is for closing the drawer after acting on it
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });


        header = navigationView.getHeaderView(0);
        name = header.findViewById(R.id.text_username);
        phone = header.findViewById(R.id.text_usernumber);
        location = header.findViewById(R.id.text_userlocation);

        image = header.findViewById(R.id.image_user);


    }

    private void showConfirDialog() {

        new AlertDialog.Builder(this)
                .setTitle("Log Out ")
                .setMessage("Do you want to logout?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth auth = FirebaseAuth.getInstance();
                        auth.signOut();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();

                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(R.drawable.ic_logout)
                .show();
    }

    private void loadNavHeader(final String Userphone) {

        name.setText(CommonClass.name);
        location.setText(CommonClass.region + " " + CommonClass.location);
        phone.setText(Userphone);

         if (!CommonClass.imageuri.equals("image")) {
            Glide.with(MainActivity.this).load(CommonClass.imageuri).into(image);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        MenuItem item = menu.findItem(R.id.nav_search);


        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            String user_phone = user.getPhoneNumber();
           loadNavHeader(user_phone);
        }
        else {

                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();

        }


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.nav_noti) {
            Intent intent = new Intent(this, NotificationActivity.class);
            startActivity(intent);
        } else if (menuItem.getItemId() == R.id.nav_update) {
            Intent intent = new Intent(this, SubscrActivity.class);
            startActivity(intent);
        } else if (menuItem.getItemId() == R.id.nav_search) {


            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(menuItem);
    }




}