package com.healthmantra.myapplication.Activity;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.healthmantra.myapplication.CommonClass;
import com.healthmantra.myapplication.DialogAddress;
import com.healthmantra.myapplication.DialogTime;
import com.healthmantra.myapplication.DialogWallet;
import com.healthmantra.myapplication.Fragments.DatePickerFragment;
import com.healthmantra.myapplication.LocationList;
import com.healthmantra.myapplication.Models.CustomerInfo;
import com.healthmantra.myapplication.Models.OrderInfoMo;
import com.healthmantra.myapplication.R;
import com.healthmantra.myapplication.TransactionMo;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.healthmantra.myapplication.Fragments.DatePickerFragment.START_DATE;
import static com.healthmantra.myapplication.Fragments.DatePickerFragment.cur;


public class ItemDetailsActicity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    String itemImage;
    private TextView textViewItemName, textViewOneTimeCut, textViewItemPrice, textViewItemName2, textViewQuantity, textViewSub, textViewDiscount, textViewDetails, textViewPrevent, textViewBenefits;
    private TextView textViewDetailsHeading, textViewBenefitsHeading, textViewPreventHeading;
    private ImageView imageViewItem;
    private Toolbar toolbar;
    private Button buttonWhatsapp;

    private TextView textViewCalenderstart, textViewCalendarends;
    private TextView textViewSun, textViewMon, textViewTues, textViewWed, textViewThur, textViewFri, textViewSat;

    private CheckBox ch1, ch2, ch3, ch4;
    private LinearLayout llonetime, lloninterval, llcustomize, llStartDate, llEndDate;


    private TextView textViewQuantityplusmminus;
    private ImageButton imageButtonPlus, imageButtonMinus;

    Button button1, button2, button3, button4, button5, button6, button7;

    Button buttons[] = null;

    ImageButton imageButtonsun, imageButtonmon, imageButtontue, imageButtonwed, imageButtonthr, imageButtonfri, imageButtonsat;

    TextView textViewCommonAllCounter;
    ImageButton imageButtonCommon;


    RadioButton radioButtonSecondDay, radioButtonThirdDay, radioButtonFourthDay, radioButtonNthDay;

    String currentDate;

    EditText editTextNthDay;


    int orderidint;
    String date;
    String time;


    DatabaseReference reference;


    RadioGroup radioGroup;
    RadioButton radioButton200, radioButton1000;
    ////////////////////////////////////////////
    // Data to send on firebase
    ///////////////////////////////////////////
    String strOrderType = "",
            strStartingTime = "0",
            strEndTime = "(optional:)",
            strPlusMinusQuantity = "0",
            strIntervalType = "0",
            strSunQua = "0",
            strMonQua = "0",
            strTueQua = "0",
            strWedQua = "0",
            strThrQua = "0",
            strFriQua = "0",
            strSatQua = "0";


    TextView textVieDeliveryStatus, textViewDeliveryAddress, textViewChangeLocation;


    RelativeLayout relativeLayoutSubgrid;
    LinearLayout llDate, llPlusMinus;

    RelativeLayout rlFrequency;

    String itemquantity, itemonetime, itemsubcription, itemprice;
    TextView textViewHeading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details_acticity);


        reference = FirebaseDatabase.getInstance().getReference().child("customer").child(CommonClass.phone);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    CustomerInfo customerInfo = snapshot.getValue(CustomerInfo.class);

                    CommonClass.walletmoney = customerInfo.getUserWalletMoney();


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        textViewHeading = findViewById(R.id.start_date_heading);

        radioGroup = findViewById(R.id.rg_quntity);

        radioButton200 = findViewById(R.id.rd_200);
        radioButton1000 = findViewById(R.id.rd_1000);


        textVieDeliveryStatus = findViewById(R.id.tv_delivery_status);

        LocationList locationList = new LocationList();


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    CustomerInfo customerInfo = snapshot.getValue(CustomerInfo.class);
                    String locationa = customerInfo.getUserLocation();

                    String address = customerInfo.getUserAddress();

                    if (!locationList.isDeliverable(locationa)) {
                        textVieDeliveryStatus.setText("Cannot deliver to :");
                        textVieDeliveryStatus.setTextColor(Color.RED);
                    } else {
                        textVieDeliveryStatus.setText("Delivery available to :");

                    }


                    textViewDeliveryAddress = findViewById(R.id.tv_delivery_location);
                    if (!address.equals("address")) {
                        textViewDeliveryAddress.setText(CommonClass.address + " " + CommonClass.location + " " + CommonClass.region);

                    } else {
                        textViewDeliveryAddress.setText(CommonClass.location + " " + CommonClass.region);
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        textViewChangeLocation = findViewById(R.id.tv_change_location);
        textViewChangeLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogAddress dialogAddress = new DialogAddress();
                dialogAddress.show(getSupportFragmentManager(), "hello");
            }
        });

        llPlusMinus = findViewById(R.id.ll_plusminus);
        rlFrequency = findViewById(R.id.rl_frequncy);

        editTextNthDay = findViewById(R.id.nth_day);

        toolbar = findViewById(R.id.toolbar_details);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        currentDate = day + 1 + "/" + (month + 1) + "/" + year;

        llStartDate = findViewById(R.id.ll_strt_date);
        llEndDate = findViewById(R.id.ii_end_date);

//Text View initialisation
        textViewSun = findViewById(R.id.text_sun_num);
        textViewMon = findViewById(R.id.text_mon_num);
        textViewTues = findViewById(R.id.text_tue_num);
        textViewWed = findViewById(R.id.text_wed_num);
        textViewThur = findViewById(R.id.text_thr_num);
        textViewFri = findViewById(R.id.text_fri_num);
        textViewSat = findViewById(R.id.text_sat_num);
        textViewCommonAllCounter = textViewSun;

