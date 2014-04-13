package TopDownShooter.MapEditor;
import TopDownShooter.ArtAssets.MapResources;
import TopDownShooter.MapSystem.MapTile;

import java.awt.*;

public class EditorOverlayManager {
    //Singleton instances:
    private SelectionModel selection;
    private MapResources mapResources;
    
    public EditorOverlayManager() {
        selection = SelectionModel.getInstance();
        mapResources = MapResources.getInstance();
    }
    
    public void drawOverlays(Graphics graphics, MapTile[][] tilesToOverlay, Rectangle screen) {
        if (selection.getSelectionType() == SelectionType.OVERLAYS) {
            switch ((OverlayCategory)selection.getSelectionCategory()) {
                case WALKABLE:
                    drawWalkableOverlay(graphics, tilesToOverlay, screen);
                    break;
            }
        }
    }
    
    private void drawWalkableOverlay(Graphics graphics, MapTile[][] tilesToOverlay, Rectangle screen) {
        for (int xIndex = 0; xIndex < tilesToOverlay.length; xIndex++) {
            for (int yIndex = 0; yIndex < tilesToOverlay[xIndex].length; yIndex++) {
                if (tilesToOverlay[xIndex][yIndex].isWalkable == true) {
                    graphics.drawImage(mapResources.getSprite(OverlayCategory.WALKABLE, 0), (tilesToOverlay[xIndex][yIndex].worldX - screen.x), (tilesToOverlay[xIndex][yIndex].worldY - screen.y), null);
                } else {
                    graphics.drawImage(mapResources.getSprite(OverlayCategory.WALKABLE, 1), (tilesToOverlay[xIndex][yIndex].worldX - screen.x), (tilesToOverlay[xIndex][yIndex].worldY - screen.y), null);
                }
            }
        }
    }
}