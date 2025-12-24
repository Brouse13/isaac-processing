package es.uib.isaac.map;

import es.uib.isaac.entity.Direction;

public record Room(
        byte posX,
        byte posY,
        RoomShape shape,
        RoomType type,
        Direction direction,
        boolean visited
) { }
