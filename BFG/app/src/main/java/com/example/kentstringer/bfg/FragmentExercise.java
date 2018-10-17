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


public class FragmentExercise extends Fragment implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private Sensor gSensor;
    private boolean isActive = false;
    private int squatsCompleted = 0;
    private boolean halfSquat = false;
    private double lastKnownPitch = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercise, container, false);

        mSensorManager = (SensorManager) getContext().getSystemService(getContext().SENSOR_SERVICE);;
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        gSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR);

        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, gSensor, SensorManager.SENSOR_DELAY_NORMAL);

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


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION && isActive) {

            float y = event.values[1];

            if (y > 2 && halfSquat && lastKnownPitch > 110 && lastKnownPitch < 140){
                squatsCompleted++;
                TextView tv = getActivity().findViewById(R.id.squatsCounter);
                tv.setText(squatsCompleted + "");
                halfSquat= false;
            }
            if (y < -2 && lastKnownPitch > 110 && lastKnownPitch < 140){
                halfSquat= true;
            }
        }else{
            float rotationMatrix[] = new float[16];
            float[] orientationValues = new float[3];

            mSensorManager.getRotationMatrixFromVector(rotationMatrix,event.values);
            SensorManager.getOrientation(rotationMatrix, orientationValues);

            //double azimuth = (int) Math.round(Math.toDegrees(Math.acos(event.values[0])));
            double pitch = (int) Math.round(Math.toDegrees(Math.acos(event.values[1])));
            //double roll = (int) Math.round(Math.toDegrees(Math.acos(event.values[2])));
            lastKnownPitch= pitch;
        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public interface Listener {
        void onMotionDetected(SensorEvent event, float acceleration);
    }
}
