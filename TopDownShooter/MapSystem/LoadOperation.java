package TopDownShooter.MapSystem;

import TopDownShooter.Utility.Circular2DArray;

import java.awt.Point;
import java.awt.event.ActionListener;
import java.util.Random;

@SuppressWarnings({"unchecked"})
public class LoadOperation extends Thread {
    MapLoader mapLoaderReference;
    private Circular2DArray arrayReference;
    private int direction;
    private Point worldCoordinates[];

    private static Object handle = new Object(); //All LoadOperations will have this particular instance, which can be synchronized on
    
    public LoadOperation(MapLoader mapLoaderReference, Circular2DArray arrayReference, int direction, Point[] worldCoordinates) {
        this.mapLoaderReference = mapLoaderReference;
        this.arrayReference = arrayReference; //Store a reference to the array we are operating on
        this.direction = direction;
        this.worldCoordinates = worldCoordinates;
    }
    
    public void run() {
        synchronized(handle) { //Get a hold on the flag object so that only one LoadOperation at a time may execute
            MapChunk[] newMapSegment = new MapChunk[arrayReference.size().x];
            Thread[] loadThreads = new Thread[arrayReference.size().x];

            for (int index = 0; index < newMapSegment.length; index++) {
                newMapSegment[index] = new MapChunk();
                loadThreads[index] = new LoadThread(mapLoaderReference, newMapSegment[index], worldCoordinates[index]);
                loadThreads[index].start();
            }
            
            try {
                for (int i = 0; i < loadThreads.length; i++) {
                    loadThreads[i].join(); //Tell each thread to exit. This operation will block until each thread is finished, so any code after this loop will only execute once ALL threads are finished
                }
            } catch (InterruptedException exception) {
                System.exit(2);
            }
            
            switch (direction) {
                case DIRECTION_LEFT:
                    arrayReference.pushLeft(newMapSegment);
                    break;
                case DIRECTION_RIGHT:
                    arrayReference.pushRight(newMapSegment);
                    break;
                case DIRECTION_UP:
                    arrayReference.pushTop(newMapSegment);
                    break;
                case DIRECTION_DOWN:
                    arrayReference.pushBottom(newMapSegment);
                    break;
            }
        }
    }

    public static final int DIRECTION_LEFT = 0;
    public static final int DIRECTION_RIGHT = 1;
    public static final int DIRECTION_UP = 2;
    public static final int DIRECTION_DOWN = 3;
}
