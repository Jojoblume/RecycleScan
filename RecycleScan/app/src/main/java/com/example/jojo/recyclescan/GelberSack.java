package com.example.jojo.recyclescan;

import java.util.ArrayList;

public class GelberSack {
    String bes1 = "Verpackungsplastik";
    String bes2 = "Verschluss (Clips, Alu, Kunststoff, Blech)";
    String bes3 = "Konservendose (Weißblech)";
    String bes4 = "Verbundstoff";
    String bes5 = "Aluminium";
    String bes6 = "PET Flasche (Ohne Pfand)";
    String bes7 = "Tetra Pak (mit Deckel)";
    String bes8 = "Plastik Banderole";

    String code1 = "01 PET";
    String code2 = "02 PE-HD";
    String code3 = "03 PVC";
    String code4 = "04 PE-LD";
    String code5 = "05 PP";
    String code6 = "06 PS";
    String code7 = "07 O";
    String code8 = "41 ALU";
    String codeX = "80-98 C/* (Verbundstoffe)";


    public ArrayList<String> listeGelb = new ArrayList<>();
    public ArrayList<String > listeCodeGelb = new ArrayList<>();

    public GelberSack(){
        listeGelb.add(bes1);
        listeGelb.add(bes2);
        listeGelb.add(bes3);
        listeGelb.add(bes4);
        listeGelb.add(bes5);
        listeGelb.add(bes6);
        listeGelb.add(bes7);
        listeGelb.add(bes8);

        listeCodeGelb.add(code1);
        listeCodeGelb.add(code2);
        listeCodeGelb.add(code3);
        listeCodeGelb.add(code4);
        listeCodeGelb.add(code5);
        listeCodeGelb.add(code6);
        listeCodeGelb.add(code7);
        listeCodeGelb.add(code8);
        listeCodeGelb.add(codeX);

    }

    public int getTonne(){
        return R.drawable.trash;
    }

}
