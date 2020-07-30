package com.example.covid_19;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.airbnb.lottie.LottieAnimationView;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context){
        this.context=context;
    }

    public int[] slide_images={

            R.raw.wash_hands,
            R.raw.social_distancing,
            R.raw.wear_mask
    };
    public String[] slide_headings= {
            "Wash hands often","Social distancing","Cover your mouth"
    };
    public String [] slide_desc={
            "clean your hands often with soap and water or an alcohol-based hand rub",
            "maintain a safe distance from anyone who is coughing or sneezing",
            "cover your nose and mouth with a mask and use your bent elbow or a tissue while coughing or sneezing"
    };

    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==(RelativeLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.slide_layout,container,false);

        LottieAnimationView slideImageView=view.findViewById(R.id.animation_view);
        TextView slideHeading=view.findViewById(R.id.intro_heading);
        TextView slideDescription=view.findViewById(R.id.intro_description);

        slideImageView.setAnimation(slide_images[position]);
        slideHeading.setText(slide_headings[position]);
        slideDescription.setText(slide_desc[position]);

        container.addView(view);

        return  view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }
}
