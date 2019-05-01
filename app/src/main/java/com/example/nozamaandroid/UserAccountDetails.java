package com.example.nozamaandroid;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class UserAccountDetails extends AppCompatActivity
{
    ListView listView;
    EditText userName, address, phoneNumber, email;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseFirestore db;
    String TAG = "userAccountDetails";
    String userId;


    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_account_details);

        listView = findViewById(R.id.listView);
        userName = findViewById(R.id.userName);
        address = findViewById(R.id.address);
        phoneNumber = findViewById(R.id.phoneNumber);
        email = findViewById(R.id.usrEmail);
        mAuth = FirebaseAuth.getInstance();

        getUserDetails();
    }

    private void getUserDetails()
    {
        currentUser = mAuth.getCurrentUser();
        if ( currentUser == null )
        {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, HomeView.class);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, currentUser.getEmail() + " is logged in.", Toast.LENGTH_SHORT).show();
            getUserFirestore();
        }
    }

    private void getUserFirestore()
    {
        currentUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        Log.d(TAG,"user id; " + currentUser.getUid());
        DocumentReference docRef = db.collection("users").document(currentUser.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // This way we can get the fields from firestore and save them to a string.
                        String getFireStoreFieldUserName = document.getString("Username");
                        String getFireStoreFieldAddress = document.getString("Address");
                        String getFireStoreFieldPhonenumber = document.getString("Phonenumber");
                        userId = document.getId();

                        userName.setText(getFireStoreFieldUserName);
                        address.setText(getFireStoreFieldAddress);
                        phoneNumber.setText(getFireStoreFieldPhonenumber);
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public void saveEdit(View view)
    {
        currentUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        // Set the "isCapital" field of the city 'DC'
        db.collection("users").document(currentUser.getUid())
                .update(
                        "Username", userName.getText().toString(),
                        "Address", address.getText().toString(),
                        "Phonenumber", phoneNumber.getText().toString()
                );
        finish();
    }

    public void removeAccount(View view)
    {
        currentUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        db.collection("users").document(currentUser.getUid())
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
        finish();
    }
}
