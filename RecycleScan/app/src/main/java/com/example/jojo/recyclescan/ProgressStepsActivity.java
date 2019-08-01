package com.example.jojo.recyclescan;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shuhart.stepview.StepView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProgressStepsActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;

    StepView stepView;
    ArrayList<String> stepNames = new ArrayList<>();

    String ean;
    String bez;
    ArrayList<String> bestandteile = new ArrayList<>();
    String currentState;

    List<String> gelb = new ArrayList<>();
    List<String> pap = new ArrayList<>();

    boolean verschlussGefragt = false;

    Fragment fragmentBezeichnung;
    Fragment fragmentList;
    Fragment fragmentBestätigen;
    EditText editTextBenutzername;
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

        dialogStart();
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //Just for Test!
        //mAuth.signOut();

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



    public void saveOnFirebase(String uid) {
        Map<String, Object> product = new HashMap<>();
        product.put("Bezeichnung", bez);
        product.put("UserID", uid);
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
                dialogBeenden();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        dialogBeenden();
    }

    public void dialogBeenden(){

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

    public void dialogStart(){

        //CustomDialog
        //https://www.codingdemos.com/android-custom-dialog-animation/
        android.app.AlertDialog.Builder dialogbuilder = new android.app.AlertDialog.Builder(ProgressStepsActivity.this);
        View dialogView = getLayoutInflater().inflate(R.layout.customdialog_barcodeunknown, null);
        dialogbuilder.setView(dialogView);
        dialogbuilder.setCancelable(false);

        dialogbuilder.setPositiveButton(
                "OK, MACH ICH!",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        dialogbuilder.setNegativeButton(
                "ABBRECHEN",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                    }
                });
        android.app.AlertDialog alertDialog = dialogbuilder.create();
        alertDialog.show();

        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorGrey));

    }

    public void dialogNewUser(){

        //CustomDialog
        //https://www.codingdemos.com/android-custom-dialog-animation/
        android.app.AlertDialog.Builder dialogbuilder = new android.app.AlertDialog.Builder(ProgressStepsActivity.this);
        View dialogView = getLayoutInflater().inflate(R.layout.customdialog_newuser, null);
        dialogbuilder.setView(dialogView);
        dialogbuilder.setCancelable(true);
        dialogbuilder.setTitle("Danke für deinen 1. Eintrag!\nBenutzername erforderlich.");
        dialogbuilder.setMessage("\nSomit erstellst du einen anonymen Account und hast viele weitere Möglichkeiten in der App!");

        editTextBenutzername = dialogView.findViewById(R.id.editTextBenutzername);

        dialogbuilder.setPositiveButton(
                "VERÖFFENTLICHEN",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String benutzername = editTextBenutzername.getText().toString();
                        if (!benutzername.isEmpty()){
                            logIn(benutzername); //+saveUserOnFirebase
                        }
                        else{
                            Toast.makeText(ProgressStepsActivity.this,"Denk dir einen Namen aus",Toast.LENGTH_LONG).show();
                        }

                    }
                });

        android.app.AlertDialog alertDialog = dialogbuilder.create();
        alertDialog.show();

    }


    public boolean isNewUser(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if ( currentUser == null){
            return true;
        }else{
            return false;
        }
    }

    public String getUserID() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String uid = currentUser.getUid();
        return uid;
    }
    public void logIn(final String name){
        if(isNewUser() == true){
            mAuth.signInAnonymously().addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    String userID = mAuth.getCurrentUser().getUid();
                    saveUserOnFirebase(name, userID);
                }
            });
        }
    }

    public void saveUserOnFirebase(final String benutzername, final String userID){
        Map<String, Object> user = new HashMap<>();
        user.put("UserID", userID);
        user.put("Benutzername", benutzername);
        db.collection("User").document(userID)
                .set(user).addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                saveOnFirebase(userID);
            }
        });
    }



}

