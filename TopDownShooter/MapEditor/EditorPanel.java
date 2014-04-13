package TopDownShooter.MapEditor;
import TopDownShooter.MapSystem.Map;
import TopDownShooter.MapSystem.MapTile;
import TopDownShooter.MapSystem.SpriteIndex;
import TopDownShooter.MapSystem.TileCategory;
import TopDownShooter.ViewScreen;

import java.awt.*;
import javax.swing.JPanel;
import javax.swing.Box;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import java.awt.event.*;
import javax.swing.Timer;
import java.awt.image.*;

public class EditorPanel extends JPanel implements ActionListener {
    private BufferedImage backBufferImage;
    private Graphics backBuffer;
    private Map map;
    private EditorKeyListener keyListener;
    private EditorOverlayManager overlayManager;
    
    private ViewScreen viewScreen;
    private SelectionModel selection;
    
    public EditorPanel() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gs = ge.getDefaultScreenDevice(); 
        GraphicsConfiguration gc = gs.getDefaultConfiguration();

        // Create an image that does not support transparency
        backBufferImage = gc.createCompatibleImage(MapEditor_Main.screenWidth, MapEditor_Main.screenHeight, Transparency.OPAQUE);
        backBuffer = backBufferImage.getGraphics();
        
        selection = SelectionModel.getInstance();
        viewScreen = ViewScreen.getInstance();
        
        map = Map.getInstance();
        map.loadWorld("World 1");
        keyListener = new EditorKeyListener(map);
        overlayManager = new EditorOverlayManager();
        
        SwingUtilities.invokeLater(new Runnable() { //The GUI needs to be added to the JPanel at a later time, since there is no way to know the size of this panel yet.
            public void run() {
                EditorPanel.this.addUIToPanel();
                //Size the ViewScreen to the actual dimensions of the viewable area now that we know them
                viewScreen.setSize(new Rectangle(new Point(0,0), new Dimension(EditorPanel.this.getWidth(), EditorPanel.this.getHeight())));
            }
        });
        
        this.setFocusable(true);
        this.addKeyListener(keyListener);
        this.AddMouseListener();
        
        new Timer(33, this).start();
    }
    private void addUIToPanel() {
        this.setLayout(null);
        
        SelectionPanel selectionPanel = new SelectionPanel();
        JScrollPane selectionScrollPane = new JScrollPane(selectionPanel);
        selectionScrollPane.setBounds(
            this.getWidth() - ((SelectionPanel.NUMBER_OF_COLUMNS * 75) + 25), //X Coordinate
            0, //Y Coordinate
            (SelectionPanel.NUMBER_OF_COLUMNS * 75) + 25, //Width
            this.getHeight() //Height
        );
        this.add(selectionScrollPane);
        
        CategoryPanel categoryPanel = new CategoryPanel();
        categoryPanel.setBounds(
            selectionScrollPane.getX() - 160, //X Coordinate is relative to the selection area
            0, //Y Coordinate
            160, //Width
            this.getHeight() //Height; the entire screen's height
        );
        this.add(categoryPanel);
        
        EditorTextPanel textPanel = EditorTextPanel.getInstance();
        textPanel.setBounds(
            0, //X Coordinate
            this.getHeight() - 100, //Y Coordinate
            this.getWidth() - categoryPanel.getWidth() - selectionScrollPane.getWidth(),
            100
        );
        this.add(textPanel);
        textPanel.resize();
    }
    
    public void AddMouseListener() {
        this.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent event) {
                Point worldCoords = new Point(event.getX() + viewScreen.getScreen().x, event.getY() + viewScreen.getScreen().y); //Get the WORLD Coordinates of the click
                
                switch (selection.getSelectionType()) {
                    case TILES:
                        MapTile editedTile = map.getTile(worldCoords); //Get a reference to the Map Tile we want to edit
                        if (editedTile != null) {
                            switch (event.getButton()) {
                                case MouseEvent.BUTTON1: //If the left mouse button was clicked, edit the sprite on this layer to whatever is currently selected
                                    if (selection.getSelectedSpriteNumber() >= 0) {
                                        editedTile.addSprite(selection.getCurrentLayer(), new SpriteIndex((TileCategory)selection.getSelectionCategory(), selection.getSelectedSpriteNumber()));
                                        map.replaceTile(worldCoords, editedTile); //Replace the current Map Tile with the new Map Tile
                                    }
                                    break;
                                case MouseEvent.BUTTON3: //If the right mouse button was clicked, remove the sprite from this layer
                                    editedTile.removeSprite(selection.getCurrentLayer());
                                    map.replaceTile(worldCoords, editedTile); //Replace the current Map Tile with the new Map Tile
                                    break;
                            }
                        }
                        break;
                    case OVERLAYS:
                        MapTile editedTile_overlay = map.getTile(worldCoords); //Get a reference to the Map Tile we want to edit
                        if (editedTile_overlay != null) {
                            switch ((OverlayCategory)selection.getSelectionCategory()) {
                                case WALKABLE:
                                    if (selection.getSelectedSpriteNumber() == 0) {
                                        editedTile_overlay.isWalkable = true;
                                    } else {
                                        editedTile_overlay.isWalkable = false;
                                    }
                                    map.replaceTile(worldCoords, editedTile_overlay);
                                    break;
                            }
                        }
                        break;
                }
            }
        });
    }
    public void actionPerformed(ActionEvent action) {
        keyListener.Update(viewScreen);
        map.centerOnWorldCoordinates(viewScreen.getCenterOfScreen());
        this.repaint();
        this.requestFocus();
    }
    
    public void paintComponent(Graphics frontBuffer) {
        //Clear the back buffer
        backBuffer.clearRect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
        
        //Draw all objects to the back buffer in order from back-to-front
        paintBackground();
        
        //Draw the back buffer all at once
        frontBuffer.drawImage(backBufferImage, 0, 0, this);
    }
    public void paintBackground() {        
        map.Draw(backBuffer, viewScreen.getScreen());
        overlayManager.drawOverlays(backBuffer, map.getTilesWithinBounds(viewScreen.getScreen()), viewScreen.getScreen());
    }
}