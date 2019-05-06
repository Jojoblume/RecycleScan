package com.example.jojo.recyclescan;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

Button scanButton;
TextView info;
FirebaseFirestore db = FirebaseFirestore.getInstance();

public static final String TAG = "MyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        info = findViewById(R.id.textView);

        scanButton = findViewById(R.id.startScan);
        scanButton.setOnClickListener(this);
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

                                Intent ergebnisIntent = new Intent(getApplicationContext(), Ergebnis.class);
                                ergebnisIntent.putExtra("EAN", ean);
                                String bezeichnung = document.get("Bezeichnung").toString();
                                ergebnisIntent.putExtra("BEZ", bezeichnung);
                                String bes1 = document.get("Bestandteil 1").toString();
                                String bes2 = document.get("Bestandteil 2").toString();
                                String bes3 = document.get("Bestandteil 3").toString();
                                String bes4 = document.get("Bestandteil 4").toString();
                                String bes5 = document.get("Bestandteil 5").toString();
                                ArrayList<String> array = new ArrayList<>();
                                array.add(bes1);
                                array.add(bes2);
                                array.add(bes3);
                                array.add(bes4);
                                array.add(bes5);
                                ergebnisIntent.putExtra("TEILE", array);
                                startActivity(ergebnisIntent);
                            } else {
                                Log.d(TAG, "No such document");
                                Intent newProductIntent = new Intent(getApplicationContext(), NewProduct.class);
                                newProductIntent.putExtra("EAN", ean);
                                startActivity(newProductIntent);
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                            Toast.makeText(MainActivity.this,"Überpüfe deine Internetverbindung",Toast.LENGTH_SHORT).show();

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
    public void onClick(View v) {
        scan();
    }
}