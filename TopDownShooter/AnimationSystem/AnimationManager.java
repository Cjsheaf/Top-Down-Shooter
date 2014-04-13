package TopDownShooter.AnimationSystem;

import java.util.*;
import java.awt.image.*;
import java.awt.event.*;
import javax.swing.Timer;

public class AnimationManager implements ActionListener {
    private ArrayList<Animation> animationList; //dynamic array of Animation(s)
    private int currentAnimation;
    private int currentFrame;
    private boolean isPaused;
    
    private Timer frameTimer;
    
    public AnimationManager(int frameDelay) {
        animationList = new ArrayList<Animation>();
        currentAnimation = 0;
        currentFrame = 0;
        isPaused = false;
        
        frameTimer = new Timer(frameDelay, this);
    }
    
    public void actionPerformed(ActionEvent event) {
        if (isPaused == false) {
            currentFrame++;
            if (currentFrame >= animationList.get(currentAnimation).GetNumberOfFrames()) {
                currentFrame = 0;
            }
        }
    }
    
    public void AddNewAnimation(Animation new_animation) {
        animationList.add(new_animation);
    }
    public BufferedImage GetCurrentImage() {
        return animationList.get(currentAnimation).GetFrame(currentFrame).image;
    }
    public int GetCurrentAnimation() {
        return currentAnimation;
    }
    /*
     * Changes the animation to the appropriate Const value. Does nothing if the animation is already playing.
     */
    public void ChangeAnimation(int new_Animation) {
        if (currentAnimation == new_Animation) {
            //Do nothing, this animation is already playing
        } else {
            currentAnimation = new_Animation;
            currentFrame = 0;
        }
    }
    public void SetFrameDelay(int new_delay) {
        frameTimer.setDelay(new_delay);
    }
    public void Start() {
        isPaused = false;
        frameTimer.start();
    }
    public void Stop() {
        isPaused = true;
        currentFrame = 0;
        frameTimer.stop();
    }
    
    /**************************CONSTANTS**************************/
    public static final int ANIM_UP = 0;
    public static final int ANIM_LEFT = 1;
    public static final int ANIM_DOWN = 2;
    public static final int ANIM_RIGHT = 3;
}