package fr.felix911.hypercraftteleport.manager.cache;


import fr.felix911.hypercraftteleport.HypercraftTeleport;
import fr.felix911.hypercraftteleport.objects.HomeObject;
import net.md_5.bungee.api.connection.ProxiedPlayer;


import java.util.*;

public class HomesCache {
    private final HypercraftTeleport pl;
    private final HashMap<UUID, Map<String, HomeObject>> homesCache;
    private final HashMap<UUID, Integer> homesBonusCache;

    public HomesCache(HypercraftTeleport hypercraftHomes) {
        pl = hypercraftHomes;
        homesCache = new HashMap<>();
        homesBonusCache = new HashMap<>();
    }

    public void addPlayer(UUID uuid, Map<String, HomeObject> playerHomes){
        homesCache.put(uuid,playerHomes);
    }

    public void increaseBonus(UUID uuid, int num) {
        int i = homesBonusCache.get(uuid);
        int n = i + num;
        homesBonusCache.put(uuid, n);
        pl.getHomeQueries().setHomesBonus(uuid,n);
    }

    public void loadPlayer(UUID playerUUID){
        pl.getHomeQueries().getPlayerHomes(playerUUID);
        pl.getHomeQueries().getPlayerHomesBonus(playerUUID);
    }

    public void reload() {

        Collection<ProxiedPlayer> playerslist = pl.getProxy().getPlayers();
        for (ProxiedPlayer player : playerslist){
            pl.getHomeQueries().getPlayerHomes(player.getUniqueId());
            pl.getHomeQueries().getPlayerHomesBonus(player.getUniqueId());
        }
    }

    public HashMap<UUID, Map<String, HomeObject>> getHomesCache() {
        return homesCache;
    }

    public HashMap<UUID, Integer> getHomesBonusCache() {
        return homesBonusCache;
    }
}
