package Actors;

import java.util.List;

public abstract class Actor {
    abstract public void act(List<Actor> newActors);
    abstract boolean isActive();
}
