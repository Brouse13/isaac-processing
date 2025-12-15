package es.uib.isaac.entity;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
public abstract class Player extends Entity {
    private final Set<Item> inventory = new HashSet<>();
    private final LiveContainer liveContainers = new LiveContainer();

    public Player(UUID uuid, int posX, int posY) {
        super(uuid, posX, posY);
    }
}
