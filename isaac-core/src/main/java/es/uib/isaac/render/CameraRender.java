package es.uib.isaac.render;

import es.uib.isaac.entity.Entity;
import es.uib.isaac.util.Vector2;
import lombok.Getter;

import java.util.List;

@Getter
public class CameraRender {
    protected static final int VIEW_WIDTH_PX  = 960;
    protected static final int VIEW_HEIGHT_PX = 704;

    private final List<Entity> entities;
    private Entity target;

    protected final Vector2 cameraPosition = new Vector2(0, 0);

    // World bounds in tiles
    private final Vector2 maxBounds;

    protected static final int TILE_SIZE = 64;

    protected final float tileWidth  = TILE_SIZE;
    protected final float tileHeight = TILE_SIZE;

    public CameraRender(List<Entity> entities, Vector2 worldSize) {
        this.entities = entities;
        this.maxBounds = worldSize;
    }

    public void follow(Entity entity) {
        this.target = entity;
    }

    public void initialize() {
    }

    public void update() {
        updateCamera();
    }


    private void updateCamera() {
        if (target == null) return;

        float viewWidthPx  = VIEW_WIDTH_PX;
        float viewHeightPx = VIEW_HEIGHT_PX;

        float worldWidthPx  = maxBounds.x * tileWidth;
        float worldHeightPx = maxBounds.y * tileHeight;

        // 1. Center camera on target
        cameraPosition.x = target.getPosX() - viewWidthPx / 2f;
        cameraPosition.y = target.getPosY() - viewHeightPx / 2f;

        boolean canScrollX = worldWidthPx > viewWidthPx;
        boolean canScrollY = worldHeightPx > viewHeightPx;

        if (canScrollX) {
            cameraPosition.x = constrain(cameraPosition.x, worldWidthPx - viewWidthPx);
        } else {
            cameraPosition.x = (worldWidthPx - viewWidthPx) / 2f;
        }

        if (canScrollY) {
            cameraPosition.y = constrain(cameraPosition.y, worldHeightPx - viewHeightPx);
        } else {
            cameraPosition.y = (worldHeightPx - viewHeightPx) / 2f;
        }
    }

    private float constrain(float value, float max) {
        return Math.max(0f, Math.min(max, value));
    }

    public void render() {
        for (Entity entity : entities) {
            if (!isVisible(entity)) continue;

            int screenX = (int) (entity.getPosX() - cameraPosition.x);
            int screenY = (int) (entity.getPosY() - cameraPosition.y);

            entity.display(screenX, screenY);
        }
    }

    private boolean isVisible(Entity entity) {
        float x = entity.getPosX();
        float y = entity.getPosY();

        return x >= cameraPosition.x &&
                x < cameraPosition.x + VIEW_WIDTH_PX &&
                y >= cameraPosition.y &&
                y < cameraPosition.y + VIEW_HEIGHT_PX;
    }
}
