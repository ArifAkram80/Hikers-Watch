package com.example.user.hikerswatch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements Button_Frag.ButtonInterface {


    public static final String TAG = "MainActivity";
    static  TextView Address_View;
    TextView Speed_View;
    MapFragment mapFragment;
    Button LOC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Address_View =  findViewById(R.id.Address);
        Speed_View =    findViewById(R.id.SpeedOmeter);

        getSupportFragmentManager().beginTransaction().add(R.id.FragMap, new MapFragment()).commit();




    }

    @Override
    public void HybridClickListener(boolean Bool) {
        mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.FragMap);
        mapFragment.ChangeMapType(Bool);
    }

    @Override
    public void KMHClickListener(boolean Bool) {
        mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.FragMap);
        mapFragment.ChangeSpeedUnit(Bool);

    }

}
