package TopDownShooter.CollisionSystem;
import TopDownShooter.Utility.Vector2D;

import java.awt.Rectangle;

public class BoundingBox {
    public double x;
    public double y;
    public double halfWidth;
    public double halfHeight;
    
    public BoundingBox() {
        this(0, 0, 0, 0);
    }
    public BoundingBox(double new_x, double new_y, double new_halfWidth, double new_halfHeight) {
        x = new_x;
        y = new_y;
        halfWidth = new_halfWidth;
        halfHeight = new_halfHeight;
    }
    /*
     * Takes a Rectangle which has its origin in the upper-left corner
     */
    public BoundingBox(Rectangle screenBounds) {
        halfWidth = (double)screenBounds.width / 2;
        halfHeight = (double)screenBounds.height / 2;
        x = (double)screenBounds.x + halfWidth;
        y = (double)screenBounds.y + halfHeight;
    }
    
    public Rectangle toScreenBounds() {
        return new Rectangle(
            (int)Math.round(x - halfWidth),
            (int)Math.round(y - halfHeight),
            (int)Math.round(halfWidth * 2),
            (int)Math.round(halfHeight * 2)
        );
    }
    
    public static Vector2D detectCollision(BoundingBox box1, BoundingBox box2) { //Returns a vector signifying how far box1 intersects box2 (if they do)
        Vector2D resultingCollision = new Vector2D();
        
        if (box1.halfWidth + box2.halfWidth > Math.abs(box1.x - box2.x)) { //If the boxes overlap on the x axis
            if (box1.x < box2.x) {
                resultingCollision.x = Math.abs(box1.x - box2.x) - (box1.halfWidth + box2.halfWidth);
            } else {
                resultingCollision.x = (box1.halfWidth + box2.halfWidth) - Math.abs(box1.x - box2.x);
            }
        } else {
            return null;
        }
        
        if (box1.halfHeight + box2.halfHeight > Math.abs(box1.y - box2.y)) { //If the boxes overlap on the y axis
            if (box1.y < box2.y) {
                resultingCollision.y = Math.abs(box1.y - box2.y) - (box1.halfHeight + box2.halfHeight);
            } else {
                resultingCollision.y = (box1.halfHeight + box2.halfHeight) - Math.abs(box1.y - box2.y);
            }
        } else {
            return null;
        }
        
        //Keep only the shallower distance (Y-biased)
        if (Math.abs(resultingCollision.y) <= Math.abs(resultingCollision.x)) {
            resultingCollision.x = 0;
        } else {
            resultingCollision.y = 0;
        }
        
        return resultingCollision;
    }
}