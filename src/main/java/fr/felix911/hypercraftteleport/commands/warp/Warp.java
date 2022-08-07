package fr.felix911.hypercraftteleport.commands.warp;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.annotation.Optional;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.felix911.apiproxy.ApiProxy;
import fr.felix911.hypercraftteleport.HypercraftTeleport;
import fr.felix911.hypercraftteleport.objects.WarpObject;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.*;

@CommandAlias("warp")
public class Warp extends BaseCommand {
    private final HypercraftTeleport pl;

    public Warp(HypercraftTeleport hypercraftTeleport) {
        this.pl = hypercraftTeleport;
    }

    @Default
    @CommandPermission("hypercraftteleport.command.warplist")
    @CommandCompletion("@warps @nothing")
    public void warplist(CommandSender commandSender){
        pl.getProxy().getScheduler().runAsync(pl, () -> {
            BaseComponent b;

            if (commandSender instanceof ProxiedPlayer){

                ProxiedPlayer sender = (ProxiedPlayer) commandSender;
                Map<String, WarpObject> cache = pl.getWarpCache().getWarpCache();

                    if (cache.isEmpty()) {
                        String out = pl.getConfigurationManager().getLang().getWarpNoWarpsFound();
                        b = new TextComponent(out);
                        sender.sendMessage(b);
                    } else {
                        List<String> export = new ArrayList<>();

                        for (String warpName : cache.keySet()){

                            WarpObject warpObject = cache.get(warpName);

                            if (sender.hasPermission("hypercraftteleport.command.warp.showall")){
                                String s = warpName + "¤" + warpObject.getBlock() + "¤" + warpObject.getCustomModelData();
                                export.add(s);
                            } else if (warpObject.isNeedPerm() && sender.hasPermission("hypercraftteleport.command.warp." + warpName)){
                                String s = warpName + "¤" + warpObject.getBlock() + "¤" + warpObject.getCustomModelData();
                                export.add(s);
                            } else if (!warpObject.isNeedPerm()){
                                String s = warpName + "¤" + warpObject.getBlock() + "¤" + warpObject.getCustomModelData();
                                export.add(s);
                            }

                        }
                        boolean edit = false;
                        if (sender.hasPermission("hypercraftteleport.command.warp.config")){
                            edit = true;
                        }
                        if (export.isEmpty()) {
                            String out = pl.getConfigurationManager().getLang().getWarpNoWarpsFound();
                            b = new TextComponent(out);
                            sender.sendMessage(b);
                        } else {
                            export.sort(Comparator.naturalOrder());
                            sendWarp(sender,export,edit);
                        }

                    }
            }else {
                b = new TextComponent(pl.getConfigurationManager().getLang().getNoConsole());
                commandSender.sendMessage(b);
            }
        });
    }

    @CommandPermission("hypercraftteleport.command.warp")
    @CommandCompletion("@warps @nothing")
    public void warp(CommandSender commandSender, String warp) {
        pl.getProxy().getScheduler().runAsync(pl, () -> {
            BaseComponent b;

            if (commandSender instanceof ProxiedPlayer) {
                ProxiedPlayer sender = (ProxiedPlayer) commandSender;
                Map<String, WarpObject> cache = pl.getWarpCache().getWarpCache();

                String etat;
                if (cache.containsKey(warp)){

                    WarpObject warpObject = cache.get(warp);
                    etat = ApiProxy.teleportPlayerToLocation(sender,warpObject.getServer(),warpObject.getWorld(),warpObject.getX(),warpObject.getY(),warpObject.getZ(),warpObject.getPitch(),warpObject.getYaw());
                    if (etat.equalsIgnoreCase("fail")) {
                        etat = pl.getConfigurationManager().getLang().getFailTp();
                    } else if(etat.equalsIgnoreCase("succes")){
                        etat = pl.getConfigurationManager().getLang().getHomeTeleport();
                        etat = etat.replace("{home}", warp);
                    }
                } else {
                    etat = pl.getConfigurationManager().getLang().getWarpNoWarpFound();
                    etat = etat.replace("{home}", warp);
                }
                b = new TextComponent(etat);
                sender.sendMessage(b);


            } else {
            b = new TextComponent(pl.getConfigurationManager().getLang().getNoConsole());
            commandSender.sendMessage(b);
        }
        });
    }

    private void sendWarp(ProxiedPlayer sender, List<String> export, boolean edit){

        ServerInfo server = sender.getServer().getInfo();
        try {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();

            out.writeUTF("warpListGui");
            out.writeUTF(String.valueOf(sender.getUniqueId()));
            out.writeUTF(String.valueOf(export));
            out.writeBoolean(edit);

            server.sendData("hypercraft:teleport", out.toByteArray());
        } catch (Exception e) {
            pl.getLogger().info(ChatColor.RED + "error envoi sur : " + ChatColor.GOLD + server.getName());
        }
    }

}
