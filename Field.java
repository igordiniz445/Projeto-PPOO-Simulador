import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

/**
 * Representa uma grade retangular de posições do campo. Cada posição é capaz de
 * armazenar um único animal.
 * 
 * @author David J. Barnes and Michael Kolling
 * @version 2002-04-09
 */
public class Field {
    private static final Random rand = new Random();

    // A profundidade e largura de um campo
    private int depth, width;
    // Compartimento para animais.
    private Object[][] field;

    /**
     * Representa um campo com as dimenções dadas.
     * 
     * @param depth A profundidade do campo.
     * @param width A largura do campo.
     */
    public Field(int depth, int width) {
        this.depth = depth;
        this.width = width;
        field = new Object[depth][width];
    }

    /**
     * Esvazia o campo.
     */
    public void clear() {
        for (int row = 0; row < depth; row++) {
            for (int col = 0; col < width; col++) {
                field[row][col] = null;
            }
        }
    }

    /**
     * Coloca um animal em um determinado local. Se já haver um animal no local ele
     * será perdido.
     * 
     * @param animal O animal a ser colocado.
     * @param row    Coordenada da linha da local.
     * @param col    Coordenada da coluna da local.
     */
    public void place(Object animal, int row, int col) {
        place(animal, new Location(row, col));
    }

    /**
     * Coloca um animal em um determinado local. Se já haver um animal no local ele
     * será perdido.
     * 
     * @param animal   O animal a ser colocado.
     * @param location Onde colocar o animal.
     */
    public void place(Object animal, Location location) {
        field[location.getRow()][location.getCol()] = animal;
    }

    /**
     * Retorna a localidade de um animal, se tiver.
     * 
     * @param location Localidade no campo.
     * @return O animal em uma localidade, ou nulo se não existir.
     */
    public Object getObjectAt(Location location) {
        return getObjectAt(location.getRow(), location.getCol());
    }

    /**
     * Retorna a localidade de um animal, se tiver.
     * 
     * @param row A linha desejada.
     * @param col A coluna desejada.
     * @return O animal em uma localidade, ou nulo se não existir.
     */
    public Object getObjectAt(int row, int col) {
        return field[row][col];
    }

    /**
     * Gera um local aleatório que é adjacente a um dado local, ou a mesma local. O
     * local retornado estará nos limites válidos do campo.
     * 
     * @param location O local do qual gerar um adjacente.
     * @return Uma local válida na área da grade. Pode ser o mesmo objeto que o
     *         local passado.
     */
    public Location randomAdjacentLocation(Location location) {
        int row = location.getRow();
        int col = location.getCol();

        // Gera um offset de -1, 0 ou +1 para ambos linha e coluna atuais.
        int nextRow = row + rand.nextInt(3) - 1;
        int nextCol = col + rand.nextInt(3) - 1;
        // Checagem para verificar se o local está fora dos limites.
        if (nextRow < 0 || nextRow >= depth || nextCol < 0 || nextCol >= width) {
            return location;
        } else if (nextRow != row || nextCol != col) {
            return new Location(nextRow, nextCol);
        } else {
            return location;
        }
    }

    /**
     * Tenta achar uma local que é adjacente a um dado local. Se não houver, então
     * retorna o local atual se estiver livre. Se não, retorna nulo. O local
     * retornado estará entre os valores limite do campo.
     * 
     * @param location O local do qual gerar um adjacente.
     * @return O local de onde gerar uma adjacente. Um local válido na grade. Pode
     *         ser o mesmo objeto Location passado pelo parametro, ou nulo se todas
     *         as locais estiverem preenchidas.
     */
    public Location freeAdjacentLocation(Location location) {
        Iterator adjacent = adjacentLocations(location);
        while (adjacent.hasNext()) {
            Location next = (Location) adjacent.next();
            if (field[next.getRow()][next.getCol()] == null) {
                return next;
            }
        }
        // Verifica se o local atual está livre.
        if (field[location.getRow()][location.getCol()] == null) {
            return location;
        } else {
            return null;
        }
    }

    /**
     * Gera um iterador sobre uma lista embaralhada de locais adjacentes a um dado
     * local. A lista não incluirá o local em si. Todas as locais estarão na grade.
     * 
     * @param location Um local de onde gerar os adjacents.
     * @return Um iterador que itera sobre dada localização adjacente passada.
     */
    public Iterator adjacentLocations(Location location) {
        int row = location.getRow();
        int col = location.getCol();
        LinkedList locations = new LinkedList();
        for (int roffset = -1; roffset <= 1; roffset++) {
            int nextRow = row + roffset;
            if (nextRow >= 0 && nextRow < depth) {
                for (int coffset = -1; coffset <= 1; coffset++) {
                    int nextCol = col + coffset;
                    // Exclui locais inválidos de um dado local.
                    if (nextCol >= 0 && nextCol < width && (roffset != 0 || coffset != 0)) {
                        locations.add(new Location(nextRow, nextCol));
                    }
                }
            }
        }
        Collections.shuffle(locations, rand);
        return locations.iterator();
    }

    /**
     * @return A profundidade do campo.
     */
    public int getDepth() {
        return depth;
    }

    /**
     * @return A largura do campo.
     */
    public int getWidth() {
        return width;
    }
}
