package TopDownShooter.Utility;

/**
 * A custom interface for creating classes which notify ActionListeners about some custom event.
 * 
 * There may very well be an official interface somewhere in the Java API that does this, but I don't know about it.
 */

import java.awt.event.*;

public interface ActionGenerator {
    void addActionListener(ActionListener listener);
    
    void notifyListeners(ActionEvent event);
}
