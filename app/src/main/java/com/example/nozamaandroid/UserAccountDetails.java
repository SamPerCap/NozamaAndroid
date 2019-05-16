package com.example.nozamaandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nozamaandroid.BLL.BLLUser;
import com.example.nozamaandroid.Models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserAccountDetails extends AppCompatActivity {
    EditText etUsername, etAddress, etPhonenumber;
    TextView tvEmail;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUserFirebase = mAuth.getCurrentUser();
    String userKey = "userKey";
    String currentUserId;
    FirebaseFirestore db;
    String TAG = "userAccountDetails";
    Users currentUser;
    BLLUser bllUser = new BLLUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_account_details);

        etUsername = findViewById(R.id.userName);
        etAddress = findViewById(R.id.address);
        etPhonenumber = findViewById(R.id.phoneNumber);
        tvEmail = findViewById(R.id.userEmail);

        getUserDetails();
    }

    private void getUserDetails() {
        if (currentUserFirebase == null) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, HomeView.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, currentUserFirebase.getEmail() + " is logged in.", Toast.LENGTH_SHORT).show();
            currentUserId = getIntent().getExtras().getString(userKey);
            Log.d(TAG, "UserID: " + mAuth.getCurrentUser().getUid());

           // currentUser = bllUser.getUserInfo(currentUserId);
            etAddress.setText(currentUser.getAddress());
            etPhonenumber.setText(currentUser.getPhoneNumber());
            etUsername.setText(currentUser.getUserName());
            tvEmail.setText(currentUser.getEmail());
        }
    }

    public void saveEdit(View view) {
        currentUserFirebase = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        db.collection("users").document(currentUserFirebase.getUid())
                .update(
                        "Username", etUsername.getText().toString(),
                        "Address", etAddress.getText().toString(),
                        "Phonenumber", etPhonenumber.getText().toString()
                );
        finish();
    }

    public void removeAccount(View view) {
        currentUserFirebase = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        db.collection("users").document(currentUserFirebase.getUid())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User account deleted.");
                            Toast.makeText(UserAccountDetails.this, "User account has been successfully deleted.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        finish();
    }
}
