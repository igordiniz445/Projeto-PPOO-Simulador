package Core;

import java.awt.*;

import Controllers.Field;

public interface SimulatorView {
    
    public void setColor(Class cl, Color color);
    public boolean isViable(Field field);
    public void showStatus(int step, Field field);

}
