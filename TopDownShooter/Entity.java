package TopDownShooter;
import TopDownShooter.AnimationSystem.AnimationManager;
import TopDownShooter.ArtAssets.SpriteSheet;
import TopDownShooter.CollisionSystem.*;
import TopDownShooter.Utility.Vector2D;

import java.awt.*;

public class Entity implements Collidable {
    private Rectangle bounds; //x and y represent the center of the entity. Its boundaries for any given direction are half of either the width or height.
    private Vector2D velocity;
    private double speed;
    private double maxVelocity;
    private double friction; //A number between 0 and 1 representing the percentage of velocity this entity will lose per frame.
    private int health;
    private boolean canInteract;
    protected AnimationManager animationManager;
    
    public Entity(Rectangle new_bounds, double new_speed, double new_maxVelocity, double new_friction, int startingHealth) {
        bounds = new_bounds;
        velocity = new Vector2D(0,0);
        speed = new_speed;
        maxVelocity = new_maxVelocity;
        health = startingHealth;
        friction = new_friction;
        
        animationManager = new AnimationManager(50);
        CollisionManager collisionManager = CollisionManager.getInstance();
        collisionManager.addCollidableEntity(this);
    }
    
    public void Update() {
        //Currently empty. Declared anyway so children can override
    }
    
    public Point getPosition() {
        return new Point(bounds.x, bounds.y);
    }
    public BoundingBox getBoundingBox() {
        return new BoundingBox(bounds);
    }
    
    public void teleport(int new_x, int new_y) {
        bounds.x = new_x;
        bounds.y = new_y;
    }
    public void move(Vector2D movementVector) { //Primarily used by collision detection to move the entity out of obstacles
        bounds.x += movementVector.x;
        bounds.y += movementVector.y;
    }
    public void changeVelocity(Vector2D movementVector) {
        velocity.x += movementVector.x * speed;
        velocity.y += movementVector.y * speed;
        
        //Remove any excess velocity over maxVelocity
        double currentSpeedSq = velocity.distanceSq(0, 0);
        
        if (currentSpeedSq > (maxVelocity * maxVelocity)) { //If the entity's going too fast, get only its direction and multiply it by the maxSpeed
            Vector2D normalizedVelocity = new Vector2D(velocity);
            normalizedVelocity.normalize();
            
            velocity.x = normalizedVelocity.x * maxVelocity;
            velocity.y = normalizedVelocity.y * maxVelocity;
        }
    }
    private void updateDirection() {
        if (Math.abs(velocity.y) > Math.abs(velocity.x)) { //If the direction's y-magnitude exceeds its x-magnitude: (Left and Right animations take priority)
            if (velocity.y > 0) { //If the entity is moving upward:
                animationManager.ChangeAnimation(AnimationManager.ANIM_DOWN);
            } else if (velocity.y < 0) { //If the entity is moving downward:
                animationManager.ChangeAnimation(AnimationManager.ANIM_UP);
            }
        } else {
            if (velocity.x > 0) { //If the entity is moving right:
                animationManager.ChangeAnimation(AnimationManager.ANIM_RIGHT);
            } else if (velocity.x < 0) { //If the entity is moving left:
                animationManager.ChangeAnimation(AnimationManager.ANIM_LEFT);
            }
        }
        animationManager.Start();
    }
    protected void updatePosition() {
        bounds.x += velocity.x;
        bounds.y += velocity.y;
        velocity.x *= friction;
        velocity.y *= friction;
        
        //If the x-velocity is negligible, then make it zero (it will never quite reach zero otherwise)
        if ((velocity.x < 1) && (velocity.x > -1)) {
            velocity.x = 0;
        }
        //If the y-velocity is negligible, then do the same
        if ((velocity.y < 1) && (velocity.y > -1)) {
            velocity.y = 0;
        }
        
        if ((velocity.y == 0) && (velocity.x == 0)) { //If all movement has stopped:
            animationManager.Stop();
        }
        
        updateDirection();
    }
    
    public void Draw(Graphics graphics, Rectangle screen) {
        if (ViewScreen.isObjectOnscreen(this.getBoundingBox(), screen) == true) {
            graphics.drawImage(animationManager.GetCurrentImage(), bounds.x - screen.x, bounds.y - screen.y, null);
            
            if (DebugFlags.DEBUG_MODE == true && DebugFlags.BOUNDING_BOXES_VISUALIZED == true) { //[DEBUG]
                graphics.setColor(Color.BLUE);
                Rectangle temp = this.getBoundingBox().toScreenBounds();
                graphics.drawRect(temp.x - screen.x, temp.y - screen.y, temp.width, temp.height);
            }
        }
    }
    
    /****************CONSTANTS****************/
    public static final int ENTITY_UP = 0;
    public static final int ENTITY_LEFT = 1;
    public static final int ENTITY_DOWN = 2;
    public static final int ENTITY_RIGHT = 3;
}