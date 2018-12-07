package com.example.kentstringer.bfg;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kentstringer.bfg.Exercises.Burpee;
import com.example.kentstringer.bfg.Exercises.Exercise;
import com.example.kentstringer.bfg.Exercises.Lunge;
import com.example.kentstringer.bfg.Exercises.ShadowBox;
import com.example.kentstringer.bfg.Exercises.Squat;
import com.example.kentstringer.bfg.models.Monster;
import com.example.kentstringer.bfg.models.PlayerCharacter;
import com.example.kentstringer.bfg.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Random;

public class FragmentCircut extends Fragment implements SensorEventListener, LocationListener {
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private Sensor gSensor;
    private Sensor aSensor;
    private boolean isActive = false;
    private String exerciseType = "";
    private double lastKnownPitch = 0;
    private double lastKnownDirection = 0;
    private float[] mGeomagnetic;
    private float[] mGravity;
    private double time = 0;
    private String[] fighterExercises = {"Squats", "Lunges", "Burpees", "Shadow Boxing"};
    private String[] scoutExercises = {"Squats", "Lunges", "Burpees", "Sprints"};
    private String[] rangerExercises = {"Squats", "Lunges", "Burpees", "Shadow Boxing", "Sprints"};
    private Handler handler = new Handler();
    private Runnable runnable = null;
    private Handler imageChanger = new Handler();
    private Runnable imageChang = null;
    private User user;
    private PlayerCharacter pc;
    private Monster monster;
    private Random randy = new Random();
    private boolean singleBattle = false;
    SharedPreferences sharedpreferences;
    private boolean notSprint = true;
    private MediaPlayer mp = null;
    LocationManager locationManager;
    private Location startLocation;
    private int monstersKilled = 0;
    private int bonusXP = 200;

    private Exercise squat = new Squat(2, -2, -70, -110, 3000, 0, "Squats");
    private Exercise lunge = new Lunge(2, -1, -50, -130, 3000, 2, "Lunges");
    private Exercise burpee = new Burpee(-70, -110, 10, -10, 4000, 0, "Burpees");
    private Exercise shadowbox = new ShadowBox(3, -2, 0, 0, 3000, 0, "Shadow Boxing");

