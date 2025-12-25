package es.uib.isaac.entity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Direction {
    NORTH(1),
    EAST(1),
    SOUTH(3),
    WEST(4),
    NORTH_EAST(5),
    NORTH_WEST(7),
    SOUTH_EAST(8),
    SOUTH_WEST(9);
    private final int value;

    public static Direction fromByte(byte b) {
        for (Direction s : Direction.values()) {
            if (s.value == b) return s;
        }
        return null;
    }
}
