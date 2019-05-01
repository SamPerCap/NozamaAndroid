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
        db = FirebaseFirestore.getInstance();
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                // This way we can get the fields from firestore and save them to a string.
                                String getFireStoreFieldUserName = document.getString("Username");
                                String getFireStoreFieldAddress = document.getString("Address");
                                String getFireStoreFieldPhonenumber = document.getString("Phonenumber");
                                userId = document.getId();

                                userName.setText(getFireStoreFieldUserName);
                                address.setText(getFireStoreFieldAddress);
                                phoneNumber.setText(getFireStoreFieldPhonenumber);
                            }
                            // We can use this to add to the listview the items this user has purchased, but we need to get relations to work first
                            //ArrayAdapter<String> adapter = new ArrayAdapter<String>(UserAccountDetails.this,android.R.layout.simple_dropdown_item_1line,listItemName);
                            //listView.setAdapter(adapter);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public void saveEdit(View view)
    {
        // Set the "isCapital" field of the city 'DC'
        db.collection("users").document(userId)
                .update(
                        "Username", userName.getText().toString(),
                        "Address", address.getText().toString(),
                        "Phonenumber", phoneNumber.getText().toString()
                );
        finish();
    }
}
