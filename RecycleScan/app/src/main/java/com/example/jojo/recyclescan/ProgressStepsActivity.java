package com.example.jojo.recyclescan;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.shuhart.stepview.StepView;

import java.util.ArrayList;

public class ProgressStepsActivity extends AppCompatActivity {
    StepView stepView;
    ArrayList<String> stepNames = new ArrayList<>();

    Fragment selectedFragment;
    Fragment fragment2;
    FloatingActionButton weiter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_steps);

        stepNames.add("Bezeichnung");
        stepNames.add("Bestandteile");
        stepNames.add("Bestätigen");
        stepView = findViewById(R.id.stepView);
        stepView.setSteps(stepNames);

        //TODO: Button in Activity oder lieber in Fragment? Frage, wie in Fragment stepView.go() ausgeführt werden kann...
        weiter = findViewById(R.id.weiter);


        selectedFragment = new FragmentBezeichnung();
        fragment2 = new FragmentSingleChoiceList();

        getFragment();

        weiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Nicht bei jedem Fragment...
                stepView.go(stepView.getCurrentStep() + 1, true);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment2).commit();

            }
        });
    }


    public String getEAN() {
        return getIntent().getExtras().getString("EAN");
    }

    public void getFragment() {
        int step = stepView.getCurrentStep();
        switch (step) {
            case 0:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, selectedFragment).commit();
                break;
            case 1:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment2).commit();
                break;

            default:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, selectedFragment).commit();
                break;
        }
    }


}

