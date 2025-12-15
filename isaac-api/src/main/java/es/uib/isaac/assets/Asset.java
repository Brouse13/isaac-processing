package es.uib.isaac.assets;

public interface Asset {
    int SIZE_X = 64;
    int SIZE_Y = 64;

    /**
     * Display the asset
     */
    void display(float xPos, float yPos);

    void display(float xPos, float yPos, int width, int height);
}
