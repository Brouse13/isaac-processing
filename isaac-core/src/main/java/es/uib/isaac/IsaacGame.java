package es.uib.isaac;

import es.uib.isaac.controlls.PlayerController;
import es.uib.isaac.entity.BasePlayer;
import es.uib.isaac.entity.Direction;
import es.uib.isaac.entity.Player;
import es.uib.isaac.map.RoomLayout;
import es.uib.isaac.map.RoomShape;
import es.uib.isaac.network.MqttAdapter;
import es.uib.isaac.render.GameRoomRender;
import es.uib.isaac.ui.ListRenderer;
import es.uib.isaac.render.StaticRender;
import processing.core.PApplet;

import static es.uib.isaac.Constants.POLL_RATE;

public class IsaacGame extends PApplet {
    public static IsaacGame INSTANCE;
    private Player isaac;
    private final ListRenderer listRenderer = new ListRenderer();
    RoomLayout layout = new RoomLayout((byte) 0, RoomShape.SQUARE, 28, 16, null, null);
    GameRoomRender gameRoomRender;
    private final MqttAdapter mqttAdapter = new MqttAdapter();
    private PlayerController playerController;

    int lastPollTime = 0;

    @Override
    public void settings() {
        INSTANCE = this;
        size(Constants.WIDTH, Constants.HEIGHT);

        isaac = new BasePlayer(0, 0);
        isaac.initialize();
        isaac.setDirection(Direction.SOUTH);

        mqttAdapter.connect("localhost", 1883, "isaac-processing");
    }

    @Override
    public void setup() {
        frameRate(60);

        //listRenderer.addRender("isaac-lives", new LiveRender(isaac.getLiveContainers()));

        gameRoomRender = new GameRoomRender(layout, "assets/rooms/basement-lroom.png");
        gameRoomRender.getEntities().add(isaac);
        gameRoomRender.follow(isaac);
        listRenderer.addRender("map-render", gameRoomRender);

        listRenderer.getRenders().forEach(StaticRender::initialize);

        playerController = new PlayerController(mqttAdapter, isaac);
    }

    @Override
    public void draw() {
        int now = millis();

        if (now - lastPollTime >= POLL_RATE) {
            updateGame();
            lastPollTime = now;
        }

        renderGame();
    }

    void renderGame() {
        background(0);
        gameRoomRender.render();
    }

    void updateGame() {
        playerController.updateDirection();
        isaac.update();
        gameRoomRender.update();
    }


    public void keyPressed() {
        playerController.keyPressEvent(key);
    }

    public void keyReleased() {
        playerController.keyReleaseEvent(key);
    }

}
