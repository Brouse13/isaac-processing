package es.uib.isaac.assets;

import es.uib.isaac.IsaacGame;
import processing.core.PImage;

public class StaticResource implements Asset {
    private PImage image;
    private final int width, height;
    private final String path;

    public StaticResource(String path, int row, int col, int width, int height) {
        this.image = IsaacGame.INSTANCE.loadImage(path);
        this.width = width;
        this.height = height;
        this.path = path;
        loadImage(row, col);
    }

    public StaticResource(String path, int row, int col) {
        this(path, row, col, WIDTH, HEIGHT);
    }

    private void loadImage(int row, int col) {
        if (image == null) {
            throw new RuntimeException("Could not load sprite sheet: " + path);
        }

        // Extract specific tile
        int sx = col * width;
        int sy = row * height;
        this.image = image.get(sx, sy, width, height);
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
