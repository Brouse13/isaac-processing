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

    public void moveEntity() {
        switch (direction) {
            case NORTH -> posY -= stats.getSpeed();
            case SOUTH -> posY += stats.getSpeed();
            case EAST -> posX += stats.getSpeed();
            case WEST -> posX -= stats.getSpeed();
        }
    }

    public abstract void initialize();
    public abstract void update();
    public abstract void display();
}
