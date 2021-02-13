package Actors;

import java.util.List;

import Controllers.Field;

public abstract class Actor {

    protected boolean active;

    public abstract void act(Field field, Field updatedField, List<Actor> newAnimals, List<Actor> animals);
    
    public boolean isActive() {
        return this.active;
    }
    public void setActive(boolean alive){
        this.active = alive;
    }
}
