package com.example.kentstringer.bfg;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.kentstringer.bfg.models.User;

import java.text.DecimalFormat;

public class FragmentProfile extends Fragment {

    private User user;
    private Handler handler = new Handler();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        user = ((CharactersActivity)getActivity()).user;

        Button moveArenaButton = view.findViewById(R.id.profileMove);
        moveArenaButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ((CharactersActivity)getActivity()).setViewPager(1);
            }
        });

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                updatePage();
                handler.postDelayed(this, 2000);
            }
        };
        handler.postDelayed(runnable, 500);

        return view;
    }

    public void updatePage() {
        try {
            TextView nameInput = getView().findViewById(R.id.nameSelect);
            nameInput.setText("User profile");

            TextView levelInput = getView().findViewById(R.id.levelInput);
            levelInput.setText("User level: " + user.getLevel() + "");

            TextView xpInput = getView().findViewById(R.id.xpInput);
            xpInput.setText("User XP: " + user.getExperience() + "");

            TextView xpNeededInput = getView().findViewById(R.id.xpNeededInput);
            xpNeededInput.setText("XP to next level: " + (user.getLevel() * 2500) + "");

            TextView runInput = getView().findViewById(R.id.runInput);
            int miles = (int)+user.getTotalDistanceRun()/5280;
            double subMile = (+user.getTotalDistanceRun()%5280)/5280;
            DecimalFormat df = new DecimalFormat(".##");
            String subMileFormatted = df.format(subMile);
            runInput.setText("Total distance run: " + miles + "" + subMileFormatted + " Miles");

            TextView killsInput = getView().findViewById(R.id.killsInput);
            killsInput.setText("Total monsters killed: " + user.getTotalMonsterKilled() + "");

            TextView activeInput = getView().findViewById(R.id.activeInput);
            activeInput.setText("Active character: " + user.getActivePlayerCharacter().getPcName() + "");
        }catch(NullPointerException npe){

        }
    }
}
