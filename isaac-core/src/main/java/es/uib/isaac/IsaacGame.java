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

public class IsaacGame extends PApplet {
    public static IsaacGame INSTANCE;
    private Player isaac;
    private final ListRenderer listRenderer = new ListRenderer();
    RoomLayout layout = new RoomLayout((byte) 0, RoomShape.SQUARE, 28, 16, null, null);
    GameRoomRender gameRoomRender;
    private final MqttAdapter mqttAdapter = new MqttAdapter();
    private PlayerController playerController;

    final float DT = 1.0f / 60.0f;
    float accumulator = 0;
    long lastTime = System.nanoTime();

    @Override
    public void settings() {
        INSTANCE = this;
        size(Constants.WIDTH, Constants.HEIGHT);

        mqttAdapter.connect("localhost", 1883, "isaac-processing");
    }

    @Override
    public void setup() {
        isaac = new BasePlayer(0, 0);
        isaac.initialize();
        isaac.setDirection(Direction.SOUTH);

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
        long now = System.nanoTime();
        accumulator += (now - lastTime) / 1_000_000_000f;
        lastTime = now;

        while (accumulator >= DT) {
            updateGame();
            accumulator -= DT;
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
