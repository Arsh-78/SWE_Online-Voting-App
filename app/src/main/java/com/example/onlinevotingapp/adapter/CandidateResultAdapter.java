package com.example.onlinevotingapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onlinevotingapp.R;
import com.example.onlinevotingapp.activities.VotingActivity;
import com.example.onlinevotingapp.model.Candidate;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CandidateResultAdapter extends RecyclerView.Adapter<CandidateResultAdapter.ViewHolder> {
    private Context context;
    private List<Candidate> list;
    private FirebaseFirestore firebaseFirestore;
    public CandidateResultAdapter(Context context, List<Candidate> list) {
        this.context = context;
        this.list = list;
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.candidate_result_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.name.setText(list.get(position).getName());
        holder.group.setText(list.get(position).getGroup());
        holder.position.setText(list.get(position).getPosition());

        Glide.with(context).load(list.get(position).getImage()).into(holder.image);

        firebaseFirestore.collection("Candidate/"+list.get(position).getId()+"/Vote")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException error) {
                        if(!documentSnapshots.isEmpty())
                        {
                            int count = documentSnapshots.size();
                            Candidate candidate = list.get(position);
                            candidate.setCount(count);
                            list.set(position,candidate);

                            notifyDataSetChanged();
                        }
                    }
                });

        holder.result.setText("Result : " + list.get(position).getCount()) ;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private CircleImageView image;
        private TextView name,position,group,result;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(com.airbnb.lottie.R.id.image);
            name = itemView.findViewById(R.id.name);
            position = itemView.findViewById(R.id.post);
            group = itemView.findViewById(R.id.group);
            result = itemView.findViewById(R.id.candidate_result);

        }
    }
}
