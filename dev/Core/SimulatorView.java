package Core;

import java.awt.*;

import Controllers.Field;

/*
 * Interface para implementação de interface
 * gráfica do simulador.
 * 
 * @author Grupo
 * @version 1.0 SNAPSHOT
 */
public interface SimulatorView {
    
    /**
     * Método responsável por definir uma cor a um ator.
     * 
     * @param cl Uma classe de ator presente na simulação.
     * @param color Uma cor.
     */
    public void setColor(Class cl, Color color);

    /**
     * Método responsável por verificar condições
     * de existencia da simulação.
     * 
     * @param field Objeto do campo.
     * @return true Se deverá ser prosseguida a simualação.
     */
    public boolean isViable(Field field);

    /**
     * Método de renderização do campo de simulação na interface
     * gráfica.
     * 
     * @param step Passo do processo de simulação.
     * @param field Campo atualizado da simulação.
     */
    public void showStatus(int step, Field field);

    /**
     * Método responsável pela renderização das informações
     * de condição da estação atual na interface gráfica.
     * 
     * @param season Objeto da estação atual.
     */
    public void updateSeasonField(String season);

    /**
     * Método responsável pela renderização informações sobre 
     * quantiade de comida no passo do processo da simulação.
     * 
     * @param level Quantidade de comida,
     */
    public void updateFoodLevelField(int level);

}
