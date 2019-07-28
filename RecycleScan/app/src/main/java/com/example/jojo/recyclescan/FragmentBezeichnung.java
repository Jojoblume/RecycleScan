package com.example.jojo.recyclescan;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class FragmentBezeichnung extends Fragment {

    View view;
    TextView ean;
    FloatingActionButton weiter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);


        view = inflater.inflate(R.layout.fragment_bezeichnung, container, false);
        ean = view.findViewById(R.id.textViewEAN2);
        ean.setText(getArguments().getString("EAN"));


        weiter = view.findViewById(R.id.weiter);
        weiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**NEED, wenn nicht stepper Fragment, sondern "unterFragment"
                 * getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, fragmentList, "findFragmentList")
                        .commit();**/
                ((ProgressStepsActivity)getActivity()).getFragment();

                ((ProgressStepsActivity)getActivity()).goStep();
            }
        });


        //TODO: get editTextTeüxt and tranfer data to Activity.
        // In Activity alle Daten sammeln und dann abschließen

        return view;
    }
}
