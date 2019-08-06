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
import android.widget.TextView;
import android.widget.Toast;

import com.example.jojo.recyclescan.ProgressStepsActivity;
import com.example.jojo.recyclescan.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Die Hauptliste der Verpackungsbestandteile.
 * Zuerst werden nur die Häufigsten Bestandteile angezeigt;
 * bei Klick auf "Mehr Anzeigen" werden die restlichen Bestandteile aufgelistet.
 */
public class FragmentSingleChoiceList extends Fragment {

    View view;
    ListView list1;
    ArrayAdapter<String> adapter;
    List<String> arrayKurz1;
    List<String> arrayKurz2;
    TextView mehrAnzeigen;
    TextView überschrift;

    FloatingActionButton weiterBestandteil;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_singlechoicelist, container, false);

        list1 = view.findViewById(R.id.listViewBes1);
        arrayKurz1 = Arrays.asList(getResources().getStringArray(R.array.arrayKurz1));
        arrayKurz2 = Arrays.asList(getResources().getStringArray(R.array.arrayKurz2));

        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_single_choice, arrayKurz1 );
        list1.setAdapter(adapter);
        list1.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

        //Ermittlung, der wievielste Bestandteil ausgewählt wird.
        überschrift = view.findViewById(R.id.textViewÜberschrif);
        int count = ((ProgressStepsActivity)getActivity()).getBestandteileCount() +1;
        überschrift.setText(count+". Bestandteil auswählen");

        mehrAnzeigen = view.findViewById(R.id.textViewMehrAnzeigen);
        mehrAnzeigen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mehrAnzeigen.setVisibility(View.GONE);
                updateData();
            }
        });

        weiterBestandteil = view.findViewById(R.id.weiterBestandteilListe);
        weiterBestandteil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = list1.getCheckedItemPosition();
                //Fehler abfangen, wenn kein Item/Bestandteil ausgewählt ist.
                if (list1.isItemChecked(pos)){
                    String bestandteil = list1.getItemAtPosition(pos).toString();
                    if (Arrays.asList(getResources().getStringArray(R.array.arrayBestandteileGelb)).contains(bestandteil))
                    {
                        ((ProgressStepsActivity)getActivity()).addBestandteil(bestandteil);
                    }
                    //Bei Glas, Pappe und "Weiß nicht" soll Bestandteil nicht dem Array hinzugefügt werden.
                    //currentState ist zur Ermittlung, was ausgewählt wurde.
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

    /**
     * ArrayList wird erneuert, wenn auf "Mehr Anzeigen" geklickt wird.
     */
    private void updateData() {
        List<String> arrayErweitert = new ArrayList<>();
        arrayErweitert.addAll(arrayKurz1);
        arrayErweitert.addAll(arrayKurz2);
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_single_choice, arrayErweitert);
        list1.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }
}
