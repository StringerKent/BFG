package com.example.kentstringer.bfg;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SplashScreenActivity extends AppCompatActivity {
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        final Intent intent = new Intent(getApplicationContext(),
                MainActivity.class);
        sp = getSharedPreferences("userSave", Context.MODE_PRIVATE);
        //show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                startActivity(intent);
                finish();
            }
        }, 1000);
    }

    public void show() {
        StringBuilder str = new StringBuilder();
        if (sp.contains("user")) {
            JSONObject js = null;
            js = new JSONObject(sp.getAll());
            try {
                //String name = js.getString("name");
                String xp = js.getString("xp");
                String level = js.getString("level");
                String kills = js.getString("kills");
                String distance = js.getString("distance");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
