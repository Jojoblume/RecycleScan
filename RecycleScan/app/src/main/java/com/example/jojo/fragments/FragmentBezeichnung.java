package com.example.jojo.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.jojo.recyclescan.ProgressStepsActivity;
import com.example.jojo.recyclescan.R;

/**
 * Erstes Fragment, dass nach der Bezeichnung des Produkts fragt.
 */
public class FragmentBezeichnung extends Fragment {

    View view;
    TextView ean;
    FloatingActionButton weiter;
    EditText editBezeichnung;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        view = inflater.inflate(R.layout.fragment_bezeichnung, container, false);
        ean = view.findViewById(R.id.textViewEAN2);
        ean.setText(getArguments().getString("EAN"));

        editBezeichnung = view.findViewById(R.id.editTextBez);
        String bezeichnung = ((ProgressStepsActivity)getActivity()).getBezeichnung();

        if(! bezeichnung.equals("") ){
            editBezeichnung.setText(bezeichnung);
            editBezeichnung.setSelection(editBezeichnung.getText().length());
            //Tastatur wird nur angezeigt, wenn vorher nicht das Dialogfenster erscheint. Animation war dann ruckelig.
            //https://stackoverflow.com/questions/10508363/show-keyboard-for-edittext-when-fragment-starts
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }

        weiter = view.findViewById(R.id.weiter);
        weiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String bez = editBezeichnung.getText().toString();
                //Abfangen, ob EditText leer gelassen wird.
                if(bez.equals("") || bez.equals(" "))
                {
                    //https://stackoverflow.com/questions/6290531/check-if-edittext-is-empty {
                    //setError eleganter, aber Design lässt sich nicht einfach ändern.
                    //editBezeichnung.setError("Bitte trage den Namen des Produkts ein");

                    //https://stackoverflow.com/questions/2115758/how-do-i-display-an-alert-dialog-on-android
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                    builder1.setTitle("Leeres Feld");
                    builder1.setMessage("Bitte trage den Namen des Produkts ein. Dieser soll anderen Benutzern helfen, zu erkennen, dass sie das richtge Produkt gescannt haben.");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert = builder1.create();
                    alert.show();
                }
                else{

                    ((ProgressStepsActivity)getActivity()).getBezeichnung(bez);
                    ((ProgressStepsActivity)getActivity()).getFragment();

                }

            }
        });

        return view;
    }
}
