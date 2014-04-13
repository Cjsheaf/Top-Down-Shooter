package TopDownShooter.MapSystem;

public enum TileCategory {
    GRASS(0), DIRT(1), HOUSE_EXTERNAL(2), DUNGEON(3), OTHER(4);
    
    public final int id;
    
    TileCategory(int new_id) {
        id = new_id;
    }
    
    public static TileCategory fromInteger(int x) {
        switch(x) {
            case 0: return GRASS;
            case 1: return DIRT;
            case 2: return HOUSE_EXTERNAL;
            case 3: return DUNGEON;
            case 4: return OTHER;
        }
        return null;
    }
    
    public static final int numberOfElements = 5;
}
