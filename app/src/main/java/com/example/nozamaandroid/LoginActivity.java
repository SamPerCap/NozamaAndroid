package com.example.nozamaandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.nozamaandroid.Models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity
{
    EditText userName, password;
    String TAG = "Login Activity";
    String userKey = "userKey", passwordKey = "passwordKey";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        userName = findViewById(R.id.usernameTxt);
        password = findViewById(R.id.passwordTxt);
    }

    public void loginUser(View view)
    {
        Users user = new Users();
        Intent intent = new Intent(this, HomeView.class);
        // we want to save the username to the user class to pass it onwards and so we can grab it later
        // This particular information will be grabbed in everyclass for now with getIntent

        String userNameStr = userName.getText().toString();
        String passwordStr = password.getText().toString();
        user.setUserName(userNameStr);
        user.setPassword(passwordStr);
        Log.d(TAG, "What is the username: " + userNameStr);
        Log.d(TAG, "Can we get the username: " + user.getUserName());
        Log.d(TAG, "Can we get the password: " + user.getPassword());

        intent.putExtra(userKey, userNameStr);
        intent.putExtra(passwordKey, passwordStr);

        startActivity(intent);

        fireStoreData();
    }

    private void fireStoreData()
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot doc: task.getResult())
                {
                    Log.d(TAG, "Get all the users" + doc.getData());
                }
            }
        });
    }
}
