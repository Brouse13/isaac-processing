package es.uib.isaac;

import es.uib.isaac.controller.ControllerButton;
import es.uib.isaac.controller.ControllerEvent;
import es.uib.isaac.controller.ControllerHandler;
import es.uib.isaac.entity.BasePlayer;
import es.uib.isaac.entity.Direction;
import es.uib.isaac.entity.Player;
import es.uib.isaac.network.MqttAdapter;
import es.uib.isaac.render.ListRenderer;
import es.uib.isaac.render.StaticRender;
import es.uib.isaac.ui.LiveRender;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import processing.core.PApplet;

import java.nio.charset.StandardCharsets;

public class IsaacGame extends PApplet {
    public static IsaacGame INSTANCE;
    private Player isaac;
    private boolean moving = false;
    private final ListRenderer listRenderer = new ListRenderer();
    private Controller selectedController;
    private ControllerHandler controllerHandler;
    private final MqttAdapter mqttAdapter = new MqttAdapter();

    @Override
    public void settings() {
        INSTANCE = this;
        size(Constants.WIDTH, Constants.HEIGHT);
        for (Controller controller : ControllerEnvironment.getDefaultEnvironment().getControllers()) {
            // Find first controller
            if (controller.getType() != Controller.Type.GAMEPAD) continue;

            System.out.println("Selecting controller " + controller.getName());
            selectedController = controller;
            controllerHandler = new ControllerHandler(controller, this::onKeyPressed, this::onKeyReleased);
            break;
        }

        if (selectedController == null) {
            System.out.println("No controller available");
        }
    }

    @Override
    public void setup() {
        isaac = new BasePlayer(100, 100);
        isaac.initialize();
        isaac.setDirection(Direction.SOUTH);
        listRenderer.addRender("isaac-lives", new LiveRender(isaac.getLiveContainers()));

        listRenderer.getRenders().forEach(StaticRender::initialize);

        mqttAdapter.connect("192.168.1.174", 1883, "isaac-processing");
        mqttAdapter.subscribe("map/rooms", this::handle);
        mqttAdapter.publish("test", "Hola miau miau".getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public void draw() {

        /*
        if (key == 'w') isaac.setDirection(Direction.NORTH);
        else if (key == 's') isaac.setDirection(Direction.SOUTH);
        else if (key == 'a') isaac.setDirection(Direction.WEST);
        else if (key == 'd') isaac.setDirection(Direction.EAST);
         */

        background(200);
        listRenderer.getRenders().forEach(StaticRender::render);
        isaac.display();

        if (moving) isaac.update();
    }

    private void onKeyPressed(ControllerEvent button) {
        if (button.button() == ControllerButton.CROSS) {
            moving = true;
            if (button.value() == 0.25f) isaac.setDirection(Direction.NORTH);
            else if (button.value() == 0.5f) isaac.setDirection(Direction.EAST);
            else if (button.value() == 0.75f) isaac.setDirection(Direction.SOUTH);
            else if (button.value() == 1.0f) isaac.setDirection(Direction.WEST);
        }

        System.out.println("Pressed button " + button.button());
    }

    private void onKeyReleased(ControllerEvent button) {
        moving = false;
        System.out.println("Released button " + button.button());
    }

    public void keyReleased() {
        moving = false;
    }

    public void keyPressed() {
        moving = true;
    }

    public void handle(String topic, byte[] payload) {
        final int ROOM_SIZE = 4; // posX, posY, metadata(2)

        if (payload.length < 1) return;

        int numRooms = Byte.toUnsignedInt(payload[0]);
        System.out.println("Number of rooms: " + numRooms);

        int roomIndex = 0;

        for (int offset = 1;
             offset + ROOM_SIZE <= payload.length && roomIndex < numRooms;
             offset += ROOM_SIZE)
        {
            int posX = Byte.toUnsignedInt(payload[offset]);
            int posY = Byte.toUnsignedInt(payload[offset + 1]);

            // Little-endian metadata
            int metadata =
                    Byte.toUnsignedInt(payload[offset + 2]) |
                            (Byte.toUnsignedInt(payload[offset + 3]) << 8);

            // Unpack metadata
            int shape   = (metadata >> 0) & 0x07; // bits 0–2
            int type    = (metadata >> 3) & 0x07; // bits 3–5
            int dir     = (metadata >> 6) & 0x07; // bits 6–8
            boolean visited = ((metadata >> 9) & 0x01) != 0;

            System.out.printf(
                    "Room %d: posX=%d, posY=%d, shape=%d, type=%d, dir=%d, visited=%b%n",
                    roomIndex, posX, posY, shape, type, dir, visited
            );

            roomIndex++;
        }
    }

}
