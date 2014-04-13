package TopDownShooter;
import TopDownShooter.MapSystem.Map;
import TopDownShooter.CollisionSystem.CollisionManager;
import TopDownShooter.CollisionSystem.BoundingBox;

import java.awt.*;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.event.*;
import javax.swing.Timer;
import java.awt.image.*;

public class GamePanel extends JPanel implements ActionListener {
    private BufferedImage backBufferImage;
    private Graphics backBuffer;
    private Player player;
    private Map map;
    
    //Singleton references:
    private ViewScreen viewScreen;
    private CollisionManager collisionManager;
    
    public GamePanel() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gs = ge.getDefaultScreenDevice(); 
        GraphicsConfiguration gc = gs.getDefaultConfiguration();

        // Create an image that does not support transparency
        backBufferImage = gc.createCompatibleImage(TopDownShooter_Main.screenWidth, TopDownShooter_Main.screenHeight, Transparency.OPAQUE);
        backBuffer = backBufferImage.getGraphics();
        
        player = new Player(new Point(750, 750));
        
        map = Map.getInstance();
        map.loadWorld("World 1");
        viewScreen = ViewScreen.getInstance();
        collisionManager = CollisionManager.getInstance();
        
        
        SwingUtilities.invokeLater(new Runnable() { //The GUI needs to be added to the JPanel at a later time, since there is no way to know the size of this panel yet.
            public void run() {
                viewScreen.setSize(new Rectangle(new Point(0,0), new Dimension(GamePanel.this.getWidth(), GamePanel.this.getHeight()))); //Size the ViewScreen to the actual dimensions of the viewable area now that we know them
                viewScreen.CenterOnObject(player.getBoundingBox());
            }
        });
        
        this.setFocusable(true);
        this.addKeyListener(player);
        
        new Timer(16, this).start(); //Master game timer
    }
    
    public void actionPerformed(ActionEvent action) {
        player.Update();
        viewScreen.CenterOnObject(player.getBoundingBox());
        map.centerOnWorldCoordinates(new Point((int)player.getBoundingBox().x, (int)player.getBoundingBox().y));
        collisionManager.handleCollisions();
        this.repaint();
        this.requestFocus();
    }
    
    public void paintComponent(Graphics frontBuffer) {
        //Clear the back buffer
        backBuffer.clearRect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
        
        //Draw all objects to the back buffer in order from back-to-front
        paintBackground();
        paintEntities();
        
        //Draw the back buffer onto the front buffer all at once to avoid flickering from repeated draw commands
        frontBuffer.drawImage(backBufferImage, this.getX(), this.getY(), this);
    }
    public void paintBackground() {        
        map.Draw(backBuffer, viewScreen.getScreen());
    }
    public void paintEntities() {
        player.Draw(backBuffer, viewScreen.getScreen());
    }
}