package com.example.jojo.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.jojo.recyclescan.ProgressStepsActivity;
import com.example.jojo.recyclescan.R;

/**
 * Fragment, das nachfragt, ob es noch mehr Bestandteile an der Verpackung vorhanden sind.
 */
public class FragmentFrageMehr extends Fragment {

    View view;

    Button ja;
    Button nein;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_frage_mehr, container, false);

        ja = view.findViewById(R.id.buttonJa);
        nein = view.findViewById(R.id.buttonNein);

        ja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ProgressStepsActivity)getActivity()).setCurrentState("MehrJa");
                ((ProgressStepsActivity)getActivity()).getFragment();
            }
        });

        nein.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ProgressStepsActivity)getActivity()).setCurrentState("ENDE");
                ((ProgressStepsActivity)getActivity()).getFragment();

            }
        });

        return view;
    }
}
