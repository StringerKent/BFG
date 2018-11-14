package com.example.kentstringer.bfg;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kentstringer.bfg.models.PlayerCharacter;
import com.example.kentstringer.bfg.models.User;

import org.json.JSONException;
import org.json.JSONObject;

public class StatsFragment extends Fragment {
      private User user;
      private int allSquats;
      private int allLunges;
      private int allBurpees;
      private int allShadowBoxing;
      private int allSprints;
      private int avgLevel;
      private int avgSquatPwr;
      private int avgLungePwr;
      private int avgBurpeePwr;
      private int avgShadowBoxingPwr;
      private int avgSprintPwr;
      private Handler handler = new Handler();
    SharedPreferences sharedpreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stats, container, false);
        user = ((SecondActivity)getActivity()).user;


        int totalLevel = 0;
        int totalSquatPwr= 0;
        int totalLungePwr = 0;
        int totalBurpeePwr = 0;
        int totalShadowBoxingPwr = 0;
        int totalSprintPwr = 0;

        for (PlayerCharacter pc : user.getPlayerCharacters()) {
            allSquats += pc.getSquatsComplete();
            allLunges += pc.getLungesComplete();
            allBurpees += pc.getBurpeesComplete();
            allShadowBoxing += pc.getShadowboxingComplete();
            allSprints += pc.getSprintsComplete();
            totalLevel += pc.getLevel();
            totalSquatPwr += pc.getSquatPwr();
            totalLungePwr += pc.getLungePwr();
            totalBurpeePwr += pc.getBurpeePwr();
            totalShadowBoxingPwr += pc.getShadowBoxingPwr();
            totalSprintPwr += pc.getSprintPwr();

        }
        avgLevel = totalLevel/user.getPlayerCharacters().size();
        avgSquatPwr = totalSquatPwr/user.getPlayerCharacters().size();
        avgLungePwr = totalLungePwr/user.getPlayerCharacters().size();
        avgBurpeePwr = totalBurpeePwr/user.getPlayerCharacters().size();
        avgShadowBoxingPwr = totalShadowBoxingPwr/user.getPlayerCharacters().size();
        avgSprintPwr = totalSprintPwr/user.getPlayerCharacters().size();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                populateFragment();
            }
        };
        handler.postDelayed(runnable, 100);
        return inflater.inflate(R.layout.fragment_stats, container, false);
    }

    private void populateFragment() {
        try {

            TextView tSquat = getView().findViewById(R.id.totalSquat);
            tSquat.setText("Total squats: \n" + allSquats);

            TextView tLunge = getView().findViewById(R.id.totalLunge);
            tLunge.setText("Total lunges: \n" + allLunges);

            TextView tBurpee = getView().findViewById(R.id.totalBurpee);
            tBurpee.setText("Total burpees: \n" + allBurpees);

            TextView tShadow = getView().findViewById(R.id.totalShadow);
            tShadow.setText("Total shadow boxings: \n" + allShadowBoxing);

            TextView tSprint = getView().findViewById(R.id.totalSprints);
            tSprint.setText("Total sprints: \n" + allSprints);

            TextView aSquat = getView().findViewById(R.id.avgSquatPwr);
            aSquat.setText("Average squat power: \n" + avgSquatPwr);

            TextView aLunge = getView().findViewById(R.id.avgLungePwr);
            aLunge.setText("Average lunge power: \n" + avgLungePwr);

            TextView aBurpee = getView().findViewById(R.id.avgBurpeePwr);
            aBurpee.setText("Average burpee power: \n" + avgBurpeePwr);

            TextView aShadow = getView().findViewById(R.id.avgShadowPwr);
            aShadow.setText("Average shadow boxing power: \n" + avgShadowBoxingPwr);

            TextView aSprint = getView().findViewById(R.id.avgSprintPwr);
            aSprint.setText("Average sprint power: \n" + avgSprintPwr);

            TextView aLevel = getView().findViewById(R.id.avgLevel);
            aLevel.setText("Average level: \n" + avgLevel);

            TextView charNum = getView().findViewById(R.id.characterNum);
            charNum.setText("Characters: \n" + user.getPlayerCharacters().size());
        }catch (NullPointerException npe){

        }
    }


}
