package TopDownShooter.MapEditor;
import TopDownShooter.Utility.Model;
import TopDownShooter.Utility.ActionGenerator;
import TopDownShooter.MapSystem.TileCategory;

import java.util.List;
import java.util.ArrayList;
import java.awt.event.*;

public class SelectionModel extends Model implements ActionGenerator {
    private static SelectionModel instance;
    
    private List<ActionListener> actionListenerList;
    
    private SelectionType selectedType; //Determines what the editor is currently editing (such as map tiles, props, etc.)
    private Enum selectedCategory; //Determines the sub-category of available selections (if the selectionType is TILES, this will represent one of the possible TileCategory(s))
    private int selectedSpriteNumber;
    
    //SelectionType.TILES-only variables:
    private int currentLayer;
    
    private SelectionModel() {
        actionListenerList = new ArrayList<ActionListener>();
        setSelection(SelectionType.TILES, TileCategory.GRASS); //Change to the default selection type (Map)
        selectedSpriteNumber = -1; //Nothing is selected yet
    }
    
    public static SelectionModel getInstance() {
        if (instance == null) {
            instance = new SelectionModel();
        }
        return instance;
    }
    public static void resetInstance() {
        instance = new SelectionModel();
    }
    
    //SelectionType getters and setters
    public SelectionType getSelectionType() {
        return selectedType;
    }
    public void setSelection(SelectionType new_type, Enum new_category) {
        selectedType = new_type;
        selectedCategory = new_category;
        selectedSpriteNumber = -1; //Nothing is selected in this category
        notifyViewers(); //It's possible that the type or category has changed, so all Views need to update their displays
    }
    
    //SelectionCategory getter
    public Enum getSelectionCategory() {
        return selectedCategory;
    }
    
    //SelectionType.TILES-specific methods:
    public int getSelectedSpriteNumber() {
        return selectedSpriteNumber;
    }
    public void setSelectedSpriteNumber(int new_selectedSpriteNumber) {
        selectedSpriteNumber = new_selectedSpriteNumber;
        notifyListeners(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "New Selection"));
    }
    public int getCurrentLayer() {
        return currentLayer;
    }
    public void setCurrentLayer(int newLayer) {
        if (newLayer >= 0 && newLayer < MAX_NUMBER_OF_LAYERS) {
            currentLayer = newLayer;
            notifyViewers();
        }
    }
    
    //ActionGenerator interface methods
    public void addActionListener(ActionListener new_listener) {
        actionListenerList.add(new_listener);
    }
    public void notifyListeners(ActionEvent event) { //Notify the SelectionButton(s) that a new selection has been made
        for (ActionListener listener : actionListenerList) {
            listener.actionPerformed(event);
        }
    }
    
    public static final int MAX_NUMBER_OF_LAYERS = 6;
}
