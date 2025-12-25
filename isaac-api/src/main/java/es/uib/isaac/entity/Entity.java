package es.uib.isaac.entity;

import es.uib.isaac.assets.Asset;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public abstract class Entity {
    private final UUID uuid;
    private final Stats stats = new Stats();

    private Asset asset;
    private Direction direction = Direction.NORTH;
    private float posX, posY;

    public Entity(UUID uuid, int posX, int posY) {
        this.uuid = uuid;
        this.posX = posX;
        this.posY = posY;
    }

    public void moveEntity(float deltaTime) {
        double speed = stats.getSpeed();

        double dx = 0, dy = 0;

        switch (direction) {
            case NORTH -> dy = -1;
            case SOUTH -> dy = 1;
            case EAST -> dx = 1;
            case WEST -> dx = -1;
            case NORTH_EAST -> { dx = 1; dy = -1; }
            case NORTH_WEST -> { dx = -1; dy = -1; }
            case SOUTH_EAST -> { dx = 1; dy = 1; }
            case SOUTH_WEST -> { dx = -1; dy = 1; }
        }

        if (dx != 0 && dy != 0) {
            double invSqrt2 = 1 / Math.sqrt(2);
            dx *= invSqrt2;
            dy *= invSqrt2;
        }

        posX += (float) (dx * speed * deltaTime);
        posY += (float) (dy * speed * deltaTime);
    }

    public abstract void initialize();
    public abstract void update(float deltaTime);
    public abstract void display(float posX, float posY);
}
