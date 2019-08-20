package com.example.jojo.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.jojo.recyclescan.ProgressStepsActivity;
import com.example.jojo.recyclescan.R;

import java.util.ArrayList;


/**
 * Letztes Fragment, das die Übersicht der ausgewählten Bestandteile anzeigt.
 */
public class FragmentBestatigen extends Fragment {

    View view;
    ListView übersicht;
    FloatingActionButton btnBestatigen;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_bestatigen, container, false);

        ArrayList<String> bestandteile = ((ProgressStepsActivity)getActivity()).getBestandteile();
        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, bestandteile);
        übersicht = view.findViewById(R.id.listViewÜbersicht);
        übersicht.setAdapter(adapter);

        btnBestatigen = view.findViewById(R.id.btnBestatigen);
        btnBestatigen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (((ProgressStepsActivity)getActivity()).isNewUser() == false){
                    String userID = ((ProgressStepsActivity)getActivity()).getUserID();
                    ((ProgressStepsActivity)getActivity()).saveProductOnFirebase(userID);
                } else {
                    ((ProgressStepsActivity)getActivity()).dialogNewUser();
                }


            }
        });

        return view;
    }
}
