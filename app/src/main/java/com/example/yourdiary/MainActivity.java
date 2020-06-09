package com.example.yourdiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
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

public class MainActivity extends AppCompatActivity {
    Toolbar t;
    FirebaseFirestore f;
    FirebaseAuth fAuth;
    String userid,checkcode,security;
    ListView lv1;
    String l[],name[];
    TextView t1;
    EditText code;
    int day[],month[],year[],pos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar
        t = findViewById(R.id.toolbar);
        t.setTitle("Your Diary");
        setSupportActionBar(t);

        //Initializing the Firebase
        fAuth = FirebaseAuth.getInstance();
        userid = fAuth.getCurrentUser().getUid();
        f = FirebaseFirestore.getInstance();
        t1=findViewById(R.id.textView10);
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
                if(r!=0){
                    t1.setVisibility(View.VISIBLE);
                }
                day=new int[r];
                month=new int[r];
                year=new int[r];
                name=new String[r];
                for(int j=0;j<l.length;j++){
                    String temp[] = l[j].split("-",3);
                    day[j]=Integer.parseInt(temp[0]);
                    month[j]=Integer.parseInt(temp[1]);
                    year[j]=Integer.parseInt(temp[2]);
                    switch (month[j]){
                        case 01:name[j]=String.valueOf(day[j])+" January, "+String.valueOf(year[j]);
                                break;
                        case 02:name[j]=String.valueOf(day[j])+" February, "+String.valueOf(year[j]);
                            break;
                        case 03:name[j]=String.valueOf(day[j])+" March, "+String.valueOf(year[j]);
                            break;
                        case 04:name[j]=String.valueOf(day[j])+" April, "+String.valueOf(year[j]);
                            break;
                        case 05:name[j]=String.valueOf(day[j])+" May, "+String.valueOf(year[j]);
                            break;
                        case 6:name[j]=String.valueOf(day[j])+" June, "+String.valueOf(year[j]);
                            break;
                        case 07:name[j]=String.valueOf(day[j])+" July, "+String.valueOf(year[j]);
                            break;
                        case 8:name[j]=String.valueOf(day[j])+" August, "+String.valueOf(year[j]);
                            break;
                        case 9:name[j]=String.valueOf(day[j])+" September, "+String.valueOf(year[j]);
                            break;
                        case 10:name[j]=String.valueOf(day[j])+" October, "+String.valueOf(year[j]);
                            break;
                        case 11:name[j]=String.valueOf(day[j])+" November, "+String.valueOf(year[j]);
                            break;
                        case 12:name[j]=String.valueOf(day[j])+" December, "+String.valueOf(year[j]);
                            break;
                    }
                }

                ArrayAdapter<String> arr = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, Arrays.asList(name));
                lv1 = findViewById(R.id.lv1);
                lv1.setAdapter(arr);
                lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        pos=position;
                        code=new EditText(view.getContext());
                        AlertDialog.Builder resetD=new AlertDialog.Builder(MainActivity.this);
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
                                            Intent I = new Intent(getApplicationContext(), Show.class);
                                            I.putExtra("A", pos);
                                            startActivity(I);
                                        }
                                        else{
                                            Toast.makeText(MainActivity.this,"Security Code is Incorrect.",Toast.LENGTH_SHORT).show();
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
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflator = getMenuInflater();
        inflator.inflate(R.menu.menutool1,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.item_add:
                code=new EditText(getApplicationContext());
                AlertDialog.Builder resetD=new AlertDialog.Builder(MainActivity.this);
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
                                    startActivity(new Intent(getApplicationContext(),createcard.class));
                                    finish();
                                    return ;
                                }
                                else{
                                    Toast.makeText(MainActivity.this,"Security Code is Incorrect.",Toast.LENGTH_SHORT).show();
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

                return true;
            case R.id.item4:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),Login.class));
                finish();
                return true;
            case R.id.item2:
                startActivity(new Intent(getApplicationContext(),Editprofile.class));
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
        moveTaskToBack(true);
    }
}
