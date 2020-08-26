package com.healthmantra.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.healthmantra.myapplication.Activity.TransactionHistory;
import com.healthmantra.myapplication.Models.CustomerInfo;
import com.healthmantra.myapplication.Paytm.Api;
import com.healthmantra.myapplication.Paytm.Checksum;
import com.healthmantra.myapplication.Paytm.Constants;
import com.healthmantra.myapplication.Paytm.Paytma;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import com.razorpay.Checkout;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Wallet extends AppCompatActivity implements PaytmPaymentTransactionCallback {
    Button button2, button3, button4, button5;
    EditText editText, editTextEmail;
    TextView amountInWallet, textView;
    DatabaseReference reference;

    String amount, email1;



    Toolbar toolbar;

    ImageButton walletHistory;

    private static final String TAG = Wallet.class.getSimpleName();

    FirebaseUser user1;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        user1 = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();



        editTextEmail = findViewById(R.id.et_email);


        textView = findViewById(R.id.tv_email_validate);


        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        editTextEmail.addTextChangedListener(new TextWatcher() {
            String email = editTextEmail.getText().toString().trim();

            public void afterTextChanged(Editable s) {

                if (email.matches(emailPattern) && s.length() > 0) {
                    // or

                    textView.setText("Valid email address");
                    textView.setTextColor(Color.GREEN);
                } else {
                    //or
                    email = editTextEmail.getText().toString().trim();
                    textView.setText("Invalid email address");
                    textView.setTextColor(Color.RED);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // other stuffs
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // other stuffs
            }
        });

        walletHistory = findViewById(R.id.ib_wallet_history);
        walletHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Wallet.this, TransactionHistory.class);
                startActivity(intent);
            }
        });

        toolbar = findViewById(R.id.toolbar_wallet);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


//        Bundle bundle = new Bundle();
//        amount = bundle.getString("amount");

        amount = CommonClass.amount;

        // Toast.makeText(this, amount, Toast.LENGTH_SHORT).show();
        //amount = getIntent().getExtras().getString("amount");

        //prevAmount = CommonClass.walletmoney;

        amountInWallet = findViewById(R.id.textView2);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        reference.child("customer").child(user.getPhoneNumber()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                CustomerInfo customerInfo = snapshot.getValue(CustomerInfo.class);
                String walletMoney = customerInfo.getUserWalletMoney();


                CommonClass.walletmoney = walletMoney;
                amountInWallet.setText(walletMoney);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        editText = findViewById(R.id.et_amount);
        editText.setText("1500");
        button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amount = editText.getText().toString().trim();
//                Random rnd = new Random();
//                int orderidint = 100000 + rnd.nextInt(900000);
//
//                Intent intent = new Intent(Wallet.this, checksum.class);
//                intent.putExtra("orderid", String.valueOf(orderidint));
//                intent.putExtra("custid", String.valueOf(orderidint));
//                startActivity(intent);
                if (!amount.equals("")) {
//                    startPayment();


                    generateCheckSum();

                    // email1 = editTextEmail.getText().toString().trim();
                }
            }
        });


        button3 = findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setText("500");
            }
        });
        button4 = findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setText("1500");
            }
        });
        button5 = findViewById(R.id.button5);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setText("3000");
            }
        });

        //updateTransactionData();
    }


    private void startPayment() {
        final Activity activity = this;


        /**
         * Instantiate Checkout
         */
        Checkout checkout = new Checkout();

        checkout.setKeyID("rzp_test_HYCfB8Yv7L5BVk");

        /**
         * Set your logo here
         */
        checkout.setImage(R.drawable.icon);

        /**
         * Reference to current activity
         */


        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            options.put("name", "HealthMntra");
            options.put("description", "Reference No. #123456");
            options.put("image", "http://healthmntra.com/appbanner/logo.png");
            //options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            //options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
            options.put("amount", amount + "00");//pass amount in currency subunits
            //options.put("prefill.email", "gaurav.kumar@example.com");
            //options.put("prefill.contact","9988776655");
            checkout.open(activity, options);
        } catch (Exception e) {
            Log.e(TAG, "Error in starting Razorpay Checkout", e);
        }
    }


