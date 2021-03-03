package Actors;

import java.util.List;
import java.util.Random;

import Utils.Location;
import Controllers.Field;
import Core.Simulator;

/**
 * Um modelo simples de um coelho, que determina suas açoes como comer, reproduzir e morrer
 * 
 * @author David J. Barnes and Michael Kolling
 * @version 2002-04-11
 */
public class Rabbit extends Animal {
    // Caracteristicas de todos os coelhos

    // Idade para se reproduzie
    private static final int BREEDING_AGE = 5;
    // Idade maxima de vida
    private static final int MAX_AGE = 50;
    // Chance de se reproduzir
    private static final double BREEDING_PROBABILITY = 0.15;
    // Numero maximo de reproduções
    private static final int MAX_LITTER_SIZE = 5;
    // Dias que um coelho pode passar sem comer
    private int CARROT_FOOD_LEVEL = 3;
    // Variavel pra criar deciçoes aleatórias
    private static final Random rand = new Random();


    private Field currentField;

    /**
     * Cria um novo coelho. O coelho pode nascer com idade 0 ou uma idade aleatória
     * 
     * @param randomAge Se verdadeiro, o coelho nasce com idade aleatória.
     * @param field O tabuleiro
     * @param location  A atual localização do coelho
     */
    public Rabbit(boolean randomAge, Field field, Location location) {
        super(0, true, location);
        this.currentField = field;
        if (randomAge) {
            setAge(rand.nextInt(MAX_AGE));
        }
    }

    /**
     * Coelho sempre irá correr, as vezes se alimentar e/ou reproduzir, e sempre irá invelhecer
     * will breed or die of old age.
     */
    public void run(Field updatedField, List<Actor> newRabbits) {
        incrementAge();
        eatFood();
        if (isActive()) {
            giveBirth(newRabbits, updatedField);

            Location newLocation = updatedField.freeAdjacentLocation(location);
            // Only transfer to the updated field if there was a free location
            if (newLocation != null) {
                setLocation(newLocation);
                updatedField.place(this, newLocation);
            } else {
                // can neither move nor stay - overcrowding - all locations taken
                setActive(false);
            }
        }
    }

    private void eatFood() {

        if (isActive()) {

            int globalFoodLevel = Simulator.getCondition("RABBIT_FOOD_LEVEL");

            if ((globalFoodLevel) >= 1 && CARROT_FOOD_LEVEL <= 3) {
                if(rand.nextInt(globalFoodLevel) > globalFoodLevel/20){
                    Simulator.updateConditions("RABBIT_FOOD_LEVEL", globalFoodLevel - 1);
                }else{
                    CARROT_FOOD_LEVEL ++;
                }
            } else {
                this.setActive(false);
            }
            
        
        }

    }


    private void giveBirth(List<Actor> newRabbits, Field updatedField) {
        int births = breed();
        for(int b = 0; b < births; b++){
            Location loc = updatedField.randomAdjacentLocation(location);
            Rabbit newRabbit = new Rabbit(false, currentField, loc);
            newRabbits.add(newRabbit);
        }
    }

    /**
     * Increase the age. This could result in the rabbit's death.
     */
    @Override
    public void incrementAge() {
        setAge(getAge()+1);
        if (getAge() > MAX_AGE) {
            setActive(false);
        }
    }

    /**
     * Generate a number representing the number of births, if it can breed.
     * 
     * @return The number of births (may be zero).
     */
    private int breed() {
        int births = 0;
        if (canBreed() && rand.nextDouble() <= BREEDING_PROBABILITY) {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
    }

    /**
     * A rabbit can breed if it has reached the breeding age.
     */
    @Override
    protected boolean canBreed() {
        return getAge() >= BREEDING_AGE;
    }

    /**
     * Tell the rabbit that it's dead now :(
     */
    public void setEaten() {
        setActive(false);
    }

    /**
     * Set the animal's location.
     * 
     * @param row The vertical coordinate of the location.
     * @param col The horizontal coordinate of the location.
     */
    public void setLocation(int row, int col) {
        this.location = new Location(row, col);
    }

    @Override
    public void action(Field field, Field updatedField, List<Actor> newAnimals) {
        run(updatedField, newAnimals);
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "Coelho";
    }

    @Override
    public int getBreedingAge() {
        // TODO Auto-generated method stub
        return this.BREEDING_AGE;
    }


}
