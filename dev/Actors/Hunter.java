package Actors;

import View.Drawable;
import Controllers.Field;
import Controllers.SeasonsController;
import Utils.Location;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Class que representa a entidade Caçador que implementa
 * Actor e Drawable para efetivar sua representação na GUI.
 * 
 * @author Grupo
 * @version 1.0 SNAPSHOT
 */
public class Hunter implements Actor, Drawable {
    
    /**
     * Representa a idade maxima.
     */
    private static final int MAX_AGE = 350;

    /**
     * Representa a probabilidade de reprodução.
     */
    private static final double BREEDING_PROBABILITY = 0.05;

    /**
     * Representa a idade em que começa a reproduzir.
     */
    private static final int BREEDING_AGE = 20;
    
    /**
     * Representa a quantidade máxima de filhos
     */
    private static final int MAX_LITTER_SIZE = 2;

    /**
     * Representa a quantidade de comida.
     */
    private static final int ANIMAL_FOOD_VALUE = 6;

    /**
     * Representa um objeto de randomização de valores do Java
     * para ser usado por diversas tarefas na classe.
     */
    private static final Random rand = new Random();

    /**
     * Representa se o ator está ativo
     * na simulação.
     */
    private boolean active;

    /**
     * Representa a localização em
     * coordenadas no campo.
     */
    private Location location;

    /**
     * Representa o campo de simulação.
     */
    private Field field;

    /**
     * Representa a idade no ciclo de vida do ator.
     */
    private Integer age;

    /**
     * Representa a quantidade de comida.
     */
    private int foodLevel;

    /**
     * Construtor da entidade caçador com base em parametros.
     * 
     * @param randomAge Se gerará atributos randomicos ou não,
     * @param field Campo de simulação
     * @param location Localização no campo de simulação.
     */
    public Hunter(boolean randomAge, Field field, Location location) {

        this.setLocation(location);
        this.setField(field);
        this.setActive(true);

        if (randomAge) {

            setAge(rand.nextInt(MAX_AGE));
            setFoodLevel(rand.nextInt(ANIMAL_FOOD_VALUE));
            
        } else {

            this.age = 0;
            setFoodLevel(ANIMAL_FOOD_VALUE);

        }
    }

    /**
     * IMplementa método de ação do ator para seu ciclo de vida.
     * 
     * @param field Representa o campo de simulação.
     * @param updatedField Representa o campo de simulação atualizado.
     * @param newAnimals Representa os novos animais nascidos.
     * @param animals Representa a lista de animais atores.
     */
    public void act(Field field, Field updatedField, List<Actor> newAnimals ,List<Actor> animals) {
        incrementAge();
        incrementHunger();
        if (isActive()) {
            // novos animais nas localizações adjacentes.
            giveBirth(newAnimals, updatedField);
            // move para a nova localização.
            Location newLocation = goHunt(getField(), location);
            if (newLocation == null) { // Não tem comida, move randomicamente.
                newLocation = updatedField.freeAdjacentLocation(location);
            }
            if (newLocation != null) {
                setLocation(newLocation);
                updatedField.place(this, newLocation);
            } else {
                setActive(false);
            }
        }
    }

    /**
     * Representa o ato de caçar, procurando alvos nas posições adjacentes
     * ocupadas por atores.
     * 
     * @param field Campo de simulação
     * @param location Localização atual do ator no campo de simulação.
     * @return Location Localização do caçador.
     */
    private Location goHunt(Field field, Location location) {

        Iterator adjacentLocations = field.adjacentLocations(location);
        
        while (adjacentLocations.hasNext()) {
            Location where = (Location) adjacentLocations.next();
            Object animal = field.getObjectAt(where);
        
            if (animal instanceof Animal) {
        
                if(animal instanceof Fox){
                    //10% de chance do caçador morrer pra raposa
        
                    if(rand.nextInt(100) < 10){
                        //Tentou caçar a raposa, e morreu
                        setActive(false);
        
                    } else {
                        //era uma raposa, comeu a raposa
                        return eatAnimal(animal, where); 
                    }

                } else {
                    //era um coelho, comeu o coelho
                    return eatAnimal(animal, where);
                }

            }

        }

        return null;
    }
    
