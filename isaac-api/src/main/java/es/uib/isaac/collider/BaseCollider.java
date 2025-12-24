package es.uib.isaac.collider;

import es.uib.isaac.entity.Entity;
import es.uib.isaac.util.Vector2;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BaseCollider {
    private Vector2 pos1;

    private final int width;
    private final int height;

    boolean hasCollided(Entity entity) {
        Vector2 entityPos = new Vector2(entity.getPosX(), entity.getPosY());
        Vector2 pos2 = new Vector2(pos1.getX() + width, pos1.getY() + height);

        return entityPos.isInAABB(pos1, pos2);
    }

}
