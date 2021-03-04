package Controllers;

import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;

import Utils.Counter;

/**
 * Essa classe coleta e provém alguns dados estatisticos a cerca do estado
 * do campo utilizado na simulação. Responsável pela criação e manutenção
 * de um contador para outras instancias no processo de simulação.
 * 
 * @author David J. Barnes and Michael Kolling
 * @version 2002-04-23
 */
public class FieldStats {
    
    /**
     * Responsável por armazenas contadores das mais diversas instancias
     * presentes na simulação como Fox, rabbit, etc. 
     */
    private HashMap counters;

    /**
     * Denota se o contador está em um estado válido e atualizado.
     */
    private boolean countsValid;

    /**
     * Construtor do objeto FieldStats
     */
    public FieldStats() {

        // Instancia um hashmap para armazenar os contadores para cada tipo de aninmal.
        counters = new HashMap();
        countsValid = true;
    }

    /**
     * Método responsável pelo acesso a informação sobre
     * quais animais estão presentes no campo da simulação.
     * 
     * @param field Campo de simulação.
     * @return Uma string contendo informações sobre quais animais estão no campo.
     */
    public String getPopulationDetails(Field field) {

        StringBuffer buffer = new StringBuffer();

        if (!countsValid) {

            generateCounts(field);

        }

        Iterator keys = counters.keySet().iterator();
        
        while (keys.hasNext()) {
        
            Counter info = (Counter) counters.get(keys.next());
            buffer.append(info.getName());
            buffer.append(": ");
            buffer.append(info.getCount());
            buffer.append(' ');
        
        }
        
        return buffer.toString();

    }

    /**
     * Reseta todos os contadores para zero, invalidando o conjunto
     * de estatisticas.
     */
    public void reset() {

        countsValid = false;
        Iterator keys = counters.keySet().iterator();

        while (keys.hasNext()) {

            Counter cnt = (Counter) counters.get(keys.next());
            cnt.reset();

        }

    }

    /**
     * Incrementa o contador de cada animal, um por um.
     *
     * @param animalClass Uma classe de animal presente na simulação.
     */
    public void incrementCount(Class animalClass) {

        Counter cnt = (Counter) counters.get(animalClass);

        if (cnt == null) {

            cnt = new Counter(animalClass.getName());
            counters.put(animalClass, cnt);

        }

        cnt.increment();

    }

    /**
     * Indica que uma contagem foi finalizada.
     */
    public void countFinished() { countsValid = true; }

    /**
     * Determina se a simulação ainda é viavel prosseguir.
     * 
     * @param field Campo de simulação.
     * @return true Se ainda há mais de uma espécie de ator presente na simualação em estado vivo.
     */
    public boolean isViable(Field field) {

        // quantos contadores são diferentes de zero
        int nonZero = 0;
        if (!countsValid) {

            generateCounts(field);

        }

        Iterator keys = counters.keySet().iterator();

        while (keys.hasNext()) {
            
            Counter info = (Counter) counters.get(keys.next());

            if (info.getCount() > 0) {

                nonZero++;

            }

        }

        return nonZero > 1;

    }

    /**
     * Gera contagens do número de raposas e coelhos. Estes não são mantidos atualizados,
     * apenas quando é feito uma solicitação para tal ação.
     */
    private void generateCounts(Field field) {

        reset();

        for (int row = 0; row < field.getDepth(); row++) {

            for (int col = 0; col < field.getWidth(); col++) {

                Object animal = field.getObjectAt(row, col);
                if (animal != null) {

                    incrementCount(animal.getClass());

                }

            }

        }

        countsValid = true;

    }
}