    private Exercise activeExercise = new Exercise();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_circut, container, false);
        user = ((MainActivity)getActivity()).user;
        pc = user.getActivePlayerCharacter();
        mSensorManager = (SensorManager) getContext().getSystemService(getContext().SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        gSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        aSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, gSensor, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, aSensor, SensorManager.SENSOR_DELAY_NORMAL);

        Button moveButton = view.findViewById(R.id.arenaMoveButton);
        moveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).changeViewPager(1);
            }
        });

        final int exerciseChangeTime = pc.getLevel() <= 10 ? 30000 : 60000;

        Button b = view.findViewById(R.id.beginWorkOut);
        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(!((MainActivity)getActivity()).exercising) {
                    if (!isActive) {
                        ((MainActivity) getActivity()).exercising = true;
                        ImageView iv = getActivity().findViewById(R.id.monsterImage);
                        iv.setImageResource(R.drawable.monsterone);
                        Button button = view.findViewById(R.id.beginWorkOut);
                        Button b = getActivity().findViewById(R.id.retreatButton);
                        b.setVisibility(View.VISIBLE);
                        if (button.getText().equals("Ready!")) {
                            singleBattle = true;
                            b.setText("Retreat");
                        }
                        pc = user.getActivePlayerCharacter();
                        changeExercise();
                        isActive = !isActive;
                        time = System.currentTimeMillis();
                        runnable = new Runnable() {
                            @Override
                            public void run() {
                                if (isActive) {
                                    changeExercise();
                                    handler.postDelayed(this, exerciseChangeTime);
                                }
                            }
                        };
                        handler.postDelayed(runnable, exerciseChangeTime);

                        changeImage();

                        monster = new Monster(pc.getLevel());

                        ProgressBar pb = getActivity().findViewById(R.id.healthBar);
                        pb.setProgress(100);
                        ProgressBar p = getActivity().findViewById(R.id.characterHealth);
                        p.setProgress(100);
                    }
                }else{
                    Toast.makeText(getContext(), "You can't use the arena on a run. Try looking for trouble instead!",Toast.LENGTH_LONG).show();
                }

            }
        });

        Button s = view.findViewById(R.id.retreatButton);
        s.setVisibility(View.INVISIBLE);
        s.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(isActive) {
                    retreatButtonPressed();
                }
            }
        });
        return view;
    }

    private void changeImage() {
        int[] monsterImages = {R.drawable.monsterone, R.drawable.monstertwo, R.drawable.monsterthree, R.drawable.monsterfour, R.drawable.monsterfive, R.drawable.monstersix, R.drawable.monsterseven
        , R.drawable.monstereight, R.drawable.monsternine, R.drawable.monsterone};

        try {
            ImageView iv = getActivity().findViewById(R.id.monsterImage);
            int choice = pc.getLevel()/2 -1;
            if (choice < 0){
                choice = 0;
            }
            iv.setImageResource(monsterImages[choice]);
        }catch (NullPointerException npe){

        }
    }

    private void retreatButtonPressed(){
        if(!singleBattle) {
            ((MainActivity) getActivity()).exercising = false;
        }
        Button button = getActivity().findViewById(R.id.beginWorkOut);
        button.setText("Begin Training");
        handler.removeCallbacks(runnable);
        imageChanger.removeCallbacks(imageChang);
        isActive = !isActive;
        if (singleBattle) {
            View v = ((MainActivity) getActivity()).getViewPager(1);
            TextView t = v.findViewById(R.id.nameSelect);
            t.setText("Welcome to the Arena");
            Button b = getActivity().findViewById(R.id.retreatButton);
            b.setText("End Training");
            singleBattle = false;
        } else {
            user.receiveXP(monstersKilled * bonusXP);
            pc.killMonster(monstersKilled * bonusXP);
            pc.setMonstersKilled(pc.getMonstersKilled() - 1);
            Toast.makeText(getContext(), "You killed " + monstersKilled + " monsters earning " + (monstersKilled * bonusXP) + " bonus xp!", Toast.LENGTH_LONG).show();
        }
        ImageView iv = getActivity().findViewById(R.id.monsterImage);
        iv.setImageResource(0);
        ProgressBar pb = getActivity().findViewById(R.id.healthBar);
        pb.setProgress(0);
        ProgressBar p = getActivity().findViewById(R.id.characterHealth);
        p.setProgress(0);
        String str = user.toJSON();

        sharedpreferences = getActivity().getSharedPreferences("userSave", getContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("user", str);
        editor.commit();
    }



    private void monsterAttack(){
        if(isActive) {
            TextView tv = new TextView(getContext());
            mp = MediaPlayer.create(getContext(), R.raw.dart);
            mp.start();
            int monsterDmg = makeAttack(monster.getCounterAttackPwr(), true);
            tv.setText("Monster attacked you dealing " + monsterDmg + " damage!");
            tv.setTextSize(20);
            pc.takeDmg(monsterDmg);
            if (pc.getHp() <= 0) {
                Toast.makeText(getContext(), "YOU FEIGNTED! You have lost " + (monster.getExperience()) + " xp!", Toast.LENGTH_LONG).show();
                pc.setHp(pc.getMaxHp());
                pc.killMonster(monster.getExperience() * -1);
                user.receiveXP(monster.getExperience() * -1);
                singleBattle = false;
                isActive = !isActive;
                TextView t = getView().findViewById(R.id.nameSelect);
                Button s = getView().findViewById(R.id.retreatButton);
                Button b = getView().findViewById(R.id.beginWorkOut);
                s.setText("End Training");
                b.setText("Begin Training");
                t.setText("Welcome to the Arena");
                ProgressBar pb = getActivity().findViewById(R.id.healthBar);
                pb.setProgress(0);
                ProgressBar p = getActivity().findViewById(R.id.characterHealth);
                p.setProgress(0);
                ((MainActivity) getActivity()).changeViewPager(1);
            }
            LinearLayout ll = getActivity().findViewById(R.id.display);
            ll.addView(tv, 0);
        }
    }

    @SuppressLint("MissingPermission")
    private void changeExercise() {
        String exer = getExercise();
        while(exer.equals(exerciseType)){
            exer = getExercise();
        }
        switch (exer){
            case "Squats":
                activeExercise = squat;
                break;
            case "Lunges":
                activeExercise = lunge;
                break;
            case "Burpees":
                activeExercise = burpee;
                break;
            case "Shadow Boxing":
                activeExercise = shadowbox;
                break;
            default:
                break;
        }
        mp = MediaPlayer.create(getContext(), R.raw.changeover);
        mp.start();
        exerciseType = exer;
        Button button = getView().findViewById(R.id.beginWorkOut);
        button.setText(exer);
        if (exer.equals("Sprints")){
            button.setText(exer + "-40 meter dash");
            notSprint = false;
            locationManager = (LocationManager)getActivity().getSystemService(getActivity().getApplicationContext().LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);

            Location l = new Location(LocationManager.GPS_PROVIDER);
            l.setLatitude(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude());
            l.setLongitude(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude());
            startLocation = l;
        }else{
            notSprint = true;
        }
    }

    private String getExercise() {
        if (pc.getWorkoutClass().equals("Fighter")){
            return fighterExercises[randy.nextInt(fighterExercises.length)];
        }else if(pc.getWorkoutClass().equals("Scout")){
            return scoutExercises[randy.nextInt(scoutExercises.length)];
        }else{
            return rangerExercises[randy.nextInt(rangerExercises.length)];
        }
    }

    private int makeAttack(int pwr, boolean monsterAttack){
        int attackVal = randy.nextInt(pwr - (pwr/2)) + pwr/2 + 1;
        if(!monsterAttack) {
            switch(randy.nextInt(3)){
                case 0:
                    mp = MediaPlayer.create(getContext(), R.raw.punch);
                    break;
                case 1:
                    mp = MediaPlayer.create(getContext(), R.raw.punchtwo);
                    break;
                default:
                    mp = MediaPlayer.create(getContext(), R.raw.punchthree);
                    break;
            }
            mp.start();
            TextView tv = new TextView(getContext());
            tv.setText("Successful attack! You dealt " + attackVal + " damage!");
            tv.setTextSize(20);
            monster.takeDmg(attackVal);
            LinearLayout ll = getActivity().findViewById(R.id.display);
            ll.addView(tv, 0);
        }
        return randy.nextInt(pwr - (pwr/2)) + pwr/2 + 1;
    }

    int newCount = 0;

    @Override
    public void onSensorChanged(SensorEvent event) {
        double newTime = System.currentTimeMillis();
            try {
                if (isActive && newCount++ % 3 == 0 && notSprint) {
                    newCount = 0;

                    int monsterTimer = activeExercise.getTimer();
                    if (newTime - time >= monsterTimer) {
                        monsterAttack();
                        time = newTime;
                    } else {
                        switch (event.sensor.getType()) {
                            case Sensor.TYPE_LINEAR_ACCELERATION:

                                float x = event.values[0];
                                float y = event.values[1];
                                float z = event.values[2];

                                if(activeExercise.receiveSensorInformation(x,y,z,lastKnownPitch, lastKnownDirection, pc)){
                                    makeAttack(activeExercise.sendAttackInformation(pc), false);
                                    time = System.currentTimeMillis();
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
                        statusUpdate();
                        ProgressBar pb = getActivity().findViewById(R.id.healthBar);
                        pb.setProgress(monster.getPercentageLife());
                        ProgressBar cb = getActivity().findViewById(R.id.characterHealth);
                        cb.setProgress(pc.getPercentageLife());
                    }
                }
            } catch (NullPointerException npe) {

            }
    }

    private void statusUpdate(){
        if (monster.getHp() <= 0) {
            mp = MediaPlayer.create(getContext(), R.raw.monsterdead);
            mp.start();
            monstersKilled++;
            TextView tv = new TextView(getContext());
            tv.setText("The monster has been slain. You earn " + monster.getExperience() + "XP.");
            tv.setTextSize(20);
            LinearLayout ll = getActivity().findViewById(R.id.display);
            ll.addView(tv, 0);
            int pcLevel = pc.getLevel();
            int userLevel = user.getLevel();
            pc.killMonster(monster.getExperience());
            user.receiveXP(monster.getExperience());
            if (pcLevel < pc.getLevel()) {
                mp = MediaPlayer.create(getContext(), R.raw.levelup);
                mp.start();
                TextView levelUp = new TextView(getContext());
                levelUp.setText("YOU LEVELED UP TO LEVEL " + pc.getLevel() + "!");
                levelUp.setTextSize(20);
                ll.addView(levelUp,0);
            }else{
                TextView levelUp = new TextView(getContext());
                levelUp.setText("Current XP " + pc.getExperience() + "/" + pc.getExperienceNeeded());
                levelUp.setTextSize(20);
                ll.addView(levelUp, 0);
            }
            if(userLevel < user.getLevel()){
                mp = MediaPlayer.create(getContext(), R.raw.levelup);
                mp.start();
                TextView levelUp = new TextView(getContext());
                levelUp.setText("YOUR USER LEVELED UP TO LEVEL " + pc.getLevel() + "!");
                levelUp.setTextSize(20);
                ll.addView(levelUp,0);
            }
            if (singleBattle) {
                singleBattle = false;
                retreatButtonPressed();
                isActive = false;
                TextView t = getView().findViewById(R.id.nameSelect);
                Button s = getView().findViewById(R.id.retreatButton);
                Button b = getView().findViewById(R.id.beginWorkOut);
                s.setText("End Training");
                b.setText("Begin Training");
                t.setText("Welcome to the Arena");
                handler.removeCallbacks(runnable);
                imageChanger.removeCallbacks(imageChang);

            } else {
                monster = new Monster();
                monster.setLevel(pc.getLevel());
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onLocationChanged(Location location) {
        try{
            if (!notSprint){
                double newTime = System.currentTimeMillis();
                if(startLocation.distanceTo(location) > 40){
                    makeAttack(pc.getShadowBoxingPwr(), false);
                    pc.setSprintsComplete(pc.getSprintsComplete() + 1);
                    time = System.currentTimeMillis();
                }else{
                    int monsterTimer = 10000;
                    if (newTime - time >= monsterTimer) {
                        monsterAttack();
                        time = newTime;
                    }
                }
            }
        }catch(NullPointerException npe){

        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
