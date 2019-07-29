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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FragmentSingleChoiceList extends Fragment {

    View view;
    ListView list1;
    ArrayAdapter<String> adapter;
    List<String> arrayKurz1;
    List<String> arrayKurz2;
    TextView mehrAnzeigen;

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
                String bestandteil = list1.getItemAtPosition(pos).toString();
                ((ProgressStepsActivity)getActivity()).addBestandteil(bestandteil);

                ((ProgressStepsActivity)getActivity()).getFragment();
                ((ProgressStepsActivity)getActivity()).goStep();
            }
        });

        return view;
    }

    private void updateData() {

        List<String> arrayErweitert = new ArrayList<>();
        arrayErweitert.addAll(arrayKurz1);
        arrayErweitert.addAll(arrayKurz2);
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_single_choice, arrayErweitert);
        list1.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }
}
