package com.example.covid_19;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Home_Activity extends AppCompatActivity {

    private static final int REQUEST_CODE_LOCATION_PERMISSIONS=1;


    Button button;
    public String state="",district="";

    public TextView loc_state,loc_district,loc_active,loc_confirmed,loc_deceased,loc_recovered;

    public LocationManager locationManager;

    private RecyclerView mrecyclerView;
    private RecyclerView.Adapter mAdapter;
    private  RecyclerView.LayoutManager mlayoutManager;


    private CardView See_stats;


    private RelativeLayout District_Piechart,blueBackground,whiteBackground;
    private TextView cancel_Pie,More_Info;
    PieChart pieChartDistrict;


    double lattitude,longitude;
    private ProgressBar progressBar;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==REQUEST_CODE_LOCATION_PERMISSIONS && grantResults.length>0){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                getCurrentLocation();
            }else {
                Toast.makeText(this,"Permission denied",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_);


        button=findViewById(R.id.loc_btn);
        loc_state=findViewById(R.id.loc_State);
        loc_district=findViewById(R.id.loc_District);
        loc_active=findViewById(R.id.loc_active);
        loc_confirmed=findViewById(R.id.loc_confirmed);
        loc_deceased=findViewById(R.id.loc_deceased);
        loc_recovered=findViewById(R.id.loc_recovered);

        See_stats=findViewById(R.id.stats);


        //when app launches
        whiteBackground=findViewById(R.id.white_rl);


        //For pie chart
        District_Piechart=findViewById(R.id.rl_District);
        blueBackground=findViewById(R.id.blue_rl);
        cancel_Pie=findViewById(R.id.cross_pie);
        More_Info=findViewById(R.id.more_info);




        locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);


        pieChartDistrict=findViewById(R.id.piechart_District);

        progressBar=findViewById(R.id.progress_bar);


        //Symptoms
        ArrayList<ExampleItem> exampleList = new ArrayList<>();
        exampleList.add(new ExampleItem(R.raw.thermometer, "Fever", "You have a fever if your temperature is above 37.8C. This can make you feel warm, cold,or shivery."));
        exampleList.add(new ExampleItem(R.raw.tired, "Tiredness", "A person with fatigue may feel drained, weak or sluggish with an overall lack of energy" ));
        exampleList.add(new ExampleItem(R.raw.cough, "Dry Cough", "So while we think it's a main symptom, it appears only two out of three times for patients with COVID"));

        mrecyclerView=findViewById(R.id.home_symptoms_RecyclerView);
        mrecyclerView.setHasFixedSize(true);
        mlayoutManager=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        mAdapter=new ExampleAdapter(exampleList);
        mrecyclerView.setLayoutManager(mlayoutManager);
        mrecyclerView.setAdapter(mAdapter);

        checkRunTimepermission();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isLocationButtonOn()){
                    getCurrentLocation();
                }else{
                    showAlert();
                }

            }
        });




//clicking on active,confirmed,deceased and recovered buttons to animate pie graph based on district stats
        loc_active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                        if(!loc_active.getText().equals("unknown")){
                            whiteBackground.setVisibility(View.INVISIBLE);
                            blueBackground.setVisibility(View.VISIBLE);
                            District_Piechart.setVisibility(View.VISIBLE);
                            piechart();
                        }


            }
        });
        loc_confirmed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!loc_confirmed.getText().equals("unknown")){
                    whiteBackground.setVisibility(View.INVISIBLE);
                    blueBackground.setVisibility(View.VISIBLE);
                    District_Piechart.setVisibility(View.VISIBLE);
                    piechart();
                }
            }
        });
        loc_deceased.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!loc_deceased.getText().equals("unknown")){
                    whiteBackground.setVisibility(View.INVISIBLE);
                    blueBackground.setVisibility(View.VISIBLE);
                    District_Piechart.setVisibility(View.VISIBLE);
                    piechart();
                }
            }
        });
        loc_recovered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!loc_recovered.getText().equals("unknown")){
                    whiteBackground.setVisibility(View.INVISIBLE);
                    blueBackground.setVisibility(View.VISIBLE);
                    District_Piechart.setVisibility(View.VISIBLE);
                    piechart();
                }
            }
        });

        cancel_Pie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                District_Piechart.setVisibility(View.INVISIBLE);
                blueBackground.setVisibility(View.INVISIBLE);
                whiteBackground.setVisibility(View.VISIBLE);
            }
        });
        More_Info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),more_info.class));
            }
        });


