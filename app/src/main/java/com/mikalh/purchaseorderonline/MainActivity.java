package com.mikalh.purchaseorderonline;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mikalh.purchaseorderonline.Model.User;
import com.mikalh.purchaseorderonline.Pager.MainPager;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {
    Button register_login, loginDo;
    EditText email_login, password_login;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore firebaseFirestore;
    CustomDialog customDialog;
    TextInputLayout tilPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        email_login = findViewById(R.id.email_login);
        password_login = findViewById(R.id.password_login);
        loginDo = findViewById(R.id.loginDo);
        tilPassword = findViewById(R.id.tilPassword);
        loginDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
        register_login = findViewById(R.id.register_login);
        register_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,register.class);
                startActivity(i);
            }
        });
    }
    void login(){
        tilPassword.setError(null);
        customDialog = new CustomDialog(MainActivity.this);
        customDialog.show();
        String email = email_login.getText().toString();
        String password = password_login.getText().toString();
        if (email.equals("Adminss") && password.equals("Admin555")){
            Intent i = new Intent(getApplicationContext(),AdminActivity.class);
            startActivity(i);
        }
        else if (!email.isEmpty() && !password.isEmpty()){
            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        customDialog.dismiss();
                        final FirebaseUser user = auth.getCurrentUser();
                        firebaseFirestore.collection("Users").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot snapshot = task.getResult();
                                    try {
                                        boolean blocked = snapshot.getBoolean("Block").booleanValue();
                                        if (blocked){
                                            Toast.makeText(getApplicationContext(),"Akun Anda Terblokir",Toast.LENGTH_LONG).show();
                                        }else {
                                            popRole(user);
                                        }
                                    }catch (Exception e){
                                        Log.e("Error Blocked",e.getMessage());
                                        Crashlytics.logException(e);
                                        firebaseFirestore.collection("Users").document(user.getUid()).update("Block",false).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    popRole(user);
                                                }
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Crashlytics.logException(e);
                                            }
                                        });
                                    }
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Crashlytics.logException(e);
                            }
                        });

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("Error Login",e.getMessage());
                    tilPassword.setError(e.getMessage());
                    customDialog.dismiss();
                }
            });
        }else {
            tilPassword.setError("Please input Email and Password");
            customDialog.dismiss();
            //Toast.makeText(MainActivity.this,"Please input Email and Password",Toast.LENGTH_LONG).show();
        }
    }
    public void popRole(final FirebaseUser user){
        final Dialog dialog = new Dialog(this);
       /* dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);*/
        dialog.setContentView(R.layout.choose_role);
        dialog.setCanceledOnTouchOutside(false);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
//This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        final Button buttonSeller = dialog.findViewById(R.id.buttonSeller);
        final Button buttonBuyer = dialog.findViewById(R.id.buttonBuyer);

        buttonBuyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseFirestore.collection("Users").document(user.getUid()).update("roleActive","Pembeli").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Intent i = new Intent(MainActivity.this,buyerActivity.class);
                            startActivity(i);
                            dialog.dismiss();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Crashlytics.logException(e);
                        dialog.dismiss();
                    }
                });

            }
        });
        buttonSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseFirestore.collection("Users").document(user.getUid()).update("roleActive","Penjual").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Intent i = new Intent(MainActivity.this,newUserUI.class);
                            startActivity(i);
                            dialog.dismiss();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Crashlytics.logException(e);
                        dialog.dismiss();
                    }
                });

            }
        });
        dialog.show();

    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
