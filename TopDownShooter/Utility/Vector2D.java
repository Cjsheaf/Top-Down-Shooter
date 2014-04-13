package TopDownShooter.Utility;

import java.awt.geom.Point2D;
import java.awt.Graphics;

public class Vector2D extends Point2D.Double {
    /********************Constructors********************/
    public Vector2D() {
        super(0, 0);
    }
    public Vector2D(double xCoord, double yCoord) {
        super(xCoord, yCoord);
    }
    public Vector2D(Point2D.Double new_coords) {
        super(new_coords.x, new_coords.y);
    }
    public Vector2D(Vector2D vectorToCopy) {
        super(vectorToCopy.x, vectorToCopy.y);
    }
    
    /********************Data Operations********************/
    public void normalize() {
        //This code has trouble with negative numbers, but is faster:
        /*double lengthSquared = this.distanceSq(0, 0);
        
        this.x = (this.x * this.x) / lengthSquared;
        this.y = (this.y * this.y) / lengthSquared;*/
        
        double length = this.distance(0, 0);
        
        this.x = this.x / length;
        this.y = this.y / length;
    }
    
    /********************Other Operations********************/
    public void draw(Graphics screen, Point2D.Double origin) {
        screen.drawLine((int)origin.x, (int)origin.y, (int)(origin.x + this.x), (int)(origin.y + this.y));
    }
    
    /********************Static Utility Methods********************/
    public static double dotProduct(Vector2D vectorA, Vector2D vectorB) {
        return (vectorA.x * vectorB.x) + (vectorA.y * vectorB.y);
    }
    public static Vector2D projectAOntoB(Vector2D vectorA, Vector2D vectorB) {
        double dotProduct = Vector2D.dotProduct(vectorA, vectorB);
        
        Vector2D resultingVector = new Vector2D(
            (dotProduct / (vectorB.x * vectorB.x + vectorB.y * vectorB.y)) * vectorB.x,
            (dotProduct / (vectorB.x * vectorB.x + vectorB.y * vectorB.y)) * vectorB.y
        );
        return resultingVector;
    }
}