package fr.felix911.hypercraftteleport.commands.home;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import fr.felix911.apiproxy.ApiProxy;
import fr.felix911.hypercraftteleport.HypercraftTeleport;
import fr.felix911.hypercraftteleport.objects.HomeObject;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Map;
import java.util.UUID;

@CommandAlias("home")
public class Home extends BaseCommand {
    private final fr.felix911.hypercraftteleport.HypercraftTeleport pl;

    public Home(HypercraftTeleport hypercraftHomes) {
        pl = hypercraftHomes;
    }

    @Default
    @CommandPermission("hypercraftteleport.command.home")
    @CommandCompletion("@homes @nothing")
    public void home(CommandSender commandSender, String home) {
        pl.getProxy().getScheduler().runAsync(pl, () -> {
            BaseComponent b;

            if (commandSender instanceof ProxiedPlayer) {

                ProxiedPlayer sender = (ProxiedPlayer) commandSender;

                Map<UUID, Map<String, HomeObject>> cacheHomes = pl.getHomeCache().getHomesCache();
                String etat;
                    Map<String, HomeObject> playerHomes = cacheHomes.get(sender.getUniqueId());

                    if (playerHomes == null) {
                        String out = pl.getConfigurationManager().getLang().getHomeSenderNoHomes();
                        b = new TextComponent(out);
                    } else {
                        if (playerHomes.containsKey(home)){
                            HomeObject homeObject = playerHomes.get(home);
                            etat = ApiProxy.teleportPlayerToLocation(sender,homeObject.getServer(),homeObject.getWorld(),homeObject.getX(),homeObject.getY(),homeObject.getZ(),homeObject.getPitch(),homeObject.getYaw());
                            if (etat.equalsIgnoreCase("fail")) {
                                etat = pl.getConfigurationManager().getLang().getFailTp();
                            } else if(etat.equalsIgnoreCase("succes")){
                                etat = pl.getConfigurationManager().getLang().getHomeTeleport();
                                etat = etat.replace("{home}", home);
                            }
                        } else {
                            etat = pl.getConfigurationManager().getLang().getHomeNoHomeFound();
                            etat = etat.replace("{home}", home);
                        }
                        b = new TextComponent(etat);
                    }
                    sender.sendMessage(b);
            } else {
                b = new TextComponent(pl.getConfigurationManager().getLang().getNoConsole());
                commandSender.sendMessage(b);
            }
        });
    }


    @CommandPermission("hypercraftteleport.command.home.other")
    @CommandCompletion("@homes @nothing")
    public void home(CommandSender commandSender, String home, String player) {
        pl.getProxy().getScheduler().runAsync(pl, () -> {
            BaseComponent b;
            if (commandSender instanceof ProxiedPlayer) {

                ProxiedPlayer sender = (ProxiedPlayer) commandSender;

                UUID playerUUID = ApiProxy.getPlayerUUID(player);

                if (playerUUID == null) {
                    String out = pl.getConfigurationManager().getLang().getPlayerNotFound();
                    out = out.replace("{player}", player);
                    b = new TextComponent(out);
                } else {
                    Map<UUID, Map<String, HomeObject>> cacheHomes = pl.getHomeCache().getHomesCache();
                    Map<String, HomeObject> playerHomes = cacheHomes.get(playerUUID);
                    String etat;

                    if (!cacheHomes.containsKey(playerUUID)) {
                        pl.getHomeQueries().getPlayerHomes(playerUUID);
                         playerHomes = cacheHomes.get(playerUUID);
                    }
                        if (playerHomes == null) {
                            String out = pl.getConfigurationManager().getLang().getHomeNoHomes();
                            out = out.replace("{player}", player);
                            b = new TextComponent(out);
                        } else {
                            if (playerHomes.containsKey(home)){
                                HomeObject homeObject = playerHomes.get(home);
                                etat = ApiProxy.teleportPlayerToLocation(sender,homeObject.getServer(),homeObject.getWorld(),homeObject.getX(),homeObject.getY(),homeObject.getZ(),homeObject.getPitch(),homeObject.getYaw());
                                if (etat.equalsIgnoreCase("fail")) {
                                    etat = pl.getConfigurationManager().getLang().getFailTp();
                                } else if(etat.equalsIgnoreCase("succes")){
                                    etat = pl.getConfigurationManager().getLang().getHomeTeleport();
                                    etat = etat.replace("{home}", home);
                                }
                            } else {
                                etat = pl.getConfigurationManager().getLang().getHomeNoHomeFound();
                                etat = etat.replace("{home}", home);
                            }
                            b = new TextComponent(etat);
                        }
                }
                sender.sendMessage(b);
            } else {
                b = new TextComponent(pl.getConfigurationManager().getLang().getNoConsole());
                commandSender.sendMessage(b);
            }
        });
    }
}
