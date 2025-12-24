package es.uib.isaac.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Vector2 {
    public float x;
    public float y;

    public void add(Vector2 vector) {
        this.x += vector.x;
        this.y += vector.y;
    }

    public void subtract(Vector2 vector) {
        this.x -= vector.x;
        this.y -= vector.y;
    }

    public boolean isInAABB(Vector2 pos1, Vector2 pos2) {
        float minX = Math.min(pos1.x, pos2.x);
        float maxX = Math.max(pos1.x, pos2.x);
        float minY = Math.min(pos1.y, pos2.y);
        float maxY = Math.max(pos1.y, pos2.y);

        return this.x >= minX && this.x <= maxX && this.y >= minY && this.y <= maxY;
    }
}
