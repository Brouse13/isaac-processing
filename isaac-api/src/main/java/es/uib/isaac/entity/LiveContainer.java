package es.uib.isaac.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayDeque;
import java.util.Queue;

@Getter
@Setter
public class LiveContainer {
    public enum ExtraContainer { BLUE }

    private final Queue<ExtraContainer> extraContainers = new ArrayDeque<>();
    private int lives = 0;
    private int containers = 0;
    private boolean hollyMantel = false;

    /**
     * Adds an extra (temporary) heart container, like a blue heart.
     *
     * @param extraContainer extra container to add
     */
    public void add(ExtraContainer extraContainer) {
        extraContainers.add(extraContainer);
    }

    /**
     * Heals by the given amount, capped at available containers.
     *
     * @param healAmount Amount health to heal, capped at max containers.
     */
    public void heal(int healAmount) {
        lives += healAmount;
        if (lives > containers) lives = containers;
    }

    /**
     * Adds normal heart containers. If heal=true, fills them.
     *
     * @param amount Number of containers added.
     * @param heal If true, this amount will be healed.
     */
    public void addNormalContainer(int amount, boolean heal) {
        containers += amount;
        if (heal) lives = Math.min(lives + amount, containers);
    }

    /**
     * Deals damage to the container.
     * First removes extra containers (blue hearts), then reduces normal lives.
     *
     * @param damage The damage to take (usually 1 or 2).
     */
    public void damage(int damage) {
        int remainingDamage = damage;

        while (remainingDamage > 0 && !extraContainers.isEmpty()) {
            extraContainers.poll();
            remainingDamage--;
        }

        if (remainingDamage >= lives) {
            lives = 0;
        } else {
            lives -= remainingDamage;
        }
    }

    public int getExtraContainerCount() {
        return extraContainers.size();
    }
}
