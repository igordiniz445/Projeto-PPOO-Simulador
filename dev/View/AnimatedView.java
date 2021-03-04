
package View;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.HashMap;

import Controllers.*;
import Core.SimulatorView;
import Core.Simulator;

/**
 * Uma visão gráfica da grade de simulação. A vista mostra um colorido
 * retângulo para cada local que representa seu conteúdo. Ele usa um padrão
 * cor de fundo. As cores para cada tipo de espécie podem ser definidas usando o
 * método setColor
 * 
 * @author Grupo
 * @version 1.0 SNAPSHOT
 */
public class AnimatedView extends JFrame implements SimulatorView {

    /**
     * Cor usada para posição vazia.
     */
    private static final Color EMPTY_COLOR = Color.white;
    
    /**
     * Cor usasda para objetos sem cor definida.
     */
    private static final Color UNKNOWN_COLOR = Color.gray;

    /**
     * Prefixo textual na representação do passo atual na GUI.
     */
    private final String STEP_PREFIX = "Step: ";

    /**
     * Prefixo textual na representação da população na GUI.
     */
    private final String POPULATION_PREFIX = "Population: ";

    /**
     * Painel de botões de controle da simuação.
     */
    private JPanel buttonPannel;
    
    /**
     * Labels textuais de informações.
     */
    private JLabel stepLabel, population, foodLevel, seasonLabel, textSpeed;

    /**
     * Botões de controle da simulação.
     */
    private JButton startButton, pauseButton, forwardButton;

    /**
     * Visualização do campo da simulação.
     */
    private FieldView fieldView;

    /**
     * TextArea da informação sobre velocidade.
     */
    private JTextArea speed;

    
    /**
     * Um hashmap com as cores dos participantes da simulção.
     */
    private HashMap colors;

    /**
     * Estatisticas para serem apresentadas.
     */
    private FieldStats stats;

    /**
     * Construtor sobre parametros de largura e profundidade.
     * 
     * @param height Altura do campo de simulação.
     * @param width Largura do campo de simulação.
     */
    public AnimatedView(int height, int width) {

        stats = new FieldStats();
        colors = new HashMap();

        setTitle("Fox and Rabbit Simulation");
        startButton = new JButton("Iniciar");
        pauseButton = new JButton("Pausar");
        forwardButton = new JButton("Avançar");
        foodLevel = new JLabel("Comida dos Coelhos: ");
        seasonLabel = new JLabel("Estação atual: ");
        textSpeed = new JLabel ("Digite a velocidade de simulação: ");
        speed = new JTextArea();
        speed.setMaximumSize(new Dimension(10,5));

        stepLabel = new JLabel(STEP_PREFIX, JLabel.CENTER);
        population = new JLabel(POPULATION_PREFIX, JLabel.CENTER);

        setLocation(100, 50);

        fieldView = new FieldView(height, width);

        Container contents = getContentPane();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        contents.add(RightPannel(), BorderLayout.EAST);
        contents.add(stepLabel, BorderLayout.NORTH);
        contents.add(fieldView, BorderLayout.CENTER);
        contents.add(population, BorderLayout.SOUTH);
        pack();
        setVisible(true);

    }

    /**
     * Cria menu lateral com botões e indicadores;
     * 
     * @return JPanel Um painel com os botões.
     */
    public JPanel RightPannel() {
        JPanel rightPannel = new JPanel();
        rightPannel.setLayout(new GridLayout(6,1));
        rightPannel.add(textSpeed);
        rightPannel.add(speed);
        rightPannel.add(CreateButtonsPannel());
        rightPannel.add(FoodPannel());
        rightPannel.add(SeasonPannel());
        return rightPannel;
    }

    /**
     * Constroi um painel com informações de comida.
     * @return JPanel Um painel com as informações de comida.
     */
    public JPanel FoodPannel() {

        JPanel labePanel = new JPanel();
        labePanel.add(foodLevel);
        return labePanel;

    }

    /**
     * Constroi um painel com informações de estação.
     * @return JPanel Um painel com as informações de estação.
     */
    public JPanel SeasonPannel() {

        JPanel seasonPanel = new JPanel();
        seasonPanel.add(seasonLabel);
        return seasonPanel;

    }

    /**
     * Constroi um painel com botões de controle.
     * @return JPanel Um painel com os botões de controle.
     */
    public JPanel CreateButtonsPannel() {

        buttonPannel = new JPanel();

        buttonPannel.add(startButton, BorderLayout.LINE_START);
        buttonPannel.add(pauseButton, BorderLayout.AFTER_LAST_LINE);
        buttonPannel.add(forwardButton, BorderLayout.AFTER_LINE_ENDS);
        //buttonPannel.add(foodLevel, BorderLayout.PAGE_END);
        //buttonPannel.add(seasonLabel, BorderLayout.LINE_END);
        
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

    /**
     * Atualiza a informação da quantidade de comida na simulação.
     *
     * @param level Valor da quantidade de comida.
     */
    public void updateFoodLevelField(int level) { this.foodLevel.setText("Comida para coelho: "+level );}

    /**
     * Atualiza a informação da da estação atual na simulação.
     *
     * @param season Estação atual.
     */
    public void updateSeasonField(String season) { this.seasonLabel.setText("Estação atual: "+season); }

    /**
     * Define a cor para ser usada por cada animal.
     * 
     * @param animalClass Classe do animal.
     * @param color Cor a ser usada.
     */
    public void setColor(Class animalClass, Color color) { colors.put(animalClass, color); }

    /**
     * Acessa a cor de um animal.
     * 
     * @param animalClass A classe do animal cujo a cor vai ser buscada.
     * @return Color A cor do animal.
     */
    private Color getColor(Class animalClass) {

        Color col = (Color) colors.get(animalClass);

        return (col == null) ? UNKNOWN_COLOR : col;
        // if (col == null) {
        //     return UNKNOWN_COLOR;
        // } else {
        //     return col;
        // }
    }

    /**
     * Exibe as informações do campo de simulação atual.
     * 
     * @param step  Passo atual do processo de simulação..
     * @param field Campo de simulação atual.
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
     * Determina se a simualação deev continuar.
     * 
     * @return true Se a condição de existencia ainda não foi alcançada.
     */
    public boolean isViable(Field field) { return stats.isViable(field); }

    /**
     * Fornece uma visão gráfica de um campo retangular. Esta é uma classe aninhada (um
     * classe definida dentro de uma classe) que define um componente personalizado para o usuário
     * interface. Este componente exibe o campo. Esta é uma GUI bastante avançada
     * stuff - você pode ignorar isso para o seu projeto, se desejar.
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
