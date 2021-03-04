package Actors;

import java.util.List;

import Controllers.Field;
import Utils.Location;

/**
 * Classe responsável pela implementação da entidade
 * animal, herdada de Ator.
 * 
 * @author Grupo
 * @version 1.0 SNAPSHOT
 */
public abstract class Animal implements Actor {
    
    /**
     * Representa se o animal está na condição
     * ativo.
     */
    protected boolean active;

    /**
     * Representa a idade do animal.
     */
    protected int age;

    /**
     * Representa o objeto de coordenadas
     * do animal no campo da simulação.
     */
    protected Location location;

    /**
     * Representa a quantidade de comida.
     */
    protected int foodLevel;

    /**
     * Construtor do objeto animal.
     * Define os parametros aos atributos.
     * 
     * @param age Inteiro que representa a idade.
     * @param alive Booleano que representa condição de ativo ou não ativo.
     * @param location Objeto que representa as coordenadas de localização no campo da simulação.
     */
    public Animal(int age, boolean alive, Location location) {

        super();
        this.age = age;
        this.active = alive;
        this.location = location;

    }

    /**
     * Método modificador do atributo ativo.
     * 
     * @param alive Flag booleana para ativo ou não ativo.
     */
    public void setActive(boolean alive) { this.active = alive; }

    /**
     * Método acessador do atributo active.
     * 
     * @return true Se o animal encontra-se na condição ativo na simulação.
     */
    public boolean isActive() { return this.active; }

    /**
     * Método acessador do atributo idade.
     * 
     * @return int A idade do animal.
     */
    protected int getAge() { return this.age; }

    /**
     * Método modificador do atributo idade.
     * 
     * @param age Um valor inteiro que representa a idade do animal.
     */
    protected void setAge(int age) { this.age = age; }

    /**
     * Método acessador do atributo loacation.
     * 
     * @return Location Uma instancia do objeto location com as coordenadas de localização do animal.
     */
    protected Location getLocation() { return this.location; }
    
    /**
     * Método modificador do atributo location.
     * 
     * @param location Objeto location com as coordenadas de localização do animal no campo de simulação.
     */
    protected void setLocation(Location location) { this.location = location; }

    /**
     * Método acessador do atributo foodLevel.
     * 
     * @return int Nível de comida.
     */
    protected int getFoodLevel() { return this.foodLevel; }

    /**
     * Método modificador do atributo foodLevel.
     * 
     * @param foodLevel Nível de comida.
     */
    protected void setFoodLevel(int foodLevel) { this.foodLevel = foodLevel; }

    /**
     * Método abstrato action para um animal implementar sua ação.
     * 
     * @param fiel Campo de simulação.
     * @param updatedField Campo de simulação atualizado
     * @param newAnimal Lista de novos animais nascidos
     */
    protected abstract void action(Field field, Field updatedField, List<Actor> newAnimals );

    /**
     * Implementação do método act, representa ação do animal.
     * 
     * @param fiel Campo de simulação.
     * @param updatedField Campo de simulação atualizado
     * @param newAnimal Lista de novos animais nascidos
     * @param animals Lista de animais no campo de simulação.
     */
	public void act(Field field, Field updatedField, List<Actor> newAnimals, List<Actor> animals) {
    
        action(field, updatedField, newAnimals);

	}

    /**
     * Método acessador da informação breed
     * @return true Se animal pode se reproduzir
     */
    protected abstract boolean canBreed();

    /**
     * Método acessador da informação breedingAge
     * 
     * @return int Idade em que  o animal pode começar a se preorduzir.
     */
    protected abstract int getBreedingAge();

    /**
     * Método que incrementa idade do animal.
     */
    protected abstract void incrementAge();
}