//    @Override
//    public void onPaymentSuccess(String s) {
//        try {
//
//            Toast.makeText(this, "Payment Successful", Toast.LENGTH_LONG).show();
//
//            reference.child("customer").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()).child("userPaymentStatus")
//                    .setValue("1");
//
//            if (newuser.equals("0")) {
//                if (Integer.parseInt(amount) >= 1000 && Integer.parseInt(amount) <= 1499) {
//                    int cashback = Integer.parseInt(amount) * 7 / 100;
//
//                    reference.child("customer").child(user1.getPhoneNumber()).child("userFirstTimeStatus").setValue("1");
//                    TransactionMo transactionMo = new TransactionMo();
//                    transactionMo.setOrderid("0");
//                    transactionMo.setTxnid(String.valueOf(generateTimeStamp()));
//                    transactionMo.setTxnamount(String.valueOf(cashback));
//                    transactionMo.setTxnStatus("Successful");
//                    transactionMo.setTxntime(time() + " " + date());
//                    transactionMo.setType("cashback");
//                    reference.child("TransactionHistory").child(user1.getPhoneNumber()).child(String.valueOf(generateTimeStamp())).setValue(transactionMo);
//
//                    int updateAmount = Integer.parseInt(prevAmount) + Integer.parseInt(amount) + cashback;
//
//                    TransactionMo transactionMo1 = new TransactionMo();
//                    transactionMo1.setOrderid("0");
//                    transactionMo1.setTxnid(String.valueOf(generateTimeStamp()));
//                    transactionMo1.setTxnamount(amount);
//                    transactionMo1.setTxnStatus("Successful");
//                    transactionMo1.setTxntime(time() + " " + date());
//                    transactionMo1.setType("plus");
//
//
//                    reference.child("TransactionHistory").child(user1.getPhoneNumber()).child(String.valueOf(generateTimeStamp())).setValue(transactionMo1);
//
//
//                    reference.child("customer").child(user1.getPhoneNumber()).child("userWalletMoney").setValue(String.valueOf(updateAmount));
//
//
//                    amountInWallet.setText(String.valueOf(updateAmount));
//
//
//                } else if (Integer.parseInt(amount) >= 1500) {
//                    int cashback = Integer.parseInt(amount) * 10 / 100;
//
//                    CommonClass.cashback = cashback;
//
//                    Log.d("TAG", "onDataChange:  Cashback" + cashback);
//                    reference.child("customer").child(user1.getPhoneNumber()).child("userFirstTimeStatus").setValue("1");
//
//
//                    TransactionMo transactionMo = new TransactionMo();
//                    transactionMo.setOrderid("0");
//                    transactionMo.setTxnid(String.valueOf(generateTimeStamp()));
//                    transactionMo.setTxnamount(String.valueOf(cashback));
//                    transactionMo.setTxnStatus("Successful");
//                    transactionMo.setTxntime(time() + " " + date());
//                    transactionMo.setType("cashback");
//
//
//                    reference.child("TransactionHistory").child(user1.getPhoneNumber()).child(String.valueOf(generateTimeStamp())).setValue(transactionMo);
//
//                    int updateAmount = Integer.parseInt(prevAmount) + Integer.parseInt(amount) + cashback;
//
//                    TransactionMo transactionMo1 = new TransactionMo();
//                    transactionMo1.setOrderid("0");
//                    transactionMo1.setTxnid(String.valueOf(generateTimeStamp()));
//                    transactionMo1.setTxnamount(amount);
//                    transactionMo1.setTxnStatus("Successful");
//                    transactionMo1.setTxntime(time() + " " + date());
//                    transactionMo1.setType("plus");
//
//
//                    reference.child("TransactionHistory").child(user1.getPhoneNumber()).child(String.valueOf(generateTimeStamp())).setValue(transactionMo1);
//
//
//                    reference.child("customer").child(user1.getPhoneNumber()).child("userWalletMoney").setValue(String.valueOf(updateAmount));
//
//
//                    amountInWallet.setText(String.valueOf(updateAmount));
//
//
//                }
//
//            }
//            else {
//                reference.child("customer").child(user1.getPhoneNumber()).child("userFirstTimeStatus").setValue("1");
//                int updateAmount = Integer.parseInt(prevAmount) + Integer.parseInt(amount);
//
//                TransactionMo transactionMo1 = new TransactionMo();
//                transactionMo1.setOrderid("0");
//                transactionMo1.setTxnid(String.valueOf(generateTimeStamp()));
//                transactionMo1.setTxnamount(amount);
//                transactionMo1.setTxnStatus("Successful");
//                transactionMo1.setTxntime(time() + " " + date());
//                transactionMo1.setType("plus");
//
//
//                reference.child("TransactionHistory").child(user1.getPhoneNumber()).child(String.valueOf(generateTimeStamp())).setValue(transactionMo1);
//
//
//                reference.child("customer").child(user1.getPhoneNumber()).child("userWalletMoney").setValue(String.valueOf(updateAmount));
//
//
//                amountInWallet.setText(String.valueOf(updateAmount));
//
//
//            }
//
//
//        } catch (Exception e) {
//            Log.e(TAG, "Exception in onPaymentSuccess", e);
//        }
//    }
//
//    @Override
//    public void onPaymentError(int i, String s) {
//        try {
//            Toast.makeText(this, "Payment Failed", Toast.LENGTH_LONG).show();
//            reference.child("customer").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()).child("userPaymentStatus")
//                    .setValue("0");
////            if (!textView.getText().toString().trim().equals("Invalid email address")){
////                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
////                reference.child("customer").child(user.getPhoneNumber()).child("userEmail").setValue(email1);
////                CommonClass.email =email1;
////            }
//            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//            TransactionMo transactionMo = new TransactionMo();
//            transactionMo.setOrderid("0");
//            transactionMo.setTxnid(String.valueOf(generateTimeStamp()));
//            transactionMo.setTxnamount(amount);
//            transactionMo.setTxnStatus("Failed");
//            transactionMo.setTxntime(time() + " " + date());
//            transactionMo.setType("plus");
//
//
//            reference.child("TransactionHistory").child(user.getPhoneNumber()).child(String.valueOf(generateTimeStamp())).setValue(transactionMo);
//
//
//        } catch (Exception e) {
//            Log.d(TAG, "Exception in onPaymentError", e);
//        }
//    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    private long generateTimeStamp() {

        long timstamp = System.currentTimeMillis();
        return timstamp;
    }

    private String time() {


        DateFormat dfa = new SimpleDateFormat("HH:mm");
        String time = dfa.format(Calendar.getInstance().getTime());
        return time;
    }


    private String date() {
        DateFormat df = new SimpleDateFormat("dd/MMM/yyyy");
        String date = df.format(Calendar.getInstance().getTime());
        return date;
    }





