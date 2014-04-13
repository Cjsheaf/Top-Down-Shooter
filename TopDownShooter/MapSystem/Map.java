package TopDownShooter.MapSystem;
import TopDownShooter.Utility.Vector2D;
import TopDownShooter.Utility.Circular2DArray;

import java.awt.*;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.TreeMap;

public class Map {
    /*
     * NOTES ABOUT THE MAP HEIRARCHY:
     *  - The lowest level of the map heirarchy is the Map Tile. Each map tile has a dimension of exactly 32 by 32 pixels.
     *  - An array of Map Tiles is grouped into a Map Chunk for bulk storage and loading. (currently a 16x16 array, see class MapChunk's constant variables)
     *  - The Map (this class) contains an array of Map Chunks, which defines the current subset of the map that's active and loaded into memory.
     * NOTES ABOUT THE COORDINATE SYSTEM:
     *  - There are three types of coordinates, and they all directly relate to each other by measure of pixels.
     *      * World Coordinates: the x and y pair associated with the upper-left pixel of an object/tile/anything.
     *      * Global Tile Coordinates: the x and y pair associated with a given tile in the TOTAL set of tiles. That tile's World Coordinates will be exactly 32 times
     *        larger, since each tile is 32x32 pixels.
     *      * Local Tile Coordinates: the specific array index inside a MapTile's PARENT MapChunk. An example would be: a MapTile with the GLOBAL tile coordinates of
     *        (20,0) would have LOCAL tile coordinates of (4,0) relative to MapChunk (1,0) which is its parent MapChunk. The reason being, MapChunk (0,0) stores GLOBAL MapTiles
     *        (0,0) through (15,0) in LOCAL array locations (0,0) through (15,0), which MapChunk (1,0) stores GLOBAL MapTiles (16,0) through (31,0) in LOCAL array locations
     *        (0,0) through (15,0).
     *      * Chunk Coordinates: the x and y pair associated with a given chunk in the total set of chunks. A chunk's pixel dimensions are going to be (currently) 16
     *        times* larger than the dimensions of a individual Map Tile. (*see class MapChunk's constant variables for the exact number of tiles in a Map Chunk)
     */
    
    private static Map instance;
    private Circular2DArray<MapChunk> loadedChunks;
    private Point mapPosition; //Stores the Chunk Coordinate of the chunk at loadedChunks[0][0] for reference
    private MapLoader mapLoader;
    
    private Map() { //Creates an instance of Map with the world specified in the arguments loaded by default
        loadedChunks = new Circular2DArray<MapChunk>(MapChunk.class, Map.LOADED_AREA_X, Map.LOADED_AREA_Y);
        mapPosition = new Point(0, 0);
    }
    
    public static Map getInstance() {
        if (instance == null) {
            instance = new Map();
        }
        return instance;
    }
    public static void terminateInstance() {
        instance = null;
    }
    
    public MapTile getTile(Point worldCoords) { //Determines the map tile assocaited with the given world coordinates and returns it. Tiles that are requested should only be in a loaded chunk (could be extended to request tiles from chunks on file)
        Point chunkCoords = Map.WorldToChunk(worldCoords);
        Point globalTileCoords = Map.WorldToMapTile(worldCoords); //Convert the world coordinates into global tile coordinates to be used by the next line
        Point localTileCoords = Map.MapTileToLocalTile(globalTileCoords); //Determine the LOCAL array index of the tile stored in the MapChunk indexed by chunkCoords
        
        if (isChunkLoaded(chunkCoords, mapPosition) == true) {
            return loadedChunks.get(new Point(chunkCoords.x - mapPosition.x, chunkCoords.y - mapPosition.y)).getTile(localTileCoords.x, localTileCoords.y);
        } else {
            System.out.println("ERROR: Cannot get tile! Map chunk at World Coordinates: " + worldCoords.x + ", " + worldCoords.y + " is not currently loaded!");
            return null;
        }        
    }
    public void replaceTile(Point worldCoords, MapTile new_mapTile) {  //Determines the map tile associated with the given world coordinates and replaces it
        Point chunkCoords = Map.WorldToChunk(worldCoords);
        Point globalTileCoords = Map.WorldToMapTile(worldCoords); //Convert the world coordinates into global tile coordinates to be used by the next line
        Point localTileCoords = Map.MapTileToLocalTile(globalTileCoords); //Determine the LOCAL array index of the tile stored in the MapChunk indexed by chunkCoords
        
        if (isChunkLoaded(chunkCoords, mapPosition) == true) {
            loadedChunks.get(new Point(chunkCoords.x - mapPosition.x, chunkCoords.y - mapPosition.y)).replaceTile(localTileCoords.x, localTileCoords.y, new_mapTile);
        } else {
            System.out.println("ERROR: Cannot replace tile! Map chunk at World Coordinates: " + worldCoords.x + ", " + worldCoords.y + " is not currently loaded!");
        }
    }
    
