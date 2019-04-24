package com.example.nozamaandroid.DALProducts;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.example.nozamaandroid.R;

public class AddProduct extends AppCompatActivity {

    EditText editText, editText2;

    @Override
    protected void onCreate(Bundle saveInstance)
    {
        super.onCreate(saveInstance);
        setContentView(R.layout.addproduct_detail);
        editText = findViewById(R.id.dbName);
        editText2 = findViewById(R.id.dbValue);
    }

    private void textData()
    {

    }

    public void saveData(View view)
    {

    }
}
