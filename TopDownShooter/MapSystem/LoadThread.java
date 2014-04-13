package TopDownShooter.MapSystem;

import java.awt.Point;

public class LoadThread extends Thread {
    private MapLoader mapLoaderReference;
    private MapChunk chunkReference; //A reference to the location for the chunk we need to load
    private Point worldCoordinates; //The world coordinates of the chunk we need to load

    public LoadThread(MapLoader mapLoaderReference, MapChunk chunkReference, Point worldCoordinates) {
        this.mapLoaderReference = mapLoaderReference;
        this.chunkReference = chunkReference;
        this.worldCoordinates = worldCoordinates;
    }

    public void run() {
        synchronized(chunkReference) {
            chunkReference.replaceChunk(mapLoaderReference.getChunk(worldCoordinates));
        }
    }
}