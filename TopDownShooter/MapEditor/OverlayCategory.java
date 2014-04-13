package TopDownShooter.MapEditor;


public enum OverlayCategory {
    WALKABLE(0);
    
    public final int id;
    
    OverlayCategory(int new_id) {
        id = new_id;
    }
    
    public static OverlayCategory fromInteger(int x) {
        switch(x) {
            case 0: return WALKABLE;
        }
        return null;
    }
    
    public static final int numberOfElements = 1;
}