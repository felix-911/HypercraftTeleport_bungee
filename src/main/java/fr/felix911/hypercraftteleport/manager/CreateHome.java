package fr.felix911.hypercraftteleport.manager;


import fr.felix911.hypercraftteleport.HypercraftTeleport;
import fr.felix911.hypercraftteleport.objects.HomeObject;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CreateHome {

    private final HypercraftTeleport pl;

    public CreateHome(HypercraftTeleport hypercraftHomes) {
        this.pl = hypercraftHomes;
    }

    public void createHome(UUID senderUUID, UUID playerUUID, String rank, HomeObject home) {
        pl.getProxy().getScheduler().runAsync(pl, () -> {
            String s = "";
            TextComponent b;

            ProxiedPlayer sender = pl.getProxy().getPlayer(senderUUID);

            Map<UUID, Map<String, HomeObject>> cacheHomes = pl.getHomeCache().getHomesCache();

            if (!cacheHomes.containsKey(playerUUID)) {
                pl.getHomeQueries().getPlayerHomes(playerUUID);
            }
            Map<String, HomeObject> playerHomes = cacheHomes.get(playerUUID);

            if (!pl.checkLimits(playerUUID ,rank)) {
                if (playerHomes.containsKey(home.getName())) {
                    playerHomes.put(home.getName(), home);
                    pl.getHomeCache().addPlayer(playerUUID, playerHomes);
                    pl.getHomeQueries().updateHome(playerUUID, home);
                    pl.updateHomeBukkit(sender,playerUUID,playerHomes);
                    s = pl.getConfigurationManager().getLang().getHomeCreate();
                    s = s.replace("{home}", home.getName());
                } else {
                    s = pl.getConfigurationManager().getLang().getHomeLimits();
                }
                b = new TextComponent(s);
                sender.sendMessage(b);
                return;
            }

            if (playerHomes == null) {
                playerHomes = new HashMap<>();
                playerHomes.put(home.getName(), home);
                pl.getHomeCache().addPlayer(playerUUID, playerHomes);
                pl.getHomeQueries().saveHome(playerUUID, home);
                pl.updateHomeBukkit(sender,senderUUID,playerHomes);
                s = pl.getConfigurationManager().getLang().getHomeCreate();
                s = s.replace("{home}", home.getName());
            } else if (playerHomes.containsKey(home.getName())) {
                playerHomes.put(home.getName(), home);
                pl.getHomeCache().addPlayer(playerUUID, playerHomes);
                pl.getHomeQueries().updateHome(playerUUID, home);
                s = pl.getConfigurationManager().getLang().getHomeUpdate();
                s = s.replace("{home}", home.getName());

            } else {
                playerHomes.put(home.getName(), home);
                pl.getHomeCache().addPlayer(playerUUID, playerHomes);
                pl.getHomeQueries().saveHome(playerUUID, home);
                pl.updateHomeBukkit(sender,playerUUID,playerHomes);
                s = pl.getConfigurationManager().getLang().getHomeCreate();
                s = s.replace("{home}", home.getName());
            }
            b = new TextComponent(s);
            sender.sendMessage(b);
        });
    }
}
