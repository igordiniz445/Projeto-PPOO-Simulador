package Actors;

import java.util.List;

import Controllers.Field;

public interface Actor {

    public void act(Field field, Field updatedField, List<Actor> newAnimals, List<Actor> animals);
    
    public boolean isActive();

    public void setActive(boolean alive);
    
}
