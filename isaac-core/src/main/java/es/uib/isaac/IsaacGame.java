package es.uib.isaac;

import es.uib.isaac.entity.BasePlayer;
import es.uib.isaac.entity.Direction;
import es.uib.isaac.entity.Player;
import es.uib.isaac.map.RoomLayout;
import es.uib.isaac.map.RoomShape;
import es.uib.isaac.render.GameRoomRender;
import es.uib.isaac.ui.ListRenderer;
import es.uib.isaac.render.StaticRender;
import processing.core.PApplet;
import processing.core.PImage;

public class IsaacGame extends PApplet {
    public static IsaacGame INSTANCE;
    private Player isaac;
    private boolean moving = false;
    private boolean dirty = true;
    private final ListRenderer listRenderer = new ListRenderer();
    RoomLayout layout = new RoomLayout((byte) 0, RoomShape.SQUARE, 28, 16, null, null);
    GameRoomRender gameRoomRender;
    PImage img;

    @Override
    public void settings() {
        INSTANCE = this;
        size(Constants.WIDTH, Constants.HEIGHT);
    }

    public void pre() {
        if (width != Constants.WIDTH || height != Constants.HEIGHT) {
            Constants.WIDTH = width;
            Constants.HEIGHT = height;
        }
    }

    @Override
    public void setup() {
        surface.setResizable(true);
        registerMethod("pre", this);

        isaac = new BasePlayer(0, 0);
        isaac.initialize();
        isaac.setDirection(Direction.SOUTH);
        img = loadImage("assets/players/isaac.png");

        //listRenderer.addRender("isaac-lives", new LiveRender(isaac.getLiveContainers()));

        gameRoomRender = new GameRoomRender(layout, "assets/rooms/basement-lroom.png");
        gameRoomRender.getEntities().add(isaac);
        gameRoomRender.follow(isaac);
        listRenderer.addRender("map-render", gameRoomRender);

        listRenderer.getRenders().forEach(StaticRender::initialize);
    }

    @Override
    public void draw() {
        if (frameCount % 2 == 0) { // 30 updates/sec at 60 FPS
            updateGame();
        }

        if (!dirty) return;

        renderGame();
        dirty = false;
    }

    void renderGame() {
        background(0);
        gameRoomRender.render();
    }

    void updateGame() {
        if (moving) {
            isaac.update();
            dirty = true;
        }
        gameRoomRender.update();
    }

    public void keyReleased() {
        moving = false;
    }

    public void keyPressed() {
        switch (key) {
            case 'w' -> isaac.setDirection(Direction.NORTH);
            case 's' -> isaac.setDirection(Direction.SOUTH);
            case 'a' -> isaac.setDirection(Direction.WEST);
            case 'd' -> isaac.setDirection(Direction.EAST);
            default -> {}
        }
        moving = true;
        dirty = true;
    }

}
