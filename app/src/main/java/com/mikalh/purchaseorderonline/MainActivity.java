package com.mikalh.purchaseorderonline;

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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    Button register_login, loginDo;
    EditText email_login, password_login;
    FirebaseAuth auth;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user != null){
            Intent i = new Intent(MainActivity.this, userUI.class);
            startActivity(i);
        }
        email_login = findViewById(R.id.email_login);
        password_login = findViewById(R.id.password_login);
        loginDo = findViewById(R.id.loginDo);
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
        String email = email_login.getText().toString();
        String password = password_login.getText().toString();
        if (!email.isEmpty() && !password.isEmpty()){
            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        FirebaseUser user = auth.getCurrentUser();
                        Intent i = new Intent(MainActivity.this, userUI.class);
                        startActivity(i);
                    }else {
                        FirebaseAuthException e = (FirebaseAuthException) task.getException();
                        Log.e("Error Login",e.getMessage());
                        Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            });
        }else {
            Toast.makeText(MainActivity.this,"Please input Email and Password",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
