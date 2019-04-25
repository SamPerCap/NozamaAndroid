package com.example.nozamaandroid.DALProducts;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.example.nozamaandroid.MainActivity;
import com.example.nozamaandroid.Models.Products;
import com.example.nozamaandroid.R;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddProduct extends AppCompatActivity {

    public static String TAG = "ProductApp";
    EditText dbName, dbValue;
    DatabaseReference dref;

    @Override
    protected void onCreate(Bundle saveInstance)
    {
        super.onCreate(saveInstance);
        setContentView(R.layout.addproduct_detail);
        dbName = findViewById(R.id.dbName);
        dbValue = findViewById(R.id.dbValue);

        dref = FirebaseDatabase.getInstance().getReference("products");
    }

    private void addProductName()
    {
        dbName.getText().toString();
        dbValue.getText().toString();

        String prodName = dbName.getText().toString();
        String dataBase = dbValue.getText().toString();

        if ( !TextUtils.isEmpty(prodName) &&  !TextUtils.isEmpty(dataBase) )
        {
            try
            {
                // get the unique id
                String id = dref.push().getKey();

                // Send the data with id and name and value
                Products prods = new Products(id,prodName,dataBase);

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

    public void saveData(View view)
    {
        addProductName();
    }
}
