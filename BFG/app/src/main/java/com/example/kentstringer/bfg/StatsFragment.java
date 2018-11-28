package com.example.kentstringer.bfg;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.kentstringer.bfg.models.PlayerCharacter;
import com.example.kentstringer.bfg.models.User;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stats, container, false);
        user = ((CharactersActivity)getActivity()).user;

        Runnable runnable2 = new Runnable() {
            @Override
            public void run() {
                Button rightMove = getView().findViewById(R.id.statsRight);
                rightMove.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        ((CharactersActivity)getActivity()).setViewPager(1);
                    }
                });
            }
        };

        handler.postDelayed(runnable2, 100);

        int totalLevel = 0;
        int totalSquatPwr= 0;
        int totalLungePwr = 0;
        int totalBurpeePwr = 0;
        int totalShadowBoxingPwr = 0;
        int totalSprintPwr = 0;

        allSquats = 0;
        allLunges = 0;
        allBurpees = 0;
        allShadowBoxing = 0;
        allSprints = 0;

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
            tSquat.setText("Total squats: " + allSquats);

            TextView tLunge = getView().findViewById(R.id.totalLunge);
            tLunge.setText("Total lunges: " + allLunges);

            TextView tBurpee = getView().findViewById(R.id.totalBurpee);
            tBurpee.setText("Total burpees: " + allBurpees);

            TextView tShadow = getView().findViewById(R.id.totalShadow);
            tShadow.setText("Total shadow boxings: " + allShadowBoxing);

            TextView tSprint = getView().findViewById(R.id.totalSprints);
            tSprint.setText("Total sprints: " + allSprints);

            TextView aSquat = getView().findViewById(R.id.avgSquatPwr);
            aSquat.setText("Avg squat power: " + avgSquatPwr);

            TextView aLunge = getView().findViewById(R.id.avgLungePwr);
            aLunge.setText("Avg lunge power: " + avgLungePwr);

            TextView aBurpee = getView().findViewById(R.id.avgBurpeePwr);
            aBurpee.setText("Avg burpee power: " + avgBurpeePwr);

            TextView aShadow = getView().findViewById(R.id.avgShadowPwr);
            aShadow.setText("Avg shadow boxing power: " + avgShadowBoxingPwr);

            TextView aSprint = getView().findViewById(R.id.avgSprintPwr);
            aSprint.setText("Avg sprint power: " + avgSprintPwr);

            TextView aLevel = getView().findViewById(R.id.avgLevel);
            aLevel.setText("Avg level: " + avgLevel);

            TextView charNum = getView().findViewById(R.id.characterNum);
            charNum.setText("Characters: " + user.getPlayerCharacters().size());
        }catch (NullPointerException npe){

        }
    }


}
