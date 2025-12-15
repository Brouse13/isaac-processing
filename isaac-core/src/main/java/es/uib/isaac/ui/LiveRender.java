package es.uib.isaac.ui;

import es.uib.isaac.assets.SingleTileAsset;
import es.uib.isaac.entity.LiveContainer;
import es.uib.isaac.entity.LiveContainer.ExtraContainer;
import es.uib.isaac.render.StaticRender;

public class LiveRender implements StaticRender {
    private static final int RED_FULL_HEART =       0;
    private static final int HALF_RED_HEART =       1;
    private static final int BLUE_FULL_HEART =      2;
    private static final int HALF_BLUE_HEART =      3;
    private static final int BLACK_FULL_HEART =     4;
    private static final int HALF_BLACK_HEART =     5;
    private static final int EMPTY_FULL_CONTAINER = 6;
    private static final int HALF_WHITE_CONTAINER = 7;
    private static final int HOLLY_MANTEL =         8;
    private static final int POS_X = 10;
    private static final int POS_Y = 10;
    private static final String PATH = "assets/ui/hearts.png";
    private final LiveContainer liveContainer;
    private final SingleTileAsset[] containersImage = new SingleTileAsset[HOLLY_MANTEL + 1]; // Last index + 1

    public LiveRender(LiveContainer liveContainer) {
        this.liveContainer = liveContainer;
    }

    @Override
    public void initialize() {
        containersImage[RED_FULL_HEART] = new SingleTileAsset(PATH, 0, 0);
        containersImage[HALF_RED_HEART] = new SingleTileAsset(PATH, 0, 1);
        containersImage[BLUE_FULL_HEART] = new SingleTileAsset(PATH, 0, 2);
        containersImage[HALF_BLUE_HEART] = new SingleTileAsset(PATH, 0, 3);
        containersImage[BLACK_FULL_HEART] = new SingleTileAsset(PATH, 0, 4);
        containersImage[HALF_BLACK_HEART] = new SingleTileAsset(PATH, 0, 5);

        containersImage[EMPTY_FULL_CONTAINER] = new SingleTileAsset(PATH, 1, 0);
        containersImage[HALF_WHITE_CONTAINER] = new SingleTileAsset(PATH, 1, 1);
        containersImage[HOLLY_MANTEL] = new SingleTileAsset(PATH, 1, 2);
    }

    @Override
    public void render() {
        int xPos = POS_X;
        int emptyContainers = liveContainer.getContainers() - liveContainer.getLives();

        // Add all full red hearts
        for (int i = 0; i < liveContainer.getLives() / 2; i++) {
            containersImage[RED_FULL_HEART].display(xPos, POS_Y, 32, 32);
            xPos += 32;
        }

        // If even add a half red and empty
        if (liveContainer.getLives() % 2 == 1) {
            containersImage[HALF_RED_HEART].display(xPos, POS_Y, 32, 32);
            xPos += 32;
        }

        // If is odd, the container was added on previous step
        for (int i = 0; i < emptyContainers / 2; i++) {
            containersImage[EMPTY_FULL_CONTAINER].display(xPos, POS_Y, 32, 32);
            xPos += 32;
        }

        boolean skipFirst = liveContainer.getExtraContainers().size() % 2 == 1;
        boolean tmp = skipFirst;
        for (ExtraContainer ignored : liveContainer.getExtraContainers()) {
            if (tmp) { tmp = false; continue; }

            // When we add more hearts this logic will be replaced
            containersImage[BLUE_FULL_HEART].display(xPos, POS_Y, 32, 32);
            xPos += 32;
        }

        // If blue hearts were odd we add the half container
        if (skipFirst) {
            containersImage[HALF_BLUE_HEART].display(xPos, POS_Y, 32, 32);
            xPos += 32;
        }

        if (liveContainer.isHollyMantel()) {
            containersImage[HOLLY_MANTEL].display(xPos, POS_Y, 32, 32);
            xPos += 32;
        }
    }
}