    public void saveWorld() { //Saves all currently loaded chunks to disk. Should be called before closing the game.
        for (int xIndex = 0; xIndex < Map.LOADED_AREA_X; xIndex++) {
            for (int yIndex = 0; yIndex < Map.LOADED_AREA_X; yIndex++) {
                mapLoader.saveChunk(loadedChunks.get(new Point(xIndex, yIndex)));
            }
        }
    }
    public void loadWorld(String worldName) {
        mapLoader = new MapLoader(worldName);
        
        for (int xIndex = 0; xIndex < Map.LOADED_AREA_X; xIndex++) {
            for (int yIndex = 0; yIndex < Map.LOADED_AREA_Y; yIndex++) {
                //[TODO] Swap this code over to using the mutltithreaded chunk-loading code
                loadedChunks.set(
                    new Point(xIndex, yIndex), //The X and Y indexes of the Circular2DArray to set
                    mapLoader.getChunk(new Point((xIndex * MapChunk.CHUNK_WIDTH), (yIndex * MapChunk.CHUNK_HEIGHT))) //The MapChunk to set it to
                );
            }
        }
    }
    
    public void Update(Rectangle screen) { //Handles the dispatch of load and save operations for map chunks and entities/props. Calls Update() on loaded MapChunks
        //[TODO]
    }
    public void Draw(Graphics graphics, Rectangle screen) { //Determines which MapChunks are on-screen and calls their Draw() method
        //[TODO] actually cull offscreen map chunks. Currently it's just drawing all of them at once.
        for (int xIndex = 0; xIndex < Map.LOADED_AREA_X; xIndex++) {
            for (int yIndex = 0; yIndex < Map.LOADED_AREA_Y; yIndex++) {
                if (loadedChunks.get(new Point(xIndex, yIndex)) != null) {
                    loadedChunks.get(new Point(xIndex, yIndex)).Draw(graphics, screen);
                }
            }
        }
    }
    
    public Point getMapPosition() {
        return mapPosition;
    }
    public void moveMapPosition(Vector2D movementVector) {
        if (movementVector.x < 0) { //Left
            for (int movesX = -1; movesX >= movementVector.x; movesX--) {
                Point[] newColumn = new Point[Map.LOADED_AREA_Y];
                for (int columnIndex = 0; columnIndex < newColumn.length; columnIndex++) {
                    newColumn[columnIndex] = new Point(
                        (mapPosition.x + movesX) * MapChunk.CHUNK_WIDTH,
                        (mapPosition.y + columnIndex) * MapChunk.CHUNK_HEIGHT
                    );
                }
                
                LoadOperation thread = new LoadOperation(mapLoader, loadedChunks, LoadOperation.DIRECTION_LEFT, newColumn);
                thread.start();
                mapPosition.x--;
            }
        } else if (movementVector.x > 0) { //Right
            for (int movesX = 1; movesX <= movementVector.x; movesX++) {
                Point[] newColumn = new Point[Map.LOADED_AREA_Y];
                for (int columnIndex = 0; columnIndex < newColumn.length; columnIndex++) {
                    newColumn[columnIndex] = new Point(
                        ((mapPosition.x + Map.LOADED_AREA_X - 1) + movesX) * MapChunk.CHUNK_WIDTH,
                        (mapPosition.y + columnIndex) * MapChunk.CHUNK_HEIGHT
                    );
                }
                
                LoadOperation thread = new LoadOperation(mapLoader, loadedChunks, LoadOperation.DIRECTION_RIGHT, newColumn);
                thread.start();
                mapPosition.x++;
            }
        }
        
        if (movementVector.y < 0) { //Up
            for (int movesY = -1; movesY >= movementVector.y; movesY--) {
                Point[] newRow = new Point[Map.LOADED_AREA_X];
                for (int rowIndex = 0; rowIndex < newRow.length; rowIndex++) {
                    newRow[rowIndex] = new Point(
                        (mapPosition.x + rowIndex) * MapChunk.CHUNK_WIDTH,
                        (mapPosition.y + movesY) * MapChunk.CHUNK_HEIGHT
                    );
                }
                
                LoadOperation thread = new LoadOperation(mapLoader, loadedChunks, LoadOperation.DIRECTION_UP, newRow);
                thread.start();
                mapPosition.y--;
            }
        } else if (movementVector.y > 0) { //Down
            for (int movesY = 1; movesY <= movementVector.y; movesY++) {
                Point[] newRow = new Point[Map.LOADED_AREA_X];
                for (int rowIndex = 0; rowIndex < newRow.length; rowIndex++) {
                    newRow[rowIndex] = new Point(
                        (mapPosition.x + rowIndex) * MapChunk.CHUNK_WIDTH,
                        ((mapPosition.y + Map.LOADED_AREA_Y - 1) + movesY) * MapChunk.CHUNK_HEIGHT
                    );
                }
                
                LoadOperation thread = new LoadOperation(mapLoader, loadedChunks, LoadOperation.DIRECTION_DOWN, newRow);
                thread.start();
                mapPosition.y++;
            }
        }
    }
    public void centerOnWorldCoordinates(Point worldCoordinates) {
        Point chunkCoords = Map.WorldToChunk(worldCoordinates);
        Point adjustedMapPosition = new Point(mapPosition);
        adjustedMapPosition.x += (Map.LOADED_AREA_X / 2);
        adjustedMapPosition.y += (Map.LOADED_AREA_Y / 2);
        
        if (chunkCoords.equals(adjustedMapPosition) == true) { //If the map is already at centered on these coordinates:
            //Do nothing
        } else {
            moveMapPosition(
                new Vector2D(chunkCoords.x - adjustedMapPosition.x, chunkCoords.y - adjustedMapPosition.y) //Move the map's position by the difference of the points
            );
        }
    }
    
