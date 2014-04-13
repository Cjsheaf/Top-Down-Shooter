package TopDownShooter.MapSystem;
import TopDownShooter.ArtAssets.MapResources;
import TopDownShooter.CollisionSystem.BoundingBox;
import TopDownShooter.DebugFlags;
import TopDownShooter.ViewScreen;

import java.awt.*;
import java.awt.image.*;
import java.util.*;
import java.util.Map;

public class MapTile {
    /*
     * Map tiles are exactly 32x32 pixels in all cases.
     * Map tiles should only be created by MapLoader, or should be created for use in one of MapLoader's method arguments.
     * Potential space-time tradeoff would be to store the global tile X and Y coordinates instead of computing them every time they're needed.
     */
    public int worldX, worldY;
    public int tileType; //Water, Lava, ect.
    public boolean isWalkable;
    private TreeMap<Integer, SpriteIndex> spriteIndexMap; //A mapping of a SpriteIndex to its given Layer (the Integer)
    private TreeMap<Integer, BufferedImage> spriteMap; //A mapping of a SpriteIndex's image in the same manner as above
    
    private static MapResources mapResources;
    
    public MapTile() { //Initialises the Map Tile to a default grass tile
        worldX = 0;
        worldY = 0;
        tileType = 0;
        isWalkable = true;
        spriteIndexMap = new TreeMap<Integer, SpriteIndex>();
        spriteMap = new TreeMap<Integer, BufferedImage>();
        
        spriteIndexMap.put(0, new SpriteIndex(TileCategory.GRASS, 15)); //Put a default grass tile on Layer 0
        
        mapResources = MapResources.getInstance();
        this.loadSprites();
    }
    public MapTile(int new_worldX, int new_worldY, int new_tileType, boolean new_isWalkable, TreeMap<Integer, SpriteIndex> new_spriteIndexMap) {
        worldX = new_worldX;
        worldY = new_worldY;
        tileType = new_tileType;
        isWalkable = new_isWalkable;
        spriteIndexMap = new_spriteIndexMap;
        spriteMap = new TreeMap<Integer, BufferedImage>();
        
        mapResources = MapResources.getInstance();
        this.loadSprites();
    }
    public MapTile(MapTile tileToCopy) { //Creates a copy of the given Map Tile
        worldX = tileToCopy.worldX;
        worldY = tileToCopy.worldY;
        tileType = tileToCopy.tileType;
        isWalkable = tileToCopy.isWalkable;
        spriteIndexMap = tileToCopy.spriteIndexMap;
        spriteMap = new TreeMap<Integer, BufferedImage>();
        
        mapResources = MapResources.getInstance();
        this.loadSprites();
    }
    
    public BoundingBox getBoundingBox() {
        return new BoundingBox(new Rectangle(worldX, worldY, MapTile.TILE_WIDTH, MapTile.TILE_HEIGHT));
    }
    
    private void loadSprites() { //Retrieves the BufferedImages associated with the sprite indexes, and stores their references in spriteMap. The actual images still reside within the MapResourcesSingleton
        spriteMap.clear(); //In case we're re-loading the sprites after the map tile has been created
        for (Map.Entry<Integer, SpriteIndex> currentEntry : spriteIndexMap.entrySet()) { //For each spriteIndex mapping:
            SpriteIndex currentIndex = currentEntry.getValue();
            spriteMap.put(currentEntry.getKey(), mapResources.getSprite(currentIndex.spriteCategory, currentIndex.spriteNumber)); //Create an identical mapping of its associated image in spriteMap
                                                           //In English: The Key of 0 (Representing Layer 0) will be mapped to whatever image is supposed to be stored on Layer 0 of this MapTile
        }
    }
    
    public TreeMap<Integer, SpriteIndex> getSpriteIndexes() {
        return spriteIndexMap;
    }
    public TreeMap<Integer, BufferedImage> getSprites() {
        return spriteMap;
    }
    public void addSprite(int layer, SpriteIndex new_spriteIndex) {
        removeSprite(layer);
        
        spriteIndexMap.put(layer, new_spriteIndex);
        loadSprites();
    }
    public void removeSprite(int layer) {
        spriteIndexMap.remove(layer);
        spriteMap.remove(layer);
    }
    
    public void Draw(Graphics graphics, Rectangle screen) { //The X and Y to be drawn at is relative to the screen
        if (ViewScreen.isObjectOnscreen(this.getBoundingBox(), screen) == true) {
            for (Map.Entry<Integer, BufferedImage > currentEntry : spriteMap.entrySet()) { //For each spriteIndex mapping:
                graphics.drawImage(currentEntry.getValue(), (MapTile.this.worldX - screen.x), (MapTile.this.worldY - screen.y), MapTile.TILE_WIDTH, MapTile.TILE_HEIGHT, null);
            }
            
            if (DebugFlags.DEBUG_MODE == true && DebugFlags.BOUNDING_BOXES_VISUALIZED == true) { //[DEBUG]
                graphics.setColor(Color.RED);
                Rectangle temp = this.getBoundingBox().toScreenBounds();
                graphics.drawRect(temp.x - screen.x, temp.y - screen.y, temp.width, temp.height);
            }
        }
    }
    
    /**************************CONSTANTS**************************/
    public static final int TILE_WIDTH = 32; //In pixels
    public static final int TILE_HEIGHT = 32;
}