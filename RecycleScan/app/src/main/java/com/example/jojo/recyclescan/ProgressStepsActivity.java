package com.example.jojo.recyclescan;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.shuhart.stepview.StepView;

import java.util.ArrayList;

public class ProgressStepsActivity extends AppCompatActivity {
    StepView stepView;
    ArrayList<String> stepNames = new ArrayList<>();

    String ean;

    Fragment fragmentBezeichnung;
    Fragment fragmentList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_steps);

        fragmentBezeichnung = new FragmentBezeichnung();
        fragmentList = new FragmentSingleChoiceList();


        ean = getIntent().getExtras().getString("EAN");
        Bundle bundle = new Bundle();
        bundle.putString("EAN", ean);
        fragmentBezeichnung.setArguments(bundle);

        stepNames.add("Bezeichnung");
        stepNames.add("Bestandteile");
        stepNames.add("Bestätigen");
        stepView = findViewById(R.id.stepView);
        stepView.setSteps(stepNames);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragmentBezeichnung).commit();

        //TODO: Button in Activity oder lieber in Fragment? - JA - Frage, wie in Fragment stepView.go() ausgeführt werden kann...



    }

    public void goStep(){
        stepView.go(stepView.getCurrentStep() + 1, true);
    }

    public void getFragment() {
        int step = stepView.getCurrentStep() +1;
        switch (step) {
            case 1:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragmentList).commit();
                break;

            default:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragmentBezeichnung).commit();
                break;
        }
    }








}