    public MapTile[][] getTilesWithinBounds(Rectangle bounds) {
        Point startTile = Map.WorldToMapTile(new Point(bounds.x, bounds.y)); //The upper-left corner (relative 0,0) of the resulting rectangle of map tiles
        Point endTile = Map.WorldToMapTile(new Point(bounds.x + bounds.width, bounds.y + bounds.height)); //The lower-right corner (largest relative x,y)
        Dimension dimensionsOfArea = new Dimension(
            new Point(endTile.x - startTile.x + 1, 0).x, //Subtract the TILE origin from the most far-right TILE coordinate to get the width in TILES
            new Point(0, endTile.y - startTile.y + 1).y //Do the same but for the height
        );
        
        if (dimensionsOfArea.width > 0 && dimensionsOfArea.height > 0) {
            MapTile[][] resultingTiles = new MapTile[dimensionsOfArea.width][dimensionsOfArea.height];
        
            Point currentChunk;
            Point localTile;
            for (int xIndex = startTile.x; xIndex < startTile.x + dimensionsOfArea.width; xIndex++) {
                for (int yIndex = startTile.y; yIndex < startTile.y + dimensionsOfArea.height; yIndex++) {
                    currentChunk = Map.MapTileToChunk(new Point(xIndex, yIndex)); //Get the chunk that this MapTile is in
                    localTile = Map.MapTileToLocalTile(new Point(xIndex, yIndex)); //Get the local index of the MapTile within its chunk
                    
                    if (isChunkLoaded(currentChunk, mapPosition) == true) { //If the chunk is currently loaded, add the appropriate tile to our array
                        resultingTiles[xIndex - startTile.x][yIndex - startTile.y] = loadedChunks.get(new Point(currentChunk.x - mapPosition.x, currentChunk.y - mapPosition.y)).getTile(localTile.x, localTile.y);
                    }
                }
            }
            resultingTiles = Map.cullUnloadedTiles(resultingTiles); //Remove any null tiles from the array before returning it
            
            return resultingTiles;
        } else {
            System.out.println("dimensionsOfArea not within bounds. Returning null.");
            return null;
        }
    }
    
    /**********************HELPER FUNCTIONS***********************/
    public static boolean isChunkLoaded(Point chunkCoords, Point mapPosition) { //Returns true if the chunk is within the current boundaries of loadedChunks[][]
        if (chunkCoords.x >= mapPosition.x && chunkCoords.x < (mapPosition.x + Map.LOADED_AREA_X)) { //Is the chunk within the X boundaries
            if (chunkCoords.y >= mapPosition.y && chunkCoords.y < (mapPosition.y + Map.LOADED_AREA_Y)) { //Is the chunk within the Y boundaries
                return true;
            }
        }
        return false;
    }
    
    /*
     * Takes a 2D plane of tiles and shrinks the array so as to not include any null tiles.
     * Assumes that the area containing valid tiles is contiguous, perfectly rectangular, and the empty space surrounds it.
     */
    public static MapTile[][] cullUnloadedTiles(MapTile[][] tileArray) {
        try {
            Point firstTile = null;
            Point lastTile = null;
            for (int xIndex = 0; xIndex < tileArray.length; xIndex++) {
                for (int yIndex = 0; yIndex < tileArray[0].length; yIndex++) {
                    if (tileArray[xIndex][yIndex] != null) { //If this location is a valid tile
                        if (firstTile == null) {
                            firstTile = new Point(xIndex, yIndex); //firstTile will hold the location of the very first valid tile found
                        }
                        lastTile =  new Point(xIndex, yIndex); //lastTile will hold the location of the last valid tile found
                    }
                }
            }
            
            Dimension loadedArea = new Dimension(lastTile.x - firstTile.x + 1, lastTile.y - firstTile.y + 1);
            
            MapTile[][] culledArray = new MapTile[loadedArea.width][loadedArea.height];
            
            //Copy over the valid section of tileArray
            for (int xIndex = firstTile.x; xIndex < lastTile.x + 1; xIndex++) {
                for (int yIndex = firstTile.y; yIndex < lastTile.y + 1; yIndex++) {
                    culledArray[xIndex - firstTile.x][yIndex - firstTile.y] = tileArray[xIndex][yIndex];
                }
            }
            
            return culledArray;
        } catch (NullPointerException exception) {
            System.out.println("Null pointer exception!");
            return null;
        }
    }
    
