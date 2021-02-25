package Actors;

import View.Drawable;
import Controllers.Field;
import Utils.Location;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Hunter implements Actor, Drawable {
    
    private static final int MAX_AGE = 350;
    private static final double BREEDING_PROBABILITY = 0.05;
    private static final int BREEDING_AGE = 20;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 3;


    private static final Random rand = new Random();
    private boolean active;
    private Location location;
    private Field field;
    private Integer age;

    public Hunter(boolean active, Field field, Location location) {

        this.setLocation(location);
        this.setField(field);
        this.setActive(active);
        this.age = 0;
    }

    public void act(Field field, Field updatedField, List<Actor> newAnimals ,List<Actor> animals) {
        incrementAge();
        if (isActive()) {
            // New foxes are born into adjacent locations.
            giveBirth(newAnimals, updatedField);
            // Move towards the source of food if found.
            Location newLocation = goHunt(getField(), location);
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

    private Location goHunt(Field field, Location location) {
        Iterator adjacentLocations = field.adjacentLocations(location);
        while (adjacentLocations.hasNext()) {
            Location where = (Location) adjacentLocations.next();
            Object animal = field.getObjectAt(where);
            if (animal instanceof Animal) {
                Animal presa = (Animal) animal;
                if (presa.isActive()) {
                    presa.setActive(false);
                    return where;
                }
            }
        }
        return null;
    }

    private void giveBirth(List<Actor> newHunters, Field updatedField) {
        int births = breed();
        for(int b = 0; b < births; b++){
            Location loc = updatedField.randomAdjacentLocation(location);
            Hunter newHunter = new Hunter(true, this.field, loc);
            newHunters.add(newHunter);
        }
    }

    

    private int breed() {
        int births = 0;
        Double numeroaleatorio = rand.nextDouble();
        if (canBreed() && numeroaleatorio <= BREEDING_PROBABILITY) {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
    }

    
    protected boolean canBreed() {
        return getAge() >= BREEDING_AGE;
    }

    private void incrementAge() {
        setAge(getAge()+1);
        if (getAge() > MAX_AGE) {
            setActive(false);
        }
    }

    public void setField(Field field) {
        this.field = field;
    }

    public Integer getAge(){ return this.age; }

    public void setAge(Integer age){ this.age = age; }

    public Field getField() { return this.field; }

    public void setLocation(Location location) { this.location = location; }

    public Location getLocation() { return this.location; }

    public boolean isActive() { return this.active; }

    public void setActive(boolean active) { this.active = active; }

    public void draw() {}



}
