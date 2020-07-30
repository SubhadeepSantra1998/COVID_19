package com.example.covid_19;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Intro_Activity extends AppCompatActivity {

    private ViewPager viewPager;
    private LinearLayout dotLayout;

    private TextView[] dots;

    private SliderAdapter sliderAdapter;

    private TextView next,skip;
    private CardView next_card;

    private int currentSlide=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_);


        Toast.makeText(Intro_Activity.this,"Developed by Subhadeep",Toast.LENGTH_SHORT).show();

        viewPager=findViewById(R.id.intro_viewPager);
        dotLayout=findViewById(R.id.intro_linearLayout);


        next= findViewById(R.id.intro_next);

        skip=findViewById(R.id.skip);


        next_card=findViewById(R.id.intro_nextCard);



        sliderAdapter=new SliderAdapter(this);
        viewPager.setAdapter(sliderAdapter);

        addDotsIndicator(0);
        viewPager.addOnPageChangeListener(viewListener);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSlide=viewPager.getCurrentItem();
               if (currentSlide<dots.length)
               {
                   currentSlide++;
                   viewPager.setCurrentItem(currentSlide);
               }
               if (currentSlide==dots.length){
                   if(checkInternetConnection()){
                       startActivity(new Intent(getApplicationContext(),Home_Activity.class));
                       finish();
                   }
               }
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkInternetConnection()){
                    startActivity(new Intent(getApplicationContext(),Home_Activity.class));
                    finish();
                }

            }
        });




    }

    private boolean checkInternetConnection() {
        ConnectivityManager connectivityManager=(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo==null||!networkInfo.isConnected()||!networkInfo.isAvailable()){
            Dialog dialog =new Dialog(this);
            dialog.setContentView(R.layout.no_internet_dialog);
            dialog.setCanceledOnTouchOutside(false);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().getAttributes().windowAnimations=
                    android.R.style.Animation_Dialog;
            Button try_again=dialog.findViewById(R.id.btn_tryAgain);
            try_again.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recreate();
                }
            });
            dialog.show();
return false;
        }else {
            return true;
        }

    }

    public void addDotsIndicator(int position){
        dots=new TextView[3];
        dotLayout.removeAllViews();
        for (int i=0;i<dots.length;i++)
        {
            dots[i]=new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.active));

            dotLayout.addView(dots[i]);
        }
        if (dots.length>0){
            dots[position].setTextColor(getResources().getColor(R.color.active1));
        }
    }
    ViewPager.OnPageChangeListener viewListener=new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int i) {

            addDotsIndicator(i);
            currentSlide=i;
            if(i==0){
                next.setEnabled(true);
                next.setText("Next");

            }else if(i==dots.length-1){
                next.setEnabled(true);
                next.setText("Launch");
            }else
            {
                next.setEnabled(true);
                next.setText("Next");

            }

        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };
}
