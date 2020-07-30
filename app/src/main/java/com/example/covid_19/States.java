package com.example.covid_19;


import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.leo.simplearcloader.SimpleArcLoader;

import org.eazegraph.lib.models.PieModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class States extends Fragment {

    View view;
    public States() {
        // Required empty public constructor
    }

EditText tab2_et_Search;
    ListView tab2_ListView;
    SimpleArcLoader tab2_loader;

    public static List<StatesModel> statesModelList=new ArrayList<>();
    StatesModel statesModel;
    MyCustomAdapter myCustomAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_states, container, false);

        tab2_et_Search=view.findViewById(R.id.Tab2_Search);
        tab2_ListView=view.findViewById(R.id.Tab2_ListView);
        tab2_loader=view.findViewById(R.id.Tab2_Loader);

        fetchData();

        tab2_et_Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                myCustomAdapter.getFilter().filter(s);
                myCustomAdapter.notifyDataSetChanged();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    private void fetchData() {

        String url="https://api.covid19india.org/data.json";
        tab2_loader.start();


        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray array = response.getJSONArray("statewise");
                            for(int i=1;i<array.length();i++)
                            {
                                JSONObject obj = array.getJSONObject(i);
                                String statesName =obj.getString("state");
                                String state_confirmed=obj.getString("confirmed");
                                String state_active=obj.getString("active");
                                String state_deaths=obj.getString("deaths");
                                String state_recovered=obj.getString("recovered");

                                statesModel=new StatesModel(statesName,state_confirmed,state_active,state_deaths,state_recovered);
                                statesModelList.add(statesModel);


                            }

                            myCustomAdapter=new MyCustomAdapter(getActivity(),statesModelList);

                            tab2_ListView.setAdapter(myCustomAdapter);
                            tab2_loader.stop();
                            tab2_loader.setVisibility(View.GONE);


                        } catch (JSONException e) {
                            e.printStackTrace();
                            tab2_loader.stop();
                            tab2_loader.setVisibility(View.GONE);
                        }



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                    }
                });

        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
    }

}
