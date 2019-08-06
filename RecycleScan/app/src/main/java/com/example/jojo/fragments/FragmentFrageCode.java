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
 * Fragment, das angezeigt wird, wenn zuvor "Weiß nicht" ausgewählt wurde.
 * Es wird gefragt, ob sich ein Recycling Code an der Verpackung befindet.
 */
public class FragmentFrageCode extends Fragment {

    View view;

    Button ja;
    Button nein;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_frage_code, container, false);

        ja = view.findViewById(R.id.buttonJa);
        nein = view.findViewById(R.id.buttonNein);

        ja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ProgressStepsActivity)getActivity()).setCurrentState("CodeJa");
                ((ProgressStepsActivity)getActivity()).getFragment();
            }
        });

        nein.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ProgressStepsActivity)getActivity()).setCurrentState("CodeNein");
                ((ProgressStepsActivity)getActivity()).getFragment();

            }
        });

        return view;
    }
}
