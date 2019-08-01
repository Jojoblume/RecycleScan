package com.example.jojo.recyclescan;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;

import android.widget.ArrayAdapter;

import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import java.util.Map;

/**
 * VERALTET!
 * Activity NewProductActivity.
 * Falls gescanntes Produkt noch nicht in der Datenbank.
 * Abfrage nach Bezeichnung und Bestandteilen.
 * Multiple Choice Liste. Max 5 Bestandteile auswählen.
 * 2 Kategorien: Verpackungsbestandteile und Recyclingcodes.
 * Es sollen keine gedoppelten Angaben gemacht werden.
 */
public class NewProductActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView textEAN;
    FloatingActionButton btn_float;
    EditText textBez;
    ListView lv1;
    ListView lv2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        textEAN = findViewById(R.id.textViewEAN2);
        final String ean = getIntent().getExtras().getString("EAN");
        textEAN.setText(ean);

        textBez = findViewById(R.id.editTextBez);

        lv1 = findViewById(R.id.list1);
        lv2 = findViewById(R.id.list2);

        //Daten
        //kombinierte Liste der Bestandteile
        ArrayList<String> combined = new ArrayList<>();
        combined.addAll(Arrays.asList(getResources().getStringArray(R.array.arrayBestandteileGelb)));
        combined.addAll(Arrays.asList(getResources().getStringArray(R.array.arrayBestandteileGlas)));
        combined.addAll(Arrays.asList(getResources().getStringArray(R.array.arrayBestandteilePap)));

        ArrayAdapter<String> list = new ArrayAdapter<String>(NewProductActivity.this, android.R.layout.simple_list_item_multiple_choice, combined);
        lv1.setAdapter(list);

        // Recycling Codes
        ArrayList<String> combinedCodes = new ArrayList<>();
        combinedCodes.addAll(Arrays.asList(getResources().getStringArray(R.array.arrayCodesGelb)));
        combinedCodes.addAll(Arrays.asList(getResources().getStringArray(R.array.arrayCodesGlas)));
        combinedCodes.addAll(Arrays.asList(getResources().getStringArray(R.array.arrayCodesPap)));
        ArrayAdapter<String> listCodes = new ArrayAdapter<>(NewProductActivity.this, android.R.layout.simple_list_item_multiple_choice, combinedCodes);
        lv2.setAdapter(listCodes);


        lv1.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);


        lv2.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);


        btn_float = findViewById(R.id.floatButton);
        btn_float.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((lv1.getCheckedItemCount() + lv2.getCheckedItemCount())>5){
                    Toast.makeText(getApplicationContext(),  "Max 5 Bestandteile auswählen!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String bez = textBez.getText().toString();
                    if (bez.equals("") || bez.equals(" ")){
                        Toast.makeText(getApplicationContext(),  "Bitte eine Bezeichnung eingeben", Toast.LENGTH_SHORT).show();
                    }
                    else{

                        //Maximal 5. Früher kein Array in Firebase deswegen Bestandteil 1-5.
                        //Nun maximal 5 wegen dem User Interface. (Ansonsten ScrollView nötig)
                        String[] übergabe = {"","","","",""};
                        int counter = 0;

                        SparseBooleanArray checked = lv1.getCheckedItemPositions();
                        for (int i = 0; i < checked.size(); i++)
                        {
                            if (checked.valueAt(i))
                            {
                                int pos = checked.keyAt(i);
                                übergabe[counter] = lv1.getItemAtPosition(pos).toString();
                                counter++;
                            }
                        }
                        SparseBooleanArray checked2 = lv2.getCheckedItemPositions();
                        for (int i = 0; i < checked2.size(); i++)
                        {
                            if (checked2.valueAt(i))
                            {
                                int pos = checked2.keyAt(i);
                                übergabe[counter] = lv2.getItemAtPosition(pos).toString();
                                counter++;
                            }
                        }

                        Map<String, Object> product = new HashMap<>();
                        product.put("Bezeichnung", bez);
                        product.put("Bestandteile", Arrays.asList(übergabe));

                        db.collection("Produkte").document(ean)
                                .set(product)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(MainActivity.TAG, "DocumentSnapshot successfully written!");
                                        Intent homeIntent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(homeIntent);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(MainActivity.TAG, "Error writing document", e);
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
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
