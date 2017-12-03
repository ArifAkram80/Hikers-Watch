package com.example.user.hikerswatch;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback, GetLoctionUpdates {
    GoogleMap mMap;
    SupportMapFragment mapFragment;

    // GPSTracker class
    GPSTracker gps;

    // LogCat tag
    private static final String TAG = "MAP";
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 111;
    /// Dialog
    ProgressDialog dialog;

    private Location mLastLocation;


    double speed = 0;
    boolean bSpped = false;

    ClassB classB;
    String address = "";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.MyMapID);
        if (mapFragment == null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            fragmentTransaction.replace(R.id.MyMapID, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);

        return view;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mLastLocation = null;
        mMap = googleMap;
        classB = new ClassB(getActivity());

        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            gps = new GPSTracker(getActivity(), this);
            if (gps.location != null) {
                CenterMapLocation(gps.location, "My Location");
            }
        }
        else{
            GetLocationPermission();
        }

      
        
    }

    @Override
    public void onStart() {
        super.onStart();
      //  gps = new GPSTracker(getActivity(), this);
        Log.i("START", "ON START");
    }



    protected void MapViewChange(boolean flag) {
        if (flag) {
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        } else {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
    }
    protected void GetLocationPermission(){
        if(Build.VERSION.SDK_INT >= 23 && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            Log.d("ARIFFFF", "Permission");
            ActivityCompat.requestPermissions((Activity) getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 111);
        }
    }


    private void GetSpeed(Location pCurrentLocation) {

        if (mLastLocation != null) {
            double lat1 = mLastLocation.getLatitude();
            double lng1 = mLastLocation.getLongitude();
            double lat2 = pCurrentLocation.getLatitude();
            double lng2 = pCurrentLocation.getLongitude();

            double elapsedTime = (pCurrentLocation.getTime() - mLastLocation.getTime()) / (1000); // Convert milliseconds to Minute

            double dLat = Math.toRadians(lat2 - lat1);
            double dLon = Math.toRadians(lng2 - lng1);
            double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                    + Math.cos(Math.toRadians(lat1))
                    * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                    * Math.sin(dLon / 2);
            double c = 2 * Math.asin(Math.sqrt(a));


            double distanceInMeter = Math.round(6371000 * c);

            Log.i(TAG, "Distance " + Double.toString(distanceInMeter));

            speed = distanceInMeter / elapsedTime;
        }

        this.mLastLocation = pCurrentLocation;


        if (speed > 0) {
            if (bSpped) {
                classB.Update_Speed_View((speed * 18) / 5, "km/h");
            } else {
                classB.Update_Speed_View(speed, "m/s");
            }
        } else {
            classB.Update_Speed_View(0, "");
        }

    }


    private void GetGeoAddress(Location location) {

        Geocoder geocoder = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses != null && addresses.size() > 0) {
                if (addresses.get(0).getThoroughfare() != null) {
                    if (addresses.get(0).getSubThoroughfare() != null) {
                        address += addresses.get(0).getSubThoroughfare() + "\n";
                    }
                    address += addresses.get(0).getThoroughfare();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        classB.Update_Address_View(address);


    }

    public void CenterMapLocation(Location location, String Title) {
        LatLng userLoc = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(userLoc).title(Title).icon(BitmapDescriptorFactory.fromResource(R.drawable.humanlogo)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLoc, 15));
        classB.Update_Address_View(address);
    }


    public void ChangeMapType(boolean bool) {
        if (bool == true) {
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        } else mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }


    public void ChangeSpeedUnit(boolean bool) {
        bSpped = bool;
    }


    @Override
    public void OnLocationChangedInterface(Location location) {
        //  Log.i(TAG, location.toString());
        address = "";

        GetSpeed(location);
        GetGeoAddress(location);
        CenterMapLocation(location, "Your Location");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 111) {
            if (grantResults.length > 0) {
                if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    Log.i("ARIFFF", "Dukse -_- ");
                    gps = new GPSTracker(getActivity(), this);

                    gps.getLocation();

                    if (gps.location != null) {

                        CenterMapLocation(gps.location, "My Location");

                    }
                }
            } else {
                Toast.makeText(getActivity().getApplicationContext(), " Permission Not Granted ", Toast.LENGTH_SHORT).show();
            }
        }
    }
}




