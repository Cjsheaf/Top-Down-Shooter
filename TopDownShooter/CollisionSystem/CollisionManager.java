package TopDownShooter.CollisionSystem;
import TopDownShooter.MapSystem.Map;
import TopDownShooter.MapSystem.MapTile;
import TopDownShooter.Utility.Vector2D;

import java.util.ArrayList;
import java.awt.Rectangle;

public class CollisionManager {
    private static CollisionManager instance = null;
    private ArrayList<Collidable> collidableList;
    Map map;
    
    private CollisionManager() {
        collidableList = new ArrayList<>();
        map = Map.getInstance();
    }
    
    public static CollisionManager getInstance() {
        if (instance == null) {
            instance = new CollisionManager();
        }
        return instance;
    }
    
    public void handleCollisions() {
        Collidable entity = null;
        for (int index = 0; index < collidableList.size(); index++) {
            entity = collidableList.get(index);
            
            while (collideWithEntities(entity) == true || collideWithWorld(entity) == true); //Keep resolving collisions on this entity until there are no more collisions
        }
    }
    
    public void addCollidableEntity(Collidable newEntity) {
        collidableList.add(newEntity);
    }
    public void removeCollidableEntity(Collidable existingEntity) {
        collidableList.remove(existingEntity);
    }
    
    private boolean collideWithWorld(Collidable entity) {
        Rectangle entityScreenCoords = entity.getBoundingBox().toScreenBounds();
        MapTile potentialCollisions[][] = map.getTilesWithinBounds(entityScreenCoords); //Get all MapTiles that this entity is touching
        Vector2D collisionVector = null;
        
        for (int x = 0; x < potentialCollisions.length; x++) {
            for (int y = 0; y < potentialCollisions[x].length; y++) {
                if (potentialCollisions[x][y] != null) {
                    if (potentialCollisions[x][y].isWalkable == false) {
                        collisionVector = BoundingBox.detectCollision(entity.getBoundingBox(), potentialCollisions[x][y].getBoundingBox());
                        if (collisionVector != null) { //If a collision was returned
                            entity.move(collisionVector);
                            return true; //entity collided with a MapTile
                        }
                    }
                }
            }
        }
        
        return false; //entity did not collide with the world
    }
    private boolean collideWithEntities(Collidable entity) {
        Vector2D collisionVector = null;
        
        for (int index = 0; index < collidableList.size(); index++) {
            if (collidableList.get(index).equals(entity) == false) {
                collisionVector = BoundingBox.detectCollision(entity.getBoundingBox(), collidableList.get(index).getBoundingBox());
                if (collisionVector != null) { //If a collision was returned
                    entity.move(collisionVector);
                    return true; //entity collided with another entity
                }
            }
        }
        return false; //entity did not collide with another entity
    }
}