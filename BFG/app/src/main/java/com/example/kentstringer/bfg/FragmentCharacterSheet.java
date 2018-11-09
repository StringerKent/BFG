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
import android.widget.Toast;

import com.example.kentstringer.bfg.models.PlayerCharacter;
import com.example.kentstringer.bfg.models.User;

public class FragmentCharacterSheet extends Fragment {
    private PlayerCharacter pc;
    private Handler handler = new Handler();
    private User user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_character_sheet, container, false);
        Bundle bundle = getArguments();
        pc = (PlayerCharacter) bundle.getSerializable("pc");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                createCharacter();
            }
        };
        handler.postDelayed(runnable, 500);
        user = ((SecondActivity)getActivity()).user;
        Button btnNavSecondActivity = view.findViewById(R.id.activeButton);

        btnNavSecondActivity.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                user.setActivePlayerCharacter(pc);
                Toast.makeText(getContext(), "Active character set", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    private void createCharacter() {
        TextView nameInput = getView().findViewById(R.id.characterName);
        nameInput.setText(pc.getPcName());

        TextView classInput = getView().findViewById(R.id.characterClass);
        classInput.setText(pc.getWorkoutClass());

        TextView levelInput = getView().findViewById(R.id.characterLevel);
        levelInput.setText("Level: " + pc.getLevel());

        TextView xpInput = getView().findViewById(R.id.characterHealth);
        xpInput.setText("Max Health: " + pc.getMaxHp() + "");

        TextView xpNeededInput = getView().findViewById(R.id.characterKills);
        xpNeededInput.setText("Monsters Killed: " + pc.getMonstersKilled());

        TextView runInput = getView().findViewById(R.id.characterDistance);
        runInput.setText("Distance Ran: " +pc.getTotalDistanceRan() + " Miles");

        TextView killsInput = getView().findViewById(R.id.characterXP);
        killsInput.setText("Current XP: " +pc.getExperience() + "");

        TextView xpneeded = getView().findViewById(R.id.characterNeededXP);
        xpneeded.setText("XP to next level: " + pc.getExperienceNeeded() + "");

        TextView squatPwr = getView().findViewById(R.id.squatPwr);
        squatPwr.setText("Power: " +pc.getSquatPwr() + "");

        TextView squatComplete = getView().findViewById(R.id.squatsComplete);
        squatComplete.setText("Complete: " +pc.getSquatsComplete() + "");

        TextView squatChance = getView().findViewById(R.id.squatChance);
        squatChance.setText("Chance: "+ pc.getSquatChance() +"%");

        TextView lungePwr = getView().findViewById(R.id.lungesPwr);
        lungePwr.setText("Power: " +pc.getLungePwr() + "");

        TextView lungesComplete = getView().findViewById(R.id.lungesComplete);
        lungesComplete.setText("Complete: " +pc.getLungesComplete() + "");

        TextView lungesChance = getView().findViewById(R.id.lungesChance);
        lungesChance.setText("Chance: "+ pc.getLungeChange() +"%");

        TextView burpeesPwr = getView().findViewById(R.id.burpeesPwr);
        burpeesPwr.setText("Power: " +pc.getBurpeePwr() + "");

        TextView burpeesComplete = getView().findViewById(R.id.burpeesComplete);
        burpeesComplete.setText("Complete: " +pc.getBurpeesComplete() + "");

        TextView burpeesChance = getView().findViewById(R.id.burpeesChance);
        burpeesChance.setText("Chance: "+ pc.getBurpeeChance() +"%");

        TextView shadowPwr = getView().findViewById(R.id.shadowPwr);
        shadowPwr.setText("Power: " +pc.getShadowBoxingPwr() + "");

        TextView shadowComplete = getView().findViewById(R.id.shadowComplete);
        shadowComplete.setText("Complete: " +pc.getShadowboxingComplete() + "");

        TextView shadowChance = getView().findViewById(R.id.shadowChance);
        shadowChance.setText("Chance: "+ pc.getShadowChance() + "%");

        TextView sprintPwr = getView().findViewById(R.id.sprintsPwr);
        sprintPwr.setText("Power: " +pc.getSprintPwr() + "");

        TextView sprintComplete = getView().findViewById(R.id.sprintsComplete);
        sprintComplete.setText("Complete: " +pc.getSprintsComplete() + "");

        TextView sprintChance = getView().findViewById(R.id.sprintsChance);
        sprintChance.setText("Chance: "+ pc.getSprintChance() + "%");
    }


}
