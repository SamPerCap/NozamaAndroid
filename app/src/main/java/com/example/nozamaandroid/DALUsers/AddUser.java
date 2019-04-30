package com.example.nozamaandroid.DALUsers;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nozamaandroid.DALProducts.AddProduct;
import com.example.nozamaandroid.HomeView;
import com.example.nozamaandroid.Models.Products;
import com.example.nozamaandroid.Models.Users;
import com.example.nozamaandroid.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddUser extends AppCompatActivity
{
    EditText userName, password;
    DatabaseReference dref;
    public static String TAG = "ProductApp";
    String userKey = "userKey", passwordKey = "passwordKey";
    Map<String, Object> userMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_creation);

        userName = findViewById(R.id.usrName);
        password = findViewById(R.id.usrPw);

        dref = FirebaseDatabase.getInstance().getReference("users");
    }

    public void saveCreation(View view)
    {
        try
        {
            final Users users = new Users();

            // FireStoreDatabase initialize
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference usersCollectionRef = db.collection("users");

            userMap.put("Username", userName.getText().toString());
            userMap.put("Password", password.getText().toString());

            // Add a new document with a generated ID
            db.collection("users")
                    .add(userMap)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            users.setUserName(userName.getText().toString());
                            users.setPassword(password.getText().toString());

                            Log.i(TAG, "What is products: " + users.getUserName().toString());
                            Log.d(TAG, "What is products: " + users.getPassword().toString());

                            Intent intent = new Intent(AddUser.this,HomeView.class);
                            intent.putExtra(userKey, users.getUserName());
                            intent.putExtra(passwordKey, users.getPassword());
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

            Log.e(TAG, "What is get text: " + userName.getText());
        }
        catch(Error e)
        {
            Log.e(TAG, "Exception: " + e);
        }
    }
}
