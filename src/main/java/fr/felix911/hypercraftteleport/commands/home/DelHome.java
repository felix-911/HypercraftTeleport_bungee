package fr.felix911.hypercraftteleport.commands.home;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import fr.felix911.apiproxy.ApiProxy;
import fr.felix911.hypercraftteleport.HypercraftTeleport;
import fr.felix911.hypercraftteleport.objects.HomeObject;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Map;
import java.util.UUID;

@CommandAlias("delHome")
public class DelHome extends BaseCommand {
    private final HypercraftTeleport pl;

    public DelHome(HypercraftTeleport hypercraftHomes) {
        pl = hypercraftHomes;
    }

    @Default
    @CommandPermission("hypercraftteleport.command.delhome")
    @CommandCompletion("@homes @nothing")
    public void delHome(CommandSender commandSender, String home) {
        pl.getProxy().getScheduler().runAsync(pl, () -> {
            BaseComponent b;
            if (commandSender instanceof ProxiedPlayer) {

                ProxiedPlayer sender = (ProxiedPlayer) commandSender;
                Map<UUID, Map<String, HomeObject>> homesCacheTotal = pl.getHomeCache().getHomesCache();
                Map<String, HomeObject> playerHomes = homesCacheTotal.get(sender.getUniqueId());

                    if (playerHomes.containsKey(home)) {
                        playerHomes.remove(home);
                        homesCacheTotal.put(sender.getUniqueId(),playerHomes);
                        pl.getHomeQueries().deleteHome(sender.getUniqueId(), home);
                        pl.updateHomeBukkit(sender, sender.getUniqueId(), playerHomes);

                        String del = pl.getConfigurationManager().getLang().getHomeDelete();
                        del = del.replace("{home}", home);
                        b = new TextComponent(del);
                    } else {
                        String del = pl.getConfigurationManager().getLang().getHomeNoHome();
                        del = del.replace("{home}", home);
                        b = new TextComponent(del);
                    }
                    sender.sendMessage(b);
            } else {
                b = new TextComponent(pl.getConfigurationManager().getLang().getNoConsole());
                commandSender.sendMessage(b);
            }
        });
    }

    @Default
    @CommandPermission("hypercraftteleport.command.delhome.other")
    @CommandCompletion("@homes @players @nothing")
    public void deleteHome(CommandSender commandSender, String homeName, String playerName){

        pl.getProxy().getScheduler().runAsync(pl, () -> {
            BaseComponent b;
            if (commandSender instanceof ProxiedPlayer) {

                ProxiedPlayer sender = (ProxiedPlayer) commandSender;
                UUID playerUUID = ApiProxy.getPlayerUUID(playerName);

                if (playerUUID == null) {
                    String out = pl.getConfigurationManager().getLang().getPlayerNotFound();
                    out = out.replace("{player}", playerName);
                    b = new TextComponent(out);
                } else {
                    Map<UUID, Map<String, HomeObject>> homesCacheTotal = pl.getHomeCache().getHomesCache();
                    Map<String, HomeObject> playerHomes = homesCacheTotal.get(playerUUID);
                    if (playerHomes == null) {
                        pl.getHomeCache().loadPlayer(playerUUID);
                        playerHomes = pl.getHomeCache().getHomesCache().get(playerUUID);
                    }

                    String del;
                    if (playerHomes.containsKey(homeName)) {
                        playerHomes.remove(homeName);
                        homesCacheTotal.put(playerUUID,playerHomes);
                        pl.getHomeQueries().deleteHome(playerUUID, homeName);
                        pl.updateHomeBukkit(sender, playerUUID, playerHomes);

                        del = pl.getConfigurationManager().getLang().getHomeDeleteOther();
                        del = del.replace("{home}", homeName);
                        del = del.replace("{player}", playerName);
                    } else {
                        del = pl.getConfigurationManager().getLang().getHomeNoHome();
                        del = del.replace("{home}", homeName);
                    }
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
