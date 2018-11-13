package com.example.kentstringer.bfg;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.kentstringer.bfg.models.Monster;
import com.example.kentstringer.bfg.models.PlayerCharacter;
import com.example.kentstringer.bfg.models.User;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{


    private ViewPager mViewPager;
    public User user;
    private PlayerCharacter pc;
    private Monster monster;

//    splash screen
//    user creation
//    character creation
//    auto save/load(half done)
//    fine tune exercises

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        user = (User)bundle.getSerializable("user");
        setContentView(R.layout.main_activity);
        mViewPager = findViewById(R.id.container);
        setupViewPager(mViewPager);

    }

    private void setupViewPager(ViewPager viewPager){
        SectionStatePagerAdapter adapter = new SectionStatePagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentCircut(), "Train");
        adapter.addFragment(new FragmentMap(), "Map");
        adapter.addFragment(new FragmentProfile(), "Profile");
        //adapter.addFragment(new FragmentExercise(), "Demo Exercise");
        viewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(1);
    }

    public void changeViewPager(int index){
        mViewPager.setCurrentItem(index);
    }

    public View getViewPager(int index){
        return mViewPager.getChildAt(index);
    }

    @Override
    public void onBackPressed() {
        mViewPager.setCurrentItem(1);
    }

}
