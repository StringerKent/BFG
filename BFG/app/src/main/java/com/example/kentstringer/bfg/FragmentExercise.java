package com.example.kentstringer.bfg;

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
import android.widget.TextView;
import java.text.DecimalFormat;

public class FragmentExercise extends Fragment implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private Sensor gSensor;
    private Sensor aSensor;
    private boolean isActive = false;
    private int exercisesCompleted = 0;
    private String exerciseType = "";
    private boolean halfSquat = false;
    private double lastKnownPitch = 0;
    private double lastKnownDirection = 0;
    private float[] mGeomagnetic;
    private float[] mGravity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercise, container, false);

        mSensorManager = (SensorManager) getContext().getSystemService(getContext().SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        gSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        aSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, gSensor, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, aSensor, SensorManager.SENSOR_DELAY_NORMAL);

        Button b = view.findViewById(R.id.squatButton);
        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Button button = view.findViewById(R.id.squatButton);
                button.setText(button.getText().equals("Start Squats") ? "End Squats" : "Start Squats");
                exerciseType = "Squats";
                isActive = !isActive;
            }
        });

        Button s = view.findViewById(R.id.squatLunge);
        s.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Button button = view.findViewById(R.id.squatLunge);
                button.setText(button.getText().equals("Start Lunges") ? "End Lunges" : "Start Lunges");
                exerciseType = "Lunges";
                isActive = !isActive;
            }
        });

        Button p = view.findViewById(R.id.burpeeButton);
        p.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Button button = view.findViewById(R.id.burpeeButton);
                button.setText(button.getText().equals("Start Burpees") ? "End Burpees" : "Start Burpees");
                exerciseType = "Burpees";
                isActive = !isActive;
            }
        });
        Button sb = view.findViewById(R.id.shadowButton);
        sb.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Button button = view.findViewById(R.id.shadowButton);
                button.setText(button.getText().equals("Start Shadow Boxing") ? "End Shadow Boxing" : "Start Shadow Boxing");
                exerciseType = "Shadow";
                isActive = !isActive;
            }
        });
        return view;
    }
    int newCount = 0;
    @Override
    public void onSensorChanged(SensorEvent event) {
        //Up down array of movements
        if (isActive && newCount++%3 == 0){
            newCount = 0;
            switch (event.sensor.getType()){
                case Sensor.TYPE_LINEAR_ACCELERATION:

                    float x = event.values[1];
                    float y = event.values[1];
                    float z = event.values[1];

                    try {
                        TextView tv = getActivity().findViewById(R.id.accelTextView);
                        tv.setText(lastKnownPitch + "");
                    }catch(NullPointerException npe){}

                    if (exerciseType.equals("Squats")) {
                        if (y > 2 && halfSquat && ((lastKnownPitch > -110 && lastKnownPitch < -70))) {
                            exercisesCompleted++;
                            TextView tv = getActivity().findViewById(R.id.squatsCounter);
                            tv.setText(exerciseType + " Complete: " + exercisesCompleted + "");
                            halfSquat = false;
                        }
                        if (y < -2 && ((lastKnownPitch > -110 && lastKnownPitch < -70))) {
                            halfSquat = true;
                        }
                    }else if(exerciseType.equals("Lunges")){
                        if (y > 2 && halfSquat && ((lastKnownPitch > -110 && lastKnownPitch < -70))) {
                            exercisesCompleted++;
                            TextView tv = getActivity().findViewById(R.id.squatsCounter);
                            tv.setText(exerciseType + " Complete: " + exercisesCompleted + "");
                            halfSquat = false;
                        }
                        if (y < -1 && lastKnownDirection > 1 && ((lastKnownPitch > -130 && lastKnownPitch < -50))) {
                            halfSquat = true;
                        }
                    }else if(exerciseType.equals("Shadow")){
                        if (x < -2) {
                            exercisesCompleted++;
                            TextView tv = getActivity().findViewById(R.id.squatsCounter);
                            tv.setText(exerciseType + " Complete: " + exercisesCompleted + "");
                            halfSquat = false;
                        }
                        if (x > 3) {
                            halfSquat = true;
                        }
                    }else if(exerciseType.equals("Burpee")){
                        if (halfSquat && ((lastKnownPitch > -110 && lastKnownPitch < -70))) {
                            exercisesCompleted++;
                            TextView tv = getActivity().findViewById(R.id.squatsCounter);
                            tv.setText(exerciseType + " Complete: " + exercisesCompleted + "");
                            halfSquat = false;
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
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
