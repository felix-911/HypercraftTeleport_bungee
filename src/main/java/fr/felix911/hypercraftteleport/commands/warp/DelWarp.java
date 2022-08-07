package fr.felix911.hypercraftteleport.commands.warp;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import fr.felix911.hypercraftteleport.HypercraftTeleport;
import fr.felix911.hypercraftteleport.objects.WarpObject;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Map;

@CommandAlias("delwarp")
public class DelWarp extends BaseCommand {
    private final HypercraftTeleport pl;

    public DelWarp(HypercraftTeleport hypercraftTeleport) {
        this.pl = hypercraftTeleport;
    }

    @Default
    @CommandPermission("hypercraftteleport.command.delwarp")
    @CommandCompletion("@warps @nothing")
    public void delwarp(CommandSender commandSender, String warp){
        pl.getProxy().getScheduler().runAsync(pl, () -> {

            BaseComponent b;

            if (commandSender instanceof ProxiedPlayer){

                ProxiedPlayer sender = (ProxiedPlayer) commandSender;

                Map<String, WarpObject> warpCache = pl.getWarpCache().getWarpCache();

                    if (warpCache.containsKey(warp)) {
                        warpCache.remove(warp);

                        pl.getWarpQueries().deleteWarp(warp);

                        String del = pl.getConfigurationManager().getLang().getWarpDelete();
                        del = del.replace("{warp}", warp);
                        b = new TextComponent(del);
                    } else {
                        String del = pl.getConfigurationManager().getLang().getWarpNoWarpFound();
                        del = del.replace("{warp}", warp);
                        b = new TextComponent(del);
                    }

                sender.sendMessage(b);

            } else {
                b = new TextComponent(pl.getConfigurationManager().getLang().getNoConsole());
                commandSender.sendMessage(b);
            }

    });
}
}
