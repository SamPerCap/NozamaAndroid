package com.example.nozamaandroid;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nozamaandroid.BLL.BLLUser;
import com.example.nozamaandroid.Models.Users;
import com.example.nozamaandroid.Shared.ImageResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.mikhaellopez.circularimageview.CircularImageView;

public class UserAccountDetails extends AppCompatActivity {
    EditText etUsername, etPhonenumber;
    TextView tvEmail;
    Spinner sAddress;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUserFirebase = mAuth.getCurrentUser();
    String currentUserId;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String TAG = "userAccountDetails";
    Users currentUser;
    CircularImageView civUserImage;
    BLLUser bllUser = new BLLUser();
    Query docRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_account_details);

        etUsername = findViewById(R.id.userName);
        sAddress = findViewById(R.id.address);
        etPhonenumber = findViewById(R.id.phoneNumber);
        tvEmail = findViewById(R.id.userEmail);
        civUserImage = findViewById(R.id.userProfile);

        getUserDetails();
    }

    private void getUserDetails() {
        if (currentUserFirebase == null) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, currentUserFirebase.getEmail() + " is logged in.", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "UserID: " + mAuth.getCurrentUser().getUid());
            currentUserId = mAuth.getCurrentUser().getUid();


            docRef = db.collection("users").whereEqualTo(currentUserId, true);
            docRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    currentUser = bllUser.getUserInfo(docRef);
                    etPhonenumber.setText(currentUser.getPhoneNumber());
                    etUsername.setText(currentUser.getUserName());
                    tvEmail.setText(currentUser.getEmail());
                    bllUser.setUserImage(currentUserId, new ImageResponse() {
                        @Override
                        public void onResponseReceived(Object response) {
                            if(response != null) {
                                civUserImage.setImageBitmap((Bitmap)response);
                            }
                            else{
                                civUserImage.setImageResource(R.drawable.cake);
                            }
                        }
                    });
                }
            });
            //sAddress.setSelection(getIndex(sAddress, currentUser.getAddress()));

        }
    }

    private int getIndex(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }
        return 0;
    }

    public void saveEdit(View view) {
        currentUserFirebase = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        db.collection("users").document(currentUserFirebase.getUid())
                .update(
                        "Username", etUsername.getText().toString(),
                        "Address", sAddress.getSelectedItem().toString(),
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
