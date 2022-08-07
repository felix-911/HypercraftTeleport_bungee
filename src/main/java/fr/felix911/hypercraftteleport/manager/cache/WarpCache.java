package fr.felix911.hypercraftteleport.manager.cache;

import fr.felix911.hypercraftteleport.HypercraftTeleport;
import fr.felix911.hypercraftteleport.objects.WarpObject;

import java.util.HashMap;
import java.util.Map;

public class WarpCache {
    private final HypercraftTeleport pl;

    private final Map<String , WarpObject> warpCache = new HashMap<>();;

    public WarpCache(HypercraftTeleport hypercraftTeleport) {
        this.pl = hypercraftTeleport;

    }

    public Map<String, WarpObject> getWarpCache() {
        return warpCache;
    }
}
