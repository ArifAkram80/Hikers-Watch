package com.example.user.hikerswatch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements Button_Frag.ButtonInterface {

    static  TextView Address_View;
    TextView Speed_View;
    MapFragment mapFragment;

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
