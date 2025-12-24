package es.uib.isaac.network;

import es.uib.isaac.entity.Direction;
import es.uib.isaac.map.GameMap;
import es.uib.isaac.map.Room;
import es.uib.isaac.map.RoomShape;
import es.uib.isaac.map.RoomType;
import lombok.Getter;

@Getter
public class GameMapLoader {
    public static final int ROOM_SIZE = 4;
    public static final int PACKED_ROOM_SHAPE_SHIFT = 0;
    public static final int PACKED_ROOM_TYPE_SHIFT = 3;
    public static final int PACKED_ROOM_DIR_SHIFT = 6;
    public static final int PACKED_ROOM_VISITED_SHIFT = 9;

    private GameMap gameMap;
    public void loadGameMap(String topic, byte[] payload) {
        if (payload.length < 1) throw new IllegalArgumentException("Invalid payload length");

        int numRooms = Byte.toUnsignedInt(payload[0]);
        if (payload.length < numRooms * ROOM_SIZE + 1) throw new IllegalArgumentException("Invalid payload length");

        int roomIndex = 0;
        Room[] rooms = new Room[numRooms];

        for (int offset = 1; offset + ROOM_SIZE <= payload.length && roomIndex < numRooms; offset += ROOM_SIZE) {
            byte posX = payload[offset];
            byte posY = payload[offset + 1];

            int metadata = Byte.toUnsignedInt(payload[offset + 2]) | (Byte.toUnsignedInt(payload[offset + 3]) << 8);

            // Unpack metadata
            RoomShape shape = RoomShape.fromByte((byte)((metadata >> PACKED_ROOM_SHAPE_SHIFT) & 0x07));
            RoomType  type = RoomType.fromByte((byte)((metadata >> PACKED_ROOM_TYPE_SHIFT) & 0x07));
            Direction direction = Direction.fromByte((byte)((metadata >> PACKED_ROOM_DIR_SHIFT) & 0x07));
            boolean visited = ((metadata >> PACKED_ROOM_VISITED_SHIFT) & 0x01) != 0;

            rooms[roomIndex++] = new Room(posX, posY, shape, type, direction, visited);
        }

        gameMap = new GameMap(rooms, System.currentTimeMillis());
    }
}
