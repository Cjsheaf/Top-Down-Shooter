package TopDownShooter.MapSystem;

import java.awt.*;

public class MapChunk {
    private Point worldCoords; //Coordinate of the upper-left pixel of the chunk
    private MapTile[][] mapTiles; //The local map tiles for this chunk. "Local" map tiles are references to this array's indexes
    
    public MapChunk() {
        this(new Point(0,0));
    }
    public MapChunk(Point chunkCoords) {
        mapTiles = new MapTile[MapChunk.NUMBER_OF_TILES_X][MapChunk.NUMBER_OF_TILES_Y];
        worldCoords = chunkCoords;
    }
    
    public void loadTile(int localTileX, int localTileY, MapTile new_tile) {
        if (mapTiles[localTileX][localTileY] == null) {
            mapTiles[localTileX][localTileY] = new_tile;
        }
    }
    public MapTile getTile(int localTileX, int localTileY) {
        return mapTiles[localTileX][localTileY];
    }
    public Point getWorldCoordinates() {
        return worldCoords;
    }    
    public void replaceTile(int localTileX, int localTileY, MapTile new_mapTile) { //Replaces the local map tile. Will eventually differ from loadTile in that replaceTile will perform sanity checking on input tile.
        //[TODO] Sanity-check the input tile.
        mapTiles[localTileX][localTileY] = new_mapTile;
    }
    
    public void replaceChunk(MapChunk newChunkData) { //Replaces this MapChunk's contents (worldCoords and mapTiles) with newChunkData's contents
        this.worldCoords = newChunkData.worldCoords;
        this.mapTiles = newChunkData.mapTiles;
    }

    public void Draw(Graphics graphics, Rectangle screen) {
        for (int xIndex = 0; xIndex < MapChunk.NUMBER_OF_TILES_X; xIndex++) {
            for (int yIndex = 0; yIndex < MapChunk.NUMBER_OF_TILES_Y; yIndex++) {
                if (mapTiles[xIndex][yIndex] != null) {
                    mapTiles[xIndex][yIndex].Draw(graphics, screen);
                }
            }
        }
    }
    
    /**************************CONSTANTS**************************/
    public static final int NUMBER_OF_TILES_X = 16;
    public static final int NUMBER_OF_TILES_Y = 16;
    public static final int CHUNK_WIDTH = MapChunk.NUMBER_OF_TILES_X * 32; //512 currently
    public static final int CHUNK_HEIGHT = MapChunk.NUMBER_OF_TILES_Y * 32; //512 currently
}