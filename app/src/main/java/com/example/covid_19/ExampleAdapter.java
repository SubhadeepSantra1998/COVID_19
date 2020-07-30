package com.example.covid_19;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> {
    private ArrayList<ExampleItem> mExampleList;

    public static class ExampleViewHolder extends RecyclerView.ViewHolder{

public LottieAnimationView lottieAnimationView;
public TextView mTextView1,mTextView2;


        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);
            lottieAnimationView=itemView.findViewById(R.id.symptom_image);
            mTextView1=itemView.findViewById(R.id.symptom_heading);
            mTextView2=itemView.findViewById(R.id.symptom_details);
        }
    }
public ExampleAdapter(ArrayList<ExampleItem> exampleList){
        mExampleList=exampleList;
}

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_symptoms,parent,false);
       ExampleViewHolder evh=new ExampleViewHolder(v);
       return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
ExampleItem currentItem=mExampleList.get(position);
holder.lottieAnimationView.setAnimation(currentItem.getImageResource());
holder.mTextView1.setText(currentItem.getText1());
        holder.mTextView2.setText(currentItem.getText2());
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }
}
