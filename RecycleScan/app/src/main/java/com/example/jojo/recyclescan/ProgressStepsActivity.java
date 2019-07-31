package com.example.jojo.recyclescan;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shuhart.stepview.StepView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProgressStepsActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    StepView stepView;
    ArrayList<String> stepNames = new ArrayList<>();

    String ean;
    String bez;
    ArrayList<String> bestandteile = new ArrayList<>();
    String currentState;

    List<String> gelb = new ArrayList<>();
    List<String> pap = new ArrayList<>();
    List<String> glas = new ArrayList<>();

    boolean verschlussGefragt = false;

    Fragment fragmentBezeichnung;
    Fragment fragmentList;
    Fragment fragmentBestätigen;
    //"Unter" Fragmente
    Fragment fragmentFrageVerschluss;
    Fragment fragmentFrageSichtfenster;
    Fragment fragmentFrageCode;
    Fragment fragmentFrageMehr;

    Fragment fragmentListeCode;
    Fragment fragmentListeGlas;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_steps);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fragmentBezeichnung = new FragmentBezeichnung();
        fragmentList = new FragmentSingleChoiceList();
        fragmentBestätigen = new FragmentBestatigen();

        fragmentFrageVerschluss = new FragmentFrageVerschluss();
        fragmentFrageSichtfenster = new FragmentFrageSichtfenster();
        fragmentFrageCode = new FragmentFrageCode();
        fragmentFrageMehr = new FragmentFrageMehr();

        fragmentListeCode = new FragmentListeCode();
        fragmentListeGlas = new FragmentListeGlas();

        gelb = Arrays.asList(getResources().getStringArray(R.array.arrayBestandteileGelb));
        pap = Arrays.asList(getResources().getStringArray(R.array.arrayBestandteilePap));

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


    }

    /**
     * Hier Entscheidung, welches Fragment als nächstes angezeigt werden muss.
     * Dabei nicht nur StepView Schritte entscheidend.
     * Abhängig von Benutzerentscheidung.
     */
    public void getFragment() {

        //https://stackoverflow.com/questions/9294603/how-do-i-get-the-currently-displayed-fragment
        //Fragment myFragment = getSupportFragmentManager().findFragmentByTag("FRAGMENTBEZ");

        if (fragmentBezeichnung != null && fragmentBezeichnung.isVisible()) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragmentList).commit();
            goStep();
        }
        //1.EBENE
        else if(fragmentList != null && fragmentList.isVisible())
        {
            //GELB?
            if(gelb.contains(currentState)){
                if (verschlussGefragt == false){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragmentFrageVerschluss).commit();
                    verschlussGefragt = true;
                }
                else{
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragmentFrageMehr).commit();
                }

            }
            //PAPPE?
            else if (pap.contains(currentState))
            {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragmentFrageSichtfenster).commit();
            }
            //GLAS?
            else if (currentState.equals("Glas")){
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragmentListeGlas).commit();
                //Nachdem Farbe klar ist: Nach Verschluss fragen!
            }
            //WEISS NICHT?
            else if (currentState.equals("Ich weiß nicht"))
            {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragmentFrageCode).commit();
            }

         }
        else if ( fragmentListeGlas != null && fragmentListeGlas.isVisible()){
            if (verschlussGefragt == false){
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragmentFrageVerschluss).commit();
                verschlussGefragt = true;
            }
            else{
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragmentFrageMehr).commit();
            }

        }
        //2.EBENE
        else if (fragmentFrageCode != null && fragmentFrageCode.isVisible())
        {
            if(currentState.equals("CodeJa")){
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragmentListeCode).commit();
            }
            else
            {
                //Leeres Feld mit Fragezeichen oder so darstellen?
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragmentFrageMehr).commit();
            }
        }

        //MEHR?
        //Wenn Bei Frage Mehr? "NEIN" ausgewählt wird:
        else if (currentState.equals("ENDE"))
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragmentBestätigen).commit();
            goStep();
        }
        else if(currentState.equals("MehrJa")){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragmentList).commit();
        }
        //Im Zweifel immer fragen, ob es noch mehr Bestandteile gibt
        else
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragmentFrageMehr).commit();
        }

    }

    public void goStep(){
        stepView.go(stepView.getCurrentStep() + 1, true);
    }

    public void getBezeichnung(String data){
        bez = data;
    }

    public void addBestandteil(String bestandteil){
        bestandteile.add(bestandteil);

    }

    public Integer getBestandteileCount(){
       return bestandteile.size();
    }

    public ArrayList<String> getBestandteile(){
        return bestandteile;
    }

    public void setCurrentState(String bestandteil) {
        currentState = bestandteil;


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

