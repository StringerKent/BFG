package com.example.kentstringer.bfg;

import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kentstringer.bfg.models.Monster;
import com.example.kentstringer.bfg.models.PlayerCharacter;
import com.example.kentstringer.bfg.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class FragmentCircut extends Fragment implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private Sensor gSensor;
    private Sensor aSensor;
    private boolean isActive = false;
    private String exerciseType = "";
    private boolean halfSquat = false;
    private double lastKnownPitch = 0;
    private double lastKnownDirection = 0;
    private float[] mGeomagnetic;
    private float[] mGravity;
    private double time = 0;
    private String[] exercises = {"Squats", "Lunges", "Burpees", "Shadow Boxing"};
    private Timer myTimer;
    private User user;
    private PlayerCharacter pc;
    private Monster monster;
    private Random randy = new Random();
    private boolean singleBattle = false;
    SharedPreferences sharedpreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_circut, container, false);

        mSensorManager = (SensorManager) getContext().getSystemService(getContext().SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        gSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        aSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, gSensor, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, aSensor, SensorManager.SENSOR_DELAY_NORMAL);

        User testUSer = new User();
        PlayerCharacter testPlayerCharacter = new PlayerCharacter();
        testPlayerCharacter.setSquatPwr(10);
        testPlayerCharacter.setLungePwr(10);
        testPlayerCharacter.setBurpeePwr(10);
        testPlayerCharacter.setShadowBoxingPwr(10);
        testPlayerCharacter.setLevel(1);

        testUSer.setActivePlayerCharacter(testPlayerCharacter);

        ArrayList<PlayerCharacter> playerCharacters = new ArrayList<>();
        user = testUSer;
        pc = testPlayerCharacter;

        playerCharacters.add(pc);
        user.setPlayerCharacters(playerCharacters);
        monster = new Monster();
        monster.setLevel(1);



        Button b = view.findViewById(R.id.beginWorkOut);
        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(!isActive) {
                    Button button = view.findViewById(R.id.beginWorkOut);
                    if (button.getText().equals("Ready!")){
                        singleBattle = true;
                        Button b = getActivity().findViewById(R.id.retreatButton);
                        b.setText("Retreat");
                    }
                    String exercise = getExercise();
                    button.setText(exercise);
                    exerciseType = exercise;
                    isActive = !isActive;
                    time = System.currentTimeMillis();
                    myTimer = new Timer();
                    myTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            TimerMethod();
                        }
                    }, 0, 60000);
                    ProgressBar pb = getActivity().findViewById(R.id.healthBar);
                    pb.setProgress(100);
                    ProgressBar p = getActivity().findViewById(R.id.characterHealth);
                    p.setProgress(100);
                }
            }
        });

        Button s = view.findViewById(R.id.retreatButton);
        s.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(isActive) {
                    Button button = getActivity().findViewById(R.id.beginWorkOut);
                    button.setText("Begin Training");
                    isActive = !isActive;
                    if(singleBattle){
                        View v = ((MainActivity)getActivity()).getViewPager(1);
                        TextView t = v.findViewById(R.id.textTitle);
                        t.setText("Welcome to the Arena");
                        Button b = getActivity().findViewById(R.id.retreatButton);
                        b.setText("End Training");
                        singleBattle = false;
                        ((MainActivity)getActivity()).changeViewPager(1);
                    }
                    myTimer.cancel();
                    ProgressBar pb = getActivity().findViewById(R.id.healthBar);
                    pb.setProgress(0);
                    ProgressBar p = getActivity().findViewById(R.id.characterHealth);
                    p.setProgress(0);
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
                }
            }
        });
        return view;
    }


    private void AddReport(){
        TextView tv = new TextView(getContext());
        int monsterDmg = getAttackVal(monster.getCounterAttackPwr());
        tv.setText("Monster attacked you dealing " + monsterDmg + " damage!");
        pc.takeDmg(monsterDmg);
        if (pc.getHp() <= 0){
            Toast.makeText(getContext(), "YOU FEIGNTED!", Toast.LENGTH_LONG).show();
            pc.setHp(pc.getMaxHp());
            singleBattle = false;
            isActive = !isActive;
            TextView t = getView().findViewById(R.id.textTitle);
            Button s = getView().findViewById(R.id.retreatButton);
            Button b = getView().findViewById(R.id.beginWorkOut);
            s.setText("End Training");
            b.setText("Begin Training");
            t.setText("Welcome to the Arena");
            ProgressBar pb = getActivity().findViewById(R.id.healthBar);
            pb.setProgress(0);
            ProgressBar p = getActivity().findViewById(R.id.characterHealth);
            p.setProgress(0);
            ((MainActivity)getActivity()).changeViewPager(1);
        }
        LinearLayout ll = getActivity().findViewById(R.id.display);
        ll.addView(tv,0);
    }

    private void TimerMethod() {
        String exer = getExercise();
        while(exer.equals(exerciseType)){
            exer = getExercise();
        }
        exerciseType = exer;
        Button button = getView().findViewById(R.id.beginWorkOut);
        button.setText(exer);
    }

    private String getExercise() {

        return exercises[randy.nextInt(exercises.length)];
    }

    private int getAttackVal(int pwr){
        return randy.nextInt(pwr - (pwr/2)) + pwr/2 + 1;
    }

    int newCount = 0;
    @Override
    public void onSensorChanged(SensorEvent event) {
        //Up down array of movements
        if (isActive && newCount++%3 == 0){
            newCount = 0;
            double newTime = System.currentTimeMillis();
            int monsterTimer = exerciseType.equals("Burpees") ? 4000 : 3000;
            if (newTime - time >= monsterTimer){
                AddReport();

                time = newTime;
            }
            switch (event.sensor.getType()){
                case Sensor.TYPE_LINEAR_ACCELERATION:

                    float x = event.values[1];
                    float y = event.values[1];
                    float z = event.values[1];

                    if (exerciseType.equals("Squats")) {
                        if (y > 2 && halfSquat && ((lastKnownPitch > -110 && lastKnownPitch < -70))) {
                            TextView tv = new TextView(getContext());
                            int attackVal = getAttackVal(pc.getSquatPwr());
                            tv.setText("Successful attack! You dealt " + attackVal + " damage!");
                            monster.takeDmg(attackVal);
                            LinearLayout ll = getActivity().findViewById(R.id.display);
                            ll.addView(tv,0);
                            halfSquat = false;
                            time = System.currentTimeMillis();
                        }
                        if (y < -2 && ((lastKnownPitch > -110 && lastKnownPitch < -70))) {
                            halfSquat = true;
                        }
                    }else if(exerciseType.equals("Lunges")){
                        if (y > 2 && halfSquat && ((lastKnownPitch > -110 && lastKnownPitch < -70))) {
                            TextView tv = new TextView(getContext());
                            int attackVal = getAttackVal(pc.getLungePwr());
                            tv.setText("Successful attack! You dealt " + attackVal + " damage!");
                            monster.takeDmg(attackVal);
                            LinearLayout ll = getActivity().findViewById(R.id.display);
                            ll.addView(tv,0);
                            halfSquat = false;
                            time = System.currentTimeMillis();
                        }
                        if (y < -1 && lastKnownDirection > 1 && ((lastKnownPitch > -130 && lastKnownPitch < -50))) {
                            halfSquat = true;
                        }
                    }else if(exerciseType.equals("Shadow Boxing")){
                        if (x < -2) {
                            TextView tv = new TextView(getContext());
                            int attackVal = getAttackVal(pc.getShadowBoxingPwr());
                            tv.setText("Successful attack! You dealt " + attackVal + " damage!");
                            monster.takeDmg(attackVal);
                            LinearLayout ll = getActivity().findViewById(R.id.display);
                            ll.addView(tv,0);
                            halfSquat = false;
                            time = System.currentTimeMillis();
                        }
                        if (x > 3) {
                            halfSquat = true;
                        }
                    }else if(exerciseType.equals("Burpees")){
                        if (halfSquat && ((lastKnownPitch > -110 && lastKnownPitch < -70))) {
                            TextView tv = new TextView(getContext());
                            int attackVal = getAttackVal(pc.getBurpeePwr());
                            tv.setText("Successful attack! You dealt " + attackVal + " damage!");
                            monster.takeDmg(attackVal);

                            LinearLayout ll = getActivity().findViewById(R.id.display);
                            ll.addView(tv,0);
                            halfSquat = false;
                            time = System.currentTimeMillis();
                        }
                        if (((lastKnownPitch < 10 && lastKnownPitch > -10))) {
                            halfSquat = true;
                        }
            }
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    mGeomagnetic = event.values;

                    break;
                case Sensor.TYPE_ACCELEROMETER:
                    mGravity = event.values;
                    lastKnownDirection = event.values[2];
                    break;
            }
            if (mGravity != null && mGeomagnetic != null) {
                float Ri[] = new float[9];
                float I[] = new float[9];

                boolean success = SensorManager.getRotationMatrix(Ri, I, mGravity, mGeomagnetic);
                if (success) {
                    float orientation[] = new float[3];
                    SensorManager.getOrientation(Ri, orientation);
                    float azimut = orientation[0];
                    DecimalFormat df = new DecimalFormat("#.##");
                    double pitch = orientation[1];
                    pitch = Math.toDegrees(pitch);
                    pitch = Double.parseDouble(df.format(pitch));
                    float roll = orientation[2];

                    lastKnownPitch = pitch;
                }
            }
        }
        if (isActive) {
            if (monster.getHp() <= 0) {
                TextView tv = new TextView(getContext());
                tv.setText("The monster has been slain. You earn " + monster.getExperience() + "xp");
                LinearLayout ll = getActivity().findViewById(R.id.display);
                ll.addView(tv, 0);
                int pcLevel = pc.getLevel();
                pc.killMonster(monster.getExperience());
                if (pcLevel > pc.getLevel()){
                    TextView levelUp = new TextView(getContext());
                    levelUp.setText("YOU LEVELED UP TO LEVEL " + pc.getLevel() + "!");
                    ll.addView(levelUp);
                }
                if (singleBattle){
                    singleBattle = false;
                    isActive = !isActive;
                    TextView t = getView().findViewById(R.id.textTitle);
                    Button s = getView().findViewById(R.id.retreatButton);
                    Button b = getView().findViewById(R.id.beginWorkOut);
                    s.setText("End Training");
                    b.setText("Begin Training");
                    t.setText("Welcome to the Arena");
                    ProgressBar pb = getActivity().findViewById(R.id.healthBar);
                    pb.setProgress(0);
                    ProgressBar p = getActivity().findViewById(R.id.characterHealth);
                    p.setProgress(0);
                    ((MainActivity)getActivity()).changeViewPager(1);

                }else {
                    monster = new Monster();
                    monster.setLevel(2);
                }
            }

            ProgressBar pb = getActivity().findViewById(R.id.healthBar);
            pb.setProgress(monster.getPercentageLife());
            ProgressBar cb = getActivity().findViewById(R.id.characterHealth);
            cb.setProgress(pc.getPercentageLife());
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
