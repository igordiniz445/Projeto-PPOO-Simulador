package Actors;

import java.util.List;

import Controllers.Field;

/**
 * Implementa uma interface pai ator
 * para participantes do processo de simulação.
 * 
 * @author Grupo
 * @version 1.0 SNAPSHOT
 */
public interface Actor {

    /**
     * Método de ação do ator.
     * 
     * @param field Campo da simulação.
     * @param updatedField Campo atual da simulação.
     * @param newAnimals Novos animais nascidos.
     * @param animals lista de animais.
     */
    public void act(Field field, Field updatedField, List<Actor> newAnimals, List<Actor> animals);
    
    /**
     * Retorna se o ator continua ativo.
     * 
     * @return true Se o ator esta ativo na simulação.
     */
    public boolean isActive();

    /**
     * Define se o ator está ativo.
     * 
     * @param alive Condição de existencia.
     */
    public void setActive(boolean alive);
    
}
