package Utils;

import java.awt.Color;

/**
 * Fornece um contador para um participante da simulação. Nele contem um
 * identificador string e um contador de quantos participantes desse tipo
 * existem na simulação no momento.
 * 
 * @author David J. Barnes and Michael Kolling
 * @version 2002-04-23
 */
public class Counter {
    
    /**
     * Um nome para o tipo de participante da simulação
     */
    private String name;
    
    /**
     * A quantidade desse tipo que existem na simulação
     */
    private int count;

    /**
     * Fornece um nome para um dos tipos na simulação.
     * 
     * @param name Um nome, por exemplo "Fox".
     */
    public Counter(String name) {

        this.name = name;
        count = 0;

    }

    /**
     * Método acessador do estado do atributo name.
     * 
     * @return Uma curta descrição desse tipo.
     */
    public String getName() { return name; }

    /**
     * Método acessador do estado do atributo count.
     * 
     * @return O atual count desse tipo.
     */
    public int getCount() { return count; }

    /**
     * Incrementa o atual count em um.
     */
    public void increment() { count++; }

    /**
     * Reseta o atual count para zero.
     */
    public void reset() { count = 0; }
    
}
