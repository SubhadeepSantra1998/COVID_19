package com.example.covid_19;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MyCustomAdapter extends ArrayAdapter<StatesModel> {

    private  Context context;
    private List<StatesModel> statesModelList;
    private List<StatesModel> statesModelListFiltered;

    public MyCustomAdapter( Context context, List<StatesModel> statesModelList) {
        super(context, R.layout.list_custom_item,statesModelList);
        this.context=context;
        this.statesModelList=statesModelList;
        this.statesModelListFiltered=statesModelList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_custom_item,null,true);

        TextView statesName=view.findViewById(R.id.tv_list_States);
        TextView list_confirmed=view.findViewById(R.id.tv_list_confirmed);
        TextView list_active=view.findViewById(R.id.tv_list_active);
        TextView list_deaths=view.findViewById(R.id.tv_list_deaths);
        TextView list_recovered=view.findViewById(R.id.tv_list_recovered);

        statesName.setText(statesModelListFiltered.get(position).getStates());
        list_confirmed.setText(statesModelListFiltered.get(position).getConfirmed());
        list_active.setText(statesModelListFiltered.get(position).getActive());
        list_deaths.setText(statesModelListFiltered.get(position).getDeaths());
        list_recovered.setText(statesModelListFiltered.get(position).getRecovered());


        Animation animation= AnimationUtils.loadAnimation(context,R.anim.slide_left);
        view.startAnimation(animation);

        return view;
    }

    @Override
    public int getCount() {
        return statesModelListFiltered.size();
    }

    @Nullable
    @Override
    public StatesModel getItem(int position) {
        return statesModelListFiltered.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        Filter filter =new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults=new FilterResults();
                if (constraint==null || constraint.length()==0){
                    filterResults.count=statesModelList.size();
                    filterResults.values=statesModelList;
                }else{
                    List<StatesModel> resultsModel=new ArrayList<>();
                    String searchStr=constraint.toString().toLowerCase();
                    for(StatesModel itemsModel:statesModelList){
                        if (itemsModel.getStates().toLowerCase().contains(searchStr)){
                            resultsModel.add(itemsModel);
                        }
                        filterResults.count=resultsModel.size();
                        filterResults.values=resultsModel;
                    }
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                statesModelListFiltered=(List<StatesModel>) results.values;
                States.statesModelList=(List<StatesModel>) results.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }
}
