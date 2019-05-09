package com.example.nozamaandroid.DALUsers;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.nozamaandroid.HomeView;
import com.example.nozamaandroid.Models.Users;
import com.example.nozamaandroid.R;
import com.example.nozamaandroid.Shared.FileChooser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddUser extends AppCompatActivity
{
    String className  ="AddUser";
    String messageToCamara = "mKey";
    EditText email, password, address, phoneNumber, userName;
    DatabaseReference dref;
    public static String TAG = "ProductApp";
    String userKey = "userKey", passwordKey = "passwordKey", nameKey = "nameKey", addressKey = "addressKey", phoneKey = "phoneKey";
    Map<String, Object> userMap = new HashMap<>();
    Map<String, Object> fileMap = new HashMap<>();
    private FirebaseAuth mAuth;
    String saveUser;
    private StorageReference mStorageRef;
    Intent ImageIntent;
    String filePath;
    private ImageView _pictureView;
    String id;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_creation);
        //messageToCamara = getString(R.string.activityClass);
        email = findViewById(R.id.usrEmail3);
        password = findViewById(R.id.usrPw3);
        address = findViewById(R.id.address4);
        phoneNumber = findViewById(R.id.phonenumber3);
        userName = findViewById(R.id.userName4);
        _pictureView = findViewById(R.id.userPic);
        progressBar = findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.GONE);
        dref = FirebaseDatabase.getInstance().getReference("users");
        getFilePath();
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        id = UUID.randomUUID().toString();
    }

    private void getFilePath()
    {
        try {
            Intent intent = getIntent();
            filePath = intent.getStringExtra(messageToCamara);
            if (filePath != null) {
                Log.d(TAG, "is this here?");
                Bitmap bit = BitmapFactory.decodeFile(filePath);
                Log.d(TAG, "is bit null: " + bit);
                if (bit != null) {
                    _pictureView.setImageBitmap(bit);
                    Log.d(TAG, "image is this now: " + filePath);
                }
            }
        } catch (Exception ex) {
            Log.d(TAG, "locateItems: images " + ex);
        }
        Log.d(TAG, "let's see what is image: " + filePath);
    }

    /*@Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }*/

    private void createUser()
    {
        // Here we want to create the user to the auth in the database,
        // We create an simple email and password login
        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            saveUser = user.getUid();
                            Log.d(TAG, "What is the D: " + saveUser);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(AddUser.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    private void uploadToFireStore()
    {
        try
        {
            final Users users = new Users();

            // FireStoreDatabase initialize
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            Log.d(TAG,"What is the id: " + saveUser);
            userMap.put("Email", email.getText().toString());
            userMap.put("Password", password.getText().toString());
            userMap.put("Username", userName.getText().toString());
            userMap.put("Address", address.getText().toString());
            userMap.put("Phonenumber", phoneNumber.getText().toString());
            userMap.put("PictureId", id);

            // Add a new document with a generated ID
            db.collection("users")
                    .document(saveUser)
                    .set(userMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot added with ID: " + saveUser);
                            users.setUserName(email.getText().toString());
                            users.setPassword(password.getText().toString());
                            users.setUserName(userName.getText().toString());
                            users.setAddress(address.getText().toString());
                            users.setPhoneNumber(phoneNumber.getText().toString());

                            Log.i(TAG, "What is products: " + users.getUserName().toString());
                            Log.d(TAG, "What is products: " + users.getPassword().toString());

                            Intent intent = new Intent(AddUser.this,HomeView.class);
                            intent.putExtra(userKey, users.getUserName());
                            intent.putExtra(passwordKey, users.getPassword());
                            intent.putExtra(nameKey, users.getEmail());
                            intent.putExtra(addressKey, users.getAddress());
                            intent.putExtra(phoneKey, users.getPhoneNumber());
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            Log.w(TAG, "Error adding document", e);
                        }
                    });
            Log.e(TAG, "What is get text: " + email.getText());
        }
        catch(Error e)
        {
            Log.e(TAG, "Exception: " + e);
        }

        StorageReference spaceRef = mStorageRef.child("user-images/"+ id);

        Log.d(TAG, "What is ID: " + spaceRef);
    }

    public void imageBtn(View view)
    {
        progressBar.setVisibility(View.VISIBLE);
        try{
            createUser();
        }catch (Exception e)
        {
            Log.e(TAG, "Error creating user: " + e);
        }

        try{
            Log.d(TAG, "Starting uploadPictureToFB");
            Uri file = Uri.fromFile(new File(filePath));

            StorageReference riversRef = mStorageRef.child("user-images/" + id);
            Log.d(TAG, "What is Uri file: " + file);
            riversRef.putFile(file)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            Task<Uri> downloadUrl = mStorageRef.getDownloadUrl();
                            Log.d(TAG, "What is downloadURL: " + downloadUrl + " and name: " + mStorageRef.getName());

                            uploadToFireStore();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            Log.d(TAG, "Failed to upload an image to storage: " + exception);
                        }
                    });


        }catch (Exception e)
        {
            Log.d(TAG, "Throws an exception when creating a user or uploading to storage " + e);
        }

    }

    public void gotoCamera(View view)
    {
        Log.e(TAG, "What happens?");
        final String[] options = {"Select image", "Take new image"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick a color");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(options[which].equals(options[0]))
                {
                    ImageIntent = new Intent(AddUser.this, FileChooser.class);
                    ImageIntent.putExtra(messageToCamara,className);
                    startActivity(ImageIntent);
                }
                if(options[which].equals(options[1]))
                {


                    /*ImageIntent = new Intent(this, CamaraIntent.class);
                    ImageIntent.putExtra(messageToCamara,className);
                    startActivity(ImageIntent);*/
                }
            }
        });
        builder.show();
    }
}
