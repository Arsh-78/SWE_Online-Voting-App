package com.example.onlinevotingapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlinevotingapp.R;
import com.example.onlinevotingapp.adapter.CandidateAdapter;
import com.example.onlinevotingapp.adapter.CandidateResultAdapter;
import com.example.onlinevotingapp.model.Candidate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ResultActivity extends AppCompatActivity {

    private RecyclerView resultRv;
    private List<Candidate> list;
    private CandidateResultAdapter adapter;

    private FirebaseFirestore firebaseFirestore;
    private TextView warningtxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        resultRv = findViewById(R.id.result_rv);
        warningtxt = findViewById(R.id.warning_text);
        firebaseFirestore = FirebaseFirestore.getInstance();

        list = new ArrayList<>();
        adapter = new CandidateResultAdapter(ResultActivity.this,list);
        resultRv.setLayoutManager(new LinearLayoutManager(ResultActivity.this));
        resultRv.setAdapter(adapter);

        if (FirebaseAuth.getInstance().getCurrentUser() != null)
        {
            firebaseFirestore.collection("Candidate")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful())
                            {
                                for(DocumentSnapshot snapshot : Objects.requireNonNull(task).getResult())
                                {
                                    list.add(new Candidate(
                                            snapshot.getString("name"),
                                            snapshot.getString("group"),
                                            snapshot.getString("post"),
                                            snapshot.getString("image"),
                                            snapshot.getId()

                                    ));
                                }
                                adapter.notifyDataSetChanged();
                            }else
                            {
                                Toast.makeText(ResultActivity.this, "Candidate Not Found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })   ;
        }

    }
    @Override
    protected void onStart() {
        super.onStart();

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore.getInstance().collection("Users")
                .document(uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        String finish = task.getResult().getString("finish");

                        //assert finish != null;
                        if(finish!=null) {
                            if (!finish.equals("voted")) {
                                resultRv.setVisibility(View.GONE);
                                warningtxt.setVisibility(View.VISIBLE);
                            } else {
                                resultRv.setVisibility(View.VISIBLE);
                                warningtxt.setVisibility(View.GONE);
                            }
                        }
                        else
                        {
                            warningtxt.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }
}