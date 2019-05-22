package com.example.nozamaandroid;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.nozamaandroid.BLL.BLLProducts;
import com.example.nozamaandroid.Models.Products;
import com.example.nozamaandroid.Shared.CameraIntent;
import com.example.nozamaandroid.Shared.CameraModel;
import com.example.nozamaandroid.Shared.FileChooser;
import com.example.nozamaandroid.Shared.OnResponse;

public class AddProduct extends AppCompatActivity {

    public static String TAG = "AddProduct";
    EditText etName, etPrice, etProdDetail;
    CameraModel cameraModel = CameraModel.getInstance();
    Button btnSave;
    ImageView productImage;
    Spinner categorySpinner;
    BLLProducts bllProducts;
    Intent intent;

    @Override
    protected void onCreate(Bundle saveInstance) {
        super.onCreate(saveInstance);
        setContentView(R.layout.add_product);
        setupItems();
    }

    private void setupItems() {
        etName = findViewById(R.id.etName);
        etPrice = findViewById(R.id.etPrice);
        etProdDetail = findViewById(R.id.etProdDetail);
        btnSave = findViewById(R.id.btnSaveProduct);
        productImage = findViewById(R.id.productPic);
        categorySpinner = findViewById(R.id.categorySpinner);
    }


    public void saveData(View view) {
        Log.d(TAG, "Adding a product");
        if (etName.getText() != null
                || etProdDetail.getText() != null
                || etPrice.getText() != null
                || !categorySpinner.getSelectedItem().toString().equals("Choose Category")) {
            bllProducts = new BLLProducts();
            Products productToSave = new Products();
            productToSave.setProdName(etName.getText().toString());
            productToSave.setProdDetails(etProdDetail.getText().toString());
            productToSave.setPrice(etPrice.getText().toString());
            productToSave.setCategory(categorySpinner.getSelectedItem().toString());
            bllProducts.addProduct(productToSave, cameraModel.preImage, new OnResponse() {
                @Override
                public void onResponseReceived(Object response) {
                    if(response != null){
                        finish();
                        cameraModel.changePreImage(null);
                    }else{
                        Toast.makeText(AddProduct.this, "Error. Product not created because the image", Toast.LENGTH_LONG).show();
                    }
                    Log.d(TAG,"onResponseReceived: " + response);
                }
            });
        } else
            Toast.makeText(this, "More information is needed", Toast.LENGTH_SHORT).show();
    }

    public void gotoCamera(View view) {
        Log.d(TAG, "Going to camera");
        final String[] options = {"Select image", "Take new image"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick an image");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (options[which].equals(options[0])) {
                    intent = new Intent(AddProduct.this, FileChooser.class);
                    startActivity(intent);
                }
                if (options[which].equals(options[1])) {
                    intent = new Intent(AddProduct.this, CameraIntent.class);
                    startActivity(intent);
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (cameraModel.preImage != null) {
            productImage.setImageBitmap(cameraModel.preImage);
        }
    }
}
