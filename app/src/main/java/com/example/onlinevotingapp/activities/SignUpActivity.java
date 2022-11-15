package com.example.onlinevotingapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.onlinevotingapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpActivity extends AppCompatActivity {

    private CircleImageView userProfile;
    private EditText userName,userPassword,userEmail,userRegisterNumber;
    private Button signUpBtn;
    private Uri mainUri = null;
    private FirebaseAuth mAuth;

    public static final String PREFERENCES = "prefKey";
    public static final String Name = "nameKey";
    public static final String Email = "emailKey";
    public static final String Password = "passwordKey";
    public static final String RegisterNumber = "registerNumberKey";
    public static final String Image = "imageKey";

    SharedPreferences sharedPreferences;

    String name,password,email,registerNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        sharedPreferences = getApplicationContext().getSharedPreferences(PREFERENCES,MODE_PRIVATE);

        findViewById(R.id.haveAccount).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

            }
        });


        userProfile=findViewById(R.id.profile_image);
        userName = findViewById(R.id.user_name);
        userPassword = findViewById(R.id.user_password);
        userEmail = findViewById(R.id.user_email);
        userRegisterNumber=findViewById(R.id.user_regno);
        signUpBtn=findViewById(R.id.signup_btn);

        mAuth=FirebaseAuth.getInstance();

        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                    if(ContextCompat.checkSelfPermission(SignUpActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
                    {
                        ActivityCompat.requestPermissions(SignUpActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                    }
                    else
                    {
                        cropImage();
                    }
                }
                else
                {
                    cropImage();
                }
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 name = userName.getText().toString().trim();
                 password = userPassword.getText().toString().trim();
                 email = userEmail.getText().toString().trim();
                 registerNo = userRegisterNumber.getText().toString().trim();

                if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password)
                        && !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
                        && !TextUtils.isEmpty(registerNo) )
                {
                    createUser(email,password);
                }
                else
                {
                    Toast.makeText(SignUpActivity.this,"Please Enter Your Credentials",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void createUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(SignUpActivity.this,"User Created",Toast.LENGTH_SHORT).show();

                    verifyEmail();
                }
                else
                {
                    Toast.makeText(SignUpActivity.this,"Please Try Again",Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUpActivity.this,"Something Went Wrong",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void verifyEmail() {
        FirebaseUser user = mAuth.getCurrentUser();
        if(user!=null){
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    { //email Sent

                        SharedPreferences.Editor pref = sharedPreferences.edit();
                        pref.putString(Name,name);
                        pref.putString(Password,password);
                        pref.putString(Email,email);
                        pref.putString(RegisterNumber,registerNo);
                        pref.putString(Image,mainUri.toString());
                        pref.commit();


                        Toast.makeText(SignUpActivity.this,"Email Sent",Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
                        finish();
                    }
                    else
                    {
                        mAuth.signOut();
                        finish();
                    }
                }
            });
        }
    }


    private void cropImage() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1,1)
                .start(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mainUri = result.getUri();
                userProfile.setImageURI(mainUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }


}