package com.example.kentstringer.bfg;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kentstringer.bfg.models.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

public class FragmentMap extends Fragment{
    private MapView mMapView;
    private GoogleMap googleMap;
    private Location runStartLocation;
    private double runStartTime;
    private double runTotalDistance;
    private Button btnNavSecondActivity;
    private boolean onRun = false;
    private User user;
    SharedPreferences sharedpreferences;
    private MediaPlayer mp;
    private Handler myHandler = new Handler();

    private BroadcastReceiver broadcastReceiver;
    private ArrayList<Location> location;

    @Override
    public void onResume() {
        super.onResume();
        if(broadcastReceiver == null){
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    location = (ArrayList<Location>)intent.getExtras().get("coords");
                    @SuppressLint("MissingPermission") LatLng userLocation = new LatLng(location.get(location.size()-1).getLatitude(), location.get(location.size()-1).getLongitude());
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(userLocation).zoom(15).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    runTotalDistance = 0;
                    if (onRun) {
                        for (int i = 0; i < location.size()-1; i++) {
                            runTotalDistance += location.get(i).distanceTo(location.get(i+1));
                        }
                        double time = System.currentTimeMillis() - runStartTime;
                        TextView distanceView = getView().findViewById(R.id.distance);
                        TextView runView = getView().findViewById(R.id.runTime2);
                        int miles = (int)runTotalDistance/5280;
                        double subMile = (runTotalDistance%5280)/5280;
                        DecimalFormat df = new DecimalFormat(".##");
                        String subMileFormatted = df.format(subMile);
                        distanceView.setText("Distance: " + miles + "" + subMileFormatted);
                        int hours = (int)(((time/1000)/60)/60);
                        int minutes = (int)(((time/1000)/60));
                        int seconds = (int)((time/1000)%60);
                        runView.setText("Time: " + hours + ":" + minutes + ":" + seconds );
                        Random randy = new Random();
                        Switch s = getView().findViewById(R.id.troubleSwitch);

                        if(randy.nextInt(20) == 0 && s.isChecked() && !((MainActivity)getActivity()).encountered){
                            ((MainActivity)getActivity()).encountered = true;
                            mp = MediaPlayer.create(getContext(), R.raw.battlestart);
                            mp.start();
                            View v = ((MainActivity)getActivity()).getViewPager(1);
                            TextView tv = v.findViewById(R.id.nameSelect);
                            Button b = v.findViewById(R.id.beginWorkOut);
                            if(b == null){
                                View v2 = ((MainActivity)getActivity()).getViewPager(0);
                                tv = v2.findViewById(R.id.nameSelect);
                                b = v2.findViewById(R.id.beginWorkOut);
                            }
                            if (!tv.getText().equals("Encounter!")) {
                                tv.setText("Encounter!");
                                b.setText("Ready!");
                                ((MainActivity) getActivity()).changeViewPager(0);
                                Toast.makeText(getContext(), "Monster Found", Toast.LENGTH_LONG).show();
                            }

                        }
                    }
                }
            };
        }
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter("location_update"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(broadcastReceiver);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, null, false);
        user = ((MainActivity)getActivity()).user;
        mMapView = view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
            googleMap = mMap;
            googleMap.setMyLocationEnabled(true);
            }
        });

        TextView tv = view.findViewById(R.id.nameSelect);
        tv.setText("Welcome " + user.getName());

        btnNavSecondActivity = view.findViewById(R.id.btnNavSecondActivity);

        btnNavSecondActivity.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(!((MainActivity)getActivity()).exercising) {
                    Intent intent = new Intent(getActivity(), CharactersActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("user", user);
                    intent.putExtras(bundle);

                    startActivity(intent);
                }
            }
        });

        Button moveArenaButton = view.findViewById(R.id.mapArenaButton);
        moveArenaButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).changeViewPager(0);
            }
        });

        Button b = view.findViewById(R.id.runButton);
        b.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {
                Button b = v.findViewById(R.id.runButton);
                if (!((MainActivity) getActivity()).exercising || onRun) {
                    b.setText(b.getText().equals("Start Run") ? "End Run" : "Start Run");
                    if (b.getText().equals("End Run")) {
                        ((MainActivity) getActivity()).exercising = true;
                        ((MainActivity) getActivity()).startLocationService();
                        runStartTime = System.currentTimeMillis();
                        runTotalDistance = 0;
                    }else {
                        ((MainActivity) getActivity()).exercising = false;
                        ((MainActivity) getActivity()).stopLocationService();
                        user.endRun(runTotalDistance);
                        int miles = 0;
                        if (runTotalDistance >= 5280) {
                            miles = (int) runTotalDistance / 5280;
                        }
                        Toast.makeText(getContext(), "You ran " + miles + " miles earning " + (miles * 200) + " bonus experience and " + miles + " bonus power", Toast.LENGTH_LONG).show();
                        String str = user.toJSON();
                        sharedpreferences = getContext().getSharedPreferences("userSave", getContext().MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("user", str);
                        editor.commit();
                    }
                    onRun = !onRun;
                }else{
                    Toast.makeText(getContext(), "You can't go on a run. Finish in the arena first!",Toast.LENGTH_LONG).show();
                }
            }
        });
        Runnable runnableTime = new Runnable() {
            @Override
            public void run() {
                try {
                    if (onRun) {
                        double time = System.currentTimeMillis() - runStartTime;
                        TextView runView = getView().findViewById(R.id.runTime2);
                        int hours = (int) (((time / 1000) / 60) / 60);
                        int minutes = (int) (((time / 1000) / 60));
                        int seconds = (int) ((time / 1000)%60);
                        runView.setText("Time: " + hours + ":" + minutes + ":" + seconds );
                    }
                }catch (NullPointerException npe){

                }
                myHandler.postDelayed(this, 1000);
            }
        };
        myHandler.postDelayed(runnableTime, 1000);

        return view;

    }
}
