package es.uib.isaac.ui;

import es.uib.isaac.assets.Asset;
import es.uib.isaac.entity.Stats;
import lombok.AllArgsConstructor;
import es.uib.isaac.render.StaticRender;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class StatsRender implements StaticRender {
    private final Stats stats;
    private List<Asset> assets = new ArrayList<>();
    @Override
    public void initialize() {
    }

    @Override
    public void render() {

    }

}
