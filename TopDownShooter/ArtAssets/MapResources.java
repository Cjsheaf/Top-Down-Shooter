package TopDownShooter.ArtAssets;
import TopDownShooter.MapSystem.TileCategory;
import TopDownShooter.MapEditor.OverlayCategory;

import java.awt.image.*;
import javax.imageio.ImageIO;
import java.io.*;
import java.util.List;
import java.util.ArrayList;

public class MapResources {
    private static MapResources instance = null;
    private ArrayList<SpriteSheet> mapSprites;
    private ArrayList<SpriteSheet> editorOverlaySprites;
    
    private MapResources() {
        mapSprites = new ArrayList<SpriteSheet>();
        mapSprites.ensureCapacity(TileCategory.numberOfElements);
        editorOverlaySprites = new ArrayList<SpriteSheet>();
        editorOverlaySprites.ensureCapacity(OverlayCategory.numberOfElements);
        
        loadSprites();
    }
    
    public static MapResources getInstance() {
        if (instance == null) {
            instance = new MapResources();
        }
        return instance;
    }
    
    public BufferedImage getSprite(Enum category, int index) {
        if (category instanceof TileCategory) {
            TileCategory temp = (TileCategory)category;
            return mapSprites.get(temp.id).getSprite(index);
        } else if (category instanceof OverlayCategory) {
            OverlayCategory temp = (OverlayCategory)category;
            return editorOverlaySprites.get(temp.id).getSprite(index);
        } else {
            return null;
        }
    }
    public int getNumberOfSprites(Enum category) {
        if (category instanceof TileCategory) {
            TileCategory temp = (TileCategory)category;
            return mapSprites.get(temp.id).getNumberOfSprites();
        } else if (category instanceof OverlayCategory) {
            OverlayCategory temp = (OverlayCategory)category;
            return editorOverlaySprites.get(temp.id).getNumberOfSprites();
        } else {
            return 0;
        }
    }
    
    private void loadSprites() {
        loadGrassSprites();
        loadDirtSprites();
        loadExternalHouseSprites();
        loadDungeonTiles();
        
        loadWalkableOverlaySprites();
    }
    
    private void loadGrassSprites() {
        BufferedImage tempImage = null;
        try {
            //tempImage = ImageIO.read(getClass().getResource("Sprites/grass.png"));
            tempImage = ImageIO.read(new File("Sprites/grass.png"));
        } catch (IOException e) {
            System.exit(2);
        }
        
        mapSprites.add(TileCategory.GRASS.id, new SpriteSheet(tempImage, 3, 6));
    }
    private void loadDirtSprites() {
        BufferedImage tempImage = null;
        try {
            //tempImage = ImageIO.read(getClass().getResource("Sprites/dirt.png"));
            tempImage = ImageIO.read(new File("Sprites/dirt.png"));
        } catch (IOException e) {
            System.exit(2);
        }
        
        mapSprites.add(TileCategory.DIRT.id, new SpriteSheet(tempImage, 3, 6));
    }
    private void loadExternalHouseSprites() {
        BufferedImage tempImage = null;
        try {
            //tempImage = ImageIO.read(getClass().getResource("Sprites/house.png"));
            tempImage = ImageIO.read(new File("Sprites/house.png"));
        } catch (IOException e) {
            System.exit(2);
        }
        
        mapSprites.add(TileCategory.HOUSE_EXTERNAL.id, new SpriteSheet(tempImage, 9, 7));
    }
    
    private void loadWalkableOverlaySprites() {
        BufferedImage[] tempArray = new BufferedImage[2];
        try {
            //tempArray[0] = ImageIO.read(getClass().getResource("Sprites/transparent check mark.png"));
            //tempArray[1] = ImageIO.read(getClass().getResource("Sprites/transparent red x.png"));
            tempArray[0] = ImageIO.read(new File("Sprites/transparent check mark.png"));
            tempArray[1] = ImageIO.read(new File("Sprites/transparent red x.png"));
        } catch (IOException e) {
            System.exit(2);
        }
        
        editorOverlaySprites.add(OverlayCategory.WALKABLE.id, new SpriteSheet(tempArray));
    }
    
    public void loadDungeonTiles() {
        BufferedImage tempImage = null;
        try {
            tempImage = ImageIO.read(new File("Sprites/5x5.png"));
        } catch (IOException e) {
            System.exit(2);
        }
        
        mapSprites.add(TileCategory.DUNGEON.id, new SpriteSheet(tempImage, 5, 5));
    }
}