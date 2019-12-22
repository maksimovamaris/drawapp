package com.example.drawapp;
import java.util.ArrayList;


public class Pallet {

    private String color;

    public Pallet(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public static ArrayList<Pallet> getPallet(){
        ArrayList<Pallet> pallets = new ArrayList<>();
        pallets.add(new Pallet("#000000"));
        pallets.add(new Pallet("#404040"));
        pallets.add(new Pallet("#FF0000"));
        pallets.add(new Pallet("#FF6A00"));
        pallets.add(new Pallet("#FFD800"));
        pallets.add(new Pallet("#B6FF00"));
        pallets.add(new Pallet("#4CFF00"));
        pallets.add(new Pallet("#00FF21"));
        pallets.add(new Pallet("#00FF90"));
//        pallets.add(new Pallet("#00FFFF"));
//        pallets.add(new Pallet("#0094FF"));
//        pallets.add(new Pallet("#0026FF"));
//        pallets.add(new Pallet("#4800FF"));
//        pallets.add(new Pallet("#B200FF"));
//        pallets.add(new Pallet("#FF00DC"));
//        pallets.add(new Pallet("#FF006E"));
//        pallets.add(new Pallet("#FFFFFF"));
//        pallets.add(new Pallet("#808080"));
//        pallets.add(new Pallet("#7F0000"));
//        pallets.add(new Pallet("#7F3300"));
//        pallets.add(new Pallet("#7F6A00"));
//        pallets.add(new Pallet("#5B7F00"));
//        pallets.add(new Pallet("#267F00"));
//        pallets.add(new Pallet("#007F0E"));
//        pallets.add(new Pallet("#007F46"));
//        pallets.add(new Pallet("#007F7F"));
//        pallets.add(new Pallet("#004A7F"));
//        pallets.add(new Pallet("#00137F"));
//        pallets.add(new Pallet("#21007F"));
//        pallets.add(new Pallet("#57007F"));
//        pallets.add(new Pallet("#7F006E"));
//        pallets.add(new Pallet("#7F0037"));

        return pallets;
    }
}