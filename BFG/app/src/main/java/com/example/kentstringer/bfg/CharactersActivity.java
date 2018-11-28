package com.example.kentstringer.bfg;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.kentstringer.bfg.models.PlayerCharacter;
import com.example.kentstringer.bfg.models.User;

import org.json.JSONException;
import org.json.JSONObject;

public class CharactersActivity extends AppCompatActivity {
    private SectionStatePagerAdapter mSectionStatePagerAdapter;
    private ViewPager mViewPager;
    public User user;
    SharedPreferences sharedpreferences;
    public int currentIndex= 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        user = (User)bundle.getSerializable("user");
        setContentView(R.layout.activity_characters);
        mSectionStatePagerAdapter = new SectionStatePagerAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.CharacterContainer);
        setupViewPager(mViewPager);

    }

    private void setupViewPager(ViewPager viewPager){
        SectionStatePagerAdapter adapter = new SectionStatePagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new StatsFragment(), "stats");
        for (PlayerCharacter pc :user.getPlayerCharacters()) {
            FragmentCharacterSheet f = new FragmentCharacterSheet();
            Bundle bundle = new Bundle();
            bundle.putSerializable("pc", pc);
            f.setArguments(bundle);
            adapter.addFragment(f, "PlayerCharacter Sheet");
        }
        adapter.addFragment(new CharacterCreationActivity(), "creation");
        viewPager.setAdapter(adapter);
    }

    public void setViewPager(int i){
        mViewPager.setCurrentItem(i);
        currentIndex = i;
    }

    public int getViewPagerIndex(){
        return mViewPager.getCurrentItem();
    }

    @Override
    public void onBackPressed() {

        String str = user.toJSON();
        try {
            JSONObject j = new JSONObject(str);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sharedpreferences = getSharedPreferences("userSave", getApplicationContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("user", str);
        editor.commit();

        final Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

}
