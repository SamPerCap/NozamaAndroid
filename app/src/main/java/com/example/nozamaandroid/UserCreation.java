package com.example.nozamaandroid;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.nozamaandroid.BLL.BLLUser;
import com.example.nozamaandroid.Models.UserModel;
import com.example.nozamaandroid.Shared.CamaraIntent;
import com.example.nozamaandroid.Shared.FileChooser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class UserCreation extends AppCompatActivity {
    String className = "UserCreation";
    String messageToCamara = "mKey";
    EditText email, password, phoneNumber, userName;
    Spinner sAddress;
    DatabaseReference dref;
    public static String TAG = "Usercreation";
    String saveUser;
    String UserCreationName = "UserCreationName";
    private StorageReference mStorageRef;
    Intent ImageIntent;
    FirebaseAuth mAuth;
    String filePath;
    private ImageView pictureView;
    ProgressBar progressBar;
    String metaName, metaUplTime, metaSize, metaType;
    BLLUser bllUser = new BLLUser();
    UserModel userModel = UserModel.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_creation);
        setUpItems();
        dref = FirebaseDatabase.getInstance().getReference("users");
        bllUser.getFilePath(filePath, pictureView);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        dref = FirebaseDatabase.getInstance().getReference("users");
        Log.d(TAG, "onCreate: " + userModel.currentImage);
        if (userModel.currentImage != null) {
            pictureView.setImageBitmap(userModel.currentImage);
        }
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    private void setUpItems() {
        messageToCamara = getString(R.string.activityClass);
        email = findViewById(R.id.usrEmail3);
        password = findViewById(R.id.usrPw3);
        sAddress = findViewById(R.id.address4);
        phoneNumber = findViewById(R.id.phonenumber3);
        userName = findViewById(R.id.userName4);
        pictureView = findViewById(R.id.userPic);
        progressBar = findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.GONE);


    }

    private void createUser() {
        /*
         * Instead call several classes in the DAL, we are gonna split them
         * between DAL and BLL.
         * This class returns an array of Boolean with size 1, it cannot return
         * a single boolean.
         * The boolean is attach to the task result in the DAL meaning that if success,
         * the boolean we get is true and then we keep going.
         */
        if (password.getText().length() <= 6) {
            Toast.makeText(this, "ERROR. Password has 6 o less digits", Toast.LENGTH_LONG);
        } else {
            if (bllUser.createUser(UserCreation.this, email.getText().toString(), password.getText().toString())) {
                if (bllUser.uploadToStorage(userModel.currentImage, saveUser, mStorageRef)) {
                    if (bllUser.uploadToFirestore(
                            email.getText().toString(),
                            password.getText().toString(),
                            userName.getText().toString(),
                            sAddress.getSelectedItem().toString(),
                            phoneNumber.getText().toString(),
                            saveUser)) {
                        getMetaData();
                    }
                }
            }
        }
    }

    public void imageBtn(View view) {
        progressBar.setVisibility(View.VISIBLE);
        try {
            createUser();
        } catch (Exception e) {
            progressBar.setVisibility(View.INVISIBLE);
            Log.e(TAG, "Error creating user: " + e);
        }
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
                    ImageIntent = new Intent(UserCreation.this, FileChooser.class);
                    ImageIntent.putExtra(messageToCamara, UserCreation.class.getName());
                    startActivity(ImageIntent);
                }
                if (options[which].equals(options[1])) {


                    ImageIntent = new Intent(UserCreation.this, CamaraIntent.class);
                    ImageIntent.putExtra(messageToCamara, UserCreation.class.getName());
                    startActivity(ImageIntent);
                }
            }
        });
        builder.show();
    }

    private void getMetaData() {
        // Get reference to the file
        StorageReference forestRef = mStorageRef.child("user-images/" + saveUser);
        forestRef.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
            @Override
            public void onSuccess(StorageMetadata storageMetadata) {
                Log.d(TAG, "What is the metaData: " + storageMetadata.getContentType());
                Log.d(TAG, "What is the name: " + storageMetadata.getName());
                Log.d(TAG, "What is the size: " + storageMetadata.getSizeBytes());
                Log.d(TAG, "What is the update Time in millis: " + storageMetadata.getUpdatedTimeMillis());
                metaName = storageMetadata.getName();
                metaType = storageMetadata.getContentType();
                metaSize = storageMetadata.getSizeBytes() + "";
                metaUplTime = storageMetadata.getUpdatedTimeMillis() + "";
                bllUser.uploadMetaDataToDatabase(metaName, metaType, metaSize, metaUplTime);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
            }
        });
    }
}
