package com.healthmantra.myapplication.Fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.healthmantra.myapplication.CommonClass;
import com.healthmantra.myapplication.Models.SuggestionMo;
import com.healthmantra.myapplication.R;


public class ContactFragment extends Fragment {

    DatabaseReference reference;

    public static String FACEBOOK_URL = "https://www.facebook.com/HealthMntra";
    public static String FACEBOOK_PAGE_ID = "HealthMntra";

    Uri uri = Uri.parse("http://instagram.com/_u/HealthMntra");

    ImageButton imageButtonCall, imageButtonfacebook, imageButtonWhatsApp, imageButtonInstagram, imageButtonTwitter;

    EditText editTextSubject, editTextDescription;

    Toolbar toolbar;


    public ContactFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact, container, false);

        reference = FirebaseDatabase.getInstance().getReference();

        editTextSubject = view.findViewById(R.id.edit_subject);
        editTextDescription = view.findViewById(R.id.edit_description);

        TextView tv = view.findViewById(R.id.website_text);
        SpannableString content = new SpannableString("http://www.healthmntra.com");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        tv.setText(content);


        view.findViewById(R.id.card_call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + "+91 8874554433"));
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);

                }
            }
        });

        view.findViewById(R.id.card_facebook).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              getFacebookPageURL(getContext());

                Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                String facebookUrl = getFacebookPageURL(getContext());
                facebookIntent.setData(Uri.parse(facebookUrl));
                startActivity(facebookIntent);

            }
        });

        view.findViewById(R.id.card_instagram).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                likeIng.setPackage("com.instagram.android");

                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://instagram.com/HealthMntra")));
                }
            }
        });


        view.findViewById(R.id.card_twitter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + "HealthMntra")));
                } catch (Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/#!/" + "HealthMntra")));
                }
            }
        });

        view.findViewById(R.id.card_whatsapp).findViewById(R.id.card_whatsapp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String contact = "+918874554433"; // use country code with your phone number
                String url = "https://api.whatsapp.w4b.com/send?phone=" + contact;
                try {
                    PackageManager pm = getActivity().getPackageManager();
                    pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));

                    startActivity(i);
                } catch (PackageManager.NameNotFoundException e) {
                    Toast.makeText(getContext(), "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

        view.findViewById(R.id.card_email).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                String[] recipients = {"care@healthmntra.com"};
                intent.putExtra(Intent.EXTRA_EMAIL, recipients);
                intent.setType("text/html");
                intent.setPackage("com.google.android.gm");
                startActivity(Intent.createChooser(intent, "Send mail"));
            }
        });

        view.findViewById(R.id.website_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
                try {
                    intent.setData(Uri.parse("http://www.healthmntra.com/"));
                    startActivity(intent);
                } catch (ActivityNotFoundException exception) {
                    Toast.makeText(getActivity(), "Error text", Toast.LENGTH_SHORT).show();
                }
            }
        });
        view.findViewById(R.id.button_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                String subject = editTextSubject.getText().toString().trim();
                String desc = editTextDescription.getText().toString().trim();
                if (desc.isEmpty() || subject.isEmpty()) {
                    Toast.makeText(getContext(), "Can not be null", Toast.LENGTH_SHORT).show();
                } else {
                    SuggestionMo suggestionMo = new SuggestionMo(subject, desc, CommonClass.name, CommonClass.phone);

                    reference.child("Suggestion").push().setValue(suggestionMo);
                    editTextSubject.setText("");
                    editTextDescription.setText("");
                    Toast.makeText(getContext(), "Feedback sent successfully", Toast.LENGTH_SHORT).show();
                }


            }
        });
        return view;
    }

    public String getFacebookPageURL(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                return "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                return "fb://page/" + FACEBOOK_PAGE_ID;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_URL; //normal web url
        }
    }
}