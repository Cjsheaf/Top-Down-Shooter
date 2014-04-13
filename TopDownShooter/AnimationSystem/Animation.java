package TopDownShooter.AnimationSystem;
import TopDownShooter.ArtAssets.SpriteSheet;

public class Animation {
    private Frame frame[];
    
    public Animation(SpriteSheet new_sheet) {
        frame = new Frame[new_sheet.getNumberOfSprites()]; //Create an array just big enough to hold all the sprites in the sprite sheet.
        
        for (int index = 0; index < frame.length; index++) { //For as many Frames as exist, load the corresponding image from the sprite sheet.
            frame[index] = new Frame(new_sheet.getSprite(index));
        }
    }
    
    public Frame GetFrame(int frameNumber) {
        return frame[frameNumber];
    }
    public int GetNumberOfFrames() {
        return frame.length;
    }
    public void AddKeyFrame(int frameNumber, String new_keyFrame) {
        frame[frameNumber].isKeyFrame = true;
        frame[frameNumber].keyFrame = new_keyFrame;
    }
}