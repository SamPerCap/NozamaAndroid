package com.example.nozamaandroid;

import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.example.nozamaandroid.Models.ProductsMockData;

public class MainActivity extends ListActivity {

    int userClick = 0;

    public static String TAG = "ProductApp";

    ProductsMockData mockData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        this.setTitle("NozamaGo");

        mockData = new ProductsMockData();

        String[] product;

        product = mockData.getProducts();

        ListAdapter adapter =
                new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1,
                        product);

        setListAdapter(adapter);
    }

    public void openUserView(View view) {
        userClick+=1;
        Toast.makeText(this, "User view clicked " + userClick + " times.", Toast.LENGTH_SHORT).show();
    }
}
