package Actors;

import java.util.List;
import java.util.Random;

import Utils.Location;
import Controllers.Field;
import Core.Simulator;

/**
 * Um modelo simples de um coelho, que determina suas açoes como comer, reproduzir e morrer
 * 
 * @author David J. Barnes and Michael Kolling
 * @version 2002-04-11
 */
public class Rabbit extends Animal {
    
    /**
     * Idade para começar a se reproduzir
     */
    private static final int BREEDING_AGE = 5;

    /**
     * Idade maxima de vida
     */
    private static final int MAX_AGE = 50;
    
    /**
     * Chance de se reproduzir
     */
    private static final double BREEDING_PROBABILITY = 0.15;

    /**
     * Numero maximo de reproduções
     */
    private static final int MAX_LITTER_SIZE = 5;

    /**
     * Dias que um coelho pode passar sem comer
     */
    private int CARROT_FOOD_LEVEL = 3;

    /**
     * Variavel pra criar deciçoes aleatórias
     */
    private static final Random rand = new Random();

    
    private Field currentField;

    /**
     * Cria um novo coelho. O coelho pode nascer com idade 0 ou uma idade aleatória
     * 
     * @param randomAge Se verdadeiro, o coelho nasce com idade aleatória.
     * @param field O tabuleiro
     * @param location  A atual localização do coelho
     */
    public Rabbit(boolean randomAge, Field field, Location location) {
        super(0, true, location);
        this.currentField = field;
        if (randomAge) {
            setAge(rand.nextInt(MAX_AGE));
        }
    }

    /**
     * Coelho sempre irá correr, as vezes se alimentar e/ou reproduzir, e sempre irá invelhecer
     * 
     * @param updatedField Campo de simulação
     * @param newRabbits Coelhos Nascidos
     */
    public void run(Field updatedField, List<Actor> newRabbits) {

        incrementAge();
        eatFood();
        if (isActive()) {
            giveBirth(newRabbits, updatedField);

            Location newLocation = updatedField.freeAdjacentLocation(location);
            
            if (newLocation != null) {
                setLocation(newLocation);
                updatedField.place(this, newLocation);
            } else {
            
                setActive(false);
            }
        }
    }

    /**
     * Implementa o ato de se alimentar.
     */
    private void eatFood() {

        if (isActive()) {

            int globalFoodLevel = Simulator.getCondition("RABBIT_FOOD_LEVEL");

            if ((globalFoodLevel) >= 1 && CARROT_FOOD_LEVEL <= 3) {
                if(rand.nextInt(globalFoodLevel) > globalFoodLevel/20){
                    Simulator.updateConditions("RABBIT_FOOD_LEVEL", globalFoodLevel - 1);
                }else{
                    CARROT_FOOD_LEVEL ++;
                }
            } else {
                this.setActive(false);
            }
            
        
        }

    }

    /**
     * Método responsavel por realocar os filhoets em novas
     * posições adjacentes vazias.
     * 
     * @param newRabbits Lista de novos filhotes
     * @param updatedField Campo de simulação atualizado.
     */
    private void giveBirth(List<Actor> newRabbits, Field updatedField) {
        int births = breed();
        for(int b = 0; b < births; b++){
            Location loc = updatedField.randomAdjacentLocation(location);
            Rabbit newRabbit = new Rabbit(false, currentField, loc);
            newRabbits.add(newRabbit);
        }
    }

    /**
     * Increase the age. This could result in the rabbit's death.
     */
    @Override
    public void incrementAge() {
        setAge(getAge()+1);
        if (getAge() > MAX_AGE) {
            setActive(false);
        }
    }

    /**
     * Generate a number representing the number of births, if it can breed.
     * Gera o número de filhotes caso o coelho possa se reproduzir.
     * 
     * @return O numero de filhotes, pode ser zero inclusive.
     */
    private int breed() {
        int births = 0;
        if (canBreed() && rand.nextDouble() <= BREEDING_PROBABILITY) {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
    }

    /**
     * Verifica se o animal esta em idade reprodutiva.
     * 
     * @return true Se o coelho pode reproduzir.
     */
    @Override
    protected boolean canBreed() { return getAge() >= BREEDING_AGE; }

    /**
     * Método modificador do atributo active, caso o coelho esteja
     * ativo na simulação.
     * 
     */
    public void setEaten() {
        setActive(false);
    }

    /**
     * Método modificador do atributo localização do animal.
     * 
     * @param row Linha, a posição em coordenada X.
     * @param col Coluna, a posição em coordenada Y.
     */
    public void setLocation(int row, int col) { this.location = new Location(row, col); }

    /**
     * Sobrescrita do método action para implementar ação do animal.
     * 
     * @param field Campo de simulação.
     * @param updatedField Campo atualizado de simulação.
     * @param newAnimals Lista cmo novos animais nascidos.
     */
    @Override
    public void action(Field field, Field updatedField, List<Actor> newAnimals) {
        run(updatedField, newAnimals);
    }

    /**
     * Representa textualmente o objeto em questão.
     * 
     * @return Strnig Uma representação textual do objeto Coelho.
     */
    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "Coelho";
    }

    /**
     * Sobrescrita do método acessador da idade de reprodução do coelho.
     * 
     * @param int Valor da idade de reprodução do coelho.
     */
    @Override
    public int getBreedingAge() { return this.BREEDING_AGE; }


}
