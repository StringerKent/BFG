package com.example.kentstringer.bfg;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.Console;
import java.sql.Time;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

public class FragmentMap extends Fragment implements LocationListener {
    private static final int REQUEST_PERMISSION_FINE_LOCATION_RESULT = 0;
    private static final String TAG = "FragmentProfile";
    private MapView mMapView;
    private GoogleMap googleMap;
    LocationManager locationManager;
    private Location runStartLocation;
    private double runStartTime;
    private double runTotalDistance;
    private boolean onRun = false;
    private Timer myTimer;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, null, false);

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
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    getLocation();
                }else{
                    if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                        Toast.makeText(getActivity().getApplicationContext(), "Application requires access to location", Toast.LENGTH_SHORT).show();
                    }
                    requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_FINE_LOCATION_RESULT);
                }
            }else{
                getLocation();
            }
            googleMap = mMap;
            googleMap.setMyLocationEnabled(true);
            //LatLng sydney = new LatLng(locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLatitude(), locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLongitude());
            //CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
            //googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            }
        });

        Button b = (Button)view.findViewById(R.id.runButton);
        b.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {
                Button b = v.findViewById(R.id.runButton);
                b.setText(b.getText().equals("Start Run") ? "End Run" : "Start Run");
                onRun = !onRun;
                if (onRun) {
                    Location l = new Location(LocationManager.NETWORK_PROVIDER);
                    l.setLatitude(locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLatitude());
                    l.setLongitude(locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLongitude());
                    runStartLocation = l;
                    runStartTime = System.currentTimeMillis();
                    runTotalDistance = 0;
                }
            }
        });

        myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                TimerMethod();
            }

        }, 0, 1000);


        return view;

    }

    private void TimerMethod()
    {
        try {
            if (onRun) {
                double time = System.currentTimeMillis() - runStartTime;
                TextView runView = getView().findViewById(R.id.runTime2);
                int hours = (int) (((time / 1000) / 60) / 60);
                int minutes = (int) (((time / 1000) / 60));
                int seconds = (int) ((time / 1000));
                runView.setText("" + hours + ":" + minutes + ":" + seconds);
            }
        }catch (NullPointerException npe){

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_FINE_LOCATION_RESULT){
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getActivity().getApplicationContext(), "Application will not run without premission", Toast.LENGTH_SHORT).show();
            }
        }
    }

    void getLocation(){
        try {
            locationManager = (LocationManager)getActivity().getSystemService(getActivity().getApplicationContext().LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        }catch (SecurityException se){
            se.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        @SuppressLint("MissingPermission") LatLng sydney = new LatLng(locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLatitude(), locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLongitude());
        CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(15).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        if (onRun) {
            double distance = location.distanceTo(runStartLocation) * 3.281;
            double time = System.currentTimeMillis() - runStartTime;
            runTotalDistance += distance;
            runStartLocation = location;

            TextView distanceView = getView().findViewById(R.id.distance);
            TextView runView = getView().findViewById(R.id.runTime2);
            int miles = (int)runTotalDistance/5280;
            double subMile = (runTotalDistance%5280)/5280;
            DecimalFormat df = new DecimalFormat(".##");
            String subMileFormatted = df.format(subMile);
            distanceView.setText(miles+ "" + subMileFormatted);
            int hours = (int)(((time/1000)/60)/60);
            int minutes = (int)(((time/1000)/60));
            int seconds = (int)((time/1000));
            runView.setText("" + hours + ":" + minutes + ":" + seconds );
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
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
