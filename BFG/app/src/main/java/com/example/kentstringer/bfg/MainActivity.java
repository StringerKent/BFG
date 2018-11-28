package com.example.kentstringer.bfg;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.kentstringer.bfg.models.Monster;
import com.example.kentstringer.bfg.models.PlayerCharacter;
import com.example.kentstringer.bfg.models.User;

public class MainActivity extends AppCompatActivity{


    private ViewPager mViewPager;
    public User user;
    private PlayerCharacter pc;
    private Monster monster;
    private Fragment map = new FragmentMap();
    private Fragment circuit = new FragmentCircut();
    private Fragment profile = new FragmentProfile();

//    splash screen
//    user creation
//    character creation
//    auto save/load(half done)
//    fine tune exercises

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        user = (User) bundle.getSerializable("user");
        setContentView(R.layout.activity_main);
        mViewPager = findViewById(R.id.container);
        setupViewPager(mViewPager);

    }

    private void setupViewPager(ViewPager viewPager){
        SectionStatePagerAdapter adapter = new SectionStatePagerAdapter(getSupportFragmentManager());
        adapter.addFragment(circuit, "Train");
        adapter.addFragment(map, "Map");
        adapter.addFragment(profile, "Profile");
        viewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(1);
    }

    public void changeViewPager(int index){
        mViewPager.setCurrentItem(index);
    }

    public View getViewPager(int index){
        return mViewPager.getChildAt(index);
    }

    public void hideProfile(){
        SectionStatePagerAdapter adapter = new SectionStatePagerAdapter(getSupportFragmentManager());
        adapter.addFragment(circuit, "Train");
        adapter.addFragment(map, "Map");
        mViewPager.setAdapter(adapter);
    }

    public void reinstateProfile(){
        SectionStatePagerAdapter adapter = new SectionStatePagerAdapter(getSupportFragmentManager());
        adapter.addFragment(circuit, "Train");
        adapter.addFragment(map, "Map");
        adapter.addFragment(profile, "Profile");
        mViewPager.setAdapter(adapter);

    }


    @Override
    public void onBackPressed() {
        mViewPager.setCurrentItem(1);
    }

}
