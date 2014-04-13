package TopDownShooter.MapSystem;
import TopDownShooter.ArtAssets.MapResources;
import TopDownShooter.ViewScreen;

import java.util.*;
import java.io.*;
import java.util.Random;
import java.awt.*;
import java.util.Map;

public class MapLoader {
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *  MapLoader is responsible for managing the map files which store tile data and prop data.                                                                                            *
     *                                                                                                                                                                                      *
     *  The format for chunk blob files is:                                                                                                                                                 *
     *      - Chunk WORLD X-Coordinate                                                                                                                                                      *
     *      - Chunk WORLD Y-Coordinate                                                                                                                                                      *
     *      - As many MapTiles as a MapChunk is configured to hold (as per its constants) organized by listing every Row value (from top to bottom) in a given Column before moving on to   *
     *        the next Column. This is the format for each MapTile:                                                                                                                         *
     *          > numberOfSprites for this MapTile                                                                                                                                          *
     *          > A variable number of spriteIndexes. The number of spriteIndexes present is learned from the variable numberOfSprites.                                                     *
     *          > The tile's WORLD X-Coordinate                                                                                                                                             *
     *          > The tile's WORLD Y-Coordinate                                                                                                                                             *
     *          > The tile's tileType                                                                                                                                                       *
     *          > The tile's isWalkable flag                                                                                                                                                *
     *                                                                                                                                                                                      *
     *  An improvement for later will be to cache frequently used chunks in memory somewhere here in MapLoader. getChunk and saveChunk have been abstracted from loadChunkFromFile and      *
     *  saveChunkToFile, respectively, to accomodate this possibility.                                                                                                                      *
     *  In this scenario, getChunk should first check the cache for a requested chunk, and only then look to file if it's not found in-cache.                                               *
     *  It's likely that saveChunk didn't need to be abstracted since unloaded chunks should be comitted to file as soon as they're made anyway, but it's possible that it might need to    *
     *  be used to calculate which chunks are used most often, so I abstracted it as well anyway.                                                                                           *
     *                                                                                                                                                                                      *
     *  The method getChunk should work by checking for an existing chunk (either in-cache or in-file) and if found, load and return that. If, however, the chunk is not found (which       *
     *  means it hasn't been created yet) then the map seed should be used to generate a new chunk.                                                                                         *
     *                                                                                                                                                                                      *
     *  A possible problem that I can forsee with this is the game hanging momentarily whenever a chunk is loaded or saved (and especially if a new one is being generated). The proper     *
     *  solution would be to offload every getChunk and saveChunk call to a new thread, but that's something I have yet to learn how to properly do at the time of this writing.            *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private Random randomGen;
    private MapResources mapResources;
    private long mapGeneratorSeed;
    private String worldName;
    
    public MapLoader(String new_worldName) {
        randomGen = new Random();
        mapResources = MapResources.getInstance();
        worldName = new_worldName;
        
        File mapDirectory = new File("Worlds/" + worldName);
        if (mapDirectory.exists() == false) { //If there is no folder for this world
            mapDirectory.mkdirs();
        }
    }
    
    public MapChunk getChunk(Point chunkWorldCoords) {
        //[TODO]should first check for the chunk in cache, and then in file, and if neither of them exist, it should call generateNewChunk() and then immediately save it using saveChunkToFile(), before finally returning it
        MapChunk retrievedChunk = loadChunkFromFile(chunkWorldCoords);
        if (retrievedChunk != null) { //If the requested chunk exists, return it
            return retrievedChunk;
        } else { //Otherwise, generate a new chunk
            MapChunk newChunk = getTestChunk(chunkWorldCoords);
            saveChunk(newChunk);
            return newChunk;
        }
    }
    public void saveChunk(MapChunk chunkToSave) {
        saveChunkToFile(chunkToSave);
    }
    
    private MapChunk loadChunkFromFile(Point chunkWorldCoords) {
        //[TODO]load the specified chunk from file. The file should always have been created already by getChunk()
        Point chunkCoords = TopDownShooter.MapSystem.Map.WorldToChunk(chunkWorldCoords);
        String filePath = new String("Worlds/" + worldName + "/X-" + chunkCoords.x + " Y-" + chunkCoords.y + ".blob");
        
        File chunkFile = new File(filePath);
        if (chunkFile.exists() == false) {
            return null; //If the file doesn't exist, return null early. getChunk() should check for null and generate a new chunk if null is returned. Otherwise, continue execution of loadChunkFromFile()
        }
        
        MapChunk loadedChunk = null;
        try {
            DataInputStream dataStreamIn = new DataInputStream(new FileInputStream(chunkFile));
            
            loadedChunk = new MapChunk(new Point(dataStreamIn.readInt(), dataStreamIn.readInt())); //Create a new chunk with the world coordinates stored in file
            
            int numberOfSprites;
            MapTile tempTile;
            TreeMap<Integer, SpriteIndex> spriteIndexMap;
            for (int xIndex = 0; xIndex < MapChunk.NUMBER_OF_TILES_X; xIndex++) {
                for (int yIndex = 0; yIndex < MapChunk.NUMBER_OF_TILES_Y; yIndex++) {
                    spriteIndexMap = new TreeMap<Integer, SpriteIndex>();
                    numberOfSprites = dataStreamIn.readInt();
                    for (int spriteIterator = 0; spriteIterator < numberOfSprites; spriteIterator++) {
                        spriteIndexMap.put(dataStreamIn.readInt(), new SpriteIndex(TileCategory.fromInteger(dataStreamIn.readInt()), dataStreamIn.readInt())); //Read the layer and then read the SpriteIndex, and put them into the IndexList
                    }
                    
                    tempTile = new MapTile(dataStreamIn.readInt(), dataStreamIn.readInt(), dataStreamIn.readInt(), dataStreamIn.readBoolean(), spriteIndexMap);
                    loadedChunk.loadTile(xIndex, yIndex, tempTile);
                }
            }
            
            dataStreamIn.close();
        } catch (IOException e) {
            System.out.println("Error reading from chunk: " + filePath);
            e.printStackTrace();
        }
        
        return loadedChunk;
    }
    private void saveChunkToFile(MapChunk chunkToSave) {
        Point chunkWorldCoords = chunkToSave.getWorldCoordinates();
        Point chunkCoords = TopDownShooter.MapSystem.Map.WorldToChunk(chunkWorldCoords);
        
        String folderPath = new String("Worlds/" + worldName);
        String filePath = new String("Worlds/" + worldName + "/X-" + chunkCoords.x + " Y-" + chunkCoords.y + ".blob");
        
        File folderStructure = new File(folderPath);
        File chunkFile = new File(filePath);
        
        if (chunkFile.exists() == false) {
            try {
                folderStructure.mkdirs();
                chunkFile.createNewFile();
            } catch (IOException e) {
                System.out.println("Chunk with filepath: ' " + filePath + " ' failed to be created");
                e.printStackTrace();
            }
        }
        
        try {
            DataOutputStream dataStreamOut = new DataOutputStream(new FileOutputStream(chunkFile));
            
            //Store the worldX and worldY of the MapChunk
            dataStreamOut.writeInt(chunkWorldCoords.x);
            dataStreamOut.writeInt(chunkWorldCoords.y);
            
            //Cycle through all of the Map Tiles and store their data;
            MapTile tempTile;
            int numberOfSprites;
            for (int xIndex = 0; xIndex < MapChunk.NUMBER_OF_TILES_X; xIndex++) {
                for (int yIndex = 0; yIndex < MapChunk.NUMBER_OF_TILES_Y; yIndex++) {
                    tempTile = chunkToSave.getTile(xIndex, yIndex);
                    numberOfSprites = tempTile.getSpriteIndexes().size();
                    
                    dataStreamOut.writeInt(numberOfSprites);
                    for (Map.Entry<Integer, SpriteIndex> currentEntry : tempTile.getSpriteIndexes().entrySet()) {
                        dataStreamOut.writeInt(currentEntry.getKey()); //Write the layer this sprite is on
                        dataStreamOut.writeInt(currentEntry.getValue().spriteCategory.id); //Write the sprite's category
                        dataStreamOut.writeInt(currentEntry.getValue().spriteNumber); //Write the sprite's number within its category
                    }
                    
                    dataStreamOut.writeInt(tempTile.worldX);
                    dataStreamOut.writeInt(tempTile.worldY);
                    dataStreamOut.writeInt(tempTile.tileType);
                    dataStreamOut.writeBoolean(tempTile.isWalkable);
                }
            }
            
            dataStreamOut.close();
        } catch (IOException e) {
            System.out.println("Error writing to chunk: " + filePath);
            e.printStackTrace();
        }
    }
    private MapChunk generateNewChunk(Point chunkWorldCoords) {
        //[TODO]using the mapGeneratorSeed, randomly generate a new chunk
        return null;
    }
    
    public MapChunk getTestChunk(Point chunkWorldCoords) {
        MapChunk testChunk = new MapChunk(chunkWorldCoords);
        
        int randomGrassSprite;
        TreeMap<Integer, SpriteIndex> spriteIndexList;
        MapTile tempTile;
        for (int xIndex = 0; xIndex < MapChunk.NUMBER_OF_TILES_X; xIndex++) {
            for (int yIndex = 0; yIndex < MapChunk.NUMBER_OF_TILES_Y; yIndex++) {
                randomGrassSprite = randomGen.nextInt(3) + 15;
                spriteIndexList = new TreeMap<Integer, SpriteIndex>();
                spriteIndexList.put(0, new SpriteIndex(TileCategory.GRASS, randomGrassSprite));
                
                tempTile = new MapTile(((xIndex * 32) + chunkWorldCoords.x), ((yIndex * 32) + chunkWorldCoords.y), 0, true, spriteIndexList);
                testChunk.loadTile(xIndex, yIndex, tempTile);
            }
        }
        
        return testChunk;
    }
}
