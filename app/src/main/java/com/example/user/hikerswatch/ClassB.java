package com.example.user.hikerswatch;


import android.app.Activity;
import android.content.Context;
import android.widget.TextView;

public class ClassB {
    Context context;

    public ClassB(Context context) {
        this.context = context;
    }

    public void Update_Address_View(String add) {
        TextView Address_View = (TextView) ((Activity) context).findViewById(R.id.Address);

        Address_View.setText("Address: " +add);
    }

    public void Update_Speed_View(double Speed, String Unit) {
        String Speed_Str = String.format("%.2f ", Speed);
        TextView Speed_View = (TextView) ((Activity) context).findViewById(R.id.SpeedOmeter);

        Speed_View.setText("Speed: " + Speed_Str +Unit);
    }

}
