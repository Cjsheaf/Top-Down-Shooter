package TopDownShooter;
import TopDownShooter.AnimationSystem.AnimationManager;
import TopDownShooter.AnimationSystem.Animation;
import TopDownShooter.Utility.Vector2D;
import TopDownShooter.ArtAssets.SpriteSheet;
import TopDownShooter.MapSystem.Map;

import java.util.*;
import java.awt.image.*;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Dimension;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.*;
import java.io.IOException;
import java.awt.event.*;

public class Player extends Entity implements KeyListener {
    private boolean keyHeld_W;
    private boolean keyHeld_A;
    private boolean keyHeld_S;
    private boolean keyHeld_D;
    
    public Player(Point startingPosition) {
        super(new Rectangle(startingPosition, new Dimension(28, 46)), 2, 4, 0.75, 100); //Player is actually 32 pixels tall by 48 pixels wide, but we want his collision box to be slightly smaller
        LoadAnimations();
    }
    public void LoadAnimations() {
        PlayerResourcesSingleton playerResources = PlayerResourcesSingleton.getInstance();
        
        for (int a = 0; a < 4; a++) {
            animationManager.AddNewAnimation(playerResources.getPlayerAnimation(a));
        }
        animationManager.Stop();
    }

    public void Update() {
        processKeystrokes();
        updatePosition();
    }
    
    public void processKeystrokes() {
        if (keyHeld_W) changeVelocity(new Vector2D(0, -1));
        if (keyHeld_A) changeVelocity(new Vector2D(-1, 0));
        if (keyHeld_S) changeVelocity(new Vector2D(0, 1));
        if (keyHeld_D) changeVelocity(new Vector2D(1, 0));
    }
    
    public void keyPressed(KeyEvent event) {
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_W:
                keyHeld_W = true;
                break;
            case KeyEvent.VK_A:
                keyHeld_A = true;
                break;
            case KeyEvent.VK_S:
                keyHeld_S = true;
                break;
            case KeyEvent.VK_D:
                keyHeld_D = true;
                break;
        }
    }
    public void keyReleased(KeyEvent event) {
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_W:
                keyHeld_W = false;
                break;
            case KeyEvent.VK_A:
                keyHeld_A = false;
                break;
            case KeyEvent.VK_S:
                keyHeld_S = false;
                break;
            case KeyEvent.VK_D:
                keyHeld_D = false;
                break;
        }
    }
    public void keyTyped(KeyEvent event) {}
}

class PlayerResourcesSingleton {
    private static PlayerResourcesSingleton instance = null;
    private ArrayList<Animation> playerAnimations;
    
    private PlayerResourcesSingleton() {}
    
    public static PlayerResourcesSingleton getInstance() {
        if (instance == null) {
            instance = new PlayerResourcesSingleton();
            instance.playerAnimations = new ArrayList<Animation>();
            instance.loadPlayerAnimations();
        }
        return instance;
    }
    
    public Animation getPlayerAnimation(int animationIndex) {
        return playerAnimations.get(animationIndex);
    }
    private void loadPlayerAnimations() {
        BufferedImage tempImage = null;
        try {
            //tempImage = ImageIO.read(getClass().getResource("Sprites/soldier_blue.png"));
            tempImage = ImageIO.read(new File("Sprites/soldier_blue.png"));
        } catch (IOException e) {
            System.exit(2);
        }
        
        
        //Takes tempImage, and loads each row into a SpriteSheet named tempSheet, which is then accepted by the constructor for
        //a new Animation named tempAnimation, which is in turn loaded into the ArrayList.
        SpriteSheet tempSheet;
        Animation tempAnimation;
        for (int animNumber = 0; animNumber < 4; animNumber++) {
            tempSheet = new SpriteSheet(tempImage.getSubimage(0, animNumber * 48, tempImage.getWidth(), 48), 9);
            tempAnimation = new Animation(tempSheet);
            playerAnimations.add(tempAnimation);
        }
    }
}