package fr.felix911.hypercraftteleport.manager;

import fr.felix911.hypercraftteleport.HypercraftTeleport;
import fr.felix911.hypercraftteleport.objects.WarpObject;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.awt.*;
import java.util.Map;
import java.util.UUID;

public class CreateWarp {
    private final HypercraftTeleport pl;

    public CreateWarp(HypercraftTeleport hypercraftTeleport) {
        this.pl = hypercraftTeleport;
    }

    public void createWarp(UUID senderUUID, WarpObject warp) {
        pl.getProxy().getScheduler().runAsync(pl, () -> {
            String s = "";
            BaseComponent b;

            ProxiedPlayer sender = pl.getProxy().getPlayer(senderUUID);

            Map<String, WarpObject> warpCache = pl.getWarpCache().getWarpCache();

            if (warpCache.containsKey(warp.getName())){
                warpCache.put(warp.getName(), warp);
                pl.getWarpQueries().updateWarp(warp);
                s = pl.getConfigurationManager().getLang().getWarpUpdate();
                s = s.replace("{home}", warp.getName());
            } else {
                warpCache.put(warp.getName(), warp);
                pl.getWarpQueries().saveWarp(warp);
                pl.updateWarpBukkit(sender,warpCache);
                s = pl.getConfigurationManager().getLang().getWarpCreate();
                s = s.replace("{home}", warp.getName());
            }

            b = new TextComponent(s);
            sender.sendMessage(b);

        });
    }
}
