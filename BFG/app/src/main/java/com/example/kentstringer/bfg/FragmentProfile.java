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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kentstringer.bfg.models.PlayerCharacter;
import com.example.kentstringer.bfg.models.User;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

public class FragmentProfile extends Fragment {
    private Button btnNavSecondActivity;
    private User user;
    private Handler handler = new Handler();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        user = ((MainActivity)getActivity()).user;
        btnNavSecondActivity = view.findViewById(R.id.btnNavSecondActivity);

        btnNavSecondActivity.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getActivity(), SecondActivity.class);
                Bundle bundle = new Bundle();
                //Add your data from getFactualResults method to bundle
                bundle.putSerializable("user", user);
                //Add the bundle to the intent
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });

        final Button button = view.findViewById(R.id.createCharButton);
        button.setOnClickListener(new View.OnClickListener() {
              public void onClick(View v) {
                  TextView tv =  getView().findViewById(R.id.characterName);
                  String name = tv.getText().toString();
                  String characterClass = (String) ((Spinner) getView().findViewById(R.id.classChooser)).getSelectedItem();
                  PlayerCharacter pc;
                  switch (characterClass) {
                      case "Fighter - Strength Based":
                          pc = new PlayerCharacter(characterClass, name, 10, 10, 10,
                                  10, 10, 25, 25, 25, 25, 0);
                          break;
                      case "Ranger - Balanced Class":
                          pc = new PlayerCharacter(characterClass, name, 10, 10, 10,
                                  10, 10, 25, 25, 25, 0, 25);
                          break;
                      default:
                          pc = new PlayerCharacter(characterClass, name, 10, 10, 10,
                                  10, 10, 20, 20, 20, 20, 20);
                          break;
                  }
                  user.addNewCharacter(pc);
                  Toast.makeText(getContext(), "Character Created!", Toast.LENGTH_SHORT).show();
              }
        });
        //updatePage();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                /* do what you need to do */
                updatePage();
                /* and here comes the "trick" */
                handler.postDelayed(this, 2000);
            }
        };
        handler.postDelayed(runnable, 500);

        return view;
    }

    public void updatePage() {
        try {
            TextView nameInput = getView().findViewById(R.id.nameInput);
            nameInput.setText(user.getName());

            TextView levelInput = getView().findViewById(R.id.levelInput);
            levelInput.setText(user.getLevel() + "");

            TextView xpInput = getView().findViewById(R.id.xpInput);
            xpInput.setText(user.getExperience() + "");

            TextView xpNeededInput = getView().findViewById(R.id.xpNeededInput);
            xpNeededInput.setText((user.getLevel() * 2500) + "");

            TextView runInput = getView().findViewById(R.id.runInput);
            int miles = (int)+user.getTotalDistanceRun()/5280;
            double subMile = (+user.getTotalDistanceRun()%5280)/5280;
            DecimalFormat df = new DecimalFormat(".##");
            String subMileFormatted = df.format(subMile);
            runInput.setText(miles + "" + subMileFormatted + " Miles");

            TextView killsInput = getView().findViewById(R.id.killsInput);
            killsInput.setText(user.getTotalMonsterKilled() + "");

            TextView activeInput = getView().findViewById(R.id.activeInput);
            activeInput.setText(user.getActivePlayerCharacter().getPcName() + "");
        }catch(NullPointerException npe){

        }
    }
}
