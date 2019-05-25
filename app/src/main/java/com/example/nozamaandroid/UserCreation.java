package com.example.nozamaandroid;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.example.nozamaandroid.Models.Users;
import com.example.nozamaandroid.Shared.CamaraIntent;
import com.example.nozamaandroid.Shared.FileChooser;
import com.example.nozamaandroid.Shared.OnResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserCreation extends AppCompatActivity {
    EditText email, password, phoneNumber, userName;
    Spinner sAddress;
    public static String TAG = "Usercreation";
    Intent ImageIntent;
    private ImageView pictureView;
    ProgressBar progressBar;
    BLLUser bllUser = new BLLUser();
    UserModel userModel = UserModel.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_creation);
        setUpItems();

    }

    private void setUpItems() {
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
        Log.d(TAG, "createUser: "+ sAddress.getSelectedItem()+ " "+email.getText()+" "
        +password.getText()+ " "+
                userName.getText());

        if (password.getText().length() <= 6) {
            Toast.makeText(this, "ERROR. Password has 6 o less digits", Toast.LENGTH_LONG);
        } else if (sAddress.getSelectedItem()!= null
        && email.getText() != null
        && password.getText() != null
        && userName.getText() != null )
        {
            final Users user  = new Users();
            user.setAddress(sAddress.getSelectedItem().toString());
            user.setEmail(email.getText().toString());
            user.setPassword(password.getText().toString());
            // user.setPhoneNumber(phoneNumber.getText().toString());
            user.setUserName(userName.getText().toString());
            bllUser.createUser(user,userModel.preImage, new OnResponse() {
                @Override
                public void onResponseReceived(Object response) {

                    if( response != null)
                    {

                        userModel.changePreImage(null);
                        Intent intent = new Intent(UserCreation.this, HomeView.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(UserCreation.this, "ERROR. User not created", Toast.LENGTH_LONG);
                    }
                    Log.d(TAG, "onResponseReceived: " + response);
                }
            });
        }
                else {
            Toast.makeText(this, "ERROR. need more data", Toast.LENGTH_LONG);

        }
    }
    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (userModel.preImage != null)
        {
            pictureView.setImageBitmap(userModel.preImage);
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
                   startActivity(ImageIntent);
                }
                if (options[which].equals(options[1])) {
                    ImageIntent = new Intent(UserCreation.this, CamaraIntent.class);
                    startActivity(ImageIntent);
                }
            }
        });
        builder.show();
    }


}
