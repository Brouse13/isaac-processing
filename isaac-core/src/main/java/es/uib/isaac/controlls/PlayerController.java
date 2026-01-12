package es.uib.isaac.controlls;

import es.uib.isaac.entity.Direction;
import es.uib.isaac.entity.Entity;
import es.uib.isaac.network.MqttAdapter;

public class PlayerController {
    private final MqttAdapter mqttAdapter;
    private final Entity basePlayer;

    private boolean wPressed;
    private boolean aPressed;
    private boolean sPressed;
    private boolean dPressed;

    public PlayerController(MqttAdapter mqttAdapter, Entity basePlayer) {
        this.mqttAdapter = mqttAdapter;
        this.basePlayer = basePlayer;

        mqttAdapter.subscribe("isaac/recieveMove", this::callback);
    }

    public void keyPressEvent(char keyCode) {
        switch (keyCode) {
            case 'w' -> wPressed = true;
            case 's' -> sPressed = true;
            case 'a' -> aPressed = true;
            case 'd' -> dPressed = true;
        }
    }

    public void keyReleaseEvent(char keyCode) {
        switch (keyCode) {
            case 'w' -> wPressed = false;
            case 's' -> sPressed = false;
            case 'a' -> aPressed = false;
            case 'd' -> dPressed = false;
        }
    }

    private void callback(String topic, byte[] data) {
        if (data.length != 4) {
            System.out.println("Invalid movement data length");
            return;
        }

        // Update player position
        basePlayer.setPosX((data[0] << 8 | data[1]) / 100f);
        basePlayer.setPosY((data[2] << 8 | data[3]) / 100f);
    }

    public void updateDirection() {
        Direction direction = getDirection();
        if (direction == null) return;

        basePlayer.setDirection(direction);

        byte[] payload = new byte[1];
        payload[0] = (byte) direction.ordinal();
        mqttAdapter.publish("isaac/updateMove", payload);
    }

    private Direction getDirection() {
        if (wPressed && aPressed) return Direction.NORTH_WEST;
        if (wPressed && dPressed) return Direction.NORTH_EAST;
        if (sPressed && aPressed) return Direction.SOUTH_WEST;
        if (sPressed && dPressed) return Direction.SOUTH_EAST;
        if (wPressed)             return Direction.NORTH;
        if (sPressed)             return Direction.SOUTH;
        if (aPressed)             return Direction.WEST;
        if (dPressed)             return Direction.EAST;
        return null;
    }
}
