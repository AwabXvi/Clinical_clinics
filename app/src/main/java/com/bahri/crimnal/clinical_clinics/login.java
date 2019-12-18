package com.bahri.crimnal.clinical_clinics;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;


public class login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private String email, password,Display_name;
    private Button signin;
    private EditText email_field , password_field , username_field ;
    private  static final String TAG = "login";
    ProgressDialog progressDoalog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        signin = (Button) findViewById(R.id.btn_signin);
        email_field = (EditText) findViewById(R.id.email_txt);
        password_field = (EditText) findViewById(R.id.password_txt);
        username_field = (EditText) findViewById(R.id.username);
        progressDoalog = new ProgressDialog(login.this);
        progressDoalog.setIndeterminate(true);
        progressDoalog.setMessage("loading..");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);







        signin.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        email = email_field.getText().toString();
                        password = password_field.getText().toString();
                        Display_name = username_field.getText().toString();
                        if (email.isEmpty() || email == null) {
                            email_field.setError("Enter an email");
                            email_field.requestFocus();
                        } else if (password.isEmpty() || password == null) {
                            password_field.setError("Enter your password");
                            password_field.requestFocus();

                        }
                        else if (Display_name.isEmpty() || Display_name == null) {
                            username_field.setError("Enter your username");
                            username_field.requestFocus();

                        }
                        else {
                            progressDoalog.show();
                            signInWithFireBase(email, password);
                        }
                    }
                }

        );






    }

    private  void signInWithFireBase(String email , String password){

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDoalog.dismiss();
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(login.this, "Authentication success.",
                                    Toast.LENGTH_SHORT).show();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(Display_name)
                                    .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "User profile updated.");
                                            }
                                        }
                                    });
                            Intent i = new Intent(login.this , MainActivity.class);
                            startActivity(i);


                        } else {

                            progressDoalog.dismiss();
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(login.this, "Something went  wrong.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                }).addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        progressDoalog.dismiss();
                        Toast.makeText(login.this, "Email or password is wrong.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
        );

    }
}
