package es.uib.isaac.ui;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import es.uib.isaac.render.StaticRender;

import java.util.List;
import java.util.Map;

public class ListRenderer {
    private final Map<String, StaticRender> renders = Maps.newHashMap();

    public boolean addRender(String name, StaticRender render) {
        return renders.put(name, render) == null;
    }

    public boolean removeRender(String name) {
        return renders.remove(name) != null;
    }

    public List<StaticRender> getRenders() {
        return ImmutableList.copyOf(renders.values());
    }
}
