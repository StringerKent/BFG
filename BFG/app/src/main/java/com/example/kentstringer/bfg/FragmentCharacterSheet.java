package com.example.kentstringer.bfg;

import android.content.Intent;
import android.content.SharedPreferences;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class FragmentCharacterSheet extends Fragment {
    private PlayerCharacter pc;
    private Handler handler = new Handler();
    private User user;
    SharedPreferences sharedpreferences;

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
        handler.postDelayed(runnable, 100);
        user = ((CharactersActivity)getActivity()).user;

        Button charLeft = view.findViewById(R.id.charLeft);
        charLeft.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ((CharactersActivity)getActivity()).setViewPager(((CharactersActivity)getActivity()).getViewPagerIndex()-1);
            }
        });

        Button charRight = view.findViewById(R.id.charRight);
        charRight.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ((CharactersActivity)getActivity()).setViewPager(((CharactersActivity)getActivity()).getViewPagerIndex()+1);
            }
        });

        Button btnNavSecondActivity = view.findViewById(R.id.activeButton);

        btnNavSecondActivity.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                user.setActivePlayerCharacter(pc);
                Toast.makeText(getContext(), "Active character set", Toast.LENGTH_SHORT).show();
            }
        });
        Button deleteButton = view.findViewById(R.id.deleteCharacter);
        if(user.getPlayerCharacters().size() == 1) {
            deleteButton.setVisibility(View.GONE);
        }

        deleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                user.removeCharacter(pc);
                Toast.makeText(getContext(), "Character removed!", Toast.LENGTH_SHORT).show();
                String str = user.toJSON();
                try {
                    JSONObject j = new JSONObject(str);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                sharedpreferences = getActivity().getSharedPreferences("userSave", getContext().MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("user", str);
                editor.commit();

                final Intent intent = new Intent(getContext(), MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", user);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        return view;
    }

    private void createCharacter() {
        try {
            TextView nameInput = getView().findViewById(R.id.charName);
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
            int miles = (int) +pc.getTotalDistanceRan() / 5280;
            double subMile = (+pc.getTotalDistanceRan() % 5280) / 5280;
            DecimalFormat df = new DecimalFormat(".##");
            String subMileFormatted = df.format(subMile);
            runInput.setText("Distance Ran: " + miles + "" + subMileFormatted + " Miles");

            TextView killsInput = getView().findViewById(R.id.characterXP);
            killsInput.setText("Current XP: " + pc.getExperience() + "");

            TextView xpneeded = getView().findViewById(R.id.characterNeededXP);
            xpneeded.setText("XP to next level: " + pc.getExperienceNeeded() + "");

            TextView squatComplete = getView().findViewById(R.id.squatsComplete);
            squatComplete.setText("Squats-  Power: " + pc.getSquatPwr() + "    Complete: " + pc.getSquatsComplete() + "    Chance: " + pc.getSquatChance());

            TextView lungesComplete = getView().findViewById(R.id.lungesComplete);
            lungesComplete.setText("Lunges- Power: " + pc.getLungePwr() + "    Complete: " + pc.getLungesComplete() + "    Chance: " + pc.getLungeChange());

            TextView burpeesComplete = getView().findViewById(R.id.burpeesComplete);
            burpeesComplete.setText("Burpess- Power: " + pc.getBurpeePwr() + "    Complete: " + pc.getBurpeesComplete() + "    Chance: " + pc.getBurpeeChance());

            TextView shadowComplete = getView().findViewById(R.id.shadowComplete);
            shadowComplete.setText("Shadow Boxing- Power: " + pc.getShadowBoxingPwr() + "    Complete: " + pc.getShadowboxingComplete() + "    Chance: " + pc.getShadowChance());

            TextView sprintComplete = getView().findViewById(R.id.sprintsComplete);
            sprintComplete.setText("Sprints- Power: " + pc.getSprintPwr() + "    Complete: " + pc.getSprintsComplete() + "    Chance: " + pc.getSprintChance());
        }catch (NullPointerException npe){
            createCharacter();
        }
    }


}