    /*
     * [TEST] Method that returns an arbitrary-sized array of plain grass MapTiles
     */
    public static MapTile[][] getTestMapTileArray() {
        final int xSize = 100;
        final int ySize = 100;
        
        final int nullMargin_Top = 5;
        final int nullMargin_Left = 2;
        final int nullMargin_Bottom = 0;
        final int nullMargin_Right = 2;
        
        MapTile[][] tileArray = new MapTile[xSize][ySize];
        
        TreeMap<Integer, SpriteIndex> spriteIndexList = new TreeMap<>();
        spriteIndexList.put(0, new SpriteIndex(TileCategory.GRASS, 15));
        for (int xIndex = 0; xIndex < xSize; xIndex++) {
            for (int yIndex = 0; yIndex < ySize; yIndex++) {
                if (yIndex < nullMargin_Top) {
                    tileArray[xIndex][yIndex] = null;
                } else if (xIndex < nullMargin_Left) {
                    tileArray[xIndex][yIndex] = null;
                } else if (yIndex >= ySize - nullMargin_Bottom) {
                    tileArray[xIndex][yIndex] = null;
                } else if (xIndex >= xSize - nullMargin_Right) {
                    tileArray[xIndex][yIndex] = null;
                } else {
                    tileArray[xIndex][yIndex] = new MapTile((xIndex * 32), (yIndex * 32), 0, true, spriteIndexList);
                }
            }
        }
        return tileArray;
    }
    
    public static Point WorldToMapTile(Point worldCoords) {
        Point tileCoords = new Point();
        tileCoords.x = (int) Math.floor((double)worldCoords.x / (double)MapTile.TILE_WIDTH);
        tileCoords.y = (int) Math.floor((double)worldCoords.y / (double)MapTile.TILE_HEIGHT);
        return tileCoords;
    }
    public static Point MapTileToWorld(Point tileCoords) {
        Point worldCoords = new Point();
        worldCoords.x = tileCoords.x * MapTile.TILE_WIDTH;
        worldCoords.y = tileCoords.y * MapTile.TILE_HEIGHT;
        return worldCoords;
    }
     public static Point MapTileToChunk(Point tileCoords) { //converts the GLOBAL tiles coordinates to the coordinates of the chunk it's located in
        Point chunkCoords = new Point();
        chunkCoords.x = (int) Math.floor((double)tileCoords.x / (double)MapChunk.NUMBER_OF_TILES_X);
        chunkCoords.y = (int) Math.floor((double)tileCoords.y / (double)MapChunk.NUMBER_OF_TILES_Y);
        return chunkCoords;
    }
    public static Point MapTileToLocalTile(Point tileCoords) { //takes the GLOBAL tile coordinates and gets the LOCAL tile coordinates for the respective chunk
        Point localCoords = new Point();
        localCoords.x = ((tileCoords.x % MapChunk.NUMBER_OF_TILES_X) + MapChunk.NUMBER_OF_TILES_X) % MapChunk.NUMBER_OF_TILES_X;
        localCoords.y = ((tileCoords.y % MapChunk.NUMBER_OF_TILES_Y) + MapChunk.NUMBER_OF_TILES_Y) % MapChunk.NUMBER_OF_TILES_Y;
        return localCoords;
    }
    public static Point WorldToChunk(Point worldCoords) {
        Point chunkCoords = new Point();
        chunkCoords.x = (int) Math.floor((double)worldCoords.x / (double)MapChunk.CHUNK_WIDTH);
        chunkCoords.y = (int) Math.floor((double)worldCoords.y / (double)MapChunk.CHUNK_HEIGHT);
        return chunkCoords;
    }
    public static Point ChunkToWorld(Point chunkCoords) {
        Point worldCoords = new Point();
        worldCoords.x = chunkCoords.x * MapChunk.CHUNK_WIDTH;
        worldCoords.y = chunkCoords.y * MapChunk.CHUNK_HEIGHT;
        return worldCoords;
    }
    
    /**************************CONSTANTS**************************/
    public static final int LOADED_AREA_X = 10;
    public static final int LOADED_AREA_Y = 10;
}