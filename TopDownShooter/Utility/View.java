package TopDownShooter.Utility;

/**
 * Receives notifications from a class that extends Model (as part of
 * the Model-View-Controller design pattern) and updates its display
 * as needed.
 */
public interface View {
    void notifyChange();
}
