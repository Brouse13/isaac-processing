package es.uib.isaac.map;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum RoomType {
    START(1),
    NORMAL(2),
    TREASURE(3),
    BOSS(4);

    private final int value;

    public static RoomType fromByte(byte b) {
        for (RoomType s : RoomType.values()) {
            if (s.value == b) return s;
        }
        return null;
    }
}
