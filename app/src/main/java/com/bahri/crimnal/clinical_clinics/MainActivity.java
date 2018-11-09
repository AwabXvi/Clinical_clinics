package com.bahri.crimnal.clinical_clinics;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private CollectionReference ref;
    private  final  String TAG = "MainActivity";
    private ArrayList<String> mName = new ArrayList<>();
    private  ArrayList <String> mImage = new ArrayList<>();
    private  ArrayList <String> mPhone = new ArrayList<>();
    private  ArrayList <String> mDoctor = new ArrayList<>();
    private  ArrayList <String> mID = new ArrayList<>();
    private  ArrayList <Boolean> mAcceptence = new ArrayList<>();
    private String img_url , phone, doctor , name ,clinic_name, id;
    private  Boolean Acceptence_state;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(MainActivity.this, IntentService.class);
        startService(intent);
        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        ref = db.collection("Reservation");
        if (user != null){
            clinic_name = user.getDisplayName();
        }


        ref.whereEqualTo("clinic_name" ,clinic_name ).get().addOnSuccessListener(
                new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            name = (String) documentSnapshot.get("patient");
                            doctor = (String) documentSnapshot.get("doctor");
                            img_url = (String) documentSnapshot.get("photo_url");
                            phone = (String)  documentSnapshot.get("phone");
                            Acceptence_state = (Boolean) documentSnapshot.get("acceptence");
                            mName.add(name);
                            mDoctor.add(doctor);
                            mImage.add(img_url);
                            mAcceptence.add(Acceptence_state);
                            mPhone.add(phone);
                            id = (String) documentSnapshot.getId();
                            mID.add(id);

                        }

                        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
                        recyclerView.setHasFixedSize(true);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
                        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(mName,mAcceptence,mID,mImage,mPhone,mDoctor,MainActivity.this);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(recyclerViewAdapter);

                    }
                }
        ).addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "query failed", Toast.LENGTH_LONG).show();
                    }
                }
        );



    }


    @Override
    protected void onStart() {
        super.onStart();
        user = mAuth.getCurrentUser();
        if (user == null) {
            Intent i = new Intent(MainActivity.this, login.class);
            startActivity(i);
            finish();
        } else {
            return;
        }
    }

}
