package fr.felix911.hypercraftteleport.commands.home;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.annotation.Optional;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.felix911.apiproxy.ApiProxy;
import fr.felix911.apiproxy.objects.PlayerObject;
import fr.felix911.hypercraftteleport.HypercraftTeleport;
import fr.felix911.hypercraftteleport.objects.HomeObject;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.*;

@CommandAlias("homes")
public class Homes extends BaseCommand {
    private final HypercraftTeleport pl;

    public Homes(HypercraftTeleport hypercraftHomes) {
        pl = hypercraftHomes;
    }

    @Default
    @CommandPermission("hypercraftteleport.command.homes")
    @CommandCompletion("@nothing @nothing")
    public void homes(CommandSender commandSender) {
        pl.getProxy().getScheduler().runAsync(pl, () -> {
            BaseComponent b;

            if (commandSender instanceof ProxiedPlayer) {

                ProxiedPlayer sender = (ProxiedPlayer) commandSender;
                listHomes(sender, sender.getName(), "self");

            } else {
                b = new TextComponent(pl.getConfigurationManager().getLang().getNoConsole());
                commandSender.sendMessage(b);
            }
        });
    }

    @CommandPermission("hypercraftteleport.command.homes.other")
    @CommandCompletion("@players @nothing")
    public void homes(CommandSender commandSender, String player) {
        pl.getProxy().getScheduler().runAsync(pl, () -> {
            BaseComponent b;

            if (commandSender instanceof ProxiedPlayer) {

                ProxiedPlayer sender = (ProxiedPlayer) commandSender;

                List<String> splitServer = pl.getConfigurationManager().getConfig().getSplitServer();

                if (sender.hasPermission("hypercrafthomes.command.home.homes.other")) {
                    listHomes(sender, player, "other");
                } else if (sender.hasPermission("hypercrafthomes.command.home.homes.anim") && splitServer.contains(sender.getServer().getInfo().getName())){
                    listHomes(sender, player, "anim");
                } else {
                    b = new TextComponent(pl.getConfigurationManager().getLang().getNoPerm());
                    sender.sendMessage(b);
                }

            } else {
                b = new TextComponent(pl.getConfigurationManager().getLang().getNoConsole());
                commandSender.sendMessage(b);
            }
        });
    }

    private void listHomes(ProxiedPlayer sender, String player, String type){
        BaseComponent b;

        UUID playerUUID;
        if (player == null){
            playerUUID = sender.getUniqueId();
        } else {
            playerUUID = ApiProxy.getPlayerUUID(player);

            if (playerUUID == null) {
                String out = pl.getConfigurationManager().getLang().getPlayerNotFound();
                out = out.replace("{player}", player);
                b = new TextComponent(out);
                sender.sendMessage(b);
                return;
            }
        }

        Map<UUID, Map<String, HomeObject>> cacheHomes = pl.getHomeCache().getHomesCache();

        if (!cacheHomes.containsKey(playerUUID)) {
            pl.getHomeCache().loadPlayer(playerUUID);
        }

        Map<String, HomeObject> playerHomes = cacheHomes.get(playerUUID);

        if (playerHomes.isEmpty()) {
            String out = pl.getConfigurationManager().getLang().getHomeNoHomes();
            out = out.replace("{player}", player);
            b = new TextComponent(out);
            sender.sendMessage(b);
        } else {
            List<Integer> value = new ArrayList<>();
            String rank;
            rank = ApiProxy.getSpecifiqueRank(sender.getUniqueId(), pl.getConfigurationManager().getConfig().getlPVIP());
            if (rank == null){
                rank = ApiProxy.getSpecifiqueRank(sender.getUniqueId(), pl.getConfigurationManager().getConfig().getlPplayers());
            }
            int limit = pl.getConfigurationManager().getConfig().getMapLimites().get(rank);
            Map<String, HomeObject> homesPlayer = pl.getHomeCache().getHomesCache().get(playerUUID);
            int bonus = pl.getHomeCache().getHomesBonusCache().get(playerUUID);
            int homes = 0;
            if (!(homesPlayer == null)){
                homes = homesPlayer.size();
            }
            value.add(homes);
            value.add(limit + bonus);
            value.add(limit);
            value.add(bonus);

            List<String> splitServer = pl.getConfigurationManager().getConfig().getSplitServer();
            List<String> export = new ArrayList<>();
            String senderServer = sender.getServer().getInfo().getName();
            switch (type){
                case "self":
                case "other":
                    if (splitServer.contains(senderServer)){
                        for (String name : playerHomes.keySet()){
                            HomeObject home = playerHomes.get(name);
                            if (splitServer.contains(home.getServer())) {
                                String s = home.getName() + "¤" + home.getBlock() + "¤" + home.getWorld() + "¤" + home.getServer() + "¤" + home.getCustomModelData();
                                export.add(s);
                            }
                        }
                    } else {
                        for (String name : playerHomes.keySet()){
                            HomeObject home = playerHomes.get(name);
                            if (!splitServer.contains(home.getServer())) {
                                String s = home.getName() + "¤" + home.getBlock() + "¤" + home.getWorld() + "¤" + home.getServer() + "¤" + home.getCustomModelData();
                                export.add(s);
                            }
                        }
                    }
                    break;
                case "anim":
                    for (String name : playerHomes.keySet()){
                        HomeObject home = playerHomes.get(name);
                        if (splitServer.contains(home.getServer())) {
                            String s = home.getName() + "¤" + home.getBlock() + "¤" + home.getWorld() + "¤" + home.getServer() + "¤" + home.getCustomModelData();
                            export.add(s);
                        }
                    }
                    break;
            }

            if (export.isEmpty()) {
                String out = pl.getConfigurationManager().getLang().getHomeSenderNoHomes();
                b = new TextComponent(out);
                sender.sendMessage(b);
            } else {
                export.sort(Comparator.naturalOrder());
                sendHomes(sender,playerUUID,value,export);
            }
        }
    }

    private void sendHomes(ProxiedPlayer sender, UUID playerUUID, List<Integer> value, List<String> export){

        ServerInfo server = sender.getServer().getInfo();
        try {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();

            out.writeUTF("homesListGui");
            out.writeUTF(String.valueOf(sender.getUniqueId()));
            out.writeUTF(String.valueOf(playerUUID));
            out.writeUTF(String.valueOf(value));
            out.writeUTF(String.valueOf(export));

            server.sendData("hypercraft:teleport", out.toByteArray());
        } catch (Exception e) {
            pl.getLogger().info(ChatColor.RED + "error envoi sur : " + ChatColor.GOLD + server.getName());
        }
    }
}
