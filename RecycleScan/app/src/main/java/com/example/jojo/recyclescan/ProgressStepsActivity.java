package com.example.jojo.recyclescan;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shuhart.stepview.StepView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ProgressStepsActivity extends AppCompatActivity {
    StepView stepView;
    ArrayList<String> stepNames = new ArrayList<>();

    String ean;
    String bez;
    ArrayList<String> bestandteile = new ArrayList<>();

    Fragment fragmentBezeichnung;
    Fragment fragmentList;
    Fragment fragmentBestätigen;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_steps);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fragmentBezeichnung = new FragmentBezeichnung();
        fragmentList = new FragmentSingleChoiceList();
        fragmentBestätigen = new FragmentBestatigen();


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
        //Hier der ganze Code für die Verzweigungen???
        int step = stepView.getCurrentStep() +1;
        switch (step) {
            case 1:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragmentList).commit();
                break;
            case 2:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragmentBestätigen).commit();
                break;

            default:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragmentBezeichnung).commit();
                break;
        }
    }

    public void getBezeichnung(String data){
        bez = data;
    }

    public void addBestandteil(String bestandteil){
        bestandteile.add(bestandteil);

    }

    public ArrayList<String> getBestandteile(){
        return bestandteile;
    }


    public void saveOnFirebase() {
        Map<String, Object> product = new HashMap<>();
        product.put("Bezeichnung", bez);
        product.put("Bestandteile", bestandteile);

        db.collection("Produkte").document(ean)
                .set(product)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent ergebnisIntent = new Intent(getApplicationContext(), ErgebnisActivity.class);
                        ergebnisIntent.putExtra("EAN", ean);
                        ergebnisIntent.putExtra("BEZ", bez);
                        ergebnisIntent.putExtra("TEILE", bestandteile);
                        startActivity(ergebnisIntent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                dialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        dialog();
    }

    public void dialog(){

        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Beenden?");
        builder1.setMessage("Sicher, dass du den Vorgang abbrechen und auf den Start Bildschirm zurückkehren möchtest?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Ja, Sicher",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                    }
                });
        builder1.setNegativeButton(
                "Abbrechen",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder1.create();
        alert.show();

    }
}

