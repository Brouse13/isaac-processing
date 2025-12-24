package es.uib.isaac.map;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum RoomShape {
    SQUARE(1),
    CORRIDOR(2),
    L_ROOM(3),
    LARGE(4);
    private final int value;

    public static RoomShape fromByte(byte b) {
        for (RoomShape s : RoomShape.values()) {
            if (s.value == b) return s;
        }
        return null;
    }
}
