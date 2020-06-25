package com.desirestodesigns.groceries.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.desirestodesigns.groceries.R;
import com.desirestodesigns.groceries.adapter.SlidePagerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class ViewPagerActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private static final String TAG = "View Page Activity";
    private LinearLayout mDotLayout;
    private TextView[] mDots;
    private FirebaseUser mCurrentUser;
    private FirebaseAuth mAuth;
//    private LinearLayout mDotLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        Log.i(TAG,"View Pager Activity");
        viewPager = findViewById(R.id.viewpager);
//        mDotLayout = findViewById(R.id.dotlayout);
//        mDotLayout = findViewById(R.id.dots);
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        if (mCurrentUser != null) {
            Intent loginIntent = new Intent
                    (ViewPagerActivity.this, MainActivity.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(loginIntent);
            finish();
        }
        SlidePagerAdapter adapter = new SlidePagerAdapter
                (getSupportFragmentManager());
        viewPager.setAdapter(adapter);

//        addDotsIndicators(0);
//        viewPager.addOnPageChangeListener(viewListener);
    }
//    public void addDotsIndicators(int position){
//        mDots = new TextView[2];
//        for(int i = 0;i<mDots.length;i++){
//            mDots[i] = new TextView(this);
//            //cODE THAT CREATES DOTS
//            mDots[i].setText(Html.fromHtml("&#8226"));
//            mDots[i].setTextSize(35);
//            mDots[i].setTextColor(getResources().getColor(R.color.colorTransparent));;
//
//            mDotLayout.addView(mDots[i]);
//        }
//        if((mDots.length>0) || (mDots.length<3)){
//            mDots[position].setTextColor(getResources().getColor(R.color.colorWhite));
//        }
//    }
//    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
//        @Override
//        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//        }
//
//        @Override
//        public void onPageSelected(int position) {
//            addDotsIndicators(position);
//        }
//
//        @Override
//        public void onPageScrollStateChanged(int state) {
//
//        }
//    };
//@Override
//protected void onStart() {
//    super.onStart();
//    if (mCurrentUser == null) {
//        Intent loginIntent = new Intent(ViewPagerActivity.this, LoginActivity.class);
//        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(loginIntent);
//        finish();
//    }
//}
}
