package com.example.nozamaandroid;

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
import com.example.nozamaandroid.Models.UserModel;
import com.example.nozamaandroid.Models.Users;
import com.example.nozamaandroid.Shared.OnResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.lang.reflect.Array;

public class UserAccountDetails extends AppCompatActivity {
    EditText etUsername, etPhonenumber;
    TextView tvEmail;
    Spinner sAddress;
    UserModel userModel= UserModel.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUserFirebase = mAuth.getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String TAG = "userAccountDetails";
    Users currentUser;
    CircularImageView civUserImage;
    BLLUser bllUser = new BLLUser();


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

           userModel.getUserById(mAuth.getCurrentUser().getUid(), new OnResponse() {
               @Override
               public void onResponseReceived(Object response) {
                   Log.d(TAG, "onResponseReceived user: " + response);
                   if (response != null) {
                       currentUser = (Users) response;

                       etUsername.setText(currentUser.getUserName());
                       String[] city = getResources().getStringArray(R.array.cities);
                       for (int i = 0; i < city.length; i++) {
                           if (city[i] == currentUser.getAddress()) {
                               sAddress.setSelection(i);
                               break;
                           }
                       }
                       etPhonenumber.setText(currentUser.getPhoneNumber());
                       tvEmail.setText(currentUser.getEmail());
                   }
               }
           });
           bllUser.getImageById(mAuth.getCurrentUser().getUid(), new OnResponse() {
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
    }


    public void saveEdit(View view) {
        currentUserFirebase = mAuth.getCurrentUser();
      //  userModel.updateUser()
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
