package es.uib.isaac.ui;

import es.uib.isaac.Constants;
import es.uib.isaac.IsaacGame;
import es.uib.isaac.map.GameMap;
import es.uib.isaac.map.Room;
import es.uib.isaac.network.GameMapLoader;
import es.uib.isaac.network.MqttAdapter;
import es.uib.isaac.render.StaticRender;
import processing.core.PImage;

public class MapRender implements StaticRender {
    private final IsaacGame instance = IsaacGame.INSTANCE;
    private final GameMapLoader mapLoader = new GameMapLoader();

    private final MqttAdapter mqttAdapter;
    private PImage pImage;

    public MapRender(MqttAdapter mqttAdapter) {
        this.mqttAdapter = mqttAdapter;
    }

    @Override
    public void initialize() {
        pImage = instance.loadImage("assets/rooms/basement-square.png");
        // mqttAdapter.subscribe("isaac/map", mapLoader::loadGameMap);
    }

    @Override
    public void render() {
        float percentageX = Constants.WIDTH * 0.03f;
        float percentageY = Constants.WIDTH * 0.02f;

        instance.image(pImage,
                percentageX, percentageY,
                Constants.WIDTH - 2 * percentageX, Constants.HEIGHT - 2 * percentageY
        );

        if (mapLoader.getGameMap() == null) return;

        mqttAdapter.unsubscribe("isaac/map", mapLoader::loadGameMap);

        GameMap gameMap = mapLoader.getGameMap();
        Room[] rooms = gameMap.rooms();
    }
}
