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
    private boolean halfSquat = false;
    private double lastKnownPitch = 0;
    private double lastKnownDirection = 0;
    private float[] mGeomagnetic;
    private float[] mGravity;
    private double time = 0;
    private String[] fighterExercises = {"Squats", "Lunges", "Burpees", "Shadow Boxing"};
    private String[] scoutExercises = {"Squats", "Lunges", "Burpees", "Sprints"};
    private String[] rangerExercises = {"Squats", "Lunges", "Burpees", "Shadow Boxing", "Sprints"};
    private Handler handler = new Handler();
    private Handler imageChanger = new Handler();
    private User user;
    private PlayerCharacter pc;
    private Monster monster;
    private Random randy = new Random();
    private boolean singleBattle = false;
    SharedPreferences sharedpreferences;
    private boolean isStanding = false;
    private boolean notSprint = true;
    private MediaPlayer mp = null;
    LocationManager locationManager;
    private Location startLocation;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_circut, container, false);
        user = ((MainActivity)getActivity()).user;
        mSensorManager = (SensorManager) getContext().getSystemService(getContext().SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        gSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        aSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, gSensor, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, aSensor, SensorManager.SENSOR_DELAY_NORMAL);

        Button b = view.findViewById(R.id.beginWorkOut);
        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(!isActive) {
                    ImageView iv = getActivity().findViewById(R.id.monsterImage);
                    iv.setImageResource(R.drawable.lardo);
                    Button button = view.findViewById(R.id.beginWorkOut);
                    if (button.getText().equals("Ready!")){
                        singleBattle = true;
                        Button b = getActivity().findViewById(R.id.retreatButton);
                        b.setText("Retreat");
                    }
                    pc = user.getActivePlayerCharacter();
                    changeExercise();
                    isActive = !isActive;
                    time = System.currentTimeMillis();
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            if(isActive) {
                                changeExercise();
                                handler.postDelayed(this, 60000);
                            }
                        }
                    };
                    handler.postDelayed(runnable, 60000);

                    Runnable imageChang = new Runnable() {
                        @Override
                        public void run() {
                            /* do what you need to do */
                            if(isActive) {
                                changeImage();
                                imageChanger.postDelayed(this, 500);
                            }
                        }
                    };
                    imageChanger.postDelayed(imageChang, 500);

                    monster = new Monster(pc.getLevel());
                    monster.setCounterAttackPwr(1);

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
                        TextView t = v.findViewById(R.id.nameSelect);
                        t.setText("Welcome to the Arena");
                        Button b = getActivity().findViewById(R.id.retreatButton);
                        b.setText("End Training");
                        singleBattle = false;
                        ((MainActivity)getActivity()).changeViewPager(1);
                    }
                    ImageView iv = getActivity().findViewById(R.id.monsterImage);
                    iv.setImageResource(0);
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

    private void changeImage() {
        ImageView iv = getActivity().findViewById(R.id.monsterImage);
        if (isStanding) {
            iv.setImageResource(R.drawable.lardo);
            isStanding = false;
        }else{
            iv.setImageResource(R.drawable.lardostanding);
            isStanding = true;
        }
    }


    private void monsterAttack(){
        TextView tv = new TextView(getContext());
        mp = MediaPlayer.create(getContext(), R.raw.dart);
        mp.start();
        int monsterDmg = makeAttack(monster.getCounterAttackPwr(), true);
        tv.setText("Monster attacked you dealing " + monsterDmg + " damage!");
        pc.takeDmg(monsterDmg);
        if (pc.getHp() <= 0){
            Toast.makeText(getContext(), "YOU FEIGNTED!", Toast.LENGTH_LONG).show();
            pc.setHp(pc.getMaxHp());
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
            ((MainActivity)getActivity()).changeViewPager(1);
        }
        LinearLayout ll = getActivity().findViewById(R.id.display);
        ll.addView(tv,0);
    }

    @SuppressLint("MissingPermission")
    private void changeExercise() {
        String exer = getExercise();
        while(exer.equals(exerciseType)){
            exer = getExercise();
        }
        mp = MediaPlayer.create(getContext(), R.raw.beep);
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

                    int monsterTimer = exerciseType.equals("Burpees") ? 4000 : 3000;
                    if (newTime - time >= monsterTimer) {
                        monsterAttack();
                        time = newTime;
                    } else {
                        switch (event.sensor.getType()) {
                            case Sensor.TYPE_LINEAR_ACCELERATION:

                                float x = event.values[1];
                                float y = event.values[1];
                                float z = event.values[1];

                                if (exerciseType.equals("Squats")) {
                                    if (y > 2 && halfSquat && ((lastKnownPitch > -110 && lastKnownPitch < -70))) {
                                        makeAttack(pc.getSquatPwr(), false);
                                        pc.setSquatsComplete(pc.getSquatsComplete() + 1);
                                        halfSquat = false;
                                        time = System.currentTimeMillis();
                                    }
                                    if (y < -2 && ((lastKnownPitch > -110 && lastKnownPitch < -70))) {
                                        halfSquat = true;
                                    }
                                } else if (exerciseType.equals("Lunges")) {
                                    if (y > 2 && halfSquat && ((lastKnownPitch > -110 && lastKnownPitch < -70))) {
                                        makeAttack(pc.getLungePwr(), false);
                                        pc.setLungesComplete(pc.getLungesComplete() + 1);
                                        halfSquat = false;
                                        time = System.currentTimeMillis();
                                    }
                                    if (y < -1 && lastKnownDirection > 1 && ((lastKnownPitch > -130 && lastKnownPitch < -50))) {
                                        halfSquat = true;
                                    }
                                } else if (exerciseType.equals("Shadow Boxing")) {
                                    if (x < -2) {
                                        makeAttack(pc.getShadowBoxingPwr(), false);
                                        pc.setShadowboxingComplete(pc.getShadowboxingComplete() + 1);
                                        halfSquat = false;
                                        time = System.currentTimeMillis();
                                    }
                                    if (x > 3) {
                                        halfSquat = true;
                                    }
                                } else if (exerciseType.equals("Burpees")) {
                                    if (halfSquat && ((lastKnownPitch > -110 && lastKnownPitch < -70))) {
                                        makeAttack(pc.getBurpeePwr(), false);
                                        pc.setBurpeesComplete(pc.getBurpeesComplete() + 1);
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
            TextView tv = new TextView(getContext());
            tv.setText("The monster has been slain. You earn " + monster.getExperience() + "XP.");
            LinearLayout ll = getActivity().findViewById(R.id.display);
            ll.addView(tv, 0);
            int pcLevel = pc.getLevel();
            pc.killMonster(monster.getExperience());
            user.receiveXP(monster.getExperience());
            if (pcLevel < pc.getLevel()) {
                mp = MediaPlayer.create(getContext(), R.raw.levelup);
                mp.start();
                TextView levelUp = new TextView(getContext());
                levelUp.setText("YOU LEVELED UP TO LEVEL " + pc.getLevel() + "!");
                ll.addView(levelUp,0);
            }else{
                TextView levelUp = new TextView(getContext());
                levelUp.setText("Current XP " + pc.getExperience() + "/" + pc.getExperienceNeeded());
                ll.addView(levelUp, 0);
            }
            if (singleBattle) {
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

            } else {
                monster = new Monster();
                monster.setLevel(pc.getLevel());
                monster.setCounterAttackPwr(1);
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
