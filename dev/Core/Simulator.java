package Core;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;
import java.awt.Color;

import Controllers.Field;
import View.AnimatedView;
import Utils.*;
import Actors.*;

/**
 * A simple predator-prey simulator, based on a field containing rabbits and
 * foxes.
 * 
 * @author David J. Barnes and Michael Kolling
 * @version 2002-04-09
 */
public class Simulator {
    // The private static final variables represent
    // configuration information for the simulation.
    // The default width for the grid.
    private static final int DEFAULT_WIDTH = 100;
    // The default depth of the grid.
    private static final int DEFAULT_DEPTH = 100;
    // The probability that a fox will be created in any given grid position.
    private static final double FOX_CREATION_PROBABILITY = 0.02;
    // The probability that a rabbit will be created in any given grid position.
    private static final double RABBIT_CREATION_PROBABILITY = 0.08;
    // The probability that a Huntter will be created in any given grid position.
    private static final double HUNTER_CREATION_PROBABILITY = 0.998;

    // The list of animals in the field
    private List<Actor> actors;
    // The list of animals just born
    private List<Actor> newActors;
    // The current state of the field.
    private Field field;
    // A second field, used to build the next stage of the simulation.
    private Field updatedField;
    // The current step of the simulation.
    private int step;
    // A graphical view of the simulation.
    private SimulatorView view;

    /**
     * Construct a simulation field with default size.
     */
    public Simulator() {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH);
    }

    /**
     * Create a simulation field with the given size.
     * 
     * @param depth Depth of the field. Must be greater than zero.
     * @param width Width of the field. Must be greater than zero.
     */
    public Simulator(int depth, int width) {
        if (width <= 0 || depth <= 0) {
            System.out.println("The dimensions must be greater than zero.");
            System.out.println("Using default values.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }
        actors = new ArrayList<Actor>();
        newActors = new ArrayList<Actor>();
        field = new Field(depth, width);
        updatedField = new Field(depth, width);

        // Create a view of the state of each location in the field.
        view = new AnimatedView(depth, width);
        view.setColor(Fox.class, Color.blue);
        view.setColor(Rabbit.class, Color.orange);
        view.setColor(Hunter.class, Color.red);

        // Setup a valid starting point.
        reset();
    }

    /**
     * Run the simulation from its current state for a reasonably long period, e.g.
     * 500 steps.
     */
    public void runLongSimulation() {
        simulate(500);
    }

    /**
     * Run the simulation from its current state for the given number of steps. Stop
     * before the given number of steps if it ceases to be viable.
     */
    public void simulate(int numSteps) {
        for (int step = 1; step <= numSteps && view.isViable(field); step++) {
            try {
                TimeUnit.MILLISECONDS.sleep(300);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            simulateOneStep();
        }
    }

    /**
     * Run the simulation from its current state for a single step. Iterate over the
     * whole field updating the state of each fox and rabbit.
     */
    public void simulateOneStep() {
        step++;
        newActors.clear();

        // let all animals act
        for (Iterator iter = actors.iterator(); iter.hasNext();) {

            Actor actor = (Actor)iter.next();
            
            if(actor.isActive())
                actor.act(field, updatedField, newActors, actors);
            else
                iter.remove();
        }
        // add new born animals to the list of animals
        actors.addAll(newActors);

        // Swap the field and updatedField at the end of the step.
        Field temp = field;
        field = updatedField;
        updatedField = temp;
        updatedField.clear();

        // display the new field on screen
        view.showStatus(step, field);
    }

    /**
     * Reset the simulation to a starting position.
     */
    public void reset() {
        step = 0;
        actors.clear();
        field.clear();
        updatedField.clear();
        populate(field);

        // Show the starting state in the view.
        view.showStatus(step, field);
    }

    /**
     * Populate the field with foxes and rabbits.
     */
    private void populate(Field field) {
        Random rand = new Random();
        field.clear();
        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                if (rand.nextDouble() <= FOX_CREATION_PROBABILITY) {
                    Fox fox = new Fox(true, field, new Location(row, col));
                    adicionaNoMapa(fox, row, col);
                } else if (rand.nextDouble() <= RABBIT_CREATION_PROBABILITY) {
                    Rabbit rabbit = new Rabbit(true, field, new Location(row, col));
                    adicionaNoMapa(rabbit, row, col);
                } else if (rand.nextDouble() > HUNTER_CREATION_PROBABILITY){
                    Hunter hunter = new Hunter(true, field, new Location(row, col));
                    adicionaNoMapa(hunter, row, col);
                }
                // else leave the location empty.
            }
        }
        Collections.shuffle(actors);
    }


    private void adicionaNoMapa(Actor ator, int row, int col){
        actors.add(ator);
        field.place(ator, row, col);
    }
}
