package com.mikalh.purchaseorderonline;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseLongArray;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mikalh.purchaseorderonline.Adapter.FirestoreAdapter;
import com.mikalh.purchaseorderonline.Model.User;

public class SplashScreen extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        if (user!=null){
            String ID = user.getUid();
            firebaseFirestore.collection("Users").document(user.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot snapshot) {
                    if (!snapshot.exists()){
                        Intent i = new Intent(SplashScreen.this,MainActivity.class);
                        startActivity(i);
                    }else {
                        try{
                            Boolean block = snapshot.getBoolean("Block");
                            if (block){
                                Intent i = new Intent(SplashScreen.this,MainActivity.class);
                                startActivity(i);
                            }else {
                                User user1ogin = snapshot.toObject(User.class);

                                if (user1ogin.getRoleActive().equals("Penjual")) {
                                    Intent i = new Intent(SplashScreen.this, newUserUI.class);
                                    startActivity(i);
                                } else if (user1ogin.getRoleActive().equals("Pembeli")) {
                                    Intent i = new Intent(SplashScreen.this, buyerActivity.class);
                                    startActivity(i);
                                }
                            }
                        }catch (Exception e){
                            Crashlytics.logException(e);
                            Intent i = new Intent(SplashScreen.this,MainActivity.class);
                            startActivity(i);
                        }
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Crashlytics.logException(e);
                }
            });

        }else {
            Intent i = new Intent(SplashScreen.this,MainActivity.class);
            startActivity(i);
        }

    }
}
