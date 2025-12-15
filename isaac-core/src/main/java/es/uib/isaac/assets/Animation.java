package es.uib.isaac.assets;

import es.uib.isaac.IsaacGame;
import processing.core.PImage;

import static es.uib.isaac.Constants.FPS;

public class Animation implements Asset {
    public static final int TILE_WIDTH = 64;
    public static final int TILE_HEIGHT = 64;

    private final PImage[] frames;
    private final int frameCount;
    private int frame;
    private int frameCounter;

    /**
     * Loads frames from a single-row sprite sheet.
     *
     * @param spriteSheetPath Path to the sprite sheet image (e.g. "assets/animations/snake.png").
     * @param cols Number of frames (columns) in the sprite sheet.
     */
    public Animation(String spriteSheetPath, int row, int cols) {
        this.frameCount = cols;
        this.frames = new PImage[frameCount];

        // Load the animation file
        PImage spriteSheet = IsaacGame.INSTANCE.loadImage(spriteSheetPath);
        if (spriteSheet == null) {
            throw new RuntimeException("Could not load sprite sheet: " + spriteSheetPath);
        }

        if (row * TILE_HEIGHT + TILE_HEIGHT > spriteSheet.height) {
            throw new RuntimeException("Row " + row + " is too big");
        }

        // Slice horizontally: one row, multiple columns
        for (int x = 0; x < cols; x++) {
            int sx = x * TILE_WIDTH;

            if (sx + TILE_WIDTH <= spriteSheet.width) {
                frames[x] = spriteSheet.get(sx, row * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT);
            } else {
                throw new RuntimeException("Frame " + x + " exceeds sprite sheet width.");
            }
        }
    }

    @Override
    public void display(float xPos, float yPos) {
        frameCounter++;
        if (frameCounter >= FPS) {
            frameCounter = 0;
            frame = (frame + 1) % frameCount;
        }
        IsaacGame.INSTANCE.image(frames[frame], xPos, yPos);
    }

    @Override
    public void display(float xPos, float yPos, int width, int height) {
        frameCounter++;
        if (frameCounter >= FPS) {
            frameCounter = 0;
            frame = (frame + 1) % frameCount;
        }
        IsaacGame.INSTANCE.image(frames[frame], xPos, yPos, width, height);
    }
}
