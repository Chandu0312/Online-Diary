package com.example.yourdiary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class createcard extends AppCompatActivity {
    private final int REQ_CODE = 100;
    Toolbar t;
    EditText e1,e2;
    Button b1;
    FirebaseAuth fauth;
    FirebaseFirestore fstore;
    String userid;
    ImageView b2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createcard);

        //Toolbar
        t = findViewById(R.id.toolbar);
        t.setTitle("Share Your feelings");
        setSupportActionBar(t);

        fauth=FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();
        userid=fauth.getCurrentUser().getUid();
        e1=findViewById(R.id.editText9);
        e2=findViewById(R.id.editText11);
        b1=findViewById(R.id.button4);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date=e1.getText().toString();
                String data=e2.getText().toString();
                if(date.length()<10 || date.charAt(2)!='-' || date.charAt(5)!='-' || Integer.parseInt(String.valueOf(date.charAt(0)))>3 || Integer.parseInt(String.valueOf(date.charAt(3)))>1 ||
                        (Integer.parseInt(String.valueOf(date.charAt(3)))==1 && Integer.parseInt(String.valueOf(date.charAt(4)))>2) ){
                    e1.setError("Invalid Date Of Birth. Please Enter in the above given Format.");
                    return;
                }
                if(data.isEmpty()){
                    e2.setError("Please share your feelings. We assure you that we will store that in our Heart");
                    return;
                }
                DocumentReference doc1=fstore.collection("users").document(userid).collection("Mypages").document(date);
                Map<String,Object> user=new HashMap<>();
                user.put("date",date);
                user.put("feelings",data);
                doc1.set(user);
                Toast.makeText(createcard.this,"Your feelings are saved in our heart.",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
        b2=findViewById(R.id.imageView);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Need to speak");
                try {
                    startActivityForResult(intent, REQ_CODE);
                } catch (ActivityNotFoundException a) {
                    Toast.makeText(getApplicationContext(),
                            "Sorry your device not supported",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE: {
                if (resultCode == RESULT_OK && data!= null) {
                    ArrayList result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String a=e2.getText().toString()+" ";
                    e2.setText(a+(CharSequence) result.get(0));
                }
                break;
            }
        }
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }
    protected void onResume()
    {
        super.onResume();
        requestforcamera();

    }

    private void requestforcamera()
    {
        Dexter.withActivity(this).withPermission(Manifest.permission.RECORD_AUDIO).withListener(new PermissionListener()
        {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {

            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                Toast.makeText(createcard.this, "Record Permission needed.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();
    }
}
