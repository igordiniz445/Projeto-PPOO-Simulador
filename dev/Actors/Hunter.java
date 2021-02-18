package Actors;

import View.Drawable;
import Controllers.Field;
import Utils.Location;

import java.util.List;
import java.util.Random;

public class Hunter implements Actor, Drawable {
    
    private static final Random RAND = new Random();
    private boolean active;
    private Location location;
    private Field field;

    public Hunter(Field field, boolean active, Location location) {

        this.setLocation(location);
        this.setField(field);
        this.setActive(active);

    }

    public void act(Field field, Field updatedField, List<Actor> newAnimals ,List<Actor> animals) {

    }

    public void setField(Field field) { this.field = field; }

    public Field getField() { return this.field; }

    public void setLocation(Location location) { this.location = location; }

    public Location getLocation() { return this.location; }

    public boolean isActive() { return this.active; }

    public void setActive(boolean active) { this.active = active; }

    public void draw() {}



}
