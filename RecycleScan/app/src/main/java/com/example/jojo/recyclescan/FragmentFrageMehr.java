package com.example.jojo.recyclescan;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
                /**getActivity().getSupportFragmentManager().beginTransaction()
                 .replace(R.id.fragmentContainer, fragmentÜbersicht)
                 .commit();**/
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
