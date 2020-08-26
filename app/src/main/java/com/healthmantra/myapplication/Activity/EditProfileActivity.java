package com.healthmantra.myapplication.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.healthmantra.myapplication.CommonClass;
import com.healthmantra.myapplication.LocationList;
import com.healthmantra.myapplication.Models.CustomerInfo;
import com.healthmantra.myapplication.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class EditProfileActivity extends AppCompatActivity {

    CircleImageView circleImageView;
    ImageButton selectImage;
    EditText name, addresss, email;
    RadioButton male, female;

    Button buttonSave;

    RadioGroup radioGroup;
    RadioButton radioButton;

    TextView location;

    String imageUri;

    ArrayList<String> regionList = new ArrayList<>();
    SpinnerDialog spinnerDialog;
    DatabaseReference reference;
    FirebaseUser user;
    ProgressDialog progressDialog;
    Toolbar toolbar;
    private FirebaseAuth firebaseAuth;
    private StorageReference mStorageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        progressDialog = new ProgressDialog(this);


        imageUri = CommonClass.imageuri;


        toolbar = findViewById(R.id.toolbar_editProfil);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        reference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        user = firebaseAuth.getCurrentUser();
        mStorageReference = FirebaseStorage.getInstance().getReference();
        circleImageView = findViewById(R.id.image_view_edit_profile);


        male = findViewById(R.id.rd_male);
        male.setChecked(true);
        female = findViewById(R.id.rd_female);

        LocationList locationList = new LocationList();
        regionList = locationList.getRegionList();



        location = findViewById(R.id.tv_edit_profile_location);
        spinnerDialog = new SpinnerDialog(this, regionList, "Select Location");
        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                location.setText("");
                location.setText(item);

            }
        });


        selectImage = findViewById(R.id.add_image_button);
        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .setAspectRatio(1, 1)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(EditProfileActivity.this);
            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                spinnerDialog.showSpinerDialog();
            }
        });
        radioGroup = findViewById(R.id.et_edit_profile_gender);
        name = findViewById(R.id.et_edit_profile_name);
        addresss = findViewById(R.id.et_edit_profile_address);
        email = findViewById(R.id.et_edit_profile_email);
        buttonSave = findViewById(R.id.button_save_profile);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uname = name.getText().toString().trim();
                String uaddress = addresss.getText().toString().trim();
                String uemai = email.getText().toString().trim();







                int rdid = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(rdid);

                String gender = radioButton.getText().toString();

                String ulocation = location.getText().toString().trim();

                String uregion = "Lucknow";

                String uimage = imageUri;

                if (!uname.equals("") && !uaddress.equals("") && !uemai.equals("")  && !ulocation.equals("") ){
                    CommonClass.address = uaddress;
                    CommonClass.location = ulocation;
                    CommonClass.name = uname;
                    CommonClass.email = uemai;
                    CommonClass.region= uregion;
                    CommonClass.imageuri = imageUri;
                    CommonClass.gender = gender;

                    CustomerInfo customerInfo =  new CustomerInfo();


                    customerInfo.setUserOrders(CommonClass.order);
                    customerInfo.setUserSuscription(CommonClass.subnumber);
                    customerInfo.setUserAddress(CommonClass.address);
                    customerInfo.setUserWalletMoney(CommonClass.walletmoney);
                    customerInfo.setUserRegion(CommonClass.region);
                    customerInfo.setUserLocation(CommonClass.location);
                    customerInfo.setUserPhone(CommonClass.phone);
                    customerInfo.setUserImage(CommonClass.imageuri);
                    customerInfo.setUserName(CommonClass.name);
                    customerInfo.setUserEmail(CommonClass.email);
                    customerInfo.setUserGender(CommonClass.gender);





                    reference.child("customer").child(user.getPhoneNumber()).setValue(customerInfo);
                }

                else {
                    Toast.makeText(EditProfileActivity.this, "Fill all fields", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                final Uri resultUri = result.getUri();

                CommonClass.imageuri = resultUri.toString();
                circleImageView.setImageURI(resultUri);

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),resultUri);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();

                    bitmap.compress(Bitmap.CompressFormat.JPEG, 25, baos);
                    byte[] datai = baos.toByteArray();
                    mStorageReference.child("customer").child(CommonClass.phone).putBytes(datai).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            //Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();


                            mStorageReference.child("customer").child(CommonClass.phone).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    imageUri = uri.toString();
//
                                    progressDialog.dismiss();


                                }
                            });
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage((int) progress + "% Loaded...");
                            progressDialog.show();
                        }
                    })
                            .addOnCanceledListener(new OnCanceledListener() {
                                @Override
                                public void onCanceled() {
                                    // Toast.makeText(getContext(), "Not Success", Toast.LENGTH_SHORT).show();
                                }
                            });

                } catch (IOException e) {
                    e.printStackTrace();
                }


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        user = firebaseAuth.getCurrentUser();
        if (user != null) {
           name.setText(CommonClass.name);
           email.setText(CommonClass.email);
           addresss.setText(CommonClass.address);
           location.setText(CommonClass.location);
           if (CommonClass.gender .equals("Male")){
               male.setChecked(true);
           }
           else {
               female.setChecked(true);
           }
            if (!CommonClass.imageuri.equals("image")) {
                Glide.with(this).load(CommonClass.imageuri).into(circleImageView);
            }
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
}