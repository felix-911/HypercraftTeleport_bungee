package fr.felix911.hypercraftteleport.commands.warp;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import fr.felix911.hypercraftteleport.HypercraftTeleport;
import fr.felix911.hypercraftteleport.manager.cache.WarpCache;
import fr.felix911.hypercraftteleport.objects.WarpObject;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Map;

@CommandAlias("warpconfig")
public class WarpConfig extends BaseCommand {
    private final HypercraftTeleport pl;

    public WarpConfig(HypercraftTeleport hypercraftTeleport) {
        this.pl = hypercraftTeleport;
    }

    @Default
    @CommandPermission("hypercraftteleport.command.warpconfig")
    @CommandCompletion("@warps @switch")
    public void warpconfig(CommandSender commandSender, String warpName, String etat){
        pl.getProxy().getScheduler().runAsync(pl, () -> {

            BaseComponent b;
            String s = "";

            if (commandSender instanceof ProxiedPlayer){
                ProxiedPlayer sender = (ProxiedPlayer) commandSender;

                Map<String, WarpObject> warpCache = pl.getWarpCache().getWarpCache();

                if (etat.equalsIgnoreCase("true") || etat.equalsIgnoreCase("false")){
                    if (warpCache.containsKey(warpName)){
                        WarpObject warp = warpCache.get(warpName);

                        boolean bo = Boolean.parseBoolean(etat);
                        warp.setNeedPerm(bo);
                        warpCache.put(warpName, warp);
                        pl.getWarpQueries().editconfig(warpName,bo);

                        if (bo){
                            s = pl.getConfigurationManager().getLang().getWarpPermWarp();
                        } else {
                            s = pl.getConfigurationManager().getLang().getWarpNoPermWarp();
                        }

                    } else {
                        s = pl.getConfigurationManager().getLang().getWarpNoWarpFound();
                    }
                }


            } else {
                s = pl.getConfigurationManager().getLang().getNoConsole();
            }
            b = new TextComponent(s);
            commandSender.sendMessage(b);
        });

    }

}
