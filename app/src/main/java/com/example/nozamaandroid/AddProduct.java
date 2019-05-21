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

import com.example.nozamaandroid.BLL.BLLProducts;
import com.example.nozamaandroid.Models.Products;
import com.example.nozamaandroid.Shared.CameraIntent;
import com.example.nozamaandroid.Shared.FileChooser;

public class AddProduct extends AppCompatActivity {

    public static String TAG = "AddProduct";
    EditText etName, etPrice, etProdDetail;
    CameraIntent cameraIntent = CameraIntent.getInstance();
    Button btnSave;
    ImageView productImage;
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
    }


    public void saveData(View view) {
        bllProducts = new BLLProducts();
        Products productToSave = new Products();
        productToSave.setProdName(etName.getText().toString());
        productToSave.setProdDetails(etProdDetail.getText().toString());
        productToSave.setPrice(etPrice.getText().toString());
        bllProducts.addProduct(productToSave);
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
        if (cameraIntent.preImage != null) {
            productImage.setImageBitmap(cameraIntent.preImage);
        }
    }
}
