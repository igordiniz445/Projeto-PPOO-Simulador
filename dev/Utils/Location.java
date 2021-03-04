package Utils;

/**
 * Representa um objeto com coordenadas em um sistema
 * retangular de grid, o campo da simulação.
 * 
 * @author David J. Barnes and Michael Kolling
 * @version 2002-04-09
 */
public class Location {

    /**
     * Linha, repersenta uma coordenada X.
     */
    private int row;

    /**
     * Coluna, repersenta uma coordenada Y.
     */
    private int col;

    /**
     * Construtor do objeto que repersenta
     * as coordenadas X e Y.
     * 
     * @param row A linha, coordenada X.
     * @param col A coluna, coordenada Y.
     */
    public Location(int row, int col) {

        this.row = row;
        this.col = col;

    }

    /**
     * Implementa operador de equidade de objeto.
     */
    public boolean equals(Object obj) {

        if (obj instanceof Location) {

            Location other = (Location) obj;

            return row == other.getRow() && col == other.getCol();

        } else {

            return false;

        }
    }

    /**
     * Return a string of the form row,column
     * Retorna uma string no formato linha,coluna.
     * 
     * @return String Uma strnig representando as coordenadas de localização.
     */
    public String toString() { return row + "," + col; }

    /**
     * use os 16 bits superiores para o valor da linha e os inferiores para a coluna. Exceto
     * para grades muito grandes, isso deve fornecer um código hash exclusivo para cada (linha, coluna)
     * 
     * @return int Um valor inteiro do calculo.
     */
    public int hashCode() { return (row << 16) + col; }

    /**
     * Método acessador do atributo linha, coordenada X.
     * 
     * @return A linha, coordenada X.
     */
    public int getRow() { return row; }

    /**
     * Método acessador do atributo coluna, coordenada Y.
     * 
     * @return A linha, coordenada Y.
     */
    public int getCol() { return col; }
    
}
