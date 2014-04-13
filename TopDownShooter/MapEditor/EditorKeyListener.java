package TopDownShooter.MapEditor;
import TopDownShooter.MapSystem.Map;
import TopDownShooter.ViewScreen;

import java.awt.*;
import java.awt.event.*;

public class EditorKeyListener implements KeyListener {
    private int speed;
    private boolean isMovingUp;
    private boolean isMovingLeft;
    private boolean isMovingDown;
    private boolean isMovingRight;  
    private boolean ctrlHeld;
    private Map mapReference; //Holds a reference to the *current* Map in order to call functions on it
    
    private EditorTextPanel editorTextPanel;

    public EditorKeyListener(Map new_mapReference) {
        mapReference = new_mapReference;
        editorTextPanel = EditorTextPanel.getInstance();
        speed = 16;
    }
    
    public void Update(ViewScreen screen) {
        Point movementVector = new Point();
        
        if (isMovingUp == true) {
            movementVector.y -= speed;
        }
        if (isMovingLeft == true) {
            movementVector.x -= speed;
        }
        if (isMovingDown == true) {
            movementVector.y += speed;
        }
        if (isMovingRight == true) {
            movementVector.x += speed;
        }
        
        screen.Move(movementVector);
    }
    public void keyPressed(KeyEvent event) {
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_W:
                isMovingUp = true;
                break;
            case KeyEvent.VK_A:
                isMovingLeft = true;
                break;
            case KeyEvent.VK_S:
                if (ctrlHeld == true) {
                    editorTextPanel.addText("Saving Map...");
                    mapReference.saveWorld();
                    editorTextPanel.addText("Map Has Been Successfully Saved.");
                }
                else {
                    isMovingDown = true;
                }
                break;
            case KeyEvent.VK_D:
                isMovingRight = true;
                break;
            case KeyEvent.VK_CONTROL:
                ctrlHeld = true;
                break;
            }
    }
    public void keyReleased(KeyEvent event) {
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_W:
                isMovingUp = false;
                break;
            case KeyEvent.VK_A:
                isMovingLeft = false;
                break;
            case KeyEvent.VK_S:
                isMovingDown = false;
                break;
            case KeyEvent.VK_D:
                isMovingRight = false;
                break;
            case KeyEvent.VK_CONTROL:
                ctrlHeld = false;
                break;
            }
    }
    public void keyTyped(KeyEvent event) {}
}