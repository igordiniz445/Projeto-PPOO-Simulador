package Core;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;
import java.awt.Color;
import java.net.SocketTimeoutException;
import java.util.HashMap;

import Actors.*;
import Controllers.Field;
import Controllers.SeasonsController;
import View.AnimatedView;
import Utils.*;


/*
 * Classe responsável pelo encapsulamento de todo o projeto de 
 * software da simulação. Responsável pela instanciação da simulação
 * em si e inicio dos procedimentos e rotinas de execução.
 * 
 * @author David J. Barnes and Michael Kolling
 * @version 2002-04-09
 */
public class Simulator{

    /**
     * Valor padrão de largura do campo de simulação.
     */
    private static final int DEFAULT_WIDTH = 100;

    /**
     * Valor de profundidade padrão do campo de simulação.
     */
    private static final int DEFAULT_DEPTH = 100;

    /**
     * Valor da probabilidade de uma raposa ser criada em qualquer posição 
     * do campo de simualação.
     */
    private static final double FOX_CREATION_PROBABILITY = 0.02;

    /**
     * Valor da probabilidade do coelho ser criado em qualquer posição 
     * do campo de simulação.
     */
    private static final double RABBIT_CREATION_PROBABILITY = 0.08;

    // The probability that a Huntter will be created in any given grid position.
    /**
     * Valor da probabilidade do caçador ser criado em qualquer posição 
     * do campo de simulação.
     */
    private static final double HUNTER_CREATION_PROBABILITY = 0.998;

    /**
     * Representa se a simulação encontra-se em um estado pausado.
     */
    private static boolean isSimulationPaused = true;

    /**
     * Representa a ação de avançar um passo no processo
     * de simulação.
     */
    public static boolean nextFlag = false;

    // private static boolean canRunOneStep = false;

    /**
     * Lisat de atores presenets na simulação.
     */
    private List<Actor> actors;

    /**
     * Lista de animais que nasceram.
     */
    private List<Actor> newActors;

    /**
     * Objeto do campo da simulação.
     */
    private Field field;
    
    /**
     * Uma segunda instancia de objeto de campo da simulação
     * utilizado para armazenar o próximo passo do processo
     * de simulação.
     */
    private Field updatedField;

    /**
     * Representa o passo atual do processo de simulação.
     */
    private int step;

    /**
     * Interface gráfica do processo de simulação.
     */
    private SimulatorView view;

    /**
     * Representa o limite máximo inicial de comida presente na simulção.
     */
    private static final int FOOD_UPPER_BOUND = 5000000;

    /**
     * Abstrai a velocide de execução da visualização da simualção.
     */
    private static int speed;

    /**
     * Representa o limite mínimo inicial de comida presente na simulação.
     */
    private static final int FOOD_LOWER_BOUND = 10000;

    /**
     * Armazena condições que podem ser definidas como fatores variavéis 
     * da simulação.
     */
    private static Map<String, Integer> conditions;

    /**
     * Construct a simulation field with default size.
     * Construtor do objeto Simulator com parametros de
     * largura e profundidade padrões.
     */
    public Simulator() {

        this(DEFAULT_DEPTH, DEFAULT_WIDTH);
        this.initConditions();
    }

    /**
     * Método responsável pela definição de condições variaveis ao ambiente
     * da simulação. Este atributo conditions pode ser usado para alterar
     * condições de execução em qualquer instancia deste modelo.
     */
    private void initConditions() {

        Random rand = new Random();

        conditions = new HashMap<String, Integer>();
        conditions.put("RABBIT_FOOD_LEVEL", rand.nextInt(FOOD_UPPER_BOUND - FOOD_LOWER_BOUND) + FOOD_LOWER_BOUND);

    }

