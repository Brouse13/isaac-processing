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

    public boolean updateDirection() {
        Direction direction;

        if (wPressed && aPressed) {
            direction = Direction.NORTH_WEST;
        } else if (wPressed && dPressed) {
            direction = Direction.NORTH_EAST;
        } else if (sPressed && aPressed) {
            direction = Direction.SOUTH_WEST;
        } else if (sPressed && dPressed) {
            direction = Direction.SOUTH_EAST;
        } else if (wPressed) {
            direction = Direction.NORTH;
        } else if (sPressed) {
            direction = Direction.SOUTH;
        } else if (aPressed) {
            direction = Direction.WEST;
        } else if (dPressed) {
            direction = Direction.EAST;
        } else {
            return false;
        }

        byte[] payload = new byte[1];
        payload[0] = (byte) direction.ordinal();
        mqttAdapter.publish("isaac/updateMove", payload);
        return true;
    }
}
