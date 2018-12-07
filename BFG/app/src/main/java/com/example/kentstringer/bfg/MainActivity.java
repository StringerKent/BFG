package com.example.kentstringer.bfg;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.kentstringer.bfg.models.Monster;
import com.example.kentstringer.bfg.models.PlayerCharacter;
import com.example.kentstringer.bfg.models.User;

public class MainActivity extends AppCompatActivity{


    private ViewPager mViewPager;
    public User user;
    private Fragment map = new FragmentMap();

    private Fragment circuit = new FragmentCircut();
    public boolean encountered = false;
    public boolean exercising = false;

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
