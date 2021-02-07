package Actors;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import Controllers.Field;
import Utils.Location;

public abstract class Animal {
    
    protected int age;
    protected boolean alive;
    protected Location location;
    protected int foodLevel;

    public Animal() {}

    public Animal(int age, boolean alive, Location location, int foodLevel) {
        this.age = age;
        this.alive = alive;
        this.location = location;
        this.foodLevel = foodLevel;
    }

    protected int getAge() {
        return this.age;
    }

    protected void setAge(int age) {
        this.age = age;
    }

    public boolean isAlive() {
        return this.alive;
    }

    protected void setAlive(boolean alive) {
        this.alive = alive;
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

    protected abstract void action(Field field, Field updatedField, List<Animal> newAnimals );

	public void act(Field field, Field updatedField, List<Animal> newAnimals, List<Animal> animals) {
        action(field, updatedField, newAnimals);
	}
}
