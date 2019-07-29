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
import java.util.List;
import java.util.Map;

public class ProgressStepsActivity extends AppCompatActivity {
    StepView stepView;
    ArrayList<String> stepNames = new ArrayList<>();

    String ean;
    String bez;
    ArrayList<String> bestandteile = new ArrayList<>();
    String currentBestandteil;

    List<String> gelb = new ArrayList<>();

    boolean verschlussGefragt = false;

    Fragment fragmentBezeichnung;
    Fragment fragmentList;
    Fragment fragmentBestätigen;

    //"Unter" Fragmente
    Fragment fragmentFrageVerschluss;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_steps);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fragmentBezeichnung = new FragmentBezeichnung();
        fragmentList = new FragmentSingleChoiceList();
        fragmentBestätigen = new FragmentBestatigen();

        fragmentFrageVerschluss = new FragmentFrageVerschluss();

        gelb = Arrays.asList(getResources().getStringArray(R.array.arrayBestandteileGelb));


        ean = getIntent().getExtras().getString("EAN");
        Bundle bundle = new Bundle();
        bundle.putString("EAN", ean);
        fragmentBezeichnung.setArguments(bundle);

        stepNames.add("Bezeichnung");
        stepNames.add("Bestandteile");
        stepNames.add("Bestätigen");
        stepView = findViewById(R.id.stepView);
        stepView.setSteps(stepNames);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragmentBezeichnung, "FRAGMENTBEZ").commit();


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
        if(fragmentList != null && fragmentList.isVisible())
        {
            if(gelb.contains(currentBestandteil)){
                if (verschlussGefragt == false){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragmentFrageVerschluss).commit();
                    verschlussGefragt = true;
                }
                else{
                    //starte Frage Fragment
                    //mit folgender Frage: "Noch mehr?"
                    //Bei Nein: Übericht + ((ProgressStepsActivity)getActivity()).goStep();
                    //Bei Ja: fragmentList und der Loop startet von neu...
                }

            }
            //else if Pappe {}
            //else if Glas{}
            //else if "Weiß nicht"{}
            //unten else dann weg?
            else
            {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragmentBestätigen).commit();
                goStep();
            }
         }
        if  (fragmentFrageVerschluss != null && fragmentFrageVerschluss.isVisible()){
            //Später gibts noch mehr?
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragmentBestätigen).commit();
            goStep();
        }

        /**int step = stepView.getCurrentStep() +1;
        switch (step) {
            case 1:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragmentList, "FRAGMENTLIST").commit();
                break;
            case 2:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragmentBestätigen, "FRAGMENTBEST").commit();
                break;

            default:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragmentBezeichnung, "FRAGMENTBEZ").commit();
                break;
        }**/
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

    public ArrayList<String> getBestandteile(){
        return bestandteile;
    }

    public void setCurrentBestandteil(String bestandteil) {
        currentBestandteil = bestandteil;
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

