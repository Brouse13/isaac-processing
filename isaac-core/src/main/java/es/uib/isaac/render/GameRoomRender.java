package es.uib.isaac.render;

import es.uib.isaac.Constants;
import es.uib.isaac.IsaacGame;
import es.uib.isaac.map.RoomLayout;
import es.uib.isaac.util.Vector2;
import processing.core.PImage;

import java.util.ArrayList;

public class GameRoomRender extends CameraRender implements TileRender {
    private final PImage image;

    public GameRoomRender(RoomLayout layout, String assetFile) {
        super(new ArrayList<>(), new Vector2(layout.width(), layout.height()));

        image = IsaacGame.INSTANCE.loadImage(assetFile);
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void initialize() {
        super.initialize();
    }

    @Override
    public void render() {
        float scaleX = Constants.WIDTH  / (float) VIEW_WIDTH_PX;
        float scaleY = Constants.HEIGHT / (float) VIEW_HEIGHT_PX;
        float scale  = Math.min(scaleX, scaleY);

        IsaacGame.INSTANCE.pushMatrix();
        IsaacGame.INSTANCE.scale(scale);

        int srcX = (int) cameraPosition.x;
        int srcY = (int) cameraPosition.y;

        IsaacGame.INSTANCE.image(image,
                0, 0, VIEW_WIDTH_PX, VIEW_HEIGHT_PX,
                srcX, srcY, srcX + VIEW_WIDTH_PX, srcY + VIEW_HEIGHT_PX
        );

        super.render();

        IsaacGame.INSTANCE.popMatrix();
    }
}
