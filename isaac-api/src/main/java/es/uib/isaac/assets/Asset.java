package es.uib.isaac.assets;

public interface Asset {
    /// Default width for the render asset function.
    int WIDTH = 64;

    /// Default height for the render asset function.
    int HEIGHT = 64;

    /**
     * Display the asset using default {@param SIZE_X} and {@param SIZE_Y} for the assets.
     *
     * @param xPos Position x on the screen.
     * @param yPos Position y on the screen.
     */
    void display(float xPos, float yPos);

    /**
     * Display the asset using given with and height.
     *
     * @param xPos Position x on the screen.
     * @param yPos Position y on the screen.
     * @param width With of the asset.
     * @param height Height of the asset
     */
    void display(float xPos, float yPos, int width, int height);
}
