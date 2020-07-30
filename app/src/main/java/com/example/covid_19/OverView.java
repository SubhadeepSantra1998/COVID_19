package com.example.covid_19;


import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.leo.simplearcloader.SimpleArcLoader;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class OverView extends Fragment {

View view;
    public OverView() {
        // Required empty public constructor
    }

    TextView overview_tvActive, overview_tvRecovered, overview_tvDeath, overview_tvConfirmed, overview_tvDeltaConfirmed, overview_tvDeltaDeaths,overview_tvDeltaRecovered,overview_tvUnaffectedStates;
    SimpleArcLoader tab1_simpleArcLoader;
    ScrollView tab1_scrollView;
    PieChart tab1_pieChart;

int count=0;
String scount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_over_view, container, false);

        overview_tvActive=view.findViewById(R.id.Overview_tv_Active);
        overview_tvRecovered=view.findViewById(R.id.Overview_tv_Recovered);
        overview_tvDeath=view.findViewById(R.id.Overview_tv_Death);
        overview_tvConfirmed=view.findViewById(R.id.Overview_tv_Confirmed);
        //overview_tvDeltaConfirmed=view.findViewById(R.id.Overview_tv_DeltaConfirmed);
        //overview_tvDeltaDeaths=view.findViewById(R.id.Overview_tv_DeltaDeaths);
        //overview_tvDeltaRecovered=view.findViewById(R.id.Overview_tv_DeltaRecovered);
        overview_tvUnaffectedStates=view.findViewById(R.id.Overview_tv_UnaffectedStates);

        tab1_simpleArcLoader=view.findViewById(R.id.tab1_loader);
        tab1_scrollView=view.findViewById(R.id.tab1_scrollStats);
        tab1_pieChart=view.findViewById(R.id.tab1_pieChart);

        fetchData();

        return view;
    }

    private void fetchData() {

        String url="https://api.covid19india.org/data.json";
        tab1_simpleArcLoader.start();


        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray array = response.getJSONArray("statewise");
                            JSONObject jsonObject = array.getJSONObject(0);
                            overview_tvActive.setText(jsonObject.getString("active"));
                            overview_tvRecovered.setText(jsonObject.getString("recovered"));
                            overview_tvDeath.setText(jsonObject.getString("deaths"));
                            overview_tvConfirmed.setText(jsonObject.getString("confirmed"));



                            tab1_pieChart.addPieSlice(new PieModel("Confirmed",Integer.parseInt(overview_tvConfirmed.getText().toString()), Color.parseColor("#29b6f6")));
                            tab1_pieChart.addPieSlice(new PieModel("Active",Integer.parseInt(overview_tvActive.getText().toString()), Color.parseColor("#ffa726")));
                            tab1_pieChart.addPieSlice(new PieModel("Deaths",Integer.parseInt(overview_tvDeath.getText().toString()), Color.parseColor("#ef5350")));
                            tab1_pieChart.addPieSlice(new PieModel("Recovered",Integer.parseInt(overview_tvRecovered.getText().toString()), Color.parseColor("#66bb6a")));
                            tab1_pieChart.startAnimation();

                            tab1_simpleArcLoader.stop();
                            tab1_simpleArcLoader.setVisibility(View.GONE);
                            tab1_scrollView.setVisibility(View.VISIBLE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            tab1_simpleArcLoader.stop();
                            tab1_simpleArcLoader.setVisibility(View.GONE);
                            tab1_scrollView.setVisibility(View.VISIBLE);
                        }



                    }
                },
                new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                tab1_simpleArcLoader.stop();
                tab1_simpleArcLoader.setVisibility(View.GONE);
                tab1_scrollView.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        requestQueue.add(request);


        JsonObjectRequest request1=new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response1) {
                        try {
                            JSONArray array1 = response1.getJSONArray("statewise");
                            for(int i=1;i<array1.length();i++)
                            {
                                JSONObject obj1 = array1.getJSONObject(i);

                                String state_active=obj1.getString("active");
                                if(state_active.equals("0")) {
                                    count++;
                                }
                                scount=Integer.toString(count);
                                overview_tvUnaffectedStates.setText(scount);


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                    }
                });

        RequestQueue request_Queue= Volley.newRequestQueue(getActivity());
        request_Queue.add(request1);
    }


    }