//when See statistics button is clicked
        See_stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });


    }

    private void checkRunTimepermission() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override public void onPermissionGranted(PermissionGrantedResponse response) {

                        if(!isLocationButtonOn()){
                            showAlert();
                        }
                    }
                    @Override public void onPermissionDenied(PermissionDeniedResponse response) {
                        Intent intent=new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri=Uri.fromParts("package",getPackageName(),null);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }



    private void getCurrentLocation() {

        progressBar.setVisibility(View.VISIBLE);

        final LocationRequest locationRequest=new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.getFusedLocationProviderClient(Home_Activity.this)
                .requestLocationUpdates(locationRequest,new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                LocationServices.getFusedLocationProviderClient(Home_Activity.this)
                        .removeLocationUpdates(this);
                if( locationRequest !=null && locationResult.getLocations().size()>0){
                    int latestLocationIndex=locationResult.getLocations().size()-1;
                     lattitude=locationResult.getLocations().get(latestLocationIndex).getLatitude();
                     longitude=locationResult.getLocations().get(latestLocationIndex).getLongitude();

                    Geocoder geocoder=new Geocoder(Home_Activity.this,Locale.getDefault());

                    try {
                        List<Address> address=geocoder.getFromLocation(lattitude,longitude,1);
                        state=address.get(0).getAdminArea();
                        district=address.get(0).getSubAdminArea();


                        String url="https://api.covid19india.org/v2/state_district_wise.json";

                        JsonArrayRequest request=new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {

                                for(int i=0;i<response.length();i++)
                                {
                                    try {
                                        JSONObject object = response.getJSONObject(i);
                                        String statesData =object.getString("state");
                                        if (statesData.equals(state)){
                                            JSONArray districtArray=object.getJSONArray("districtData");
                                            for (int j=0;j<districtArray.length();j++){
                                                JSONObject districtNameData=districtArray.getJSONObject(j);
                                                String distData =districtNameData.getString("district");
                                                if (distData.equals(district))
                                                {
                                                    String state_active=districtNameData.getString("active");
                                                    String state_confirmed=districtNameData.getString("confirmed");
                                                    String state_deceased=districtNameData.getString("deceased");
                                                    String state_recovered=districtNameData.getString("recovered");




                                                    loc_state.setText(state);
                                                    loc_district.setText(district);
                                                    loc_active.setText(state_active);
                                                    loc_confirmed.setText(state_confirmed);
                                                    loc_deceased.setText(state_deceased);
                                                    loc_recovered.setText(state_recovered);

                                                }
                                            }

                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Log.i("Error1",e.getMessage());
                                    }
                                }


                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                            }
                        });
                        RequestQueue requestQueue= Volley.newRequestQueue(Home_Activity.this);
                        requestQueue.add(request);


                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                progressBar.setVisibility(View.INVISIBLE);


            }
        }, Looper.getMainLooper());

    }




  private boolean isLocationButtonOn() {
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
          button.setBackgroundResource(R.drawable.ic_location_on_black_24dp);
          return true;
      }else {
          button.setBackgroundResource(R.drawable.ic_location_off_black_24dp);
          return false;
      }

  }



    private void showAlert() {
        final AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setCancelable(false)
                .setMessage("To continue, turn on device location to know your district statistics")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        button.setBackgroundResource(R.drawable.ic_location_on_black_24dp);
                    }
                })
                .setNegativeButton("NO THANKS", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        dialog.show();
    }



private void piechart(){
    pieChartDistrict.clearChart();
    pieChartDistrict.addPieSlice(new PieModel("confirmed",Integer.parseInt(loc_confirmed.getText().toString()), Color.parseColor("#FFCC96")));
    pieChartDistrict.addPieSlice(new PieModel("active",Integer.parseInt(loc_active.getText().toString()), Color.parseColor("#93DDFF")));
    pieChartDistrict.addPieSlice(new PieModel("deceased",Integer.parseInt(loc_deceased.getText().toString()), Color.parseColor("#FF9290")));
    pieChartDistrict.addPieSlice(new PieModel("recovered",Integer.parseInt(loc_recovered.getText().toString()), Color.parseColor("#90FF95")));
    pieChartDistrict.startAnimation();
}


    @Override
    public void onBackPressed() {
        final AlertDialog.Builder exit=new AlertDialog.Builder(this);
        exit.setMessage("Are you sure wanna exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        startActivity(new Intent(getApplicationContext(),Stay_home_stay_safe.class));
                        //Home_Activity.super.onBackPressed();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog exitDialog=exit.create();
        exitDialog.show();

    }



}
