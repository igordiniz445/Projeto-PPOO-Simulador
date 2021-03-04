package Actors;

import java.util.List;
import java.io.File;
import java.util.Iterator;
import java.util.Random;

import Utils.Location;
import Controllers.Field;

/**
 * Implementação da entidade Raposa. São possibilitadas as raposas,
 * os movimentos de posição, caça e morte.
 * 
 * @author Grupo, David J. Barnes and Michael Kolling
 * @version 1.0 SNAPSHOT
 */
public class Fox extends Animal {

    /**
     * Representa a idade em que a raposa começa a se reproduzir.
     */
    private static final int BREEDING_AGE = 10;
    
    /**
     * Representa o tempo máximo do ciclo de vida da raposa.
     */
    private static final int MAX_AGE = 150;
    
    /**
     * Probabilidade de reprodução da raposa.
     */
    private static final double BREEDING_PROBABILITY = 0.09;
    
    /**
     * Número máximo de processos de reprodução.
     */
    private static final int MAX_LITTER_SIZE = 3;
    
    /**
     * Representa a necessidade de alimentação da raposa.
     */
    private static final int RABBIT_FOOD_VALUE = 4;
    
    /**
     * Objeto de randomização do java para diferentes tarefas na classe.
     */
    private static final Random rand = new Random();

    /**
     * Campo de simulação atual
     */
    private Field currentField;

    /**
     * Construtor da raposa. Define atributos com base em parametros.
     * 
     * @param location Localização no campo de simulação
     * @param field Campo de simulação
     * @param randomAge Se verdadeiro, define atributos aleatorios.
     */
    public Fox(boolean randomAge, Field field, Location location) {

        super(0, true, location);
        this.currentField = field;

        if (randomAge) {

            setAge(rand.nextInt(MAX_AGE));
            setFoodLevel(rand.nextInt(RABBIT_FOOD_VALUE));

        } else {
            // leave age at 0
            setFoodLevel(RABBIT_FOOD_VALUE);
        }
    }

    /**
     * Implementa o processo de caça de coelhos por parte da raposa. Se neste
     * procedimento um coelho for caçado, significa a morte deste coelho.
     * 
     * @param updatedField Campo de simulação
     * @param newFoxes Novas raposas nascidas.
     */
    public void hunt(Field updatedField, List<Actor> newFoxes) {

        incrementAge();
        incrementHunger();

        if (isActive()) {
            // Novas raposas estão nascendo e indo para posições adjacentes
            giveBirth(newFoxes);
           
            // Movendo raposa para localização da comida
            Location newLocation = findFood(currentField, location);
            if (newLocation == null) { // sem comida, move randomicamente
                newLocation = updatedField.freeAdjacentLocation(location);
            }
            if (newLocation != null) {
                setLocation(newLocation);
                updatedField.place(this, newLocation);
            } else {
                // Não pode se mover pois não há posições livres nem pode continuar no local
                setActive(false);
            }
        }
    }

    /**
     * Implementa a reprodução das raposas.
     * 
     * @param newFoxes Novas raposas da ninhada.
     */
    private void giveBirth(List<Actor> newFoxes) {

        List<Location> free = currentField.getFreeAdjacentLocation(location);
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++){
            Location loc = free.remove(0);
            Fox newFox = new Fox(false, currentField, loc);
            newFoxes.add(newFox);
        }
    }

    /**
     * Incrementa a idade, se atingir idade limite,
     * resulta em sua morte.
     */
    @Override
    public void incrementAge() {

        setAge(getAge()+1);

        if (getAge() > MAX_AGE) {
            setActive(false);
        }

    }

    /**
     * Incrementa a quantidade de fome da raposa. Se atingir o nível limite,
     * resulta em sua morte.
     */
    private void incrementHunger() {
        setFoodLevel(getFoodLevel()-1);
        if (getFoodLevel() <= 0) {
            setActive(false);
        }
    }

    /**
     * Implementa a ação de procura por comida, observando posições adjacentes
     * para caçar.
     * 
     * @param field Campo da simulação
     * @param location Objeto de localização com as coordenadas de campo.
     * @return Location A localização da comida dispoinvel.
     */
    private Location findFood(Field field, Location location) {

        Iterator adjacentLocations = field.adjacentLocations(location);

        while (adjacentLocations.hasNext()) {
            Location where = (Location) adjacentLocations.next();
            Object animal = field.getObjectAt(where);
            if (animal instanceof Rabbit) {
                Rabbit rabbit = (Rabbit) animal;
                if (rabbit.isActive()) {
                    rabbit.setEaten();
                    setFoodLevel(RABBIT_FOOD_VALUE);
                    return where;
                }
            }
        }
        return null;
    }

    /**
     * Generate a number representing the number of births, if it can breed.
     * Gera um número que representa a quantidade de nascimentos caso a raposa
     * já possa se reproduzir.
     * 
     * @return int Número de filhos.
     */
    private int breed() {
        int births = 0;
        if (canBreed() && rand.nextDouble() <= BREEDING_PROBABILITY) {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
    }


    /**
     * Modifica a localização do animal.
     * 
     * @param row Linha, representa coordenada X.
     * @param col Coluna, representa coordenada Y.
     */
    public void setLocation(int row, int col) {

        this.location = new Location(row, col);

    }

    /**
     * Implementa o método action de Animal.
     * 
     * @param field Campo de simulação.
     * @param updatedField Campo atualizado de simulação.
     * @param newAnimals Novos animais nascidos.
     */
    @Override
    public void action(Field field, Field updatedField, List<Actor> newAnimals) {
        hunt( updatedField, newAnimals);
    }
    
    /**
     * Implementa verificação se o animal já pode
     * se reproduzir.
     * 
     * @return true se o animal já puder se reproduzir.
     */
    @Override
    protected boolean canBreed() {
        return getAge() >= BREEDING_AGE;
    }

    /**
     * Método acessador do atributo idade de reprodução.
     */
    @Override
    public int getBreedingAge() {
        // TODO Auto-generated method stub
        return this.BREEDING_AGE;
    }
    
    /**
     * Método sobrescrito de reprentação textual do objeto.
     * 
     * @return String a representação textual do objeto.
     */
    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "Raposa";
    }

}
