package com.example.user.hikerswatch;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;


/**
 * A simple {@link Fragment} subclass.
 */
public class Button_Frag extends Fragment {


    Switch kmh, hybrid;

    ButtonInterface buttonInterface;

    public interface ButtonInterface {
        public void HybridClickListener(boolean Bool);

        public void KMHClickListener(boolean Bool);
    }

    public Button_Frag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_button_, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }


    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        kmh = (Switch) view.findViewById(R.id.kmhSwitch);
        hybrid = (Switch) view.findViewById(R.id.HybridSwitch);

        kmh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                buttonInterface.KMHClickListener(b);
            }
        });

        hybrid.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                buttonInterface.HybridClickListener(b);
            }
        });


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            buttonInterface = (ButtonInterface) getActivity();
        } catch (ClassCastException CC) {
            throw new ClassCastException("Error in retrieving data. Please try again");
        }

    }
}
