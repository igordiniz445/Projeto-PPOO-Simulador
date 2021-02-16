package Actors;

import java.util.List;
import java.util.Random;

import Utils.Location;
import Controllers.Field;

/**
 * A simple model of a rabbit. Rabbits age, move, breed, and die.
 * 
 * @author David J. Barnes and Michael Kolling
 * @version 2002-04-11
 */
public class Rabbit extends Animal {
    // Characteristics shared by all rabbits (static fields).

    // The age at which a rabbit can start to breed.
    private static final int BREEDING_AGE = 5;
    // The age to which a rabbit can live.
    private static final int MAX_AGE = 50;
    // The likelihood of a rabbit breeding.
    private static final double BREEDING_PROBABILITY = 0.15;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 5;
    // A shared random number generator to control breeding.
    private static final Random rand = new Random();


    private Field currentField;

    /**
     * Create a new rabbit. A rabbit may be created with age zero (a new born) or
     * with a random age.
     * 
     * @param randomAge If true, the rabbit will have a random age.
     */
    public Rabbit(boolean randomAge, Field field, Location location) {
        super(0, true, location);
        this.currentField = field;
        if (randomAge) {
            setAge(rand.nextInt(MAX_AGE));
        }
    }

    /**
     * This is what the rabbit does most of the time - it runs around. Sometimes it
     * will breed or die of old age.
     */
    public void run(Field updatedField, List<Actor> newRabbits) {
        incrementAge();
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
