package Controllers;

import Utils.Season;

public class SeasonsController {

    private static Boolean verao;
    private static Boolean outono;
    private static Boolean inverno;
    private static Boolean primavera;

    public static void definirVerao(){
        verao = true;
        outono = false;
        inverno = false;
        primavera = false;
    }

    public static void definirOutono(){
        verao = false;
        outono = true;
        inverno = false;
        primavera = false;
    }

    public static void definirInverno(){
        verao = false;
        outono = false;
        inverno = true;
        primavera = false;
    }

    public static void definirPrimavera(){
        verao = false;
        outono = false;
        inverno = false;
        primavera = true;
    }
    
    public static Season getCurrentSeason(){
        if(verao)
            return new Season("Verão", 2, 0);
        if(outono)
            return new Season("Outuno", 1, 1);
        if(inverno)
            return new Season("Inverno", 2, 1);
        if(primavera)
            return new Season("Primavera", 1, 0);
        else 
            return null;
    }
}
