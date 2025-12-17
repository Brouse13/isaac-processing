package es.uib.isaac.map;

public enum RoomShape {
    SQUARE,
    CORRIDOR,
    L_ROOM,
    LARGE;
    public static RoomShape fromByte(byte b) {
        return values()[b];
    }
}
