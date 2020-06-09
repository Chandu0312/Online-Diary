package com.example.yourdiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Signup extends AppCompatActivity {
    FirebaseAuth fauth;
    FirebaseFirestore fstore;
    EditText e1,e2,e3,e4;
    Button b1;
    ProgressBar p1;
    TextView t1;
    String userid,fn,email,pass,dob;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        e1=findViewById(R.id.editText);
        e2=findViewById(R.id.editText2);
        e3=findViewById(R.id.editText4);
        e4=findViewById(R.id.editText5);
        b1=findViewById(R.id.button);
        t1=findViewById(R.id.textView3);
        p1=findViewById(R.id.progressBar2);
        fauth=FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();
        //If Already Logged In
        if(fauth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }

        //When the register button is pressed
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fn=e1.getText().toString().trim();
                email=e2.getText().toString().trim();
                pass=e3.getText().toString().trim();
                dob=e4.getText().toString().trim();
                if(TextUtils.isEmpty(fn)){
                    e1.setError("Full Name is required.");
                    return;
                }
                if(TextUtils.isEmpty(email)){
                    e2.setError("Email is required.");
                    return;
                }
                if(TextUtils.isEmpty(pass)){
                    e3.setError("Password is required.");
                    return;
                }
                if(TextUtils.isEmpty(dob)){
                    e4.setError("Security Code is required for Authentication.");
                    return;
                }
                if(pass.length()<6){
                    e3.setError("Password must be more than 6 characters.");
                    return;
                }

                p1.setVisibility(View.VISIBLE);

                fauth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Signup.this,"User Created",Toast.LENGTH_SHORT).show();
                            userid=fauth.getCurrentUser().getUid();
                            DocumentReference doc1=fstore.collection("users").document(userid);
                            //HashMap method for storing the data
                            Map<String,Object> user=new HashMap<>();
                            user.put("fname",fn);
                            user.put("email",email);
                            user.put("DOB",dob);
                            doc1.set(user);/*.addOnSuccessListener(new onSuccessListner<Void>(){
                                @Override
                                public void onSuccess(Void aVoid){
                                    Log.d(TAG,"User Profile is created for"+userid);
                                }

                            });*/
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }
                        else{
                            Toast.makeText(Signup.this,"Error! "+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                            p1.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }
        });

        //If text is clicked..
        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));
                finish();
            }
        });

        Window window = Signup.this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(Signup.this,R.color.Reg_color));
    }
}
