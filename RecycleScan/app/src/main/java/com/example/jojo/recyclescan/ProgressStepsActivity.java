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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.jojo.fragments.FragmentBestatigen;
import com.example.jojo.fragments.FragmentBezeichnung;
import com.example.jojo.fragments.FragmentFrageCode;
import com.example.jojo.fragments.FragmentFrageMehr;
import com.example.jojo.fragments.FragmentFrageSichtfenster;
import com.example.jojo.fragments.FragmentFrageVerschluss;
import com.example.jojo.fragments.FragmentListeCode;
import com.example.jojo.fragments.FragmentListeGlas;
import com.example.jojo.fragments.FragmentSingleChoiceList;
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

    StepView stepView;
    ProgressBar progress;
    ArrayList<String> stepNames = new ArrayList<>();

    String ean;
    String bez;
    ArrayList<String> bestandteile = new ArrayList<>();
    ArrayList<String> eingetrageneProdukte = new ArrayList<>();
    String currentState;

    String bezeichnung = "";
    String creatorID = "";

    //Firebase
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;

    List<String> gelb = new ArrayList<>();
    List<String> pap = new ArrayList<>();

    boolean verschlussGefragt = false;

    //FRAGMENTE
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

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //Listen erforderlich, um zu erkennen, welcher Bestandteil ausgewählt wurde.
        gelb = Arrays.asList(getResources().getStringArray(R.array.arrayBestandteileGelb));
        pap = Arrays.asList(getResources().getStringArray(R.array.arrayBestandteilePap));

        //Je nach vorheriger Activity wird der DialogStart angezeigt oder nicht.
        String previousActivity = getIntent().getExtras().getString("INTENT");
        if (previousActivity.equals("MainActivity")){
            dialogStart();
        }
        //Hier handelt es sich um eine Korrektur. Bezeichnung wird übernommen und Creator ID ermittelt, um Punkte abziehen zu können.
        else if (previousActivity.equals("ErgebnisActivity")){
            bezeichnung = getIntent().getExtras().getString("BEZ");
            creatorID = getIntent().getExtras().getString("CREATOR");
        }

        //FRAGMENTE
        fragmentBezeichnung = new FragmentBezeichnung();
        fragmentList = new FragmentSingleChoiceList();
        fragmentBestätigen = new FragmentBestatigen();

        fragmentFrageVerschluss = new FragmentFrageVerschluss();
        fragmentFrageSichtfenster = new FragmentFrageSichtfenster();
        fragmentFrageCode = new FragmentFrageCode();
        fragmentFrageMehr = new FragmentFrageMehr();

        fragmentListeCode = new FragmentListeCode();
        fragmentListeGlas = new FragmentListeGlas();

        //Hier Datenübertragung mit Bundle.
        //Später waren Methoden einfacher zu handhaben.
        ean = getIntent().getExtras().getString("EAN");
        Bundle bundle = new Bundle();
        bundle.putString("EAN", ean);
        fragmentBezeichnung.setArguments(bundle);

        //Anzeige des Fortschritts
        stepNames.add("Bezeichnung");
        stepNames.add("Bestandteile");
        stepNames.add("Bestätigen");
        stepView = findViewById(R.id.stepView);
        stepView.setSteps(stepNames);

        //Lade-prozess
        progress = findViewById(R.id.progressBar);
        progress.setVisibility(View.INVISIBLE);

        //Erstes Fragment
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

        //Nach dem 1. Fragment folgt das Fragment mit der Liste der Bestandteile.
        if (fragmentBezeichnung != null && fragmentBezeichnung.isVisible()) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragmentList).commit();
            goStep();
        }

        //1.EBENE
        else if (fragmentList != null && fragmentList.isVisible()) {
            //Ermittlung, was ausgewählt wurde und je nach dem weiteres Fragment.
            //GELB?
            if (gelb.contains(currentState)) {
                if (verschlussGefragt == false) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragmentFrageVerschluss).commit();
                    verschlussGefragt = true;
                } else {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragmentFrageMehr).commit();
                }

            }
            //PAPPE?
            else if (pap.contains(currentState)) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragmentFrageSichtfenster).commit();
            }
            //GLAS?
            else if (currentState.equals("Glas")) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragmentListeGlas).commit();
                //Nachdem Farbe klar ist: Nach Verschluss fragen!
            }
            //WEISS NICHT?
            else if (currentState.equals("Ich weiß nicht")) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragmentFrageCode).commit();
            }

        }
        //2.EBENE

        //Nachdem die Glasfarbe ermittelt wurde, wird nach Verschluss oder Mehr gefragt.
        else if (fragmentListeGlas != null && fragmentListeGlas.isVisible()) {
            if (verschlussGefragt == false) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragmentFrageVerschluss).commit();
                verschlussGefragt = true;
            } else {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragmentFrageMehr).commit();
            }

        }
        //Je nach Antwort, werden RecyclingCodes angezeigt oder nach weiteren Bestandteilen gefragt.
        else if (fragmentFrageCode != null && fragmentFrageCode.isVisible()) {
            if (currentState.equals("CodeJa")) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragmentListeCode).commit();
            } else {
                //Leeres Feld mit Fragezeichen darstellen?
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragmentFrageMehr).commit();
            }
        }

        //MEHR?
        //Wenn Bei Frage Mehr? "NEIN" ausgewählt wird:
        else if (currentState.equals("ENDE")) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragmentBestätigen).commit();
            goStep();
        }
        //Wenn bei Frage Mehr? "JA" ausgewählt wird:
        else if (currentState.equals("MehrJa")) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragmentList).commit();
        }
        //Im Zweifel immer fragen, ob es noch mehr Bestandteile gibt
        else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragmentFrageMehr).commit();
        }

    }

    public void goStep()
    {
        stepView.go(stepView.getCurrentStep() + 1, true);
    }

    public void getBezeichnung(String data)
    {
        bez = data;
    }

    public void addBestandteil(String bestandteil)
    {
        bestandteile.add(bestandteil);

    }

    public Integer getBestandteileCount()
    {
        return bestandteile.size();
    }

    public ArrayList<String> getBestandteile()
    {
        return bestandteile;
    }

    public void setCurrentState(String bestandteil)
    {
        currentState = bestandteil;
    }

    public String getBezeichnung(){
        return bezeichnung;
    }

    public String getTitel(long punkte){
        if(punkte < 500){
            return "Müllmonster";
        }
        else if (punkte >= 500 && punkte < 1500){
            return "Schrottsammler";
        }
        else if (punkte >= 1500 && punkte <2500){
            return "Sprössling";
        }
        else if (punkte >=2500 && punkte <3500){
            return "Recycler";
        }
        else if (punkte >= 3500 && punkte <4500){
            return "Klimaheld";
        }
        else if(punkte >= 4500){
            return "Umweltaktivist";
        }
        else return "Mülmonster";
    }

    public boolean isNewUser() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            return true;
        } else {
            return false;
        }
    }

    public String getUserID() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String uid = currentUser.getUid();
        return uid;
    }

    /**
     * START DIALOGFENSTER
     */

    /**
     * Dialog, der beim Start angezeigt wird. Designmuster Bundling mit Waschbär Bild.
     */
    public void dialogStart() {

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
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                });
        android.app.AlertDialog alertDialog = dialogbuilder.create();
        alertDialog.show();

        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorGrey));

    }

    /**
     * Dialog, der den Benutzer fragt, ob er den Vorgang wirklich Beenden möchte.
     */
    public void dialogBeenden() {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Beenden?");
        builder1.setMessage("Sicher, dass du den Vorgang abbrechen und auf den Start Bildschirm zurückkehren möchtest?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Ja, Sicher",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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

    /**
     * Dialog, der angezeigt wird, wenn der Benutzer noch nicht eingeloggt ist -> also ein neuer Benutzer ist.
     */
    public void dialogNewUser() {

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
                        if (!benutzername.isEmpty()) {
                            logIn(benutzername); //+saveUserOnFirebase //+ saveProductOnFirebase //+saveProduct To User
                        } else {
                            Toast.makeText(ProgressStepsActivity.this, "Denk dir einen Namen aus", Toast.LENGTH_LONG).show();
                        }

                    }
                });

        android.app.AlertDialog alertDialog = dialogbuilder.create();
        alertDialog.show();

    }

    /**
     * START FIREBASE
     */

    /**
     * 1. Methode, die aufgerufen wird, wenn ein neuer Benutzer das 1. Produkt Veröffentlicht.
     * @param name
     */
    public void logIn(final String name) {
        progress.setVisibility(View.VISIBLE);
        if (isNewUser()== true) {
            mAuth.signInAnonymously().addOnSuccessListener(ProgressStepsActivity.this, new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    String userID = authResult.getUser().getUid();
                    saveUserOnFirebase(name, userID);
                }

             }).addOnFailureListener(ProgressStepsActivity.this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progress.setVisibility(View.GONE);
                }
            });
        }
    }

    /**
     * 2. Methode, die bei einem neuen Benutzer ausgeührt wird.
     * Eine neue Sammlung "User" wurde erstellt und die Daten des Benutzers werden dieser Sammlung hinzugefügt.
     * Somit später Verbindung zwischen Benutzern und eingetragenen Produkten.
     * @param benutzername
     * @param userID
     */
    public void saveUserOnFirebase(final String benutzername, final String userID) {

        Map<String, Object> user = new HashMap<>();
        user.put("Benutzername", benutzername);
        user.put("Punkte", 0);
        user.put("Titel", "Müllmonster");
        user.put("Produkte", eingetrageneProdukte);
        db.collection("User").document(userID)
                .set(user).addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                saveProductOnFirebase(userID);
            }
        }).addOnFailureListener(ProgressStepsActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progress.setVisibility(View.GONE);
            }
        });
    }

    /**
     * Speichert Produkt auf Firebase.
     * Wenn neuer Nutzer: 3. Methode, die aufgerufen wird.
     * Wenn eingeloggter Nutzer: 1. Methode, die aufgerufen wird.
     * @param uid
     */
    public void saveProductOnFirebase(final String uid) {
        progress.setVisibility(View.VISIBLE);
        Map<String, Object> product = new HashMap<>();
        product.put("Bezeichnung", bez);
        product.put("UserID", uid);
        product.put("Bestandteile", bestandteile);

        db.collection("Produkte").document(ean)
                .set(product)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        saveProductToUser(uid);
                    }
                }).addOnFailureListener(ProgressStepsActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progress.setVisibility(View.GONE);
            }
        });
    }

    /**
     * Speichert die EAN des eingetragenen Produkts zugehörig zum Benutzer.
     * Außerdem erhählt hier der Benutzer 50 Punkte für den Eintrag und erhält ggf. einen neuen Titel.
     * @param uid
     */
    public void saveProductToUser(final String uid) {
        final DocumentReference docRef = db.collection("User").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    eingetrageneProdukte = (ArrayList<String>) document.get("Produkte");
                    //Bei Korrektur des eigenen Eintrags, soll Produkt nicht 2x vorkommen.
                    if(!eingetrageneProdukte.contains(ean)){
                        eingetrageneProdukte.add(ean);
                    }
                    long punkte = (long) document.get("Punkte");
                    punkte = punkte + 50;
                    String newTitel = getTitel(punkte);

                    Map<String, Object> update = new HashMap<>();
                    update.put("Produkte", eingetrageneProdukte);
                    update.put("Punkte", punkte);
                    update.put("Titel", newTitel);

                    db.collection("User").document(uid)
                            .update(update)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    if(!creatorID.equals("")){
                                        decreasePointsCreator();
                                    }
                                    else{
                                        Intent ergebnisIntent = new Intent(getApplicationContext(), ErgebnisActivity.class);
                                        ergebnisIntent.putExtra("EAN", ean);
                                        ergebnisIntent.putExtra("BEZ", bez);
                                        ergebnisIntent.putExtra("TEILE", bestandteile);
                                        ergebnisIntent.putExtra("ACTIVITY", "Main");
                                        startActivity(ergebnisIntent);
                                    }


                                }
                            });
                }
                progress.setVisibility(View.GONE);
            }

        });

    }

    /**
     * Methode, die Aufgerufen wird, wenn es sich um eine Korrektur handelt.
     * Die Punkte des Erstellers werden abgezogen und ggf der neue Titel aktualisiert.
     */
    public void decreasePointsCreator(){
        final DocumentReference ref = db.collection("User").document(creatorID);
        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()){
                        long punkte = (long) doc.get("Punkte");
                        punkte = punkte - 25;
                        String newTitel = getTitel(punkte);

                        Map<String, Object> update = new HashMap<>();
                        update.put("Punkte", punkte);
                        update.put("Titel", newTitel);

                        db.collection("User").document(creatorID).update(update).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Intent ergebnisIntent = new Intent(getApplicationContext(), ErgebnisActivity.class);
                                ergebnisIntent.putExtra("EAN", ean);
                                ergebnisIntent.putExtra("BEZ", bez);
                                ergebnisIntent.putExtra("TEILE", bestandteile);
                                ergebnisIntent.putExtra("ACTIVITY", "ProgressSteps");
                                startActivity(ergebnisIntent);
                            }
                        });
                    }

                }
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

}

