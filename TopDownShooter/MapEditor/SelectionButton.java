package TopDownShooter.MapEditor;
import TopDownShooter.Utility.Controller;
import TopDownShooter.ArtAssets.MapResources;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import java.util.*;

public class SelectionButton extends JPanel implements ActionListener, Controller {
    private Enum category;
    private int spriteNumber;
    private BufferedImage sprite;
    private boolean isSelected;
    
    private MapResources mapResources;
    private SelectionModel selection;
    
    public SelectionButton(Enum selectionCategory, int new_spriteNumber) {
        selection = SelectionModel.getInstance();
        mapResources = MapResources.getInstance();
        
        selection.addActionListener(this);
        category = selectionCategory;
        spriteNumber = new_spriteNumber;
        sprite = mapResources.getSprite(category, spriteNumber);
        AddMouseListener();
    }
    
    public void paintComponent(Graphics graphics) {
        try {
            graphics.drawImage(sprite, 0, 0, this.getWidth(), this.getHeight(), this);
            
            if (isSelected == true) {
                graphics.setColor(Color.RED);
                graphics.drawRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);
            }
        } catch (NullPointerException nullImage) {
            System.out.println("No Image Reference in class SelectionButton, method paintComponent(). Tried to reference category " + category + ", spriteNumber " + spriteNumber);
            nullImage.printStackTrace();
            System.exit(2);
        }
    }
    public void AddMouseListener() {
        this.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent event) {
                selection.setSelectedSpriteNumber(spriteNumber);
                isSelected = true;
            }
        });
    }
    
    public void actionPerformed(ActionEvent event) {
        if (event.getActionCommand() == "New Selection") {
            isSelected = false;
        }
    }
    
    /*
     * I would like to see if overloading methods is possible based on Generics:
     * 
     * Such as:
     * selection.setSelectedSprite(SpriteIndex<Tile>){}
     * selection.setSelectedSprite(SpriteIndex<Prop>){}
     */
}