    /**
     * Representa o ato de se alimentar de outro animal na
     * posição adjacente localizada.
     * 
     * @param animal Animal alvo.
     * @param where Localização em coordenadas no campo de simulação.
     * @return Location Nova localização no campo de simulação.
     */
    private Location eatAnimal(Object animal, Location where) {

        Animal presa = (Animal) animal;

        if (presa.isActive()) {

            presa.setActive(false);
            setFoodLevel(ANIMAL_FOOD_VALUE);
            return where;

        }
        
        return where;

    }

    /**
     * Representa a geração de filhotes na reprodução e o deslocamento
     * dos mesmos pelo campo de simulação.
     * 
     * @param newHunters Novos caçadores nascidos.
     * @param updatedField Campo de simulação atualizado.
     */
    private void giveBirth(List<Actor> newHunters, Field updatedField) {
        int births = breed();
        for(int b = 0; b < births; b++){
            Location loc = updatedField.randomAdjacentLocation(location);
            Hunter newHunter = new Hunter(false, this.field, loc);
            newHunters.add(newHunter);
        }
    }

    /**
     * Retorna a quantidade de filhos no processo de reprodução.
     * 
     * @return int Quantidade de filhos no processo de reprodução.
     */
    private int breed() {

        int births = 0;

        if (canBreed() && rand.nextDouble() <= BREEDING_PROBABILITY) {
            births = rand.nextInt(MAX_LITTER_SIZE * (SeasonsController.getCurrentSeason().getBreedingAdjust() + 1));
        }

        return births;

    }

    /**
     * Avalia possibilidade de reprodução.
     * 
     * @return true Se caçador está na idade em seu ciclo de vida para se reproduzir.
     */
    protected boolean canBreed() { return getAge() >= BREEDING_AGE; }

    /**
     * Método para incrementar a idade do caçador em seu
     * ciclo de vida.
     */
    private void incrementAge() {
        setAge(getAge()+1);
        if (getAge() > MAX_AGE) {
            setActive(false);
        }
    }

    /**
     * Método acessador do nível de comida.
     * 
     * @return int Nível de comida.
     */
    protected int getFoodLevel() { return this.foodLevel; }

    /**
     * Método modificador do nível de comida.
     * 
     * @param foodLevel Nível de comida.
     */
    protected void setFoodLevel(int foodLevel) { this.foodLevel = foodLevel; }

    /**
     * Método de incremento da necessidade de se alimentar.
     */
    private void incrementHunger() {
        setFoodLevel(getFoodLevel() -1 - SeasonsController.getCurrentSeason().getHungerAdjust());
        if (getFoodLevel() <= 0) {
            setActive(false);
        }
    }

    /**
     * Método modificador do atributo campo de simulação.
     * 
     * @param field Campo de simulação.
     */
    public void setField(Field field) { this.field = field; }

    /**
     * Método acessador do atributo idade.
     * 
     * @return int Valor da idade no ciclo de vida.
     */
    public Integer getAge(){ return this.age; }

    /**
     * Método modificador da idade.
     * 
     * @param age Valor da idade no ciclo de vida.
     */
    public void setAge(Integer age){ this.age = age; }

    /**
     * Método acessador do atributo campo de simulalção.
     * 
     * @return Field Campo de simulação.
     */
    public Field getField() { return this.field; }

    /**
     * Método modificador do atributo localização.
     * 
     * @param location Localização por coordenada X e Y.
     */
    public void setLocation(Location location) { this.location = location; }

    /**
     * Método acessador do atributo Localiazção.
     * @return Location Objeto com coordenadas de localização.
     */
    public Location getLocation() { return this.location; }

    /**
     * Avalia se o ator ainda está ativo na simulação.
     * 
     * @return true se o ator estiver ativo na simulação.
     */
    public boolean isActive() { return this.active; }

    /**
     * Método modificador do atributo ativo.
     * 
     * @param active Booleando se o ator estiver ativo na simulação.
     */
    public void setActive(boolean active) { this.active = active; }

    public void draw() {}

}
