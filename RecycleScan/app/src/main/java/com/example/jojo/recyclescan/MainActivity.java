package com.example.jojo.recyclescan;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;


/**
 * Activity MainActivity.
 * Von hier aus Scannen starten oder Suchen nach typischen Verpackungen.
 * Außerdem allgemeine Tipps zur Mülltrennung als "Artikel"
 */
public class MainActivity extends AppCompatActivity {

Button scanButton;
EditText search;

CardView cv1;
CardView cv2;
CardView cv3;
CardView cv4;


FirebaseFirestore db = FirebaseFirestore.getInstance();
private FirebaseAuth mAuth;
FirebaseUser currentUser;


public static final String TAG = "MyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();


        search = findViewById(R.id.searchEAN);

        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ( (actionId == EditorInfo.IME_ACTION_DONE) || ((event.getKeyCode() == KeyEvent.KEYCODE_ENTER) && (event.getAction() == KeyEvent.ACTION_DOWN ))) {
                    final String ean = search.getText().toString();
                    if(ean.isEmpty()){
                        Toast.makeText(MainActivity.this, "Trage eine Barcode Nummer ein", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        final DocumentReference docRef = db.collection("Produkte").document(ean);
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                                        Intent ergebnisIntent = new Intent(getApplicationContext(), ErgebnisActivity.class);
                                        ergebnisIntent.putExtra("EAN", ean);
                                        String bezeichnung = document.get("Bezeichnung").toString();
                                        ergebnisIntent.putExtra("BEZ", bezeichnung);
                                        ArrayList<String> array = (ArrayList<String>) document.get("Bestandteile");
                                        Log.d(TAG, array.toString());

                                        ergebnisIntent.putStringArrayListExtra("TEILE", array);
                                        startActivity(ergebnisIntent);
                                    } else {
                                        Toast.makeText(MainActivity.this, "Barcode unbekannt. Zum Hinzufügen zur Datenbank bitte den Scanner benutzen", Toast.LENGTH_SHORT).show();

                                    }
                                } else {
                                    Log.d(TAG, "get failed with ", task.getException());
                                    Toast.makeText(MainActivity.this, "Überpüfe deine Internetverbindung", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

                    }
                    return true;
                }
                else{
                    return false;
                }
            }

        });

        scanButton = findViewById(R.id.startScan);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scan();
            }
        });

        cv1 = findViewById(R.id.cardView1);
        cv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent articleIntent = new Intent(MainActivity.this, ArticleActivity.class);
                articleIntent.putExtra("CV", "cv1");
                startActivity(articleIntent);
            }
        });
        cv2 = findViewById(R.id.cardView2);
        cv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent articleIntent = new Intent(MainActivity.this, ArticleActivity.class);
                articleIntent.putExtra("CV", "cv2");
                startActivity(articleIntent);
            }
        });
        cv3 = findViewById(R.id.cardView3);
        cv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent articleIntent = new Intent(MainActivity.this, ArticleActivity.class);
                articleIntent.putExtra("CV", "cv3");
                startActivity(articleIntent);
            }
        });
        cv4 = findViewById(R.id.cardView4);
        cv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent articleIntent = new Intent(MainActivity.this, ArticleActivity.class);
                articleIntent.putExtra("CV", "cv4");
                startActivity(articleIntent);
            }
        });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if ( result != null){
            if ( result.getContents() == null){
                Toast.makeText(this, "Result not found", Toast.LENGTH_SHORT).show();
            }
            else{
                final String ean =result.getContents();
                final DocumentReference docRef = db.collection("Produkte").document(ean);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                                Intent ergebnisIntent = new Intent(getApplicationContext(), ErgebnisActivity.class);
                                ergebnisIntent.putExtra("EAN", ean);
                                String bezeichnung = document.get("Bezeichnung").toString();
                                ergebnisIntent.putExtra("BEZ", bezeichnung);
                                ArrayList<String> array = (ArrayList<String>) document.get("Bestandteile");
                                Log.d(TAG, array.toString());

                                ergebnisIntent.putExtra("TEILE", array);
                                startActivity(ergebnisIntent);
                            } else {
                                Log.d(TAG, "No such document");
                                Intent newProductIntent = new Intent(getApplicationContext(), ProgressStepsActivity.class);
                                newProductIntent.putExtra("EAN", ean);
                                newProductIntent.putExtra("INTENT", "MainActivity");
                                startActivity(newProductIntent);

                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                            Toast.makeText(MainActivity.this,"Überpüfe deine Internetverbindung",Toast.LENGTH_LONG).show();

                        }
                    }
                });

            }
        }
        else {
            super.onActivityResult(requestCode,resultCode, data);
        }
    }

    public void scan() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(Portrait.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scanne einen Barcode (EAN)");
        integrator.initiateScan();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if ( currentUser == null){
            getMenuInflater().inflate(R.menu.main, menu);
        }else{
            getMenuInflater().inflate(R.menu.main_profil, menu);
        }

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.searchIcon:
                startActivity(new Intent(this, SearchActivity.class));
                break;
            case R.id.profile:
                currentUser = mAuth.getCurrentUser();
                //Eigentlich nicht sichtbar, aber hier doppelt gesichert:
                if ( currentUser == null){
                    Toast.makeText(this, "Trage das 1. Produkt ein", Toast.LENGTH_LONG).show();
                }else
                {
                    startActivity(new Intent(this, ProfilActivity.class));
                }

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Just for Test!
        //mAuth.signOut();

        currentUser = mAuth.getCurrentUser();
        invalidateOptionsMenu();

    }

}