package com.example.nozamaandroid;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nozamaandroid.BLL.BLLOrder;
import com.example.nozamaandroid.BLL.BLLUser;
import com.example.nozamaandroid.Models.Order;
import com.example.nozamaandroid.Models.UserModel;
import com.example.nozamaandroid.Models.Users;
import com.example.nozamaandroid.Shared.IMyCallBack;
import com.example.nozamaandroid.Shared.OnResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

public class UserAccountDetails extends AppCompatActivity {
    EditText etUsername, etPhonenumber;
    TextView tvEmail, tvListOfOrders, tvOrderOnWay;
    Spinner sAddress;
    UserModel userModel = UserModel.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUserFirebase = mAuth.getCurrentUser();
    String TAG = "userAccountDetails";
    Users currentUser;
    CircularImageView civUserImage;
    BLLUser bllUser = new BLLUser();
    BLLOrder bllOrder = new BLLOrder();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_account_details);
        setupItems();

        getUserDetails();
    }

    private void setupItems() {
        etUsername = findViewById(R.id.userName);
        sAddress = findViewById(R.id.address);
        etPhonenumber = findViewById(R.id.phoneNumber);
        tvEmail = findViewById(R.id.userEmail);
        civUserImage = findViewById(R.id.userProfile);
        tvListOfOrders = findViewById(R.id.tvListOfOrders);
        tvOrderOnWay = findViewById(R.id.tvOrderOnWay);

    }
    private void getUserDetails() {
        if (currentUserFirebase == null) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, currentUserFirebase.getEmail() + " is logged in.", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "UserID: " + mAuth.getCurrentUser().getUid());

            userModel.getUserById(mAuth.getCurrentUser().getUid(), new OnResponse() {
                @Override
                public void onResponseReceived(Object response) {
                    Log.d(TAG, "onResponseReceived user: " + response);
                    if (response != null) {
                        currentUser = (Users) response;
                        //Customize callback
                        getOrders(mAuth.getCurrentUser().getUid(), new IMyCallBack() {
                            @Override
                            public void onCallBack(ArrayList<Order> ordersInTotal, ArrayList<Order> orderOnWay) {
                                tvOrderOnWay.setText(""+orderOnWay.size());
                                tvListOfOrders.setText(""+ordersInTotal.size());
                            }

                        });
                        etUsername.setText(currentUser.getUserName());
                        sAddress.setSelection(setSpinnerSelection(sAddress, currentUser.getAddress()));
                        etPhonenumber.setText(currentUser.getPhoneNumber());
                        tvEmail.setText(currentUser.getEmail());
                    }
                }
            });
            bllUser.getImageById(mAuth.getCurrentUser().getUid(), new OnResponse() {
                @Override
                public void onResponseReceived(Object response) {
                    if (response != null) {
                        civUserImage.setImageBitmap((Bitmap) response);
                    } else {
                        civUserImage.setImageResource(R.drawable.cake);
                    }
                }
            });
        }
    }

    private void getOrders(String userId, IMyCallBack myCallBack) {
        bllOrder.getOrder(userId, myCallBack);
    }


    private int setSpinnerSelection(Spinner spinner, String address) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equals(address)) {
                return i;
            }
        }
        return 0;
    }


    public void saveEdit(View view) {
        currentUser.setUserName(etUsername.getText().toString());
        currentUser.setAddress(sAddress.getSelectedItem().toString());
        currentUser.setPhoneNumber(etPhonenumber.getText().toString());

        currentUserFirebase = mAuth.getCurrentUser();
        bllUser.updateUser(currentUser, mAuth.getCurrentUser().getUid());
        finish();
    }

    public void removeAccount(View view) {
        currentUserFirebase = mAuth.getCurrentUser();
        bllUser.removeAccount(currentUser.getUserId(), new OnResponse() {
            @Override
            public void onResponseReceived(Object response) {
                if (response != null) {
                    finish();
                } else {
                    Toast.makeText(UserAccountDetails.this, "User not delete", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
