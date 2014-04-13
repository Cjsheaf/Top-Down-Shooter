package TopDownShooter.CollisionSystem;
import TopDownShooter.Utility.Vector2D;

public interface Collidable {
    BoundingBox getBoundingBox();
    void move(Vector2D newCoordinates);
}