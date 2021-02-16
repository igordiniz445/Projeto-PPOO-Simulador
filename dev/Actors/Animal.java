package Actors;

import java.util.List;

import Controllers.Field;
import Utils.Location;

public abstract class Animal extends Actor {
    
    protected int age;
    protected Location location;
    protected int foodLevel;


    public Animal(int age, boolean alive, Location location) {
        super();
        this.age = age;
        this.active = alive;
        this.location = location;
    }

    protected int getAge() {
        return this.age;
    }

    protected void setAge(int age) {
        this.age = age;
    }

    protected Location getLocation() {
        return this.location;
    }

    protected void setLocation(Location location) {
        this.location = location;
    }

    protected int getFoodLevel() {
        return this.foodLevel;
    }

    protected void setFoodLevel(int foodLevel) {
        this.foodLevel = foodLevel;
    }

    protected abstract void action(Field field, Field updatedField, List<Actor> newAnimals );

	public void act(Field field, Field updatedField, List<Actor> newAnimals, List<Actor> animals) {
        action(field, updatedField, newAnimals);
	}

    protected abstract boolean canBreed();

    protected abstract int getBreedingAge();

    protected abstract void incrementAge();
}