    /**
     * Construtor do objeto simulator com parametros de profundidade e largura.
     * 
     * @param depth Profundidade do campo da simualção, deve ser maior que zero.
     * @param width Largura do campo da simualção, deve ser maior que zero.
     */
    public Simulator(int depth, int width) {

        if (width <= 0 || depth <= 0) {

            System.out.println("The dimensions must be greater than zero.");
            System.out.println("Using default values.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;

        }

        this.initConditions();
        actors = new ArrayList<Actor>();
        newActors = new ArrayList<Actor>();
        field = new Field(depth, width);
        updatedField = new Field(depth, width);

        // Cria uma view representando o estado da localização de cada elemento presente no processo da simulação.
        view = new AnimatedView(depth, width);
        view.setColor(Fox.class, Color.blue);
        view.setColor(Rabbit.class, Color.orange);
        view.setColor(Hunter.class, Color.red);

        // Setup a valid starting point.
        reset();

    }

    /**
     * Método modificador responsável pela atualização de qualquer
     * uma condições da simulação.
     * 
     * @param condition Uma string que representa a chave da condição.
     * @param value Um valor para tal condição.
     */
    public static void updateConditions(String condition, int value) {

        Simulator.conditions.replace(condition, value);

    }

    /**
     * Método acessador para o estado das condições de ambiente
     * de simulação.
     * 
     * @param condition Uma string representando a chave da condição.
     * @return int O valor da condição de simulação buscada.
     */
    public static int getCondition(String condition) {

        return Simulator.conditions.get(condition);

    }

    /**
     * Executa o processo de simulação por um longo periodo pré definido como 800 passos.
     */
    public void runLongSimulation() { simulate(800); }

    /**
     * Executa a simulação a partir de uma quantidade informada de número de passos.
     * Tal execução para depois de uma das condições de existencia ser alcançada
     * ou se o número de passos for alcançado.
     * 
     * @param numSteps Quantidade de passos.
     */
    public void simulate(int numSteps) {

        for (int step = 1; step <= numSteps && view.isViable(field); step++) {

            if(!isSimulationPaused) {

                try {

                    TimeUnit.MILLISECONDS.sleep(speed);

                } catch (InterruptedException e) {

                    e.printStackTrace();

                }
                
                this.defineSeason(step);
                simulateOneStep();

            } else {

                if (Simulator.nextFlag) {
                    
                    Simulator.nextFlag = !Simulator.nextFlag;
                    this.defineSeason(step);
                    simulateOneStep();

                } else {

                    step -= 1;

                }

            }
        }
    }

    /**
     * Método responsável por solicitar ao controlador de estação
     * uma estação atual para a simulação. A definição de uma esta
     * ção atual impacta diretamente nas taxas de reprodução e alimentação.
     * 
     * @param step Passo do processo de simulação.
     */
    private void defineSeason(int step) {

        if(step <= 200 ) {

            SeasonsController.definirPrimavera();
            view.updateSeasonField("Primavera");
            updateConditionsByPeriod(1, 5);
            
        } else if(step > 200 && step <= 400) {

            SeasonsController.definirVerao();
            view.updateSeasonField("Verão");
            updateConditionsByPeriod(2, 4);
            
        } else if (step > 400 && step <= 600) {

            SeasonsController.definirOutono();
            view.updateSeasonField("Outono");
            updateConditionsByPeriod(6, 2);

        } else {

            SeasonsController.definirInverno();
            view.updateSeasonField("Inverno");
            updateConditionsByPeriod(10, 3);

        }

    }

    /**
     * Método responsável pela correlação entre estação e 
     * condição de simulação nivél de alimentos disponíveis.
     * 
     * @param days Passo do processo de simulação.
     * @param adjustValue Taxa de ajuste
     */
    private void updateConditionsByPeriod(int days, int adjustValue) {
        
        
        Simulator.updateConditions("RABBIT_FOOD_LEVEL", 
        
            (step % days == 0) ? 
                Simulator.getCondition("RABBIT_FOOD_LEVEL") + FOOD_LOWER_BOUND * adjustValue : 
                Simulator.getCondition("RABBIT_FOOD_LEVEL") - FOOD_LOWER_BOUND * adjustValue  

        );

        // int updated = Simulator.getCondition("RABBIT_FOOD_LEVEL");

        // if ((Simulator.getCondition("RABBIT_FOOD_LEVEL") % days) == 0) {
        //     updated += 
        // }
        //Simulator.updateConditions("RABBIT_FOOD_LEVEL", updated);

        view.updateFoodLevelField(Simulator.getCondition("RABBIT_FOOD_LEVEL") <= 0 ? 0 : Simulator.getCondition("RABBIT_FOOD_LEVEL"));

    }

    /**
     * Executa os passos da simulação de forma iteratica,
     * recondicionando os estatos dos atores e condições presenetes
     * na simulação.
     * 
     */
    private void simulateOneStep() {
        
        step++;
        newActors.clear();
        
        for (Iterator iter = actors.iterator(); iter.hasNext();) {

            Actor actor = (Actor)iter.next();
            
            if(actor.isActive())
                actor.act(field, updatedField, newActors, actors);
            else
                iter.remove();
        }
        
        // Adicionando novos animais nascidos na lista
        actors.addAll(newActors);

        // Trocando o campo atual por novo campo atualizado ao final do processamento do passo atual.
        Field temp = field;
        field = updatedField;
        updatedField = temp;
        updatedField.clear();

        // Renderiza novo campo atualizado na interface gráfica.
        view.showStatus(step, field);
    }

    /**
     * Reseta todo o processo de simulação ao um novo estado q0.
     */
    public void reset() {
        step = 0;
        actors.clear();
        field.clear();
        updatedField.clear();
        populate(field);

        // Renderiza o campo com estado inicial na tela.
        view.showStatus(step, field);
    }

    /**
     * Método responsável por popular o campo com raposas e coelhos.
     * 
     * @param field Campo de simulação atual.
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

    /**
     * Método responsável por adicionar ator ao campo e a simulação.
     * 
     * @param ator Uma instancia de objeto herdado de Actor.
     * @param row Uma posição X
     * @param col Uma posição Y
     */
    private void adicionaNoMapa(Actor ator, int row, int col){
        actors.add(ator);
        field.place(ator, row, col);
    }

    /**
     * Executa uma pausa na execução dos passos
     * do processo de simulção.
     */
    public static void pauseSimulation(){ isSimulationPaused = true; }

    /**
     * Libera a execução do processo de simulação.
     */
    public static void startSimulation(){ isSimulationPaused = false; }

    /**
     * Método modificador do estado do atributo velocidade.
     * 
     * @param speed Um valor inteiro para a definição da velocidade de execução da simulação.
     */
    public static void setSpeed(int speed){ Simulator.speed = speed; }
    
}
