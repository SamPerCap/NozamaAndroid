package com.example.nozamaandroid.DALUsers;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nozamaandroid.HomeView;
import com.example.nozamaandroid.Models.Users;
import com.example.nozamaandroid.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddUser extends AppCompatActivity
{
    EditText email, password, address, phoneNumber, userName;
    DatabaseReference dref;
    public static String TAG = "ProductApp";
    String userKey = "userKey", passwordKey = "passwordKey", nameKey = "nameKey", addressKey = "addressKey", phoneKey = "phoneKey";
    Map<String, Object> userMap = new HashMap<>();
    private FirebaseAuth mAuth;
    String saveUser;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_creation);

        email = findViewById(R.id.usrEmail);
        password = findViewById(R.id.usrPw);
        address = findViewById(R.id.address);
        phoneNumber = findViewById(R.id.phonenumber);
        userName = findViewById(R.id.userName);

        dref = FirebaseDatabase.getInstance().getReference("users");

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    private void createUser()
    {
        // Here we want to create the user to the auth in the database,
        // We create an simple email and password login
        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            saveUser = user.getUid();
                            Log.d(TAG, "What is the D: " + saveUser);
                            saveUserToDataBase();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(AddUser.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    private void saveUserToDataBase()
    {
        try
        {
            final Users users = new Users();

            // FireStoreDatabase initialize
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference usersCollectionRef = db.collection("users");
            Log.d(TAG,"What is the id: " + saveUser);
            userMap.put("Email", email.getText().toString());
            userMap.put("Password", password.getText().toString());
            userMap.put("Username", userName.getText().toString());
            userMap.put("Address", address.getText().toString());
            userMap.put("Phonenumber", phoneNumber.getText().toString());

            // Add a new document with a generated ID
            db.collection("users")
                    .document(saveUser)
                    .set(userMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot added with ID: " + saveUser);
                            users.setUserName(email.getText().toString());
                            users.setPassword(password.getText().toString());
                            users.setUserName(userName.getText().toString());
                            users.setAddress(address.getText().toString());
                            users.setPhoneNumber(phoneNumber.getText().toString());


                            Log.i(TAG, "What is products: " + users.getUserName().toString());
                            Log.d(TAG, "What is products: " + users.getPassword().toString());

                            Intent intent = new Intent(AddUser.this,HomeView.class);
                            intent.putExtra(userKey, users.getUserName());
                            intent.putExtra(passwordKey, users.getPassword());
                            intent.putExtra(nameKey, users.getEmail());
                            intent.putExtra(addressKey, users.getAddress());
                            intent.putExtra(phoneKey, users.getPhoneNumber());

                            startActivity(intent);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            Log.w(TAG, "Error adding document", e);
                        }
                    });
            Log.e(TAG, "What is get text: " + email.getText());
        }
        catch(Error e)
        {
            Log.e(TAG, "Exception: " + e);
        }
    }

    public void saveCreation(View view)
    {
        createUser();
    }
}
