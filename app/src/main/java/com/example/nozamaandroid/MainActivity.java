package com.example.nozamaandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    int userClick = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openUserView(View view) {
        userClick+=1;
        Toast.makeText(this, "User view clicked " + userClick + " times.", Toast.LENGTH_SHORT).show();
    }
}
