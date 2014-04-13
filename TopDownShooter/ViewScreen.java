package TopDownShooter;
import TopDownShooter.CollisionSystem.BoundingBox;

import java.awt.*;

public class ViewScreen {
    private static ViewScreen instance;
    private Rectangle screen;
    
    private ViewScreen() {
        screen = new Rectangle();
    }
    
    public static ViewScreen getInstance() {
        if (instance == null) {
            instance = new ViewScreen();
        }
        return instance;
    }
    
    public void CenterOnCoords(Point new_coords) {
        screen.x = (new_coords.x -(screen.width / 2));
        screen.y = (new_coords.y -(screen.height / 2));
    }
    public void CenterOnObject(BoundingBox objectBoundingBox) {
        screen.x = (int)(objectBoundingBox.x - (screen.width / 2) - objectBoundingBox.halfWidth);
        screen.y = (int)(objectBoundingBox.y - (screen.height / 2) - objectBoundingBox.halfHeight);
    }
    public void Move(Point movementVector) {
        screen.x = screen.x + movementVector.x;
        screen.y = screen.y + movementVector.y;
    }
    public void setSize(Rectangle new_size) {
        screen = new_size;
    }
    
    public Rectangle getScreen() {
        return screen;
    }
    public Point getCenterOfScreen() {
        return new Point(
            screen.x + (screen.width / 2),
            screen.y + (screen.height / 2)
        );
    }
    
    public boolean isObjectOnscreen(Rectangle object) {
        if (object.x < (screen.x + screen.width) && (object.x + object.width) > screen.x) {
            if (object.y < (screen.y + screen.height) && (object.y + object.height) > screen.y) {
                return true;
            }
        }
        return false;
    }
    
    /** Utility Methods **/
    public static boolean isObjectOnscreen(BoundingBox object, Rectangle screen) {
        Rectangle convertedBounds = object.toScreenBounds();
        
        if (convertedBounds.x < (screen.x + screen.width) && (convertedBounds.x + convertedBounds.width) > screen.x) {
            if (convertedBounds.y < (screen.y + screen.height) && (convertedBounds.y + convertedBounds.height) > screen.y) {
                return true;
            }
        }
        return false;
    }
}