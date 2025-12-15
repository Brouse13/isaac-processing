package es.uib.isaac.assets;

import es.uib.isaac.IsaacGame;
import processing.core.PImage;

public class StaticResource implements Asset {
    private final PImage image;

    public StaticResource(String path, int row, int col) {
        PImage spriteSheet = IsaacGame.INSTANCE.loadImage(path);
        if (spriteSheet == null) { throw new RuntimeException("Could not load sprite sheet: " + path); }

        // Extract specific tile
        int sx = col * SIZE_X;
        int sy = row * SIZE_Y;
        image = spriteSheet.get(sx, sy, SIZE_X, SIZE_Y);
    }

    @Override
    public void display(float xPos, float yPos) {
        IsaacGame.INSTANCE.image(image, xPos, yPos);
    }

    @Override
    public void display(float xPos, float yPos, int width, int height) {
        IsaacGame.INSTANCE.image(image, xPos, yPos, width, height);
    }
}
