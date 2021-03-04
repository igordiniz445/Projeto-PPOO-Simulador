package Core;

/**
 * Classe responsável pela inicialização das rotinas de simulação.
 * Sua única serventia é ser chamado pela JVM e instanciar um Simulator
 * responsável por todo o encapsulamento do projeto.
 * 
 * @author Grupo
 * @version 1.0 SNAPSHOT
 */
public class Principal {

  /**
   * Método chamado pela JVM para execução do código.
   * 
   * @param args Argumentos via cli.
   */
  public static void main(String[] args) {

    Simulator simulator = new Simulator();
    simulator.runLongSimulation();
    //simulator.simulate();

  }

}
