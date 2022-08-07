package fr.felix911.hypercraftteleport.manager;

import co.aikar.commands.BungeeCommandCompletionContext;
import co.aikar.commands.CommandCompletions;

import fr.felix911.apiproxy.ApiProxy;
import fr.felix911.hypercraftteleport.HypercraftTeleport;
import fr.felix911.hypercraftteleport.objects.HomeObject;
import fr.felix911.hypercraftteleport.objects.WarpObject;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;


import java.util.*;


public class CommandCompletionsManager {
    private final HypercraftTeleport plugin;



    public CommandCompletionsManager(HypercraftTeleport plugin) {
        this.plugin = plugin;
        this.registerCommandCompletions();

    }

    private void registerCommandCompletions() {
        CommandCompletions<BungeeCommandCompletionContext> commandCompletions = this.plugin.getCommandManager().getCommandCompletions();

        commandCompletions.registerAsyncCompletion("homes", (c) -> {
            CommandSender sender = c.getSender();
            if (!(sender instanceof ProxiedPlayer)) {
                return null;
            } else {

                ProxiedPlayer proxiedPlayer = (ProxiedPlayer) sender;
                Map<String, HomeObject> cache = plugin.getHomeCache().getHomesCache().get(proxiedPlayer.getUniqueId());
                List<String> homes = new ArrayList<>(cache.keySet());
                homes.sort(Comparator.naturalOrder());

                return homes;
            }
        });
        commandCompletions.registerAsyncCompletion("warps", (c) -> {
            CommandSender sender = c.getSender();
            if (!(sender instanceof ProxiedPlayer)) {
                return null;
            } else {

                List<String> warps = new ArrayList<>();

                Map<String, WarpObject> cache = plugin.getWarpCache().getWarpCache();
                for(String warpName : cache.keySet()){
                    WarpObject warp = cache.get(warpName);

                    if (warp.isNeedPerm() && sender.hasPermission("hypercraftteleport.command.warp.warp." + warpName)){
                        warps.add(warpName);
                    } else if (!warp.isNeedPerm()){
                        warps.add(warpName);
                    }

                }
                warps.sort(Comparator.naturalOrder());

                return warps;
            }
        });
        commandCompletions.registerAsyncCompletion("reload", (c) -> {
            CommandSender sender = c.getSender();
            if (!(sender instanceof ProxiedPlayer)) {
                return null;
            } else {
                String config = "config";
                String language = "language";
                String all = "all";
                String cache = "cache";

                List<String> reload = new ArrayList<>();

                reload.add(config);
                reload.add(language);
                reload.add(all);
                reload.add(cache);

                return reload;
            }
        });
        commandCompletions.registerAsyncCompletion("players", (c) -> {
            CommandSender sender = c.getSender();
            if (!(sender instanceof ProxiedPlayer)) {
                return null;
            } else {
                List<String> online = ApiProxy.getNoVanishPlayerOnline();
                return online;
            }
        });
        commandCompletions.registerAsyncCompletion("switch", (c) -> {
            CommandSender sender = c.getSender();
            if (!(sender instanceof ProxiedPlayer)) {
                return null;
            } else {
                List<String> etat = new ArrayList<>();
                etat.add("true");
                etat.add("false");
                return etat;

            }
        });

    }
}
