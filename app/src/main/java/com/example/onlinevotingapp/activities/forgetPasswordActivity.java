package com.example.onlinevotingapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.onlinevotingapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgetPasswordActivity extends AppCompatActivity {

    private EditText emailEdt;
    private Button reset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        emailEdt = findViewById(R.id.email_edit);
        reset = findViewById(R.id.button);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEdt.getText().toString().trim();
                if(!TextUtils.isEmpty(email)){
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(forgetPasswordActivity.this, "Check Your Email", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else
                            {
                                Toast.makeText(forgetPasswordActivity.this,"Email not Sent",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                 }else
                {
                    Toast.makeText(forgetPasswordActivity.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}