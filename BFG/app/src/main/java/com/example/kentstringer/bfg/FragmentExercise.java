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
    private int squatsCompleted = 0;
    private boolean halfSquat = false;
    private double lastKnownPitch = 0;
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
                isActive = !isActive;
            }
        });
        return view;
    }
    int newCount = 0;
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (isActive && newCount++%3 == 0){
            newCount = 0;
            switch (event.sensor.getType()){
                case Sensor.TYPE_LINEAR_ACCELERATION:
                    float y = event.values[1];

                    if (y > 2 && halfSquat && ((lastKnownPitch > -110 && lastKnownPitch < -70))){
                        squatsCompleted++;
                        TextView tv = getActivity().findViewById(R.id.squatsCounter);
                        tv.setText(squatsCompleted + "");
                        halfSquat= false;
                    }
                    if (y < -2 && ((lastKnownPitch > -110 && lastKnownPitch < -70))){
                        halfSquat= true;
                    }
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    mGeomagnetic = event.values;

                    break;
                case Sensor.TYPE_ACCELEROMETER:
                    mGravity = event.values;
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
                    try {
                        TextView tv = getActivity().findViewById(R.id.degreeTextView);
                        tv.setText(lastKnownPitch + "");
                    }catch(NullPointerException npe){}
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
