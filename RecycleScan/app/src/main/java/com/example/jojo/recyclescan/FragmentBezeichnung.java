package com.example.jojo.recyclescan;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class FragmentBezeichnung extends Fragment {

    View view;
    TextView ean;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        ProgressStepsActivity myActivity = (ProgressStepsActivity) getActivity();

        view = inflater.inflate(R.layout.fragment_bezeichnung, container, false);
        ean = view.findViewById(R.id.textViewEAN2);
        ean.setText(myActivity.getEAN());

        //TODO: get editTextTeüxt and tranfer data to Activity.
        // In Activity alle Daten sammeln und dann abschließen

        return view;
    }
}
