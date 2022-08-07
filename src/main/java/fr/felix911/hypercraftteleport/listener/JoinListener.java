package fr.felix911.hypercraftteleport.listener;

import fr.felix911.hypercraftteleport.HypercraftTeleport;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import java.util.UUID;

public class JoinListener implements Listener {

    private final HypercraftTeleport pl;

    public JoinListener(HypercraftTeleport hypercraftHomes) {
        this.pl = hypercraftHomes;
    }

    @EventHandler
    public void OnJoinEvent(LoginEvent e){
        UUID uuid = e.getConnection().getUniqueId();
        if (!(pl.getHomeCache().getHomesCache().containsKey(uuid))){
            pl.getHomeQueries().getPlayerHomes(uuid);
            pl.getHomeQueries().getPlayerHomesBonus(uuid);
        }
    }
}
