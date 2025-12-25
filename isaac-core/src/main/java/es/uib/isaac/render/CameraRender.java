package es.uib.isaac.render;

import es.uib.isaac.Constants;
import es.uib.isaac.entity.Entity;
import es.uib.isaac.util.Vector2;
import lombok.Getter;

import java.util.List;

@Getter
public class CameraRender {
    // Viewport size in tiles
    private static final int VIEW_TILES_X = 15;
    private static final int VIEW_TILES_Y = 11;

    private final List<Entity> entities;
    private Entity target;

    // Camera position in WORLD PIXELS (top-left)
    protected final Vector2 cameraPosition = new Vector2(0, 0);

    // Viewport size in tiles
    protected final Vector2 viewPortSize = new Vector2(VIEW_TILES_X, VIEW_TILES_Y);

    // World bounds in tiles
    private final Vector2 maxBounds;

    protected float tileWidth, tileHeight;

    public CameraRender(List<Entity> entities, Vector2 worldSize) {
        this.entities = entities;
        this.maxBounds = worldSize;
    }

    public void follow(Entity entity) {
        this.target = entity;
    }

    public void initialize() {
        updateTileSize();
    }

    public void update() {
        updateTileSize();
        updateCamera();
    }

    private void updateTileSize() {
        tileWidth = Constants.WIDTH / viewPortSize.x;
        tileHeight = Constants.HEIGHT / viewPortSize.y;
    }

    private void updateCamera() {
        if (target == null) return;

        float viewWidthPx  = viewPortSize.x * tileWidth;
        float viewHeightPx = viewPortSize.y * tileHeight;

        float worldWidthPx  = maxBounds.x * tileWidth;
        float worldHeightPx = maxBounds.y * tileHeight;

        // 1. Center camera on target
        cameraPosition.x = target.getPosX() - viewWidthPx / 2f;
        cameraPosition.y = target.getPosY() - viewHeightPx / 2f;

        boolean canScrollX = worldWidthPx > viewWidthPx;
        boolean canScrollY = worldHeightPx > viewHeightPx;

        if (canScrollX) {
            cameraPosition.x = constrain(cameraPosition.x, 0, worldWidthPx - viewWidthPx);
        } else {
            cameraPosition.x = (worldWidthPx - viewWidthPx) / 2f;
        }

        if (canScrollY) {
            cameraPosition.y = constrain(cameraPosition.y, 0, worldHeightPx - viewHeightPx);
        } else {
            cameraPosition.y = (worldHeightPx - viewHeightPx) / 2f;
        }
    }

    private float constrain(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

    public void render() {
        for (Entity entity : entities) {
            // if (!isVisible(entity)) continue;

            int screenX = (int) (entity.getPosX() - cameraPosition.x);
            int screenY = (int) (entity.getPosY() - cameraPosition.y);

            entity.display(screenX, screenY);
        }
    }

    private boolean isVisible(Entity entity) {
        float viewWidthPx  = viewPortSize.x * tileWidth;
        float viewHeightPx = viewPortSize.y * tileHeight;

        float x = entity.getPosX();
        float y = entity.getPosY();

        return x >= cameraPosition.x &&
                x < cameraPosition.x + viewWidthPx &&
                y >= cameraPosition.y &&
                y < cameraPosition.y + viewHeightPx;
    }
}
