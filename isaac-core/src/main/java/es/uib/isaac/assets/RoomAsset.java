package es.uib.isaac.assets;

import es.uib.isaac.IsaacGame;
import processing.core.PConstants;
import processing.core.PImage;

public class RoomAsset extends MultiTileAsset {
    private final byte[][] room;

    public RoomAsset(String tilePath, int rows, int cols, int width, int height, byte[][] room) {
        super(tilePath, rows, cols, width, height);
        this.room = room;
    }

    public RoomAsset(String tilePath, int rows, int cols, byte[][] room) {
        super(tilePath, rows, cols);
        this.room = room;
    }

    @Override
    public void display(float xPos, float yPos) {
        for (int row = 0; row < room.length; row++) {
            for (int col = 0; col < room[row].length; col++) {

                int value = room[row][col] & 0xFF;

                int rotation = (value >> 6) & 0b11;
                int tileIndex = value & 0b0011_1111;

                if (tileIndex >= tileAssets.size()) continue;

                float x = xPos + col * tileWidth;
                float y = yPos + row * tileHeight;

                PImage tile = tileAssets.get(tileIndex);

                // Rotate the tile
                IsaacGame.INSTANCE.pushMatrix();
                IsaacGame.INSTANCE.translate(x + tileWidth / 2f, y + tileHeight / 2f);
                IsaacGame.INSTANCE.rotate(rotation * PConstants.HALF_PI);
                IsaacGame.INSTANCE.image(tile, -tileWidth / 2f, -tileHeight / 2f);

                IsaacGame.INSTANCE.popMatrix();
            }
        }
    }

    @Override
    public void display(float xPos, float yPos, int width, int height) {
        throw new UnsupportedOperationException("Map can not be rescaled");
    }
}
