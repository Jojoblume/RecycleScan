package com.example.jojo.recyclescan;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.jojo.helpers.CustomGridAdapter;
import com.example.jojo.helpers.MyGridView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;

public class ProfilActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String userID;

    TextView textTitel;
    TextView textBenutzername;
    TextView textPoints;
    TextView textPointsMax;
    ImageView img_titel;
    ProgressBar progress;

    MyGridView sammlung;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textTitel = findViewById(R.id.textViewTitel);
        textBenutzername = findViewById(R.id.textViewBenutzername);
        textPoints = findViewById(R.id.textViewPoints);
        textPointsMax = findViewById(R.id.textViewPointsMax);
        img_titel = findViewById(R.id.imageViewTitel);
        progress = findViewById(R.id.progressPoints);
        sammlung = findViewById(R.id.gridView);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        userID =   currentUser.getUid();


        setUserData();

    }

    public void setUserData(){
        final DocumentReference docRef = db.collection("User").document(userID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {

                    DocumentSnapshot document = task.getResult();
                    String benutzernameCreator = (String) document.get("Benutzername");
                    textBenutzername.setText(benutzernameCreator);

                    String titel = (String) document.get("Titel");
                    textTitel.setText(titel);
                    setImage(titel);

                    long punkte = (long) document.get("Punkte");
                    setFortschrittsbalken(punkte);

                    //SAMMLUNG
                    ArrayList<String> produkte = (ArrayList<String>) document.get("Produkte");
                    //Umkehren, damit neue Produkte oben stehen.
                    Collections.reverse(produkte);
                    setSammlung(produkte);

                }
            }
        });

    }

    /**
     * Sammlung wird erstellt und bei Klick auf Item gelangt der Benutzer zur ErgebnisActivity.
     * @param produkte
     */
    private void setSammlung(ArrayList<String> produkte) {

        CustomGridAdapter adapter = new CustomGridAdapter(ProfilActivity.this, produkte);
        sammlung.setAdapter(adapter);

        sammlung.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String ean = sammlung.getItemAtPosition(position).toString();
                DocumentReference docRef = db.collection("Produkte").document(ean);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            String bezeichnung = (String) document.get("Bezeichnung");
                            ArrayList<String> bestandteile = (ArrayList<String>) document.get("Bestandteile");

                            Intent ergebnisIntent = new Intent(getApplicationContext(), ErgebnisActivity.class);
                            ergebnisIntent.putExtra("EAN", ean);
                            ergebnisIntent.putExtra("BEZ", bezeichnung);
                            ergebnisIntent.putStringArrayListExtra("TEILE", bestandteile);
                            ergebnisIntent.putExtra("ACTIVITY", "Profil");
                            startActivity(ergebnisIntent);
                        }

                    }
                });
            }
        });
    }

    /**
     * Berechnung der Prozentzahl des Fortschritts.
     * @param punkte
     */
    private void setFortschrittsbalken(long punkte) {
        int total = 1000;
        textPoints.setText(""+punkte);
        //Bei der 1. Stufe leichter aufzusteigen.
        if ( punkte < 500){
            total = 500;
            textPointsMax.setText("500");
        }
        else if (punkte >= 500 && punkte < 1500){
            punkte = punkte -500;
            textPointsMax.setText("1500");
        }
        else if (punkte >= 1500 && punkte <2500){
            punkte = punkte -1500;
            textPointsMax.setText("2500");
        }
        else if (punkte >=2500 && punkte <3500){
            punkte = punkte -2500;
            textPointsMax.setText("3500");
        }
        else if (punkte >= 3500 && punkte <4500){
            punkte = punkte -3500;
            textPointsMax.setText("4500");
        }
        else if(punkte >= 4500){
            punkte = punkte -4500;
            textPointsMax.setText("5500");
        }
        double percent = (((double)punkte/total)*100);
        progress.setProgress((int)percent);
        progress.setVisibility(View.VISIBLE);
    }


    /**
     * Ermittlung des passenden Bildes zum Titel.
     * @param titel
     */
    private void setImage(String titel) {
        if (titel.equals("Müllmonster")){
            //Icon made by Freepik from www.flaticon.com
            img_titel.setImageResource(R.drawable.monster);
        }
        else if (titel.equals("Schrottsammler")){
            //Icon made by Freepik from www.flaticon.com
            img_titel.setImageResource(R.drawable.schrottsammler);
        }
        else if (titel.equals("Sprössling")){
            //Icon made by Icongeek26 from www.flaticon.com
            img_titel.setImageResource(R.drawable.sproessling);

        }
        else if (titel.equals("Recycler")){
            //Icon made by monkik from www.flaticon.com
            img_titel.setImageResource(R.drawable.recycler);

        }
        else if (titel.equals("Klimaheld")){
            //Icon made by Freepik from www.flaticon.com
            img_titel.setImageResource(R.drawable.klimaheld);

        }else {
            //Umweltaktivist
            //Icon made by Freepik from www.flaticon.com
            img_titel.setImageResource(R.drawable.umweltaktivist);

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
