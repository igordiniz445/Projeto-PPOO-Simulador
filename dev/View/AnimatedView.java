
package View;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.HashMap;

import Controllers.*;
import Core.SimulatorView;
import Core.Simulator;

/**
 * A graphical view of the simulation grid. The view displays a colored
 * rectangle for each location representing its contents. It uses a default
 * background color. Colors for each type of species can be defined using the
 * setColor method.
 * 
 * @author David J. Barnes and Michael Kolling
 * @version 2002-04-23
 */
public class AnimatedView extends JFrame implements SimulatorView {
    // Colors used for empty locations.
    private static final Color EMPTY_COLOR = Color.white;
    // Color used for objects that have no defined color.
    private static final Color UNKNOWN_COLOR = Color.gray;

    private final String STEP_PREFIX = "Step: ";
    private final String POPULATION_PREFIX = "Population: ";
    private JPanel buttonPannel;
    private JLabel stepLabel, population, foodLevel, seasonLabel, textSpeed;
    private JButton startButton, pauseButton, forwardButton;
    private FieldView fieldView;
    private JTextArea speed;

    // A map for storing colors for participants in the simulation
    private HashMap colors;
    // A statistics object computing and storing simulation information
    private FieldStats stats;

    /**
     * Create a view of the given width and height.
     */
    public AnimatedView(int height, int width) {

        stats = new FieldStats();
        colors = new HashMap();

        setTitle("Fox and Rabbit Simulation");
        startButton = new JButton("Iniciar");
        pauseButton = new JButton("Pausar");
        forwardButton = new JButton("Avançar");
        foodLevel = new JLabel();
        seasonLabel = new JLabel();
        textSpeed = new JLabel ("Digite a velocidade de simulação: ");
        speed = new JTextArea();

        stepLabel = new JLabel(STEP_PREFIX, JLabel.CENTER);
        population = new JLabel(POPULATION_PREFIX, JLabel.CENTER);

        setLocation(100, 50);

        fieldView = new FieldView(height, width);

        Container contents = getContentPane();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        contents.add(CreateButtonsPannel(), BorderLayout.EAST);
        contents.add(stepLabel, BorderLayout.NORTH);
        contents.add(fieldView, BorderLayout.CENTER);
        contents.add(population, BorderLayout.SOUTH);
        pack();
        setVisible(true);

    }

    /**
     * Cria menu lateral com botões e indicadores;
     */

    public JPanel CreateButtonsPannel(){
        buttonPannel = new JPanel();
        buttonPannel.add(startButton, BorderLayout.LINE_START);
        buttonPannel.add(pauseButton, BorderLayout.AFTER_LAST_LINE);
        buttonPannel.add(forwardButton, BorderLayout.AFTER_LINE_ENDS);
        buttonPannel.add(foodLevel, BorderLayout.PAGE_END);
        buttonPannel.add(seasonLabel, BorderLayout.LINE_END);
        buttonPannel.add(textSpeed, BorderLayout.LINE_START);
        buttonPannel.add(speed, BorderLayout.LINE_START);
        startButton.setEnabled(false);
        speed.addKeyListener(new KeyListener(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    e.consume();
                    Simulator.setSpeed(Integer.parseInt(speed.getText()));
                    startButton.setEnabled(true);
                }
            }

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
        });

        // speed.addActionListener(
        //     new ActionListener(){

        //         @Override
        //         public void actionPerformed(ActionEvent e){
        //             Simulator.setSpeed(Integer.parseInt(speed.getText()));
        //         }
        //     }
        // );

        startButton.addActionListener(
            new ActionListener(){
                
                @Override
                public void actionPerformed(ActionEvent e){
                    
                    Simulator.startSimulation();
                    
                }
            }
        );

        pauseButton.addActionListener(
            new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e){

                    Simulator.pauseSimulation();

                }
            }
        );
        forwardButton.addActionListener(
            new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e){
                    
                    Simulator.nextFlag = true;

                }
            }
        );
        return buttonPannel;
    }

    public void updateFoodLevelField(int level) { this.foodLevel.setText(level + " unidades de comida.");}

    public void updateSeasonField(String season) { this.seasonLabel.setText(season); }

    /**
     * Define a color to be used for a given class of animal.
     */
    public void setColor(Class animalClass, Color color) {
        colors.put(animalClass, color);
    }

    /**
     * Define a color to be used for a given class of animal.
     */
    private Color getColor(Class animalClass) {
        Color col = (Color) colors.get(animalClass);
        if (col == null) {
            // no color defined for this class
            return UNKNOWN_COLOR;
        } else {
            return col;
        }
    }

    /**
     * Show the current status of the field.
     * 
     * @param step  Which iteration step it is.
     * @param stats Status of the field to be represented.
     */
    public void showStatus(int step, Field field) {
        if (!isVisible())
            setVisible(true);

        stepLabel.setText(STEP_PREFIX + step);

        stats.reset();
        fieldView.preparePaint();

        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                Object animal = field.getObjectAt(row, col);
                if (animal != null) {
                    stats.incrementCount(animal.getClass());
                    fieldView.drawMark(col, row, getColor(animal.getClass()));
                } else {
                    fieldView.drawMark(col, row, EMPTY_COLOR);
                }
            }
        }
        stats.countFinished();

        population.setText(POPULATION_PREFIX + stats.getPopulationDetails(field));
        fieldView.repaint();
    }

    /**
     * Determine whether the simulation should continue to run.
     * 
     * @return true If there is more than one species alive.
     */
    public boolean isViable(Field field) {
        return stats.isViable(field);
    }

    /**
     * Provide a graphical view of a rectangular field. This is a nested class (a
     * class defined inside a class) which defines a custom component for the user
     * interface. This component displays the field. This is rather advanced GUI
     * stuff - you can ignore this for your project if you like.
     */
    private class FieldView extends JPanel {
        private final int GRID_VIEW_SCALING_FACTOR = 8;

        private int gridWidth, gridHeight;
        private int xScale, yScale;
        Dimension size;
        private Graphics g;
        private Image fieldImage;

        /**
         * Create a new FieldView component.
         */
        public FieldView(int height, int width) {
            gridHeight = height;
            gridWidth = width;
            size = new Dimension(0, 0);
        }

        /**
         * Tell the GUI manager how big we would like to be.
         */
        public Dimension getPreferredSize() {
            return new Dimension(gridWidth * GRID_VIEW_SCALING_FACTOR, gridHeight * GRID_VIEW_SCALING_FACTOR);
        }

        /**
         * Prepare for a new round of painting. Since the component may be resized,
         * compute the scaling factor again.
         */
        public void preparePaint() {
            if (!size.equals(getSize())) { // if the size has changed...
                size = getSize();
                fieldImage = fieldView.createImage(size.width, size.height);
                g = fieldImage.getGraphics();

                xScale = size.width / gridWidth;
                if (xScale < 1) {
                    xScale = GRID_VIEW_SCALING_FACTOR;
                }
                yScale = size.height / gridHeight;
                if (yScale < 1) {
                    yScale = GRID_VIEW_SCALING_FACTOR;
                }
            }
        }

        /**
         * Paint on grid location on this field in a given color.
         */
        public void drawMark(int x, int y, Color color) {
            g.setColor(color);
            g.fillRect(x * xScale, y * yScale, xScale - 1, yScale - 1);
        }

        /**
         * The field view component needs to be redisplayed. Copy the internal image to
         * screen.
         */
        public void paintComponent(Graphics g) {
            if (fieldImage != null) {
                g.drawImage(fieldImage, 0, 0, null);
            }
        }
    }
}
