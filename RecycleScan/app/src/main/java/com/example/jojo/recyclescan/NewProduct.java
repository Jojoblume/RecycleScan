package com.example.jojo.recyclescan;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import java.util.List;
import java.util.Map;

public class NewProduct extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView textEAN;
    FloatingActionButton btn_float;
    EditText textBez;
    ListView lv1;
    ListView lv2;

    public static String[] übergabe = {"","","","",""};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);

        textEAN = findViewById(R.id.textViewEAN2);
        final String ean = getIntent().getExtras().getString("EAN");
        textEAN.setText(ean);

        textBez = findViewById(R.id.editTextBez);

        lv1 = findViewById(R.id.list1);
        lv2 = findViewById(R.id.list2);

        //Daten
        GelberSack gelb = new GelberSack();
        Glas glas = new Glas();
        Altpapier pap = new Altpapier();
        //kombinierte Liste der Bestandteile
        ArrayList<String> combined = new ArrayList<>();
        combined.addAll(gelb.listeGelb);
        combined.addAll(glas.listeGlas);
        combined.addAll(pap.listePap);

        ArrayAdapter<String> list = new ArrayAdapter<String>(NewProduct.this, android.R.layout.simple_list_item_multiple_choice, combined);
        lv1.setAdapter(list);

        // Recycling Codes
        ArrayList<String> combinedCodes = new ArrayList<>();
        combinedCodes.addAll(gelb.listeCodeGelb);
        combinedCodes.addAll(glas.listeCodeGlas);
        combinedCodes.addAll(pap.listeCodePap);
        ArrayAdapter<String> listCodes = new ArrayAdapter<>(NewProduct.this, android.R.layout.simple_list_item_multiple_choice, combinedCodes);
        lv2.setAdapter(listCodes);


        lv1.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Verdreht?? isItemChecked...
                if ( lv1.isItemChecked(position)){
                    lv1.setItemChecked(position, true);

                }
                else
                {
                    lv1.setItemChecked(position, false);

                }

            }
        });

        lv2.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        lv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if ( lv2.isItemChecked(position)){
                    lv2.setItemChecked(position, true);

                }
                else
                {
                    lv2.setItemChecked(position, false);

                }
            }
        });

        btn_float = findViewById(R.id.floatButton);
        btn_float.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Wenn übergabe. size + andere Liste+size >5!!!
                if((lv1.getCheckedItemCount() + lv2.getCheckedItemCount())>5){
                    Toast.makeText(getApplicationContext(),  "Max 5 Bestandteile auswählen!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String bez = textBez.getText().toString();
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
                     product.put("Bestandteil 1", übergabe[0]);
                     product.put("Bestandteil 2", übergabe[1]);
                     product.put("Bestandteil 3", übergabe[2]);
                     product.put("Bestandteil 4", übergabe[3]);
                     product.put("Bestandteil 5", übergabe[4]);

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
        });

    }
}
