package TopDownShooter.MapEditor;
import TopDownShooter.Utility.View;
import TopDownShooter.ArtAssets.MapResources;

import java.awt.*;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.image.*;

public class SelectionPanel extends JPanel implements View {
    private SelectionModel selection;
    private MapResources mapResources;
    
    public SelectionPanel() {
        selection = SelectionModel.getInstance();
        mapResources = MapResources.getInstance();
        
        selection.addViewer(this);
        
        loadSelectionPanel();
    }
    
    private void loadSelectionPanel() {
        loadMapTiles(selection.getSelectionCategory());
    }
    
    private void loadMapTiles(Enum currentCategory) {
        this.removeAll();
        this.setLayout(null);
        this.setFocusable(false);
        SelectionButton selectButton;
        
        final int componentWidth = 64;
        final int componentHeight = 64;
        final int verticalMargin = 10;
        final int horizontalMargin = 10;
        final int spaceBetweenComponents = 5;
        
        int numberOfSprites = mapResources.getNumberOfSprites(currentCategory);
        this.setPreferredSize(new Dimension(
            ((componentWidth + spaceBetweenComponents) * NUMBER_OF_COLUMNS) + horizontalMargin, //Width
            ((componentHeight + spaceBetweenComponents) * (numberOfSprites / NUMBER_OF_COLUMNS)) + verticalMargin //Height
        ));
        this.revalidate();
        int spriteNumber = 0; //stores the current sprite being loaded and is incremented in the loops
        for (int xIndex = 0; xIndex < NUMBER_OF_COLUMNS; xIndex++) {
            for (int yIndex = 0; yIndex <= ((int)Math.ceil((double)numberOfSprites / (double)NUMBER_OF_COLUMNS)); yIndex++) { //There are as many rows as needed to display all the sprites (using the fixed number of columns)
                if (spriteNumber < numberOfSprites) {
                    selectButton = new SelectionButton(currentCategory, spriteNumber);
                    selectButton.setFocusable(false);
                    selectButton.setBounds(((componentWidth + spaceBetweenComponents) * xIndex) + horizontalMargin, ((componentHeight + spaceBetweenComponents) * yIndex) + verticalMargin, componentWidth, componentHeight);
                    this.add(selectButton);
                }
                spriteNumber++;
            }
        }
    }
    
    public void notifyChange() { //Called when the associated Model's state has changed
        loadSelectionPanel();
    }
    
    /**************************CONSTANTS**************************/
    public static final int NUMBER_OF_COLUMNS = 2;
}