//    private void updateTransactionData() {
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//
//        reference.child("customer").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())
//                .addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if (snapshot.exists()) {
//                            CustomerInfo customerInfo = snapshot.getValue(CustomerInfo.class);
//
//                            if (customerInfo.getUserPaymentStatus().equals("1")) {
//
//                                if (newuser.equals("0")) {
//
//                                    if (Integer.parseInt(amount) >= 1000 && Integer.parseInt(amount) <= 1499) {
//
//
//                                        int cashback = Integer.parseInt(amount) * 7 / 100;
//
//
//
//
//
//                                        reference.child("customer").child(user.getPhoneNumber()).child("userFirstTimeStatus").setValue("1");
//                                        TransactionMo transactionMo = new TransactionMo();
//                                        transactionMo.setOrderid("0");
//                                        transactionMo.setTxnid(String.valueOf(generateTimeStamp()));
//                                        transactionMo.setTxnamount(String.valueOf(cashback));
//                                        transactionMo.setTxnStatus("Successful");
//                                        transactionMo.setTxntime(time() + " " + date());
//                                        transactionMo.setType("cashback");
//                                        reference.child("TransactionHistory").child(user.getPhoneNumber()).child(String.valueOf(generateTimeStamp())).setValue(transactionMo);
//
//                                        int updateAmount = Integer.parseInt(prevAmount) + Integer.parseInt(amount) + cashback;
//
//
//
//                                        TransactionMo transactionMo1 = new TransactionMo();
//                                        transactionMo1.setOrderid("0");
//
//                                        transactionMo1.setTxnid(String.valueOf(generateTimeStamp()));
//                                        transactionMo1.setTxnamount(amount);
//
//
//                                        transactionMo1.setTxnStatus("Successful");
//
//                                        transactionMo1.setTxntime(time() + " " + date());
//                                        transactionMo1.setType("plus");
//
//
//
//
//                                        reference.child("TransactionHistory").child(user.getPhoneNumber()).child(String.valueOf(generateTimeStamp())).setValue(transactionMo1);
//
//
//                                        reference.child("customer").child(user.getPhoneNumber()).child("userWalletMoney").setValue(String.valueOf(updateAmount));
//
//
//                                        amountInWallet.setText(String.valueOf(updateAmount));
//
//
//
//                                    }
//                                    else if (Integer.parseInt(amount) >= 1500) {
//
//                                        int cashback = Integer.parseInt(amount) * 10 / 100;
//
//                                        CommonClass.cashback = cashback;
//
//                                        reference.child("customer").child(user.getPhoneNumber()).child("userFirstTimeStatus").setValue("1");
//
//
//                                        TransactionMo transactionMo = new TransactionMo();
//                                        transactionMo.setOrderid("0");
//                                        transactionMo.setTxnid(String.valueOf(generateTimeStamp()));
//                                        transactionMo.setTxnamount(String.valueOf(cashback));
//                                        transactionMo.setTxnStatus("Successful");
//                                        transactionMo.setTxntime(time() + " " + date());
//                                        transactionMo.setType("cashback");
//
//
//                                        reference.child("TransactionHistory").child(user.getPhoneNumber()).child(String.valueOf(generateTimeStamp())).setValue(transactionMo);
//
//                                        int updateAmount = Integer.parseInt(prevAmount) + Integer.parseInt(amount) + cashback;
//
//                                        TransactionMo transactionMo1 = new TransactionMo();
//                                        transactionMo1.setOrderid("0");
//                                        transactionMo1.setTxnid(String.valueOf(generateTimeStamp()));
//                                        transactionMo1.setTxnamount(amount);
//
//                                        transactionMo1.setTxnStatus("Successful");
//
//                                        transactionMo1.setTxntime(time() + " " + date());
//                                        transactionMo1.setType("plus");
//
//
//
//                                        reference.child("TransactionHistory").child(user.getPhoneNumber()).child(String.valueOf(generateTimeStamp())).setValue(transactionMo1);
//
//
//
//                                        reference.child("customer").child(user.getPhoneNumber()).child("userWalletMoney").setValue(String.valueOf(updateAmount));
//
//
//                                        amountInWallet.setText(String.valueOf(updateAmount));
//
//
//                                    }
//
//                                }
//                                else {
//
//                                    reference.child("customer").child(user.getPhoneNumber()).child("userFirstTimeStatus").setValue("1");
//                                    int updateAmount = Integer.parseInt(prevAmount) + Integer.parseInt(amount);
//
//                                    TransactionMo transactionMo1 = new TransactionMo();
//                                    transactionMo1.setOrderid("0");
//                                    transactionMo1.setTxnid(String.valueOf(generateTimeStamp()));
//                                    transactionMo1.setTxnamount(amount);
//                                    transactionMo1.setTxnStatus("Successful");
//                                    transactionMo1.setTxntime(time() + " " + date());
//                                    transactionMo1.setType("plus");
//
//
//                                    reference.child("TransactionHistory").child(user.getPhoneNumber()).child(String.valueOf(generateTimeStamp())).setValue(transactionMo1);
//
//
//                                    reference.child("customer").child(user.getPhoneNumber()).child("userWalletMoney").setValue(String.valueOf(updateAmount));
//
//
//                                    amountInWallet.setText(String.valueOf(updateAmount));
//
//
//                                }
//
//
//                            }
//
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//    }



    private void generateCheckSum() {

        //getting the tax amount first.
        String txnAmount = amount;
        Log.d("mytag","Chesum==>"+txnAmount);



        //creating a retrofit object.
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //creating the retrofit api service
        Api apiService = retrofit.create(Api.class);

        //creating paytm object
        //containing all the values required
        final Paytma paytm = new Paytma(
                Constants.M_ID,
                Constants.CHANNEL_ID,
                txnAmount,
                Constants.WEBSITE,
                Constants.CALLBACK_URL,
                Constants.INDUSTRY_TYPE_ID
        );

        //creating a call object from the apiService
        Call<Checksum> call = apiService.getChecksum(
                paytm.getmId(),
                paytm.getOrderId(),
                paytm.getCustId(),
                paytm.getChannelId(),
                paytm.getTxnAmount(),
                paytm.getWebsite(),
                paytm.getCallBackUrl(),
                paytm.getIndustryTypeId()
        );

        //making the call to generate checksum
        call.enqueue(new Callback<Checksum>() {
            @Override
            public void onResponse(Call<Checksum> call, Response<Checksum> response) {

                Log.d("mytag","Response=="+response.body().getPaytStatus());
                initializePaytmPayment(response.body().getChecksumHash(), paytm);
                Log.d("mytag","All Check_sum==>"+paytm);
            }

            @Override
            public void onFailure(Call<Checksum> call, Throwable t) {
                Log.d("mytag","Faileds==>"+paytm);
                t.printStackTrace();

            }
        });
    }

    private void initializePaytmPayment(String checksumHash, Paytma paytm) {

        //getting paytm service
        PaytmPGService Service = PaytmPGService.getStagingService();

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("MID", Constants.M_ID);
        paramMap.put("ORDER_ID", paytm.getOrderId());
        paramMap.put("CUST_ID", paytm.getCustId());
        paramMap.put("CHANNEL_ID", paytm.getChannelId());
        paramMap.put("TXN_AMOUNT", paytm.getTxnAmount());
        paramMap.put("WEBSITE", paytm.getWebsite());
        paramMap.put("CALLBACK_URL", paytm.getCallBackUrl());
        paramMap.put("CHECKSUMHASH", checksumHash);
        paramMap.put("INDUSTRY_TYPE_ID", paytm.getIndustryTypeId());


        //creating a paytm order object using the hashmap
        PaytmOrder order = new PaytmOrder(paramMap);

        //intializing the paytm service
        Service.initialize(order, null);

        //finally starting the payment transaction
        Service.startPaymentTransaction(Wallet.this, true, true, Wallet.this);
        PaytmPaymentTransactionCallback zfzs =   Service.getmPaymentTransactionCallback();
        Log.d("mytag","Paytm_call_back"+zfzs.toString());


    }



    @Override
    public void onTransactionResponse(Bundle inResponse) {


        if (inResponse.getString("STATUS").equals("TXN_SUCCESS")){
            Toast.makeText(this, "Payment Successful", Toast.LENGTH_LONG).show();


            reference.child("customer").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.exists()){

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        CustomerInfo customerInfo = snapshot.getValue(CustomerInfo.class);

                        String userstatus = customerInfo.getUserFirstTimeStatus();

                        if (userstatus.equals("0")){
                            if (Integer.parseInt(amount) >= 1000 && Integer.parseInt(amount) <= 1499) {


                                int cashback = Integer.parseInt(amount) * 7 / 100;
                                reference.child("customer").child(user.getPhoneNumber()).child("userFirstTimeStatus").setValue("1");
                                TransactionMo transactionMo = new TransactionMo();
                                transactionMo.setOrderid("0");
                                transactionMo.setTxnid(String.valueOf(generateTimeStamp()));
                                transactionMo.setTxnamount(String.valueOf(cashback));
                                transactionMo.setTxnStatus("Successful");
                                transactionMo.setTxntime(time() + " " + date());
                                transactionMo.setType("cashback");
                                reference.child("TransactionHistory").child(user.getPhoneNumber()).child(String.valueOf(generateTimeStamp())).setValue(transactionMo);

                                int updateAmount = Integer.parseInt(CommonClass.walletmoney) + Integer.parseInt(amount) + cashback;
                                TransactionMo transactionMo1 = new TransactionMo();
                                transactionMo1.setOrderid("0");

                                transactionMo1.setTxnid(String.valueOf(generateTimeStamp()));
                                transactionMo1.setTxnamount(amount);


                                transactionMo1.setTxnStatus("Successful");

                                transactionMo1.setTxntime(time() + " " + date());
                                transactionMo1.setType("plus");




                                reference.child("TransactionHistory").child(user.getPhoneNumber()).child(String.valueOf(generateTimeStamp())).setValue(transactionMo1);


                                reference.child("customer").child(user.getPhoneNumber()).child("userWalletMoney").setValue(String.valueOf(updateAmount));


                                amountInWallet.setText(String.valueOf(updateAmount));



                            }
                            else if (Integer.parseInt(amount) >= 1500) {

                                int cashback = Integer.parseInt(amount) * 10 / 100;

                                CommonClass.cashback = cashback;

                                reference.child("customer").child(user.getPhoneNumber()).child("userFirstTimeStatus").setValue("1");


                                TransactionMo transactionMo = new TransactionMo();
                                transactionMo.setOrderid("0");
                                transactionMo.setTxnid(String.valueOf(generateTimeStamp()));
                                transactionMo.setTxnamount(String.valueOf(cashback));
                                transactionMo.setTxnStatus("Successful");
                                transactionMo.setTxntime(time() + " " + date());
                                transactionMo.setType("cashback");


                                reference.child("TransactionHistory").child(user.getPhoneNumber()).child(String.valueOf(generateTimeStamp())).setValue(transactionMo);

                                int updateAmount = Integer.parseInt(CommonClass.walletmoney) + Integer.parseInt(amount) + cashback;

                                TransactionMo transactionMo1 = new TransactionMo();
                                transactionMo1.setOrderid("0");
                                transactionMo1.setTxnid(String.valueOf(generateTimeStamp()));
                                transactionMo1.setTxnamount(amount);

                                transactionMo1.setTxnStatus("Successful");

                                transactionMo1.setTxntime(time() + " " + date());
                                transactionMo1.setType("plus");



                                reference.child("TransactionHistory").child(user.getPhoneNumber()).child(String.valueOf(generateTimeStamp())).setValue(transactionMo1);



                                reference.child("customer").child(user.getPhoneNumber()).child("userWalletMoney").setValue(String.valueOf(updateAmount));


                                amountInWallet.setText(String.valueOf(updateAmount));


                            }
                            else {
                                int cashback = 0;
                                reference.child("customer").child(user.getPhoneNumber()).child("userFirstTimeStatus").setValue("1");
                                TransactionMo transactionMo = new TransactionMo();
                                transactionMo.setOrderid("0");
                                transactionMo.setTxnid(String.valueOf(generateTimeStamp()));
                                transactionMo.setTxnamount(String.valueOf(cashback));
                                transactionMo.setTxnStatus("Successful");
                                transactionMo.setTxntime(time() + " " + date());
                                transactionMo.setType("cashback");
                                reference.child("TransactionHistory").child(user.getPhoneNumber()).child(String.valueOf(generateTimeStamp())).setValue(transactionMo);

                                int updateAmount = Integer.parseInt(CommonClass.walletmoney) + Integer.parseInt(amount) + cashback;
                                TransactionMo transactionMo1 = new TransactionMo();
                                transactionMo1.setOrderid("0");

                                transactionMo1.setTxnid(String.valueOf(generateTimeStamp()));
                                transactionMo1.setTxnamount(amount);


                                transactionMo1.setTxnStatus("Successful");

                                transactionMo1.setTxntime(time() + " " + date());
                                transactionMo1.setType("plus");




                                reference.child("TransactionHistory").child(user.getPhoneNumber()).child(String.valueOf(generateTimeStamp())).setValue(transactionMo1);


                                reference.child("customer").child(user.getPhoneNumber()).child("userWalletMoney").setValue(String.valueOf(updateAmount));


                                amountInWallet.setText(String.valueOf(updateAmount));
                            }
                        }

                        else {

                          //  reference.child("customer").child(user.getPhoneNumber()).child("userFirstTimeStatus").setValue("1");
                            int updateAmount = Integer.parseInt(CommonClass.walletmoney) + Integer.parseInt(amount);

                            TransactionMo transactionMo1 = new TransactionMo();
                            transactionMo1.setOrderid("0");
                            transactionMo1.setTxnid(String.valueOf(generateTimeStamp()));
                            transactionMo1.setTxnamount(amount);
                            transactionMo1.setTxnStatus("Successful");
                            transactionMo1.setTxntime(time() + " " + date());
                            transactionMo1.setType("plus");


                            reference.child("TransactionHistory").child(user.getPhoneNumber()).child(String.valueOf(generateTimeStamp())).setValue(transactionMo1);


                            reference.child("customer").child(user.getPhoneNumber()).child("userWalletMoney").setValue(String.valueOf(updateAmount));


                            //amountInWallet.setText(String.valueOf(updateAmount));


                        }


                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });




        }
        else {
            Toast.makeText(this, "Payment Failed", Toast.LENGTH_LONG).show();


            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            TransactionMo transactionMo = new TransactionMo();
            transactionMo.setOrderid("0");
            transactionMo.setTxnid(String.valueOf(generateTimeStamp()));
            transactionMo.setTxnamount(amount);
            transactionMo.setTxnStatus("Failed");
            transactionMo.setTxntime(time() + " " + date());
            transactionMo.setType("plus");


            reference.child("TransactionHistory").child(user.getPhoneNumber()).child(String.valueOf(generateTimeStamp())).setValue(transactionMo);

        }
    }

    @Override
    public void networkNotAvailable() {

    }

    @Override
    public void clientAuthenticationFailed(String inErrorMessage) {

    }

    @Override
    public void someUIErrorOccurred(String inErrorMessage) {

    }

    @Override
    public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {

    }

    @Override
    public void onBackPressedCancelTransaction() {

    }

    @Override
    public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {

    }
}