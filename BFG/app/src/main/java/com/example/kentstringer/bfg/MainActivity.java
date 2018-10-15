package com.example.kentstringer.bfg;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        mViewPager = findViewById(R.id.container);
        setupViewPager(mViewPager);
    }

    private void setupViewPager(ViewPager viewPager){
        SectionStatePagerAdapter adapter = new SectionStatePagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AddExerciseFragment(), "AddExercise");
        adapter.addFragment(new FragmentMap(), "Map");
        adapter.addFragment(new FragmentProfile(), "Profile");
        adapter.addFragment(new FragmentExercise(), "Exercise");
        viewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(1);
    }

}
