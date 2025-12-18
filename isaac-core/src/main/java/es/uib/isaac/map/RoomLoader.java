package es.uib.isaac.map;

import lombok.experimental.UtilityClass;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class RoomLoader {

    /**
     * Load the room layout from a binary format.
     *
     * @param layoutFile binary file to read
     * @return the read layout
     * @throws IOException if any IO exception happened
     */
    public static RoomLayout getRoomLayout(String layoutFile) throws IOException {
        try (DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(layoutFile)))) {

            byte id = in.readByte();

            byte shapeByte = in.readByte();
            RoomShape shape = RoomShape.fromByte(shapeByte);

            int width = in.readUnsignedShort();
            int height = in.readUnsignedShort();

            Map<Integer, Byte> entities = readIntByteMap(in);
            Map<Integer, Byte> colliders = readIntByteMap(in);

            return new RoomLayout(id, shape, width, height, entities, colliders);
        }
    }

    private static Map<Integer, Byte> readIntByteMap(DataInputStream in) throws IOException {
        int collisionCount = in.readShort();

        Map<Integer, Byte> map = new HashMap<>(collisionCount);
        for (int i = 0; i < collisionCount; i++) {
            int key = in.readUnsignedShort();
            byte value = in.readByte();
            map.put(key, value);
        }
        return map;
    }
}
