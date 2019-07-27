package com.example.jojo.recyclescan;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Arrays;
import java.util.List;

public class FragmentSingleChoiceList extends Fragment {

    View view;
    ListView list1;
    ArrayAdapter<String> adapter;
    List<String> arrayKurz1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_singlechoicelist, container, false);

        list1 = view.findViewById(R.id.listViewBes1);
        arrayKurz1 = Arrays.asList(getResources().getStringArray(R.array.arrayKurz1));
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_single_choice, arrayKurz1 );
        list1.setAdapter(adapter);
        list1.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

        return view;
    }
}
