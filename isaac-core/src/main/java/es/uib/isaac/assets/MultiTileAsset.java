package es.uib.isaac.assets;

import com.google.common.collect.ImmutableList;
import es.uib.isaac.IsaacGame;
import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;

public abstract class MultiTileAsset implements Asset {

    protected final List<PImage> tileAssets;
    protected final int tileWidth, tileHeight;
    private final String tilePath;

    public MultiTileAsset(String tilePath, int rows, int cols, int tileWidth, int tileHeight) {
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.tilePath = tilePath;
        this.tileAssets = ImmutableList.copyOf(loadImage(rows, cols));
    }

    public MultiTileAsset(String path, int rows, int cols) {
        this(path, rows, cols, WIDTH, HEIGHT);
    }

    private List<PImage> loadImage(int rows, int cols) {
        List<PImage> frames = new ArrayList<>(rows * cols);
        PImage spriteSheet = IsaacGame.INSTANCE.loadImage(tilePath);
        if (spriteSheet == null) {
            throw new RuntimeException("Could not load sprite sheet: " + tilePath);
        }

        if (cols * tileWidth > spriteSheet.width || rows * tileHeight > spriteSheet.height) {
            throw new RuntimeException("Size of the multiTile does not match");
        }

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int sx = col * tileWidth;
                int sy = row * tileHeight;

                PImage pImage = spriteSheet.get(sx, sy, tileWidth, tileHeight);
                frames.add(pImage);
            }
        }

        if (frames.size() != rows * cols) {
            throw new RuntimeException("Size " + frames.size() + " does not match size " + (rows * cols));
        }

        return frames;
    }
}
