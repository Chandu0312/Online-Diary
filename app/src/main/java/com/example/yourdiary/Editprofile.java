package com.example.yourdiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class Editprofile extends AppCompatActivity {
    Toolbar t;
    FirebaseFirestore f;
    FirebaseAuth fAuth;
    EditText e1,e2,code;
    String security,userid,checkcode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);

        //Toolbar
        t = findViewById(R.id.toolbar);
        t.setTitle("Edit Your Profile");
        setSupportActionBar(t);

        e1=findViewById(R.id.editText7);
        e2=findViewById(R.id.editText8);
        fAuth = FirebaseAuth.getInstance();
        userid = fAuth.getCurrentUser().getUid();
        f = FirebaseFirestore.getInstance();

        Button b1=findViewById(R.id.button3);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                code=new EditText(getApplicationContext());
                AlertDialog.Builder resetD=new AlertDialog.Builder(Editprofile.this);
                resetD.setTitle("Authentication");
                resetD.setMessage("Enter the Security Code :");
                resetD.setView(code);

                resetD.setPositiveButton("Check", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        security=code.getText().toString().trim();
                        DocumentReference d =f.collection("users").document(userid);
                        d.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                                try {
                                    checkcode = documentSnapshot.getString("DOB");
                                }catch (Exception E){
                                    checkcode="Some Error we will solve it";
                                }
                                if(checkcode.equals(security)){
                                    String Nfname=e1.getText().toString().trim();
                                    String nDOB=e2.getText().toString().trim();

                                    DocumentReference doc1=FirebaseFirestore.getInstance().collection("users").document(userid);
                                    //HashMap method for storing the data
                                    Map<String,Object> user=new HashMap<>();
                                    user.put("fname",Nfname);
                                    user.put("DOB",nDOB);
                                    doc1.update(user);
                                    Toast.makeText(Editprofile.this,"Updated your profile.",Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                    finish();
                                    return;
                                }
                                else{
                                    Toast.makeText(Editprofile.this,"Security Code is Incorrect.",Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                        });
                    }
                });
                resetD.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Close the AlertDialog.
                    }
                });
                resetD.create().show();

            }
        });

    }
    //Actions in Toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflator = getMenuInflater();
        inflator.inflate(R.menu.noeditprofile,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.item4:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
                return true;
            case R.id.item3:
                startActivity(new Intent(getApplicationContext(),about.class));
                finish();
                return true;
        }
        return true;
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }
}
