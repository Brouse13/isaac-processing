package es.uib.isaac.map;

import java.util.Map;

public record RoomLayout(
        byte id,
        RoomShape shape,
        int width,
        int height,
        Map<Integer, Byte> entities,
        Map<Integer, Byte> collisionObjects
)   {

}
