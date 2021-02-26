package Utils;

public class Season {

    private String seasonName;
    private Integer breedingAdjust;
    private Integer hungerAdjust;

    public Season(String seasonName, Integer breedingAdjust, Integer hungerAdjust) {
        this.seasonName = seasonName;
        this.breedingAdjust = breedingAdjust;
        this.hungerAdjust = hungerAdjust;
    }

    public String getSeasonName() {
        return this.seasonName;
    }

    public void setSeasonName(String seasonName) {
        this.seasonName = seasonName;
    }

    public Integer getBreedingAdjust() {
        return this.breedingAdjust;
    }

    public void setBreedingAdjust(Integer breedingAdjust) {
        this.breedingAdjust = breedingAdjust;
    }

    public Integer getHungerAdjust() {
        return this.hungerAdjust;
    }

    public void setHungerAdjust(Integer hungerAdjust) {
        this.hungerAdjust = hungerAdjust;
    }

    @Override
    public String toString() {
        return "Current season is: "+this.seasonName;
    }
    
}
