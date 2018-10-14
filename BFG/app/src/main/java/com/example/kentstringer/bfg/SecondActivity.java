package com.example.kentstringer.bfg;

import android.app.AppComponentFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.kentstringer.bfg.R;

public class SecondActivity extends AppCompatActivity {
    private SectionStatePagerAdapter mSectionStatePagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity_layout);
        mSectionStatePagerAdapter = new SectionStatePagerAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.CharacterContainer);
        setupViewPager(mViewPager);

    }

    private void setupViewPager(ViewPager viewPager){
        SectionStatePagerAdapter adapter = new SectionStatePagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentCharacterSheet(), "Character Sheet");
        adapter.addFragment(new FragmentCharacterSheet(), "Character Sheet");
        adapter.addFragment(new FragmentCharacterSheet(), "Character Sheet");
        adapter.addFragment(new FragmentCharacterSheet(), "Character Sheet");
        viewPager.setAdapter(adapter);
    }

    public void setViewPager(int i){
        mViewPager.setCurrentItem(i);
    }

}
