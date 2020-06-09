package com.example.yourdiary;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    EditText e1,e2,resetpass;
    Button b1;
    ProgressBar p1;
    FirebaseAuth fauth;
    TextView t1,t2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fauth=FirebaseAuth.getInstance();
        e1=findViewById(R.id.editText3);
        e2=findViewById(R.id.editText6);
        b1=findViewById(R.id.button2);
        p1=findViewById(R.id.progressBar3);
        t1=findViewById(R.id.textView5);
        t2=findViewById(R.id.textView7);

        if(fauth.getCurrentUser()!= null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }

        //if TextView1 is pressed..
        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Signup.class));
                finish();
            }
        });

        //If textView2 is pressed...
        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetpass=new EditText(v.getContext());
                AlertDialog.Builder resetD=new AlertDialog.Builder(v.getContext());
                resetD.setTitle("Reset Password");
                resetD.setMessage("Enter your Email to Receive Reset Link.");
                resetD.setView(resetpass);

                resetD.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String mail=resetpass.getText().toString().trim();
                        fauth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Login.this,"Reset Link has sent to your Account.",Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Login.this,"Error! "+e.getMessage(),Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                });
                resetD.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Close the AlertDialog.
                    }
                });
                resetD.create().show();
            }
        });

        //If Login Button is pressed...
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=e1.getText().toString().trim();
                String pass=e2.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    e1.setError("Email is required.");
                    return;
                }
                if(TextUtils.isEmpty(pass)){
                    e2.setError("Password is required.");
                    return;
                }
                if(pass.length()<6){
                    e2.setError("Password must be more than 6 characters.");
                    return;
                }
                p1.setVisibility(View.VISIBLE);

                fauth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Login.this,"Login Sucessful",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }
                        else{
                            Toast.makeText(Login.this,"Error! "+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                            p1.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        Window window = Login.this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(Login.this,R.color.login_color));
    }
}
