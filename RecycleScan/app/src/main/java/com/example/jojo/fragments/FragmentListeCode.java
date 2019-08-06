package com.example.jojo.fragments;

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

import com.example.jojo.recyclescan.ProgressStepsActivity;
import com.example.jojo.recyclescan.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Fragment, das die Liste der Recycling Codes anzeigt.
 */
public class FragmentListeCode extends Fragment {

    View view;
    ListView list;
    ArrayAdapter<String> adapter;
    FloatingActionButton weiter;

    List<String> gelbCode = new ArrayList<>();
    List<String> glasCode = new ArrayList<>();
    List<String> papCode = new ArrayList<>();

    ArrayList<String> listCodes = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_liste_code, container, false);

        list = view.findViewById(R.id.listCodes);
        gelbCode = Arrays.asList(getResources().getStringArray(R.array.arrayCodesGelb));
        glasCode = Arrays.asList(getResources().getStringArray(R.array.arrayCodesGlas));
        papCode = Arrays.asList(getResources().getStringArray(R.array.arrayCodesPap));

        listCodes.addAll(gelbCode);
        listCodes.addAll(glasCode);
        listCodes.addAll(papCode);

        //Alphabetisch sortiert
        Collections.sort(listCodes);

        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_single_choice, listCodes);

        list.setAdapter(adapter);
        list.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

        weiter = view.findViewById(R.id.weiterCodeListe);
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
