package com.example.nozamaandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nozamaandroid.Models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity
{
    EditText email, password;
    String TAG = "Login Activity";
    String userKey = "userKey", passwordKey = "passwordKey";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        email = findViewById(R.id.userName);
        password = findViewById(R.id.passwordTxt);
        mAuth = FirebaseAuth.getInstance();
    }

    public void loginUser(View view)
    {
        mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            goToMainView();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });

    }

    private void goToMainView()
    {
        //Users userClass = new Users();
        Intent intent = new Intent(LoginActivity.this, HomeView.class);
        // we want to save the username to the user class to pass it onwards and so we can grab it later
        // This particular information will be grabbed in everyclass for now with getIntent

        /*String userNameStr = email.getText().toString();
        String passwordStr = password.getText().toString();
        userClass.setUserName(userNameStr);
        userClass.setPassword(passwordStr);
        Log.d(TAG, "What is the username: " + userNameStr);
        Log.d(TAG, "Can we get the username: " + userClass.getUserName());
        Log.d(TAG, "Can we get the password: " + userClass.getPassword());

        intent.putExtra(userKey, userNameStr);
        intent.putExtra(passwordKey, passwordStr);*/

        finish();
    }
}
