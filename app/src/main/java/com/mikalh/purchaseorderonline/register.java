package com.mikalh.purchaseorderonline;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mikalh.purchaseorderonline.Model.User;
import com.mikalh.purchaseorderonline.Pager.RegisterPager;
import com.pixelcan.inkpageindicator.InkPageIndicator;

public class register extends AppCompatActivity {
    EditText nama_register, nohp_register,email_register, password_register;
    Button registerDo;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore firestore;
    ProgressBar loadingBar_register;
    ViewPager myViewPager;
    RegisterPager registerPager;
    InkPageIndicator inkPageIndicator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        myViewPager = findViewById(R.id.myViewPagger);

        inkPageIndicator = findViewById(R.id.createPoIndicator);
        registerPager = new RegisterPager(getSupportFragmentManager(),2);
        myViewPager.setAdapter(registerPager);

        inkPageIndicator.setViewPager(myViewPager);
    }

    void Register(){
        final String Email = email_register.getText().toString();
        final String Password = password_register.getText().toString();
        final String Nohp = nohp_register.getText().toString();
        final String nama = nama_register.getText().toString();

        auth.createUserWithEmailAndPassword(Email,Password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            user = auth.getCurrentUser();
                            loadingBar_register.setVisibility(View.VISIBLE);
                            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(nama).build();
                            user.updateProfile(profileChangeRequest).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    if (e == null){
                                        Log.e("Error :",e.getMessage());
                                    }
                                }
                            });
                            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (!task.isSuccessful()){
                                        Log.e("Error Email :",task.toString());
                                    }
                                }
                            });
                            User user1 = new User(nama,Nohp,Email,"","",user.getUid(),"","","","","","","","");
                            firestore.collection("Users").document(user.getUid())
                                    .set(user1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        loadingBar_register.setVisibility(View.GONE);
                                        Toast.makeText(register.this,"Register Berhasil",Toast.LENGTH_LONG).show();
                                        Intent i = new Intent(register.this,userUI.class);
                                        startActivity(i);
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(register.this,e.getMessage(),Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                        else {
                            FirebaseAuthException e = (FirebaseAuthException) task.getException();
                            Toast.makeText(register.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(register.this,e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
}
