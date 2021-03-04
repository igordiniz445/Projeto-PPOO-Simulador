package Utils;

/**
 * Representa um objeto de uma estação, condição variante
 * do ambiente de simulação.
 * 
 * @author Igor Otávio
 * @version 1.0 SNAPSHOT
 */
public class Season {

    /**
     * Representa o nome da estação.
     */
    private String seasonName;

    /**
     * Representa a taxa de de ajuste da
     * reprodução.
     */
    private Integer breedingAdjust;

    /**
     * Representa a taxa de ajuste da
     * necessidade de se alimentar
     * de cada animal.
     */
    private Integer hungerAdjust;

    /**
     * Construtor do objeto Season
     * Define atributos com base em parametros.
     * 
     * @param seasonName Nome da estação.
     * @param breedingAdjust Taxa de ajuste da taxa de reprodução.
     * @param hungerAdjust Taxa de ajusta da taxa de alimentação.
     */
    public Season(String seasonName, Integer breedingAdjust, Integer hungerAdjust) {

        this.seasonName = seasonName;
        this.breedingAdjust = breedingAdjust;
        this.hungerAdjust = hungerAdjust;

    }

    /**
     * Método acessador do atributo name da estação.
     * 
     * @return String Nome da estação.
     */
    public String getSeasonName() { return this.seasonName; }

    /**
     * Método modificador do atributo name da estação.
     * 
     * @param seasonName Nome da estação.
     */
    public void setSeasonName(String seasonName) { this.seasonName = seasonName; }

    /**
     * Método acessador do atributo name da breedingAjust.
     * 
     * @return Integer Taxa de ajuste da taxa de reprodução.
     */
    public Integer getBreedingAdjust() {
        return this.breedingAdjust;
    }

    /**
     * Método modificador do atributo name da breedingAjust.
     * 
     * @param breedingAdjust Taxa de ajuste da taxa de reprodução.
     */
    public void setBreedingAdjust(Integer breedingAdjust) { this.breedingAdjust = breedingAdjust; }

    /**
     * Método acessador do atributo name da hungerAjust.
     * 
     * @return Integer Taxa de ajuste da taxa de alimentação.
     */
    public Integer getHungerAdjust() { return this.hungerAdjust; }

    /**
     * Método modificador do atributo name da hungerAjust.
     * 
     * @param hungerAdjust Taxa de ajuste da taxa de alimentação.
     */
    public void setHungerAdjust(Integer hungerAdjust) { this.hungerAdjust = hungerAdjust; }

    /**
     * Sobrescrita do método toString do objeto.
     * 
     * @return String Representação textual do objeto.
     */
    @Override
    public String toString() { return "Current season is: "+this.seasonName; }
    
}
