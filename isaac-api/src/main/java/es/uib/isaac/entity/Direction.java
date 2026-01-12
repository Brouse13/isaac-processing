package es.uib.isaac.entity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Direction {
    NORTH(0),
    EAST(1),
    SOUTH(2),
    WEST(3),
    NORTH_EAST(4),
    NORTH_WEST(5),
    SOUTH_EAST(6),
    SOUTH_WEST(7);
    private final int value;

    public static Direction fromByte(byte b) {
        for (Direction s : Direction.values()) {
            if (s.value == b) return s;
        }
        return null;
    }
}
