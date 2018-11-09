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
        if (user == null) {
            createTesterClasses();
        }
        setContentView(R.layout.main_activity);
        mViewPager = findViewById(R.id.container);
        setupViewPager(mViewPager);

    }

    private void setupViewPager(ViewPager viewPager){
        SectionStatePagerAdapter adapter = new SectionStatePagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentCircut(), "Train");
        adapter.addFragment(new FragmentMap(), "Map");
        adapter.addFragment(new FragmentProfile(), "Profile");
        adapter.addFragment(new FragmentExercise(), "Demo Exercise");
        viewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(1);
    }

    public void changeViewPager(int index){
        mViewPager.setCurrentItem(index);
    }

    public View getViewPager(int index){
        return mViewPager.getChildAt(index);
    }

    private void createTesterClasses() {
        User testUSer = new User();
        testUSer.setExperience(1000);
        testUSer.setName("Kent Stringer");
        testUSer.setTotalDistanceRun(100);
        testUSer.setTotalMonsterKilled(10000);
        PlayerCharacter testPlayerCharacter = new PlayerCharacter("Fighter", "Thor", 10, 10, 10,
                10, 10, 25, 25, 25, 25,
                0, 30, 30, 1, 1000, 1000, 999,
                1000, 99, 99, 200, 99, 0);

        testUSer.setActivePlayerCharacter(testPlayerCharacter);
        PlayerCharacter testPlayerCharacter2 = new PlayerCharacter("Ranger", "Drizzt", 10, 10, 10,
                10, 10, 20, 20, 20, 20,
                20, 30, 30, 3, 1000, 1000, 0,
                3000, 200, 200, 200, 200, 200);
        PlayerCharacter testPlayerCharacter3 = new PlayerCharacter("Scout", "Running Boi", 10, 10, 10,
                10, 10, 25, 25, 25, 0,
                25, 30, 30, 3, 1000, 1000, 0,
                3000, 200, 200, 200, 200, 200);

        ArrayList<PlayerCharacter> playerCharacters = new ArrayList<>();
        user = testUSer;
        pc = testPlayerCharacter;

        playerCharacters.add(pc);
        playerCharacters.add(testPlayerCharacter2);
        playerCharacters.add(testPlayerCharacter3);
        user.setPlayerCharacters(playerCharacters);
        monster = new Monster();
        monster.setLevel(1);
    }

    @Override
    public void onBackPressed() {
        mViewPager.setCurrentItem(1);
    }

}
