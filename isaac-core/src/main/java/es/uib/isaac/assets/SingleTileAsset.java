package es.uib.isaac.assets;

import es.uib.isaac.IsaacGame;
import processing.core.PImage;

public class SingleTileAsset implements Asset {
    private PImage image;
    private final int tileWidth, tileHeight;
    private final String animationPath;

    public SingleTileAsset(String animationPath, int row, int col, int tileWidth, int tileHeight) {
        this.image = IsaacGame.INSTANCE.loadImage(animationPath);
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.animationPath = animationPath;
        loadImage(row, col);
    }

    public SingleTileAsset(String animationPath, int row, int col) {
        this(animationPath, row, col, WIDTH, HEIGHT);
    }

    private void loadImage(int row, int col) {
        if (image == null) {
            throw new RuntimeException("Could not load sprite sheet: " + animationPath);
        }

        // Extract specific tile
        int sx = col * tileWidth;
        int sy = row * tileHeight;
        this.image = image.get(sx, sy, tileWidth, tileHeight);
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
