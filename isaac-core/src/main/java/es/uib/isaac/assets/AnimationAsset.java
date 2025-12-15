package es.uib.isaac.assets;

import com.google.common.collect.ImmutableList;
import es.uib.isaac.IsaacGame;
import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;

import static es.uib.isaac.Constants.FPS;

public class AnimationAsset implements Asset {
    private final List<PImage> animationFrames;
    private final String spritePath;
    private final int tileWidth, tileHeight;
    private int currentFrame, frameCounter;

    public AnimationAsset(String spritePath, int row, int size, int tileWidth, int tileHeight) {
        this.spritePath = spritePath;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.animationFrames = ImmutableList.copyOf(getAnimationFrames(row, size));
    }

    public AnimationAsset(String spritePath, int row, int size) {
        this(spritePath, row, size, WIDTH, HEIGHT);
    }

    /**
     * Get the list of all the frame animations in a list.
     *
     * @param row row to get the animation from.
     * @param size Size of the animation frames.
     * @return the loaded list with the frames.
     */
    private List<PImage> getAnimationFrames(int row, int size) {
        List<PImage> frames = new ArrayList<>(size);
        PImage spriteSheet = IsaacGame.INSTANCE.loadImage(spritePath);
        if (spriteSheet == null) {
            throw new RuntimeException("Could not load sprite sheet: " + spritePath);
        }

        if (size * tileWidth > spriteSheet.height) {
            throw new RuntimeException("Row " + row + " is too big");
        }

        // Slice horizontally: one row, multiple columns
        for (int x = 0; x < size; x++) {
            int sx = x * tileWidth;

            PImage pImage = spriteSheet.get(sx, row * tileHeight, tileWidth, tileWidth);
            frames.add(pImage);
        }

        if (frames.size() != size) {
            throw new RuntimeException("Size " + frames.size() + " does not match size " + size);
        }

        return frames;
    }

    @Override
    public void display(float xPos, float yPos) {
        frameCounter++;
        if (frameCounter >= FPS) {
            frameCounter = 0;
            currentFrame = (currentFrame + 1) % animationFrames.size();
        }
        IsaacGame.INSTANCE.image(animationFrames.get(currentFrame), xPos, yPos);
    }

    @Override
    public void display(float xPos, float yPos, int width, int height) {
        frameCounter++;
        if (frameCounter >= FPS) {
            frameCounter = 0;
            currentFrame = (currentFrame + 1) % animationFrames.size();
        }
        IsaacGame.INSTANCE.image(animationFrames.get(currentFrame), xPos, yPos, width, height);
    }
}
