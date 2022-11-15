package com.example.onlinevotingapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.onlinevotingapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {

    public static final String PREFERENCES = "prefKey";
    SharedPreferences sharedPreferences;
    public static final String IsLogIn = "islogin";

    private CircleImageView circleImg;
    private TextView nameTxt,registerNumberTxt;
    private String uid;
    private FirebaseFirestore firebaseFirestore;
    private Button createBtn, voteBtn,startBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        firebaseFirestore = FirebaseFirestore.getInstance();
        uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        circleImg = findViewById(R.id.circle_image);
        nameTxt = findViewById(R.id.name);
        registerNumberTxt = findViewById(R.id.registrationNumber);
        createBtn = findViewById(R.id.admin_btn);
        voteBtn = findViewById(R.id.give_vote);
        startBtn= findViewById(R.id.candidate_create_voting);

        sharedPreferences=getApplicationContext().getSharedPreferences(PREFERENCES,MODE_PRIVATE);
        SharedPreferences.Editor pref = sharedPreferences.edit();
        pref.putBoolean(IsLogIn,true);
        pref.commit();

        /*findViewById(R.id.log_out).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();

                startActivity(new Intent(HomeActivity.this,LoginActivity.class));
                finish();
            }
        });
    */

        firebaseFirestore.collection("Users")
                .document(uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                 String name = task.getResult().getString("name");
                 String registerNumber = task.getResult().getString("registerNumber");
                 String image = task.getResult().getString("image");

                 if(name.equals("admin"))
                 {
                     createBtn.setVisibility(View.VISIBLE);
                     startBtn.setVisibility(View.VISIBLE);
                     voteBtn.setVisibility(View.GONE);
                 }
                 else
                 {
                     createBtn.setVisibility(View.GONE);
                     startBtn.setVisibility(View.GONE);
                     voteBtn.setVisibility(View.VISIBLE);
                 }

                 nameTxt.setText(name);
                 registerNumberTxt.setText(registerNumber);
                    Glide.with(HomeActivity.this).load(image).into(circleImg);

                }else
                {
                    Toast.makeText(HomeActivity.this, "User Not Found", Toast.LENGTH_SHORT).show();
                }
            }
        });

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,Create_Candidate_Activity.class));
            }
        });
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,AllCandidateActivity.class));
            }
        });

        voteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,AllCandidateActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        SharedPreferences.Editor pref = sharedPreferences.edit();
        switch (id){
            case R.id.show_result :
                startActivity(new Intent(HomeActivity.this,ResultActivity.class));
                return true;
            case R.id.log_out:
                FirebaseAuth.getInstance().signOut();
                pref.putBoolean(IsLogIn,false);
                pref.commit();
                startActivity(new Intent(HomeActivity.this,LoginActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}