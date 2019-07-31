package com.example.jojo.recyclescan;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FragmentListeGlas extends Fragment {

    View view;
    ListView list;
    ArrayAdapter<String> adapter;
    FloatingActionButton weiter;

    List<String> listGlas;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_liste_glas, container, false);

        list = view.findViewById(R.id.listGlasfarben);

        listGlas = Arrays.asList(getResources().getStringArray(R.array.arrayBestandteileGlas));
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_single_choice, listGlas);

        list.setAdapter(adapter);
        list.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

        weiter = view.findViewById(R.id.weiterGlasListe);
        weiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = list.getCheckedItemPosition();
                //Fehler abfangen, wenn kein Item/Bestandteil ausgewählt ist.
                if (list.isItemChecked(pos)){
                    String bestandteil = list.getItemAtPosition(pos).toString();

                    ((ProgressStepsActivity)getActivity()).addBestandteil(bestandteil);
                    ((ProgressStepsActivity)getActivity()).setCurrentState(bestandteil);
                    ((ProgressStepsActivity)getActivity()).getFragment();
                }
                else{
                    Toast.makeText(getActivity(), "Du weißt es nicht?", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}
