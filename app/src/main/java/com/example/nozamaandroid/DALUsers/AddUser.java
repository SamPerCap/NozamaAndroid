package com.example.nozamaandroid.DALUsers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.example.nozamaandroid.MainActivity;
import com.example.nozamaandroid.Models.Users;
import com.example.nozamaandroid.R;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddUser extends AppCompatActivity
{
    EditText userName, password;
    DatabaseReference dref;
    public static String TAG = "ProductApp";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_creation);

        userName = findViewById(R.id.usrName);
        password = findViewById(R.id.usrPw);

        dref = FirebaseDatabase.getInstance().getReference("users");
    }

    private void addProductName()
    {
        userName.getText().toString();
        password.getText().toString();

        String user = userName.getText().toString();
        String passw = password.getText().toString();

        if ( !TextUtils.isEmpty(user) &&  !TextUtils.isEmpty(passw) )
        {
            try
            {
                // get the unique id
                String id = dref.push().getKey();

                // Send the data with id and name and value
                Users prods = new Users(id,user,passw);

                dref.child(id).setValue(prods);
            }
            catch (DatabaseException d)
            {
                Log.e(TAG,"Something Went Wrong: " + d);
            }
            goToMain();
        }
        else {
            Toast.makeText(this, "Please type in a name of the DB and the value", Toast.LENGTH_SHORT).show();
        }
    }

    private void goToMain()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void saveCreation(View view)
    {
        addProductName();
    }
}
