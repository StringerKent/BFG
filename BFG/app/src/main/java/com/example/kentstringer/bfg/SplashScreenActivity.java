package com.example.kentstringer.bfg;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.View;
import android.widget.Toast;

import com.example.kentstringer.bfg.models.PlayerCharacter;
import com.example.kentstringer.bfg.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SplashScreenActivity extends AppCompatActivity {
    SharedPreferences sp;
    private User user = null;
    private static final int REQUEST_PERMISSION_FINE_LOCATION_RESULT = 0;
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            } else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    Toast.makeText(getApplicationContext(), "Application requires access to location", Toast.LENGTH_SHORT).show();
                }
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_FINE_LOCATION_RESULT);
            }
        }

        mp = MediaPlayer.create(this, R.raw.splash);
        mp.start();

        final Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        final Intent intentUser = new Intent(getApplicationContext(), UserCreationActivity.class);
        sp = getSharedPreferences("userSave", Context.MODE_PRIVATE);
        if (sp.contains("user")) {
            show();
            Bundle bundle = new Bundle();
            bundle.putSerializable("user", user);
            intent.putExtras(bundle);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    mp.stop();
                    startActivity(intent);

                    finish();
                }
            }, 2000);
        }else{
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    mp.stop();
                    startActivity(intentUser);
                    finish();
                }
            }, 2000);
        }

    }

    public void show() {
        StringBuilder str = new StringBuilder();
        if (sp.contains("user")) {
            JSONObject js = null;
            try {
                js = new JSONObject(sp.getString("user",null));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                String name = js.getString("name");
                long xp = js.getLong("xp");
                int level = js.getInt("level");
                int kills = js.getInt("kills");
                double distance = js.getDouble("distance");
                int num = js.getInt("characterNum");
                ArrayList<PlayerCharacter> characters = new ArrayList<>();
                for (int i = 0; i < num; i++){
                    JSONObject pc = new JSONObject(js.getString("character"+i));
                    String pcName = pc.getString("name");
                    String pcClass = pc.getString("class");
                    int squatPwr = pc.getInt("squatPwr");
                    int lungePwr = pc.getInt("lungePwr");
                    int burpeePwr = pc.getInt("burpeePwr");
                    int shadowPwr = pc.getInt("shadowPwr");
                    int sprintPwr = pc.getInt("sprintPwr");

                    long squatComplete = pc.getLong("squatComplete");
                    long lungeComplete = pc.getLong("lungeComplete");
                    long burpeeComplete = pc.getLong("burpeeComplete");
                    long shadowComplete = pc.getLong("shadowComplete");
                    long sprintComplete = pc.getLong("sprintComplete");

                    int squatChance = pc.getInt("squatChance");
                    int lungeChance = pc.getInt("lungeChance");
                    int burpeeChance = pc.getInt("burpeeChance");
                    int shadowChance = pc.getInt("shadowChance");
                    int sprintChance = pc.getInt("sprintChance");

                    int pclevel = pc.getInt("level");
                    long experience = pc.getLong("experience");
                    long experienceNeeded = pc.getLong("experienceNeeded");
                    int HP = pc.getInt("HP");
                    int monstersKilled = pc.getInt("monstersKilled");
                    double totalDistanceRan = pc.getDouble("totalDistanceRan");
                    PlayerCharacter player = new PlayerCharacter(pcClass, pcName, squatPwr, lungePwr, burpeePwr, shadowPwr,
                            sprintPwr, squatChance, lungeChance, burpeeChance, shadowChance, sprintChance, HP, HP, pclevel,
                            monstersKilled, totalDistanceRan, experience, experienceNeeded, squatComplete, lungeComplete, burpeeComplete, shadowComplete, sprintComplete);
                    characters.add(player);
                }
                user = new User(name, level, xp, characters, distance, kills);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_FINE_LOCATION_RESULT){
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getApplicationContext(), "Application will not run without premission", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
