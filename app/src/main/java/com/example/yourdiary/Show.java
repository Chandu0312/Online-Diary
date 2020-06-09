package com.example.yourdiary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;

import javax.annotation.Nullable;

public class Show extends AppCompatActivity {
    Toolbar t;
    FirebaseFirestore f;
    FirebaseAuth fAuth;
    String userid;
    TextView t1,t2;
    int index;
    String l[],name,mainmessage;
    int day,month,year;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        //Toolbar
        t = findViewById(R.id.toolbar);
        t.setTitle("Happy to share Your Feelings");
        setSupportActionBar(t);

        //Intent
        Intent i =getIntent();
        index=i.getIntExtra("A",0);

        //Initializing the Firebase
        fAuth = FirebaseAuth.getInstance();
        userid = fAuth.getCurrentUser().getUid();
        f = FirebaseFirestore.getInstance();
        t1=findViewById(R.id.textView12);
        t2=findViewById(R.id.textView11);
        Task<QuerySnapshot> temp2 = f.collection("users").document(userid).collection("Mypages").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int r = 0;
                l = new String[0];
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    l = Arrays.copyOf(l, r + 1);
                    l[r]=doc.getString("date");
                    r++;
                }
                    String temp[] = l[index].split("-",3);
                    day=Integer.parseInt(temp[0]);
                    month=Integer.parseInt(temp[1]);
                    year=Integer.parseInt(temp[2]);
                    switch (month){
                        case 01:name=String.valueOf(day)+" January, "+String.valueOf(year);
                            break;
                        case 02:name=String.valueOf(day)+" February, "+String.valueOf(year);
                            break;
                        case 03:name=String.valueOf(day)+" March, "+String.valueOf(year);
                            break;
                        case 04:name=String.valueOf(day)+" April, "+String.valueOf(year);
                            break;
                        case 05:name=String.valueOf(day)+" May, "+String.valueOf(year);
                            break;
                        case 6:name=String.valueOf(day)+" June, "+String.valueOf(year);
                            break;
                        case 07:name=String.valueOf(day)+" July, "+String.valueOf(year);
                            break;
                        case 8:name=String.valueOf(day)+" August, "+String.valueOf(year);
                            break;
                        case 9:name=String.valueOf(day)+" September, "+String.valueOf(year);
                            break;
                        case 10:name=String.valueOf(day)+" October, "+String.valueOf(year);
                            break;
                        case 11:name=String.valueOf(day)+" November, "+String.valueOf(year);
                            break;
                        case 12:name=String.valueOf(day)+" December, "+String.valueOf(year);
                            break;
                    }
                t1.setText(name);
                DocumentReference d =f.collection("users").document(userid).collection("Mypages").document(l[index]);
                d.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        try {
                            mainmessage = documentSnapshot.getString("feelings");
                        }catch (Exception E){
                            mainmessage="Some Error we will solve it";
                        }
                        t2.setText(mainmessage);
                        t2.setMovementMethod(new ScrollingMovementMethod());
                    }
                });
            }
        });
        Window window = Show.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(Show.this,R.color.colorAccent));
    }
}
