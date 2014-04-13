package TopDownShooter.ArtAssets;

import java.awt.image.*;

/*
 *  Each sprite sheet accepts a BufferedImage consisting of either a single row containing any number of sprites, (first constructor)
 *  or simply an image consisting of sprites in any combination of rows and columns (second constructor).
 *  It then seperates them into their individual sprites for easier use and storage depending on which constructor was used.
 *  The first constructor is primarily used for storing Animations, which are stored with each row being a single animation. It's possible that this method is faster (unverified).
 *  The second constructor can be used for almost any other type of sprite sheet.
 */

public class SpriteSheet {
    private BufferedImage sprite[];
    
    public SpriteSheet() {}
    public SpriteSheet(BufferedImage new_sheet, int numberOfSprites) {
        sprite = new BufferedImage[numberOfSprites];
        
        int spriteWidth = (new_sheet.getWidth() / numberOfSprites);
        int spriteHeight = new_sheet.getHeight();
        for (int index = 0; index < numberOfSprites; index++) {
            sprite[index] = new_sheet.getSubimage(index * spriteWidth, 0, spriteWidth, spriteHeight);
        }
    }
    public SpriteSheet(BufferedImage new_sheet, int columns, int rows) {
        int numberOfSprites = (columns * rows);
        sprite = new BufferedImage[numberOfSprites];
        
        int spriteWidth = (new_sheet.getWidth() / columns);
        int spriteHeight = (new_sheet.getHeight() / rows);
        for (int yIndex = 0; yIndex < rows; yIndex++) {
            for (int xIndex = 0; xIndex < columns; xIndex++) {
                sprite[(yIndex * columns) + xIndex] = new_sheet.getSubimage(xIndex * spriteWidth, yIndex * spriteHeight, spriteWidth, spriteHeight);
            }
        }
    }
    public SpriteSheet(BufferedImage[] new_sprites) {
        sprite = new_sprites;
    }
    
    public BufferedImage getSprite(int spriteIndex) {
        return sprite[spriteIndex];
    }
    public int getNumberOfSprites() {
        return sprite.length;
    }
}