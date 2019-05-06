package com.example.jojo.recyclescan;

import java.util.ArrayList;

public class Altpapier {
    String bes1 = "Karton / Pappe / Papier";
    String bes2 = "Karton mit Plastik-Sichtfenster";

    String code1 = "20 PAP";
    String code2 = "21 PAP";
    String code3 = "22 PAP";


    public ArrayList<String> listePap = new ArrayList<>();
    public ArrayList<String > listeCodePap = new ArrayList<>();

    public Altpapier(){
        listePap.add(bes1);
        listePap.add(bes2);

        listeCodePap.add(code1);
        listeCodePap.add(code2);
        listeCodePap.add(code3);

    }

    public int getTonne(){
        return R.drawable.paper;
    }
}
