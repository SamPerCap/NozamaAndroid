package com.example.nozamaandroid;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.nozamaandroid.BLL.BLLUser;
import com.example.nozamaandroid.Models.Users;
import com.example.nozamaandroid.Shared.CameraIntent;
import com.example.nozamaandroid.Shared.CameraModel;
import com.example.nozamaandroid.Shared.FileChooser;
import com.example.nozamaandroid.Shared.OnResponse;

public class UserCreation extends AppCompatActivity {
    EditText email, password, phoneNumber, userName;
    Spinner sAddress;
    public static String TAG = "Usercreation";
    Intent ImageIntent;
    private ImageView pictureView;
    ProgressBar progressBar;
    BLLUser bllUser = new BLLUser();
    CameraModel cameraModel = CameraModel.getInstance();

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
        Log.d(TAG, "createUser: " + sAddress.getSelectedItem() + " " + email.getText() + " "
                + password.getText() + " " +
                userName.getText());
        if (password.getText().length() < 6) {
            Toast.makeText(this, "ERROR. Password has less than 6 digits", Toast.LENGTH_LONG).show();
        } else if (!sAddress.getSelectedItem().toString().equals("Choose Address")
                || email.getText() != null
                || password.getText() != null
                || userName.getText() != null) {
            final Users user = new Users();
            user.setAddress(sAddress.getSelectedItem().toString());
            user.setEmail(email.getText().toString());
            user.setPassword(password.getText().toString());
            user.setPhoneNumber(phoneNumber.getText().toString());
            user.setUserName(userName.getText().toString());
            bllUser.createUser(user, cameraModel.preImage, new OnResponse() {
                @Override
                public void onResponseReceived(Object response) {


                    if( response != null)
                    {

                        cameraModel.changePreImage(null);
                        Intent intent = new Intent(UserCreation.this, HomeView.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                       Toast.makeText(UserCreation.this, "ERROR. User not created because the image", Toast.LENGTH_LONG).show();

                    }
                    Log.d(TAG, "onResponseReceived: " + response);
                }

            });
        } else {
            Toast.makeText(this, "ERROR. need more data", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (cameraModel.preImage != null) {
            pictureView.setImageBitmap(cameraModel.preImage);
        }
    }

    public void createAccount(View view) {
        progressBar.setVisibility(View.VISIBLE);
        try {
            createUser();
        } catch (Exception e) {
            progressBar.setVisibility(View.INVISIBLE);
            Log.d(TAG, "Error creating user: " + e);
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
                    ImageIntent = new Intent(UserCreation.this, CameraIntent.class);
                    startActivity(ImageIntent);
                }
            }
        });
        builder.show();
    }
}
