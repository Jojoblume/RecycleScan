package com.example.jojo.recyclescan;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jojo.helpers.Bestandteil;
import com.example.jojo.helpers.MyAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Activity ErgebnisActivity.
 * Zeigt nach dem Scannen die EAN, die Bezeichnung des Produkts, sowie dessen Bestandteile mit Zuordnung der Tonne an.
 */
public class ErgebnisActivity extends AppCompatActivity {

    TextView textEAN;
    TextView textBez;
    TextView textViewBenutzername;
    TextView textKorrigieren;
    ListView lv;

    //Hinweis
    CardView cardView;
    TextView hinweis;
    TextView hinweisText;

    //Liste Bestandteile
    ArrayList<String> array;

    //Listen für Zuordnung
    private MyAdapter mAdapter;
    List<String> gelb = new ArrayList<>();
    List<String> gelbCode = new ArrayList<>();
    List<String> glas = new ArrayList<>();
    List<String> glasCode = new ArrayList<>();
    List<String> pap = new ArrayList<>();
    List<String> papCode = new ArrayList<>();


    //Firebase
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    //Firebase Current User
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String currentUserID;
    String currentUserTitel;

    //Ersteller des Produkteintrags.
    String creatorUID;
    Intent creatorDataIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ergebnis);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        array = getIntent().getStringArrayListExtra("TEILE");

        //Zuordnungslisten
        gelb = Arrays.asList(getResources().getStringArray(R.array.arrayBestandteileGelb));
        gelbCode = Arrays.asList(getResources().getStringArray(R.array.arrayCodesGelb));
        glas = Arrays.asList(getResources().getStringArray(R.array.arrayBestandteileGlas));
        glasCode = Arrays.asList(getResources().getStringArray(R.array.arrayCodesGlas));
        pap = Arrays.asList(getResources().getStringArray(R.array.arrayBestandteilePap));
        papCode = Arrays.asList(getResources().getStringArray(R.array.arrayCodesPap));

        textEAN = findViewById(R.id.textViewEAN2);
        final String ean = getIntent().getExtras().getString("EAN");
        textEAN.setText(ean);

        textBez = findViewById(R.id.textViewBez);
        final String bezeichnung = getIntent().getExtras().getString("BEZ");
        textBez.setText(bezeichnung);

        //Punkte geben
        giveUserPoints(ean);

        //Korrektur nur möglich, wenn es der eigene Eintrag ist, oder der Eintrag alt ist (anonym) oder der Titel des Creators geringer ist
        //als der Titel des aktuellen Benutzers.
        textKorrigieren = findViewById(R.id.textViewFehler);
        textKorrigieren.setVisibility(View.INVISIBLE);

        textViewBenutzername = findViewById(R.id.textViewAccount);
        final DocumentReference docRef = db.collection("Produkte").document(ean);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    //Alte Einträge besitzen nicht das Feld "UserID"
                    //Später wird dann "anonym" angezeigt.
                    if ( document.get("UserID") == null){
                        creatorUID = "";
                    }else{
                        creatorUID = (String) document.get("UserID");
                    }
                    getCreatorData();
                }
            }
        });

        //CreatorActivity wird bei Klick auf Benutzername gestartet.
        textViewBenutzername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (! creatorUID.equals("")){
                    startActivity(creatorDataIntent);
                }

            }
        });

        textKorrigieren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentKorrigieren = new Intent(ErgebnisActivity.this, ProgressStepsActivity.class);
                intentKorrigieren.putExtra("EAN", ean);
                intentKorrigieren.putExtra("INTENT", "ErgebnisActivity");
                intentKorrigieren.putExtra("BEZ", bezeichnung);
                //CreatorID wird übergeben, damit ihm Punkte abgezogen werden können.
                intentKorrigieren.putExtra("CREATOR", creatorUID);
                startActivity(intentKorrigieren);
            }
        });

        setHinweistext(bezeichnung);

        bestandteileZuordnen();

    }

    /**
     * Liste der Bestandteile mit richtigem Bild
     * Bei Klick auf Item Start InfoTonneActivity.
     */
    private void bestandteileZuordnen() {

        ArrayList<Bestandteil> newArray = new ArrayList<>();
        int drawable;

        for ( int i = 0; i < array.size(); i++){
            if ( ! array.get(i).equals("")){
                drawable = getTonne(array.get(i));
                newArray.add(new Bestandteil(drawable, array.get(i)));
            }
        }

        lv = findViewById(R.id.lvBes);
        mAdapter = new MyAdapter(this,newArray);
        lv.setAdapter(mAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ErgebnisActivity.this, InfoTonneActivity.class);
                String name = mAdapter.getName(position);
                if ( gelb.contains(name) || gelbCode.contains(name)){
                    intent.putExtra("TONNE", "gelb");
                }
                else if ( glas.contains(name) || glasCode.contains(name)){
                    intent.putExtra("TONNE", "glas");
                }
                else if ( pap.contains(name) || papCode.contains(name)){
                    intent.putExtra("TONNE", "pap");
                }
                startActivity(intent);
            }
        });
    }

    /**
     * Je nachdem, werden besondere Hinweistexte angezeigt.
     * @param bezeichnung
     */
    private void setHinweistext(String bezeichnung) {
        // Sichtbarkeit der Hinweistexte. Jeweils nur 1 Hinweis. Verpackungen mit mehr als 1 Hinweis unwahrscheinlich.
        // Falls doch, wird der Joghurt Hinweis angezeigt.
        cardView = findViewById(R.id.cardView);
        hinweis = findViewById(R.id.textViewHinweis);
        hinweisText = findViewById(R.id.textViewHinweisText);
        cardView.setVisibility(View.INVISIBLE);
        if (bezeichnung.contains("joghurt") || bezeichnung.contains("Joghurt") || bezeichnung.equals("Joghurt")){
            hinweis.setText("Hinweis: Joghurtbecher");
            hinweisText.setText("Den Joghurtbecher bitte NICHT platzsparend stapeln. Außerdem reicht es, wenn er \"löffelrein\" ist. Er muss nicht extra ausgespült werden.");
            cardView.setVisibility(View.VISIBLE);

        }
        else {
            for (int i = 0; i<array.size(); i++){
                if (array.get(i).equals("Tetra Pak")){
                    hinweis.setText("Hinweis: Tetra Pak");
                    hinweisText.setText("Verschlusskappe wieder anbringen und Trinkhalme zurück in die Packung drücken.  ");
                    cardView.setVisibility(View.VISIBLE);
                }
                else if (array.get(i).equals("Karton mit Plastik-Sichtfenster")){
                    hinweis.setText("Hinweis: Sichtfenster");
                    hinweisText.setText("Kartons / Briefe mit Plastik Sichtfenster gehören ins Altpapier. Der Kunststoffanteil ist verkraftbar und für die Weiterverarbeitung als Recyclingpapier unerheblich.");
                    cardView.setVisibility(View.VISIBLE);
                }
                else if (array.get(i).contains("Glas")){
                    hinweis.setText("Hinweis: Glas Verschluss");
                    hinweisText.setText("Moderne Müllanlagen können Deckel aussortieren. Trotzdem können Verschlüsse auch in den Gelben Sack und die gelbe Tonne geworfen werden.");
                    cardView.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    /**
     * Die Daten des Erstellers von Firebase holen.
     * Ermittlung, ob Ersteller höheren Rang hat.
     */
    private void getCreatorData(){
        if (creatorUID.equals("")){
            textViewBenutzername.setText("Anonym");
            textKorrigieren.setVisibility(View.VISIBLE);
        }
        else{
            final DocumentReference docRef = db.collection("User").document(creatorUID);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        String benutzernameCreator = (String) document.get("Benutzername");
                        textViewBenutzername.setText(benutzernameCreator);

                        String titelCreator = (String) document.get("Titel");
                        long punkteCreator = (long)document.get("Punkte");
                        ArrayList<String> produkte = (ArrayList<String>) document.get("Produkte");
                        int anzahlProdukte = produkte.size();

                        creatorDataIntent = new Intent(ErgebnisActivity.this, CreatorActivity.class);
                        creatorDataIntent.putExtra("BENUTZERNAME", benutzernameCreator);
                        creatorDataIntent.putExtra("TITEL", titelCreator);
                        creatorDataIntent.putExtra("PUNKTE", punkteCreator);
                        creatorDataIntent.putExtra("ANZAHLPRODUKTE", anzahlProdukte);

                        //Ergebnis Korrigieren sichtbar, wenn der Benutzer selbst der "Creator" ist.
                        //Currentuser erfassen nicht in onCreate() möglich...
                        mAuth = FirebaseAuth.getInstance();
                        currentUser = mAuth.getCurrentUser();
                        if (currentUser != null) {
                            currentUserID = currentUser.getUid();
                            if (currentUserID.equals(creatorUID)){
                                textKorrigieren.setVisibility(View.VISIBLE);
                            }
                            else{
                                int rangCurrentUser = getNumberTitel(currentUserTitel);
                                int rangCreator = getNumberTitel(titelCreator);
                                if (rangCurrentUser >= rangCreator){
                                    textKorrigieren.setVisibility(View.VISIBLE);
                                }
                            }

                        }


                    }
                }
            });
        }

    }

    /**
     * Der Benutzer erhält 5 Punkte, wenn er ein Produkt scannt, das sich bereits in der Datenbank befindet.
     * Er erhält die Punkte NICHT, wenn er ein eigenes Produkt scannt.
     * @param ean
     */
    private void giveUserPoints(final String ean) {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            currentUserID = currentUser.getUid();

            final DocumentReference docRef = db.collection("User").document(currentUserID);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();

                        long punkte = (long) document.get("Punkte");
                        ArrayList<String> eigeneEANs = (ArrayList<String>) document.get("Produkte");
                        //Es gibt nur Punkte, wenn man nicht die selbst eingetragenen Produkte scannt.
                        //Punkte erhöhen die Erfahrungsstufe und durch eigene Eintragungen steigt nicht das Wissen.
                        if (! eigeneEANs.contains(ean)){

                            punkte = punkte +5;
                            String newTitel = getTitel(punkte);

                            Map<String, Object> data = new HashMap<>();
                            data.put("Titel", newTitel);
                            data.put("Punkte", punkte);
                            db.collection("User").document(currentUserID).update(data);
                        }

                        //Titel für später wichtig.
                        //Ermittlung, ob Ersteller höheren Rang hat.
                        currentUserTitel = (String) document.get("Titel");

                    }
                }
            });

        }
    }

    /**
     * Zuordnung get Image
     */
    public int getTonne(String bes){

        if ( gelb.contains(bes) || gelbCode.contains(bes)){
            return R.drawable.trash;
        }
        else if(glas.contains(bes) || glasCode.contains(bes)){
            if (bes.equals(getString(R.string.farblos)) || bes.equals(getString(R.string.farblosCode))){
                return R.drawable.glass_white; //farblos
            } else if (bes.equals(getString(R.string.gruen)) || bes.equals(getString(R.string.anders)) || bes.equals(getString(R.string.gruenCode))) {
                return R.drawable.glass_green; //grün
            }
            else if ( bes.equals(getString(R.string.braun)) || bes.equals(getString(R.string.braunCode))){
                return R.drawable.glass_brown; //braun
            }
        }
        else if(pap.contains(bes) || papCode.contains(bes)){
            return R.drawable.paper;
        }
        return -1;
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

    /**
     * Um zu prüfen, ob der Ersteller einen höheren Rang hat, ist es nötig die Titelstufen in Zahlen umzuwandeln.
     * @param titel
     * @return
     */
    public int getNumberTitel(String titel){
        if (titel.equals("Müllmonster")){
            return 1;
        }
        else if (titel.equals("Schrottsammler")){
            return 2;
        }
        else if (titel.equals("Sprössling")){
            return 3;
        }
        else if (titel.equals("Recycler")){
            return 4;
        }
        else if(titel.equals("Klimaheld")){
            return 5;
        }else{
            return 6;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }




}
