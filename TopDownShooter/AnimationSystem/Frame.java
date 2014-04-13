package TopDownShooter.AnimationSystem;

import java.awt.image.*;

public class Frame {
    public BufferedImage image;
    public boolean isKeyFrame;
    public String keyFrame; //Identifies the action to be performed when this frame is displayed.
    
    public Frame(BufferedImage new_image) { //Non-KeyFrame Constructor
        image = new_image;
        isKeyFrame = false;
        keyFrame = "";
    }
    public Frame(BufferedImage new_image, String new_keyFrame) { //KeyFrame Constructor
        image = new_image;
        isKeyFrame = true;
        keyFrame = new_keyFrame;
    }
}