package Actors;

import java.util.List;
import java.io.File;
import java.util.Iterator;
import java.util.Random;

import Utils.Location;
import Controllers.Field;

/**
 * A simple model of a fox. Foxes age, move, eat rabbits, and die.
 * 
 * @author David J. Barnes and Michael Kolling
 * @version 2002-04-11
 */
public class Fox extends Animal {
    // Characteristics shared by all foxes (static fields).

    // The age at which a fox can start to breed.
    private static final int BREEDING_AGE = 10;
    // The age to which a fox can live.
    private static final int MAX_AGE = 150;
    // The likelihood of a fox breeding.
    private static final double BREEDING_PROBABILITY = 0.09;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 3;
    // The food value of a single rabbit. In effect, this is the
    // number of steps a fox can go before it has to eat again.
    private static final int RABBIT_FOOD_VALUE = 4;
    // A shared random number generator to control breeding.
    private static final Random rand = new Random();

    // Individual characteristics (instance fields).
    private Field currentField;

    /**
     * Create a fox. A fox can be created as a new born (age zero and not hungry) or
     * with random age.
     * 
     * @param randomAge If true, the fox will have random age and hunger level.
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
     * This is what the fox does most of the time: it hunts for rabbits. In the
     * process, it might breed, die of hunger, or die of old age.
     */
    public void hunt(Field updatedField, List<Actor> newFoxes) {
        incrementAge();
        incrementHunger();
        if (isActive()) {
            // New foxes are born into adjacent locations.

            giveBirth(newFoxes);
           
            // Move towards the source of food if found.
            Location newLocation = findFood(currentField, location);
            if (newLocation == null) { // no food found - move randomly
                newLocation = updatedField.freeAdjacentLocation(location);
            }
            if (newLocation != null) {
                setLocation(newLocation);
                updatedField.place(this, newLocation);
            } else {
                // can neither move nor stay - overcrowding - all locations taken
                setActive(false);
            }
        }
    }

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
     * Increase the age. This could result in the fox's death.
     */
    @Override
    public void incrementAge() {
        setAge(getAge()+1);
        if (getAge() > MAX_AGE) {
            setActive(false);
        }
    }

    /**
     * Make this fox more hungry. This could result in the fox's death.
     */
    private void incrementHunger() {
        setFoodLevel(getFoodLevel()-1);
        if (getFoodLevel() <= 0) {
            setActive(false);
        }
    }

    /**
     * Tell the fox to look for rabbits adjacent to its current location.
     * 
     * @param field    The field in which it must look.
     * @param location Where in the field it is located.
     * @return Where food was found, or null if it wasn't.
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
        hunt( updatedField, newAnimals);
    }
    
    @Override
    protected boolean canBreed() {
        return getAge() >= BREEDING_AGE;
    }

    @Override
    public int getBreedingAge() {
        // TODO Auto-generated method stub
        return this.BREEDING_AGE;
    }
    
    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "Raposa";
    }

}