// image button check......
        imageButtonsun = findViewById(R.id.imagebutton_dot_sun);
        imageButtonmon = findViewById(R.id.imagebutton_dot_mon);
        imageButtontue = findViewById(R.id.imagebutton_dot_tues);
        imageButtonwed = findViewById(R.id.imagebutton_dot_wed);
        imageButtonthr = findViewById(R.id.imagebutton_dot_thur);
        imageButtonfri = findViewById(R.id.imagebutton_dot_fri);
        imageButtonsat = findViewById(R.id.imagebutton_dot_sat);

        imageButtonCommon = imageButtonsun;


        final String itemName = getIntent().getStringExtra("itemname");
        String itemId = getIntent().getStringExtra("itemid");


        String itemPrice1000 = getIntent().getStringExtra("itemprice1000");
        String itemPrice200 = getIntent().getStringExtra("itemprice200");


        String itemsubcription1000 = getIntent().getStringExtra("itemsubcription1000");
        String itemsubcription200 = getIntent().getStringExtra("itemsubcription200");


        String itemdetail = getIntent().getStringExtra("itemdetail");

        String itembenefits = getIntent().getStringExtra("itembenefits");
        String itemprevent = getIntent().getStringExtra("itemprevent");


        String itemonetime1000 = getIntent().getStringExtra("itemonetime1000");
        String itemonetime200 = getIntent().getStringExtra("itemonetime200");

        String stock = getIntent().getStringExtra("stock");


        itemImage = getIntent().getStringExtra("itemimage");

        textViewBenefits = findViewById(R.id.item_benefits);
        textViewPrevent = findViewById(R.id.item_prevent);

        textViewDetails = findViewById(R.id.item_details);
        textViewDetailsHeading = findViewById(R.id.text_details_heading);
        textViewPreventHeading = findViewById(R.id.item_detail_prevent_heading);
        textViewBenefitsHeading = findViewById(R.id.item_detail_benefit_headng);
        textViewBenefitsHeading.setText("Benefits of " + itemName + " Juices");

        textViewDetailsHeading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textViewDetails.getVisibility() == View.VISIBLE) {
                    textViewDetails.setVisibility(View.GONE);


                } else {
                    textViewDetails.setVisibility(View.VISIBLE);
                }
            }
        });


        textViewPreventHeading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textViewPrevent.getVisibility() == View.VISIBLE) {
                    textViewPrevent.setVisibility(View.GONE);


                } else {
                    textViewPrevent.setVisibility(View.VISIBLE);
                }
            }
        });

        textViewBenefitsHeading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textViewBenefits.getVisibility() == View.VISIBLE) {
                    textViewBenefits.setVisibility(View.GONE);


                } else {
                    textViewBenefits.setVisibility(View.VISIBLE);
                }
            }
        });

        textViewDetails.setText(itemdetail);
        textViewItemName = findViewById(R.id.text_item_name);

        textViewOneTimeCut = findViewById(R.id.cross_one_time);

        textViewItemName2 = findViewById(R.id.item_name);
        textViewItemPrice = findViewById(R.id.item_price);
        imageViewItem = findViewById(R.id.item_image);
        textViewDetails = findViewById(R.id.item_details);
        textViewOneTimeCut.setPaintFlags(textViewOneTimeCut.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


        textViewSub = findViewById(R.id.item_advance_subcription);


        textViewDiscount = findViewById(R.id.item_discount);
        textViewBenefits.setText(itembenefits);
        textViewPrevent.setText(itemprevent);


        findViewById(R.id.item_detail_subscription).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ItemDetailsActicity.this, SubscrActivity.class);
                startActivity(intent);
            }
        });

        textViewItemName2.setText(itemName);


        textViewItemPrice.setText("MRP ₹" + itemPrice200);
        textViewSub.setText("₹" + itemsubcription200);
        textViewDiscount.setText("One Time Price ₹" + itemonetime200);
        textViewOneTimeCut.setText("₹" + itemonetime200);

        itemonetime = itemonetime200;
        itemquantity = "200";
        itemsubcription = itemsubcription200;


        radioButton200.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    radioButton1000.setChecked(false);
                    textViewItemPrice.setText("MRP ₹" + itemPrice200);


                    textViewSub.setText("₹" + itemsubcription200);
                    textViewDiscount.setText("One Time Price ₹" + itemonetime200);
                    textViewOneTimeCut.setText("₹" + itemonetime200);

                    itemonetime = itemonetime200;
                    itemquantity = "200";
                    itemsubcription = itemsubcription200;
                }

            }
        });

        radioButton1000.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) {
                    radioButton200.setChecked(false);
                    textViewItemPrice.setText("MRP ₹" + itemPrice1000);


                    textViewSub.setText("₹" + itemsubcription1000);


                    textViewDiscount.setText("One Time Price ₹" + itemonetime1000);
                    textViewOneTimeCut.setText("₹" + itemonetime1000);


                    itemonetime = itemonetime1000;
                    itemquantity = "1000";
                    itemsubcription = itemsubcription1000;
                }

            }
        });


        textViewItemName.setText(itemName);


        Glide.with(this).load(itemImage).into(imageViewItem);

        buttonWhatsapp = findViewById(R.id.button_whatsapp);

        if (!stock.equals("0")) {
            buttonWhatsapp.setText("Out Of Stock");
            buttonWhatsapp.setEnabled(false);
            buttonWhatsapp.setBackgroundColor(Color.DKGRAY);

            rlFrequency.setVisibility(View.GONE);
        }


        imageButtonMinus = findViewById(R.id.button_minus);
        imageButtonPlus = findViewById(R.id.button_plus);
        textViewQuantityplusmminus = findViewById(R.id.text_quantity_plus_minus);


        ch1 = findViewById(R.id.check_everyday);
        ch2 = findViewById(R.id.check_one_time);
        ch3 = findViewById(R.id.check_interval);
        ch4 = findViewById(R.id.check_customize);


        llonetime = findViewById(R.id.ll_one_time);
        lloninterval = findViewById(R.id.ll_on_interval);
        llcustomize = findViewById(R.id.ll_costomize);

        ch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {

                    textViewHeading.setText("Starting On: ");
                    CommonClass.deliveryTime = "";
                    currentDate = day + 1 + "/" + (month + 1) + "/" + year;
                    llPlusMinus.setVisibility(View.VISIBLE);
                    llDate.setVisibility(View.VISIBLE);
                    relativeLayoutSubgrid.setVisibility(View.VISIBLE);
                    buttonWhatsapp.setEnabled(true);
                    buttonWhatsapp.setText("Subscribe now");
                    textViewCalenderstart.setText(currentDate);
                    textViewCalendarends.setText("(optional:)");
                    selcted(button1);
                    textViewSun.setText("0");
                    textViewMon.setText("0");
                    textViewTues.setText("0");
                    textViewWed.setText("0");
                    textViewThur.setText("0");
                    textViewFri.setText("0");
                    textViewSat.setText("0");

                    Drawable drawable = getResources().getDrawable(R.drawable.ic_dot);
                    imageButtonsun.setBackgroundResource(R.color.fui_transparent);
                    imageButtonsun.setImageDrawable(drawable);
                    imageButtonmon.setBackgroundResource(R.color.fui_transparent);
                    imageButtonmon.setImageDrawable(drawable);
                    imageButtontue.setBackgroundResource(R.color.fui_transparent);
                    imageButtontue.setImageDrawable(drawable);

                    imageButtonwed.setBackgroundResource(R.color.fui_transparent);
                    imageButtonwed.setImageDrawable(drawable);


                    imageButtonthr.setBackgroundResource(R.color.fui_transparent);
                    imageButtonthr.setImageDrawable(drawable);


                    imageButtonfri.setBackgroundResource(R.color.fui_transparent);
                    imageButtonfri.setImageDrawable(drawable);

                    imageButtonsat.setBackgroundResource(R.color.fui_transparent);
                    imageButtonsat.setImageDrawable(drawable);

                    textViewQuantityplusmminus.setText("0");
                    ch2.setChecked(false);
                    ch3.setChecked(false);
                    ch4.setChecked(false);


                    llStartDate.setVisibility(View.VISIBLE);
                    llEndDate.setVisibility(View.VISIBLE);

                    llonetime.setVisibility(View.GONE);
                    lloninterval.setVisibility(View.GONE);
                    llcustomize.setVisibility(View.GONE);


                    strOrderType = "everyday";
                }
            }
        });
        ch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    textViewHeading.setText("Order On: ");
                    CommonClass.deliveryTime = "";
                    llPlusMinus.setVisibility(View.VISIBLE);
                    currentDate = day + "/" + (month + 1) + "/" + year;
                    llDate.setVisibility(View.VISIBLE);
                    relativeLayoutSubgrid.setVisibility(View.VISIBLE);
                    strOrderType = "one time";
                    buttonWhatsapp.setEnabled(true);
                    buttonWhatsapp.setText("order now");
                    textViewCalenderstart.setText(currentDate);
                    textViewCalendarends.setText("(optional:)");
                    selcted(button1);
                    textViewSun.setText("0");
                    textViewMon.setText("0");
                    textViewTues.setText("0");
                    textViewWed.setText("0");
                    textViewThur.setText("0");
                    textViewFri.setText("0");
                    textViewSat.setText("0");
                    Drawable drawable = getResources().getDrawable(R.drawable.ic_dot);
                    imageButtonsun.setBackgroundResource(R.color.fui_transparent);
                    imageButtonsun.setImageDrawable(drawable);
                    imageButtonmon.setBackgroundResource(R.color.fui_transparent);
                    imageButtonmon.setImageDrawable(drawable);
                    imageButtontue.setBackgroundResource(R.color.fui_transparent);
                    imageButtontue.setImageDrawable(drawable);

                    imageButtonwed.setBackgroundResource(R.color.fui_transparent);
                    imageButtonwed.setImageDrawable(drawable);


                    imageButtonthr.setBackgroundResource(R.color.fui_transparent);
                    imageButtonthr.setImageDrawable(drawable);


                    imageButtonfri.setBackgroundResource(R.color.fui_transparent);
                    imageButtonfri.setImageDrawable(drawable);

                    imageButtonsat.setBackgroundResource(R.color.fui_transparent);
                    imageButtonsat.setImageDrawable(drawable);
                    textViewCommonAllCounter.setText("0");
                    textViewQuantityplusmminus.setText("0");
                    ch1.setChecked(false);
                    ch3.setChecked(false);
                    ch4.setChecked(false);
                    llonetime.setVisibility(View.VISIBLE);
                    llStartDate.setVisibility(View.VISIBLE);
                    llEndDate.setVisibility(View.GONE);
                    llcustomize.setVisibility(View.GONE);
                    lloninterval.setVisibility(View.GONE);

                }
            }
        });
        ch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    textViewHeading.setText("Starting On: ");
                    CommonClass.deliveryTime = "";
                    llPlusMinus.setVisibility(View.VISIBLE);
                    currentDate = day + 1 + "/" + (month + 1) + "/" + year;
                    llDate.setVisibility(View.VISIBLE);
                    relativeLayoutSubgrid.setVisibility(View.VISIBLE);
                    strOrderType = "on interval";
                    buttonWhatsapp.setEnabled(true);
                    buttonWhatsapp.setText("Subscribe now");
                    textViewCalenderstart.setText(currentDate);
                    textViewCalendarends.setText("(optional:)");
                    selcted(button1);
                    textViewSun.setText("0");
                    textViewMon.setText("0");
                    textViewTues.setText("0");
                    textViewWed.setText("0");
                    textViewThur.setText("0");
                    textViewFri.setText("0");
                    textViewSat.setText("0");
                    textViewCommonAllCounter.setText("0");
                    textViewQuantityplusmminus.setText("0");
                    ch2.setChecked(false);
                    ch1.setChecked(false);
                    ch4.setChecked(false);
                    lloninterval.setVisibility(View.VISIBLE);
                    llStartDate.setVisibility(View.VISIBLE);
                    llEndDate.setVisibility(View.VISIBLE);
                    llcustomize.setVisibility(View.GONE);
                    Drawable drawable = getResources().getDrawable(R.drawable.ic_dot);
                    imageButtonsun.setBackgroundResource(R.color.fui_transparent);
                    imageButtonsun.setImageDrawable(drawable);
                    imageButtonmon.setBackgroundResource(R.color.fui_transparent);
                    imageButtonmon.setImageDrawable(drawable);
                    imageButtontue.setBackgroundResource(R.color.fui_transparent);
                    imageButtontue.setImageDrawable(drawable);

                    imageButtonwed.setBackgroundResource(R.color.fui_transparent);
                    imageButtonwed.setImageDrawable(drawable);


                    imageButtonthr.setBackgroundResource(R.color.fui_transparent);
                    imageButtonthr.setImageDrawable(drawable);


                    imageButtonfri.setBackgroundResource(R.color.fui_transparent);
                    imageButtonfri.setImageDrawable(drawable);

                    imageButtonsat.setBackgroundResource(R.color.fui_transparent);
                    imageButtonsat.setImageDrawable(drawable);
                    llonetime.setVisibility(View.GONE);
                }
            }
        });
        ch4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {

                    textViewHeading.setText("Starting On: ");
                    llPlusMinus.setVisibility(View.VISIBLE);
                    currentDate = day + 1 + "/" + (month + 1) + "/" + year;
                    CommonClass.deliveryTime = "";
                    llDate.setVisibility(View.VISIBLE);
                    relativeLayoutSubgrid.setVisibility(View.VISIBLE);
                    strOrderType = "customize";
                    buttonWhatsapp.setEnabled(true);
                    buttonWhatsapp.setText("Subscribe now");
                    textViewCalenderstart.setText(currentDate);
                    textViewCalendarends.setText("(optional:)");
                    selcted(button1);
                    textViewSun.setText("0");
                    textViewMon.setText("0");
                    textViewTues.setText("0");
                    textViewWed.setText("0");
                    textViewThur.setText("0");
                    textViewFri.setText("0");
                    textViewSat.setText("0");

                    textViewQuantityplusmminus.setText("0");
                    ch2.setChecked(false);
                    ch3.setChecked(false);
                    ch1.setChecked(false);
                    llcustomize.setVisibility(View.VISIBLE);
                    Drawable drawable = getResources().getDrawable(R.drawable.ic_dot);
                    imageButtonsun.setBackgroundResource(R.color.fui_transparent);
                    imageButtonsun.setImageDrawable(drawable);
                    imageButtonmon.setBackgroundResource(R.color.fui_transparent);
                    imageButtonmon.setImageDrawable(drawable);
                    imageButtontue.setBackgroundResource(R.color.fui_transparent);
                    imageButtontue.setImageDrawable(drawable);

                    imageButtonwed.setBackgroundResource(R.color.fui_transparent);
                    imageButtonwed.setImageDrawable(drawable);


                    imageButtonthr.setBackgroundResource(R.color.fui_transparent);
                    imageButtonthr.setImageDrawable(drawable);


                    imageButtonfri.setBackgroundResource(R.color.fui_transparent);
                    imageButtonfri.setImageDrawable(drawable);

                    imageButtonsat.setBackgroundResource(R.color.fui_transparent);
                    imageButtonsat.setImageDrawable(drawable);
                    llonetime.setVisibility(View.GONE);
                    llStartDate.setVisibility(View.VISIBLE);
                    llEndDate.setVisibility(View.VISIBLE);
                    lloninterval.setVisibility(View.GONE);
                }
            }
        });
        textViewCalenderstart = findViewById(R.id.starting_time_clalender);
        textViewCalenderstart.setText(currentDate);
        CommonClass.date = currentDate;
        textViewCalenderstart.setOnClickListener(view -> {

            Bundle bundle = new Bundle();
            bundle.putInt("DATE", 1);

            DialogFragment datePicker = new DatePickerFragment();
            datePicker.setArguments(bundle);
            datePicker.show(getSupportFragmentManager(), "date picker");

        });
        textViewCalendarends = findViewById(R.id.ends_time_clalender);
        textViewCalendarends.setOnClickListener(view -> {
            //Toast.makeText(ItemDetailsActicity.this, "clicke", Toast.LENGTH_SHORT).show();

            Bundle bundle = new Bundle();
            bundle.putInt("DATE", 2);

            DialogFragment datePicker = new DatePickerFragment();
            datePicker.setArguments(bundle);
            datePicker.show(getSupportFragmentManager(), "date picker");

        });
        button1 = findViewById(R.id.sun_day);
        button1.setOnClickListener(view -> {
            selcted(button1);
            textViewCommonAllCounter = textViewSun;
            textViewQuantityplusmminus.setText(textViewCommonAllCounter.getText().toString());
            imageButtonCommon = imageButtonsun;

            initialState2();

        });
        button2 = findViewById(R.id.mon_day);
        button2.setOnClickListener(view -> {
            selcted(button2);
            textViewCommonAllCounter = textViewMon;
            textViewQuantityplusmminus.setText(textViewCommonAllCounter.getText().toString());

            imageButtonCommon = imageButtonmon;

            initialState2();

        });
        button3 = findViewById(R.id.tues_day);
        button3.setOnClickListener(view -> {
            selcted(button3);
            textViewCommonAllCounter = textViewTues;
            textViewQuantityplusmminus.setText(textViewCommonAllCounter.getText().toString());


            imageButtonCommon = imageButtontue;

            initialState2();
        });
        button4 = findViewById(R.id.wed_day);
        button4.setOnClickListener(view -> {
            selcted(button4);
            textViewCommonAllCounter = textViewWed;
            textViewQuantityplusmminus.setText(textViewCommonAllCounter.getText().toString());


            imageButtonCommon = imageButtonwed;

            initialState2();

        });
        button5 = findViewById(R.id.thur_day);
        button5.setOnClickListener(view -> {
            selcted(button5);
            textViewCommonAllCounter = textViewThur;
            textViewQuantityplusmminus.setText(textViewCommonAllCounter.getText().toString());

            imageButtonCommon = imageButtonthr;

            initialState2();

        });
        button6 = findViewById(R.id.fri_day);
        button6.setOnClickListener(view -> {
            selcted(button6);
            textViewCommonAllCounter = textViewFri;
            textViewQuantityplusmminus.setText(textViewCommonAllCounter.getText().toString());

            imageButtonCommon = imageButtonfri;

            initialState2();

        });
        button7 = findViewById(R.id.sat_day);
        button7.setOnClickListener(view -> {
            selcted(button7);
            textViewCommonAllCounter = textViewSat;
            textViewQuantityplusmminus.setText(textViewCommonAllCounter.getText().toString());
            imageButtonCommon = imageButtonsat;

            initialState2();

        });
        buttons = new Button[7];
        buttons[0] = button1;
        buttons[1] = button2;
        buttons[2] = button3;
        buttons[3] = button4;
        buttons[4] = button5;
        buttons[5] = button6;
        buttons[6] = button7;
        imageButtonPlus.setOnClickListener(view -> {
            int max = 5;

            if (textViewCommonAllCounter.getText().equals("0")) {
                textViewQuantityplusmminus.setTextColor(Color.GRAY);
            } else {
                textViewQuantityplusmminus.setTextColor(Color.BLACK);

            }
            imageButtonMinus.setEnabled(true);
            int current = Integer.parseInt(textViewQuantityplusmminus.getText().toString().trim());
            int currentcounter = Integer.parseInt(textViewCommonAllCounter.getText().toString().trim());

            if (current != 5) {

                current = current + 1;
                currentcounter = currentcounter + 1;
                String currentstr = String.valueOf(current);
                String currentcounterstr = String.valueOf(currentcounter);
                textViewQuantityplusmminus.setText(currentstr);
                textViewQuantityplusmminus.setTextColor(Color.BLACK);

                textViewCommonAllCounter.setText(currentcounterstr);
                textViewCommonAllCounter.setTextColor(Color.BLACK);


            } else {
                textViewQuantityplusmminus.setEnabled(false);
            }

            if (textViewCommonAllCounter.getText().toString().equals("0")) {
                Drawable drawable = getResources().getDrawable(R.drawable.ic_dot);
                imageButtonCommon.setBackgroundResource(R.color.fui_transparent);
                imageButtonCommon.setImageDrawable(drawable);
                textViewCommonAllCounter.setTextColor(Color.GRAY);
                textViewQuantityplusmminus.setTextColor(Color.GRAY);
            } else {
                Drawable drawable = getResources().getDrawable(R.drawable.ic_baseline_check_24);
                imageButtonCommon.setBackgroundResource(R.drawable.check_bg);
                imageButtonCommon.setImageDrawable(drawable);
                textViewCommonAllCounter.setTextColor(Color.BLACK);
                textViewQuantityplusmminus.setTextColor(Color.BLACK);
            }

        });
        imageButtonMinus.setOnClickListener(view -> {
            int min = 0;
            if (textViewCommonAllCounter.getText().equals("0")) {
                textViewQuantityplusmminus.setTextColor(Color.GRAY);
            } else {
                textViewQuantityplusmminus.setTextColor(Color.BLACK);

            }
            imageButtonPlus.setEnabled(true);


            int current = Integer.parseInt(textViewQuantityplusmminus.getText().toString().trim());
            int currentcounter = Integer.parseInt(textViewCommonAllCounter.getText().toString().trim());


            if (current != min) {

                current = current - 1;
                currentcounter = currentcounter - 1;

                String currentstr = String.valueOf(current);
                String currentcounterstr = String.valueOf(currentcounter);
                textViewQuantityplusmminus.setText(currentstr);
                textViewCommonAllCounter.setText(currentcounterstr);

            } else {
                imageButtonMinus.setEnabled(false);
            }

            if (textViewCommonAllCounter.getText().toString().equals("0")) {
                Drawable drawable = getResources().getDrawable(R.drawable.ic_dot);
                imageButtonCommon.setBackgroundResource(R.color.fui_transparent);
                imageButtonCommon.setImageDrawable(drawable);
                textViewCommonAllCounter.setTextColor(Color.GRAY);
                textViewQuantityplusmminus.setTextColor(Color.GRAY);
            } else {
                Drawable drawable = getResources().getDrawable(R.drawable.ic_baseline_check_24);
                imageButtonCommon.setBackgroundResource(R.drawable.check_bg);
                imageButtonCommon.setImageDrawable(drawable);
                textViewCommonAllCounter.setTextColor(Color.BLACK);
                textViewQuantityplusmminus.setTextColor(Color.BLACK);
            }


        });


        relativeLayoutSubgrid = findViewById(R.id.sb_grid);
        llDate = findViewById(R.id.ll_date);

        radioButtonSecondDay = findViewById(R.id.radio_every_2nd_day);
        radioButtonThirdDay = findViewById(R.id.radio_every_3rd_day);
        radioButtonFourthDay = findViewById(R.id.radio_every_4th_day);
        radioButtonNthDay = findViewById(R.id.radio_every_nth_day);

        radioButtonSecondDay.setOnCheckedChangeListener((compoundButton, b) -> {

            if (b) {
                radioButtonThirdDay.setChecked(false);
                radioButtonFourthDay.setChecked(false);
                radioButtonNthDay.setChecked(false);
                strIntervalType = "2nd";
                editTextNthDay.setText("");
                editTextNthDay.setEnabled(false);
                editTextNthDay.setVisibility(View.GONE);

            }
        });

        radioButtonThirdDay.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                radioButtonSecondDay.setChecked(false);
                radioButtonFourthDay.setChecked(false);
                radioButtonNthDay.setChecked(false);

                strIntervalType = "3rd";
                editTextNthDay.setText("");

                editTextNthDay.setEnabled(false);
                editTextNthDay.setVisibility(View.GONE);

            }
        });


        radioButtonFourthDay.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                radioButtonThirdDay.setChecked(false);
                radioButtonSecondDay.setChecked(false);
                radioButtonNthDay.setChecked(false);

                strIntervalType = "4th";
                editTextNthDay.setText("");

                editTextNthDay.setEnabled(false);
                editTextNthDay.setVisibility(View.GONE);


            }
        });


        radioButtonNthDay.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {

                editTextNthDay.setVisibility(View.VISIBLE);
                editTextNthDay.setEnabled(true);
                radioButtonThirdDay.setChecked(false);
                radioButtonFourthDay.setChecked(false);
                radioButtonSecondDay.setChecked(false);
                strIntervalType = "nth";


            }
        });

        buttonWhatsapp.setOnClickListener(view -> {

            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference referenceroot = FirebaseDatabase.getInstance().getReference();
            DatabaseReference referenceSub = referenceroot.child("SubscriptionList").child(firebaseUser.getPhoneNumber());
            DatabaseReference referenceOrd = referenceroot.child("OrderList").child(firebaseUser.getPhoneNumber());


            if (!strOrderType.equals("")) {

                if (!textViewQuantityplusmminus.getText().toString().equals("0")) {

                    if (!CommonClass.address.equals("address")) {
                        if (!textVieDeliveryStatus.getText().toString().trim().equals("Cannot deliver to :")) {
                            if (strOrderType.equals("one time")) {
                                int price = Integer.parseInt(itemonetime) * Integer.parseInt(textViewQuantityplusmminus.getText().toString().trim());
                                if (Integer.parseInt(CommonClass.walletmoney) >= price) {

                                    if (!CommonClass.deliveryTime.equals("")) {
                                        time = time();
                                        date = date();
                                        String orderid = String.valueOf(generateTimeStamp());
                                        OrderInfoMo orderInfoMo = new OrderInfoMo();
                                        orderInfoMo.setOrderId(orderid);
                                        orderInfoMo.setOrderTime(String.valueOf(time));
                                        orderInfoMo.setOrderDate(String.valueOf(date));
                                        orderInfoMo.setOrderitemName(String.valueOf(itemName));
                                        orderInfoMo.setOrderQuantity(itemquantity);
                                        orderInfoMo.setOrderitemImage(itemImage);
                                        orderInfoMo.setOrderitemPrice(String.valueOf(price));
                                        orderInfoMo.setDeliveryStatus("0");
                                        orderInfoMo.setDeliveryTime(CommonClass.deliveryTime);
                                        orderInfoMo.setOrderPlusMinusQuantity(textViewQuantityplusmminus.getText().toString().trim());
                                        orderInfoMo.setOrderStartTime(textViewCalenderstart.getText().toString().trim());

                                        orderInfoMo.setOrderAddress(CommonClass.address);
                                        orderInfoMo.setSubscriptionstatus("Active");


                                        orderInfoMo.setTimeStamp(String.valueOf(generateTimeStamp()));


                                        remainMoneyinWallet(price);

                                        transactionHistoryUpdate(orderid, price);
                                        referenceOrd.child(orderid).setValue(orderInfoMo);

                                        Toast.makeText(this, "Order Placed", Toast.LENGTH_SHORT).show();
                                        initialState();
                                    } else {
                                        DialogTime dialogTime = new DialogTime();
                                        dialogTime.show(getSupportFragmentManager(), "hello");
                                    }


                                } else {
                                    openWalletAlert(price, "2");
                                }
                            } else if (strOrderType.equals("everyday")) {
                                if (Integer.parseInt(CommonClass.walletmoney) >= 500) {

                                    if (!CommonClass.deliveryTime.equals("")) {


                                        String orderid = String.valueOf(generateTimeStamp());
//
                                        OrderInfoMo orderInfoMo = new OrderInfoMo();
                                        orderInfoMo.setOrderId(orderid);
                                        orderInfoMo.setSubscriptionstatus("Active");

                                        orderInfoMo.setOrderTime(time());
                                        orderInfoMo.setOrderDate(date());
                                        orderInfoMo.setOrderitemName(itemName);

                                        orderInfoMo.setOrderAddress(CommonClass.address);


                                        orderInfoMo.setOrderQuantity(itemquantity);
                                        orderInfoMo.setOrderitemImage(itemImage);


                                        orderInfoMo.setDeliveryStatus("0");
                                        orderInfoMo.setDeliveryTime(CommonClass.deliveryTime);
                                        orderInfoMo.setOrderStartTime(textViewCalenderstart.getText().toString().trim());
                                        orderInfoMo.setOrderPlusMinusQuantity(textViewQuantityplusmminus.getText().toString().trim());
                                        orderInfoMo.setDeliveryTime(orderid);


                                        orderInfoMo.setOrderType("EveryDay");

                                        orderInfoMo.setOrderEndTime(textViewCalendarends.getText().toString().trim());


                                        referenceSub.child(orderid).setValue(orderInfoMo);

                                        Toast.makeText(this, "Order Placed", Toast.LENGTH_SHORT).show();
                                        initialState();


                                    } else {
                                        DialogTime dialogTime = new DialogTime();
                                        dialogTime.show(getSupportFragmentManager(), "hello");


                                    }


                                } else {
                                    openWalletAlert(500, "3");
                                }


                            } else if (strOrderType.equals("on interval")) {
                                if (!strIntervalType.equals("0")) {

                                    if (strIntervalType.equals("2nd")) {

                                        if (Integer.parseInt(CommonClass.walletmoney) >= 500) {
                                            if (!CommonClass.deliveryTime.equals("")) {
                                                String orderid = String.valueOf(generateTimeStamp());
                                                OrderInfoMo orderInfoMo = new OrderInfoMo();
                                                orderInfoMo.setOrderId(orderid);
                                                orderInfoMo.setOrderTime(time());
                                                orderInfoMo.setOrderDate(date());
                                                orderInfoMo.setOrderitemName(String.valueOf(itemName));
                                                orderInfoMo.setOrderQuantity(itemquantity);
                                                orderInfoMo.setOrderitemImage(itemImage);
                                                orderInfoMo.setSubscriptionstatus("Active");

                                                orderInfoMo.setDeliveryStatus("0");
                                                orderInfoMo.setDeliveryTime(CommonClass.deliveryTime);
                                                orderInfoMo.setOrderPlusMinusQuantity(textViewQuantityplusmminus.getText().toString().trim());
                                                orderInfoMo.setOrderStartTime(textViewCalenderstart.getText().toString().trim());
                                                orderInfoMo.setTimeStamp(orderid);
                                                orderInfoMo.setOrderIntervalType(strIntervalType);
                                                orderInfoMo.setOrderType("On Interval");

                                                orderInfoMo.setOrderEndTime(textViewCalendarends.getText().toString().trim());

                                                orderInfoMo.setOrderAddress(CommonClass.address);

                                                referenceSub.child(orderid).setValue(orderInfoMo);

                                                Toast.makeText(this, "Order Placed", Toast.LENGTH_SHORT).show();
                                                initialState();


                                            } else {
                                                DialogTime dialogTime = new DialogTime();

                                                dialogTime.show(getSupportFragmentManager(), "hello");
                                            }

                                        } else {
                                            openWalletAlert(500, "3");
                                        }


                                    } else if (strIntervalType.equals("3rd")) {

                                        if (Integer.parseInt(CommonClass.walletmoney) >= 500) {
                                            if (!CommonClass.deliveryTime.equals("")) {

                                                String orderid = String.valueOf(generateTimeStamp());
                                                OrderInfoMo orderInfoMo = new OrderInfoMo();
                                                orderInfoMo.setOrderAddress(CommonClass.address);
                                                orderInfoMo.setSubscriptionstatus("Active");

                                                orderInfoMo.setOrderId(orderid);
                                                orderInfoMo.setOrderTime(time());
                                                orderInfoMo.setOrderDate(date());
                                                orderInfoMo.setOrderitemName(String.valueOf(itemName));
                                                orderInfoMo.setOrderQuantity(itemquantity);
                                                orderInfoMo.setOrderitemImage(itemImage);

                                                orderInfoMo.setDeliveryStatus("0");
                                                orderInfoMo.setDeliveryTime(CommonClass.deliveryTime);
                                                orderInfoMo.setOrderPlusMinusQuantity(textViewQuantityplusmminus.getText().toString().trim());
                                                orderInfoMo.setOrderStartTime(textViewCalenderstart.getText().toString().trim());
                                                orderInfoMo.setTimeStamp(orderid);
                                                orderInfoMo.setOrderIntervalType(strIntervalType);
                                                orderInfoMo.setOrderType("On Interval");
                                                orderInfoMo.setOrderEndTime(textViewCalendarends.getText().toString().trim());


                                                referenceSub.child(orderid).setValue(orderInfoMo);

                                                Toast.makeText(this, "Order Placed", Toast.LENGTH_SHORT).show();
                                                initialState();


                                            } else {
                                                DialogTime dialogTime = new DialogTime();

                                                dialogTime.show(getSupportFragmentManager(), "hello");
                                            }

                                        } else {
                                            openWalletAlert(500, "3");
                                        }


                                    } else if (strIntervalType.equals("4th")) {
                                        if (Integer.parseInt(CommonClass.walletmoney) >= 500) {
                                            if (!CommonClass.deliveryTime.equals("")) {
                                                String orderid = String.valueOf(generateTimeStamp());
                                                OrderInfoMo orderInfoMo = new OrderInfoMo();
                                                orderInfoMo.setOrderId(orderid);
                                                orderInfoMo.setOrderTime(time());
                                                orderInfoMo.setOrderDate(date());
                                                orderInfoMo.setOrderitemName(String.valueOf(itemName));
                                                orderInfoMo.setOrderQuantity(itemquantity);
                                                orderInfoMo.setOrderitemImage(itemImage);
                                                orderInfoMo.setOrderAddress(CommonClass.address);
                                                orderInfoMo.setSubscriptionstatus("Active");

                                                orderInfoMo.setDeliveryStatus("0");
                                                orderInfoMo.setDeliveryTime(CommonClass.deliveryTime);
                                                orderInfoMo.setOrderPlusMinusQuantity(textViewQuantityplusmminus.getText().toString().trim());
                                                orderInfoMo.setOrderStartTime(textViewCalenderstart.getText().toString().trim());
                                                orderInfoMo.setTimeStamp(String.valueOf(generateTimeStamp()));
                                                orderInfoMo.setOrderIntervalType(strIntervalType);
                                                orderInfoMo.setOrderType("On Interval");

                                                orderInfoMo.setOrderEndTime(textViewCalendarends.getText().toString().trim());
                                                //remainMoneyinWallet(price);
                                                // transactionHistoryUpdate(generateTimeStamp(),price);

                                                referenceSub.child(orderid).setValue(orderInfoMo);

                                                Toast.makeText(this, "Order Placed", Toast.LENGTH_SHORT).show();
                                                initialState();


                                            } else {
                                                DialogTime dialogTime = new DialogTime();

                                                dialogTime.show(getSupportFragmentManager(), "hello");
                                            }

                                        } else {
                                            openWalletAlert(500, "3");
                                        }


                                    } else if (strIntervalType.equals("nth")) {
                                        if (!editTextNthDay.getText().toString().trim().equals("")) {
                                            String newNth = editTextNthDay.getText().toString().trim() + "th";

                                            if (Integer.parseInt(CommonClass.walletmoney) >= 500) {
                                                if (!CommonClass.deliveryTime.equals("")) {

                                                    String orderid = String.valueOf(generateTimeStamp());
                                                    OrderInfoMo orderInfoMo = new OrderInfoMo();
                                                    orderInfoMo.setOrderId(orderid);
                                                    orderInfoMo.setOrderTime(time());
                                                    orderInfoMo.setOrderDate(date());
                                                    orderInfoMo.setOrderitemName(String.valueOf(itemName));
                                                    orderInfoMo.setOrderQuantity(itemquantity);
                                                    orderInfoMo.setOrderitemImage(itemImage);
                                                    orderInfoMo.setDeliveryStatus("0");
                                                    orderInfoMo.setDeliveryTime(CommonClass.deliveryTime);
                                                    orderInfoMo.setOrderPlusMinusQuantity(textViewQuantityplusmminus.getText().toString().trim());
                                                    orderInfoMo.setOrderStartTime(textViewCalenderstart.getText().toString().trim());
                                                    orderInfoMo.setTimeStamp(String.valueOf(generateTimeStamp()));
                                                    orderInfoMo.setOrderIntervalType(newNth);
                                                    orderInfoMo.setOrderType("On Interval");
                                                    orderInfoMo.setOrderAddress(CommonClass.address);
                                                    orderInfoMo.setSubscriptionstatus("Active");

                                                    orderInfoMo.setOrderEndTime(textViewCalendarends.getText().toString().trim());

                                                    //remainMoneyinWallet(price);
                                                    referenceSub.child(orderid).setValue(orderInfoMo);
                                                    //transactionHistoryUpdate(generateTimeStamp(),price);

                                                    Toast.makeText(this, "Order Placed", Toast.LENGTH_SHORT).show();
                                                    initialState();
                                                    Toast.makeText(this, "clickedgf", Toast.LENGTH_SHORT).show();

                                                } else {
                                                    DialogTime dialogTime = new DialogTime();

                                                    dialogTime.show(getSupportFragmentManager(), "hello");
                                                }

                                            } else {
                                                openWalletAlert(500, "3");
                                            }
                                        } else {
                                            Toast.makeText(this, "Plz select interval day", Toast.LENGTH_SHORT).show();
                                        }


                                    }
                                } else {
                                    Toast.makeText(this, "Select interval", Toast.LENGTH_SHORT).show();
                                }

                            } else if (strOrderType.equals("customize")) {

//                                    int total_day = Integer.parseInt(textViewSun.getText().toString().trim()) + Integer.parseInt(textViewMon.getText().toString().trim()) +
//                                            Integer.parseInt(textViewTues.getText().toString().trim()) + Integer.parseInt(textViewWed.getText().toString().trim()) +
//                                            Integer.parseInt(textViewThur.getText().toString().trim()) + Integer.parseInt(textViewFri.getText().toString().trim()) +
//                                            Integer.parseInt(textViewSat.getText().toString().trim());
//                                    int price = calculateMoney(textViewCalenderstart.getText().toString().trim(), textViewCalendarends.getText().toString().trim(), Integer.parseInt(itemsubcription), Integer.parseInt(textViewSun.getText().toString().trim()), Integer.parseInt(textViewMon.getText().toString().trim()), Integer.parseInt(textViewTues.getText().toString().trim()), Integer.parseInt(textViewWed.getText().toString().trim()), Integer.parseInt(textViewThur.getText().toString().trim()), Integer.parseInt(textViewFri.getText().toString().trim()), Integer.parseInt(textViewSat.getText().toString().trim()));

                                if (Integer.parseInt(CommonClass.walletmoney) >= 500) {
                                    if (!CommonClass.deliveryTime.equals("")) {
                                        time = time();
                                        date = date();
                                        String orderid = String.valueOf(generateTimeStamp());
                                        OrderInfoMo orderInfoMo = new OrderInfoMo();
                                        orderInfoMo.setOrderId(orderid);
                                        orderInfoMo.setOrderSunQuantity(textViewSun.getText().toString().trim());
                                        orderInfoMo.setOrderMonQuantity(textViewMon.getText().toString().trim());
                                        orderInfoMo.setOrderTueQuantity(textViewTues.getText().toString().trim());
                                        orderInfoMo.setOrderWedQuantity(textViewWed.getText().toString().trim());
                                        orderInfoMo.setOrderThrQuantity(textViewThur.getText().toString().trim());
                                        orderInfoMo.setOrderFriQuantity(textViewFri.getText().toString().trim());
                                        orderInfoMo.setOrderSatQuantity(textViewSat.getText().toString().trim());
                                        orderInfoMo.setSubscriptionstatus("Active");

                                        orderInfoMo.setOrderTime(String.valueOf(time));
                                        orderInfoMo.setOrderDate(String.valueOf(date));
                                        orderInfoMo.setOrderitemName(String.valueOf(itemName));
                                        orderInfoMo.setOrderQuantity(itemquantity);
                                        orderInfoMo.setOrderitemImage(itemImage);
                                        //    orderInfoMo.setOrderitemPrice(String.valueOf(price));
                                        orderInfoMo.setDeliveryStatus("0");
                                        orderInfoMo.setDeliveryTime(CommonClass.deliveryTime);
                                        // orderInfoMo.setOrderPlusMinusQuantity(textViewQuantityplusmminus.getText().toString().trim());
                                        orderInfoMo.setOrderStartTime(textViewCalenderstart.getText().toString().trim());
                                        orderInfoMo.setTimeStamp(String.valueOf(generateTimeStamp()));
                                        orderInfoMo.setOrderIntervalType(strIntervalType);
                                        orderInfoMo.setOrderType("Customize");
                                        orderInfoMo.setOrderAddress(CommonClass.address);

                                        orderInfoMo.setOrderEndTime(textViewCalendarends.getText().toString().trim());

                                        //remainMoneyinWallet(price);
                                        referenceSub.child(orderid).setValue(orderInfoMo);
                                        //transactionHistoryUpdate(generateTimeStamp(),price);

                                        Toast.makeText(this, "Order Placed", Toast.LENGTH_SHORT).show();
                                        initialState();


                                    } else {
                                        DialogTime dialogTime = new DialogTime();

                                        dialogTime.show(getSupportFragmentManager(), "hello");
                                    }

                                } else {
                                    openWalletAlert(500, "3");
                                }


                            }


                        } else {
                            DialogAddress dialogAddress = new DialogAddress();
                            dialogAddress.show(getSupportFragmentManager(), "hello");
                        }
                    } else {

                        DialogAddress dialogAddress = new DialogAddress();
                        dialogAddress.show(getSupportFragmentManager(), "hello");
                    }


                } else {
                    Toast.makeText(this, "Add atleast one quantity", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Select Order Type", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void openWalletAlert(int price, String type) {

        DialogWallet dialogWallet = new DialogWallet();

        Bundle bundle = new Bundle();
        bundle.putString("amount", String.valueOf(price));
        CommonClass.type = type;
        CommonClass.orderValue = String.valueOf(price);
        dialogWallet.setArguments(bundle);
        dialogWallet.show(getSupportFragmentManager(), "hello");


    }


    private void remainMoneyinWallet(int price) {

        int remainmoney = Integer.parseInt(CommonClass.walletmoney) - price;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        CommonClass.walletmoney = String.valueOf(remainmoney);
        reference.child("customer").child(user.getPhoneNumber()).child("userWalletMoney").setValue(CommonClass.walletmoney);
    }


    private void initialState2() {
        if (textViewCommonAllCounter.getText().equals("0")) {
            textViewQuantityplusmminus.setTextColor(Color.GRAY);
        } else {
            textViewQuantityplusmminus.setTextColor(Color.BLACK);

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


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(ItemDetailsActicity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            CommonClass.amount = "1000";
            CommonClass.deliveryTime = "";
        }


    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, i);
        c.set(Calendar.MONTH, i1);
        c.set(Calendar.DAY_OF_MONTH, i2);


        if (cur == START_DATE) {
            String currentDateString = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(c.getTime());

            if (ch4.isChecked()) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date date = null;
                try {
                    date = sdf.parse(currentDateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int newday = day + 7;
                cal.set(Calendar.DAY_OF_MONTH, newday);

                String caltime = cal.getTime().toString();

                Date newDate = null;
                try {
                    newDate = sdf.parse(caltime);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Toast.makeText(this, newDate.toString(), Toast.LENGTH_SHORT).show();

            } else {
                CommonClass.date = currentDateString;

            }
            textViewCalenderstart.setText(currentDateString.toString());

        } else {
            String currentDateString = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(c.getTime());


            textViewCalendarends.setText(currentDateString);

        }
    }

    private void selcted(Button button) {

        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i] == button) {
                buttons[i].setBackgroundResource(R.drawable.bg_week_button_round);
                buttons[i].setTextColor(Color.WHITE);

            } else {
                buttons[i].setTextColor(getResources().getColor(R.color.colorPrimary));
                buttons[i].setBackgroundResource(R.drawable.bg_week_button_round_unselected);


            }

        }


    }


    public void initialState() {

        CommonClass.deliveryTime = "";

        llDate.setVisibility(View.GONE);
        relativeLayoutSubgrid.setVisibility(View.GONE);

        ch1.setChecked(false);
        ch2.setChecked(false);
        ch3.setChecked(false);
        ch4.setChecked(false);

        strSunQua = "0";
        strMonQua = "0";
        strTueQua = "0";
        strWedQua = "0";
        strThrQua = "0";
        strFriQua = "0";
        strSatQua = "0";
        radioButtonSecondDay.setChecked(false);
        radioButtonThirdDay.setChecked(false);
        radioButtonFourthDay.setChecked(false);
        radioButtonNthDay.setChecked(false);


        selcted(button1);


        Drawable drawable = getResources().getDrawable(R.drawable.ic_dot);
        imageButtonsun.setBackgroundResource(R.color.fui_transparent);
        imageButtonsun.setImageDrawable(drawable);

        imageButtonmon.setBackgroundResource(R.color.fui_transparent);
        imageButtonmon.setImageDrawable(drawable);

        imageButtonthr.setBackgroundResource(R.color.fui_transparent);
        imageButtonthr.setImageDrawable(drawable);

        imageButtontue.setBackgroundResource(R.color.fui_transparent);
        imageButtontue.setImageDrawable(drawable);

        imageButtonwed.setBackgroundResource(R.color.fui_transparent);
        imageButtonwed.setImageDrawable(drawable);

        imageButtonsat.setBackgroundResource(R.color.fui_transparent);
        imageButtonsat.setImageDrawable(drawable);

        imageButtonfri.setBackgroundResource(R.color.fui_transparent);
        imageButtonfri.setImageDrawable(drawable);


        textViewSun.setText("0");
        textViewSun.setTextColor(Color.GRAY);
        textViewMon.setText("0");
        textViewMon.setTextColor(Color.GRAY);
        textViewTues.setText("0");
        textViewTues.setTextColor(Color.GRAY);
        textViewWed.setText("0");
        textViewWed.setTextColor(Color.GRAY);
        textViewThur.setText("0");
        textViewThur.setTextColor(Color.GRAY);
        textViewFri.setText("0");
        textViewFri.setTextColor(Color.GRAY);
        textViewSat.setText("0");
        textViewSat.setTextColor(Color.GRAY);

        textViewQuantityplusmminus.setText("0");
        textViewQuantityplusmminus.setTextColor(Color.GRAY);

        buttonWhatsapp.setEnabled(false);

        textViewCalendarends.setText("(optional: ");
        textViewCalenderstart.setText(currentDate);

    }


    private String countDay(String startDate, String endDate) {

        long diff, diff1 = 0;

        Calendar cal = Calendar.getInstance();


        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");


        try {
            Date date1 = sdf.parse(startDate);
            Date date2 = sdf.parse(endDate);


            String current = sdf.format(cal.getTime());

            Date today = sdf.parse(current);
            diff = (date2.getTime() - date1.getTime()) + 86400000;
            if (today.after(date1)) {
                diff1 = (date2.getTime() - today.getTime()) + 86400000;

            } else {
                diff1 = diff;
            }


        } catch (Exception e) {

        }
        String dayCount = String.valueOf(TimeUnit.DAYS.convert(diff1, TimeUnit.MILLISECONDS));


        return dayCount;
    }


    private int generateId() {
        Random rnd = new Random();
        orderidint = 100000 + rnd.nextInt(900000);

        return orderidint;

    }

    private long generateTimeStamp() {

        long timstamp = System.currentTimeMillis();
        return timstamp;
    }

    private String time() {


        DateFormat dfa = new SimpleDateFormat("HH:mm");
        time = dfa.format(Calendar.getInstance().getTime());
        return time;
    }


    private String date() {
        DateFormat df = new SimpleDateFormat("dd/MMM/yyyy");
        date = df.format(Calendar.getInstance().getTime());
        return date;
    }


    private int calculateMoney(String startDate, String endDate, int price, int sunQty, int monQty, int tueQty, int wedQty, int thrQty, int friQty, int satQty) {
        int totalQty = 0;
        int totalPrice = 0;
        int totalDay = Integer.parseInt(countDay(startDate, endDate));
        Date initialDate = stringToDate(startDate);
        for (int i = 0; i <= totalDay; i++) {
            int day = dayfromDate(initialDate);
            switch (day) {
                case 1:
                    totalQty = totalQty + sunQty;
                    totalPrice = totalPrice + totalQty * price;
                    break;

                case 2:
                    totalQty = totalQty + monQty;
                    totalPrice = totalPrice + totalQty * price;
                    break;
                case 3:
                    totalQty = totalQty + tueQty;
                    totalPrice = totalPrice + totalQty * price;
                    break;
                case 4:
                    totalQty = totalQty + wedQty;
                    totalPrice = totalPrice + totalQty * price;
                    break;
                case 5:
                    totalQty = totalQty + thrQty;
                    totalPrice = totalPrice + totalQty * price;
                    break;
                case 6:
                    totalQty = totalQty + friQty;
                    break;
                case 7:
                    totalQty = totalQty + satQty;

                    break;
            }


            Calendar c = Calendar.getInstance();
            c.setTime(initialDate);
            c.add(Calendar.DATE, 1);
            initialDate = c.getTime();

        }

        return price * totalQty;
    }

    private Date stringToDate(String strDate) {


        DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

        Date date = null;
        try {
            date = format.parse(strDate);
        } catch (Exception e) {

        }


        return date;
    }

    private int dayfromDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        return c.get(Calendar.DAY_OF_WEEK);

    }

    private void transactionHistoryUpdate(String orderid, int price) {

        TransactionMo transactionMo = new TransactionMo();
        transactionMo.setTxnid(String.valueOf(orderid));
        transactionMo.setTxnamount(String.valueOf(price));
        transactionMo.setTxntime(time() + " " + date());
        transactionMo.setType("minus");
        transactionMo.setTxnStatus("Successful");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        reference.child("TransactionHistory").child(user.getPhoneNumber()).child(String.valueOf(orderid)).setValue(transactionMo);


    }


}