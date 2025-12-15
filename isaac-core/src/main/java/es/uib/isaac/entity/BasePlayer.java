package es.uib.isaac.entity;

import es.uib.isaac.assets.Animation;

import java.util.UUID;

public class BasePlayer extends Player {
    private static Animation isaac_north, isaac_south, isaac_east, isaac_west;

    public BasePlayer(int posX, int posY) {
        super(UUID.randomUUID(), posX, posY);
    }

    @Override
    public void initialize() {
        isaac_north = new Animation("assets/players/isaac.png", 3, 4);
        isaac_south = new Animation("assets/players/isaac.png", 0, 4);
        isaac_east = new Animation("assets/players/isaac.png", 2, 4);
        isaac_west = new Animation("assets/players/isaac.png", 1, 4);
        setAsset(isaac_north);

        this.getStats().setSpeed(1.0f);
        this.getLiveContainers().setContainers(6);
        this.getLiveContainers().setLives(4);
        this.getLiveContainers().getExtraContainers().add(LiveContainer.ExtraContainer.BLUE);
        this.getLiveContainers().getExtraContainers().add(LiveContainer.ExtraContainer.BLUE);
        this.getLiveContainers().setHollyMantel(true);
    }

    @Override
    public void update() {
        moveEntity();
        switch (getDirection()) {
            case NORTH -> setAsset(isaac_north);
            case SOUTH -> setAsset(isaac_south);
            case EAST -> setAsset(isaac_east);
            case WEST -> setAsset(isaac_west);
        }
    }

    @Override
    public void display() {
        this.getAsset().display(getPosX(), getPosY());
    }
}
