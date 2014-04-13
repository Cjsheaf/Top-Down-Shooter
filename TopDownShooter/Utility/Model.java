package TopDownShooter.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines the basic methods needed for the Model portion of a
 * Model-View-Controller design patter.
 * 
 * Does not force the implementing class to have a List of Views,
 * which is an implicit requirement. Would be nice to be able to do
 * that.
 */

public class Model {
    private List<View> viewerList = new ArrayList<View>();
    
    public void addViewer(View new_view) {
        viewerList.add(new_view);
    }
    
    /*
     * Whenever any data in the Model changes, all attached Views should be notified
     * so they may update their display accordingly.
     */
    public void notifyViewers() {
        for (View viewer : viewerList) {
            viewer.notifyChange();
        }
    }
}
