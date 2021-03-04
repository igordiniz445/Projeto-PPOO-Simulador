package Controllers;

import Utils.Season;

/**
 * Essa classe abstrai um controlador para uma condição para
 * o processo de smiulação, estação do ano. Responśavel
 * pelas defniições de estação atual na simuação.
 * 
 * @author Igor Otávio
 * @version 1.0 SNAPSHOT
 */
public class SeasonsController {

    /**
     * Define condição da estação verão.
     */
    private static Boolean verao;

    /**
     * Define condição da estação outono.
     */
    private static Boolean outono;

    /**
     * Define condição da estação inverno.
     */
    private static Boolean inverno;

    /**
     * Define condição da estação primavera.
     */
    private static Boolean primavera;

    /**
     * Define o estado das estações pré definidas
     * pelos atributos nessa classe perante a condição
     * da estação atual ser o verão.
     */
    public static void definirVerao() {

        verao = true;
        outono = false;
        inverno = false;
        primavera = false;

    }

    /**
     * Define o estado das estações pré definidas
     * pelos atributos nessa classe perante a condição
     * da estação atual ser o outono.
     */
    public static void definirOutono() {

        verao = false;
        outono = true;
        inverno = false;
        primavera = false;

    }

    /**
     * Define o estado das estações pré definidas
     * pelos atributos nessa classe perante a condição
     * da estação atual ser o inverno.
     */
    public static void definirInverno() {

        verao = false;
        outono = false;
        inverno = true;
        primavera = false;

    }

    /**
     * Define o estado das estações pré definidas
     * pelos atributos nessa classe perante a condição
     * da estação atual ser o primavera.
     */
    public static void definirPrimavera() {

        verao = false;
        outono = false;
        inverno = false;
        primavera = true;

    }

    /**
     * Método responśavel por retornar a instancia da
     * estação definida como atual.
     * 
     * @return Season Uma instancia da estação atual.
     */
    public static Season getCurrentSeason() {

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
