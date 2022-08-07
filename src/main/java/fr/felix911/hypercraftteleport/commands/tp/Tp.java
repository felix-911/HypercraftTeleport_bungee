package fr.felix911.hypercraftteleport.commands.tp;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import fr.felix911.apiproxy.ApiProxy;
import fr.felix911.hypercraftteleport.HypercraftTeleport;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;


public class Tp extends BaseCommand {
    private final HypercraftTeleport pl;

    public Tp(HypercraftTeleport hypercraftTeleport) {
        pl = hypercraftTeleport;
    }

    @CommandAlias("tp")
    @CommandPermission("hypercraftteleport.command.tp")
    @CommandCompletion("@players @nothing")
    public void tp(CommandSender commandSender, String player) {
        pl.getProxy().getScheduler().runAsync(pl, () -> {
                    BaseComponent b;

                    if (commandSender instanceof ProxiedPlayer){
                        ProxiedPlayer sender = (ProxiedPlayer) commandSender;
                        ProxiedPlayer target = pl.getProxy().getPlayer(player);
                        UUID playeruuid = null;
                        if (target == null) {
                            playeruuid = ApiProxy.getPlayerUUID(player);

                            if (playeruuid == null) {
                                String nop = pl.getConfigurationManager().getLang().getPlayerNotFound();
                                nop = nop.replace("{player}",player);
                                b = new TextComponent(nop);
                                commandSender.sendMessage(b);
                                return;
                            }
                        }

                        ProxiedPlayer toMove;
                        if (!sender.getName().equalsIgnoreCase(player)){
                            toMove = target;
                        } else {
                            toMove = sender;
                        }

                            String etat;
                        etat = ApiProxy.teleportPlayerToPlayer(sender.getUniqueId(),toMove.getUniqueId());

                        if (etat.equalsIgnoreCase("fail")) {
                                etat = pl.getConfigurationManager().getLang().getFailTp();
                            } else if(etat.equalsIgnoreCase("succes")){
                                etat = pl.getConfigurationManager().getLang().getToPlayer();
                                etat = etat.replace("{player}", player);
                            }
                            b = new TextComponent(etat);
                            sender.sendMessage(b);

                    } else {
                        String nop = pl.getConfigurationManager().getLang().getNoConsole();
                        b = new TextComponent(nop);
                        commandSender.sendMessage(b);
                    }
                }
        );}

    @CommandAlias("tphere")
    @CommandPermission("hypercraftteleport.command.tp")
    @CommandCompletion("@players @nothing")
    public void tphere(CommandSender commandSender, String player) {
        pl.getProxy().getScheduler().runAsync(pl, () -> {
                    BaseComponent b;

                    if (commandSender instanceof ProxiedPlayer){
                        ProxiedPlayer sender = (ProxiedPlayer) commandSender;
                        ProxiedPlayer target = pl.getProxy().getPlayer(player);
                        UUID playeruuid = null;
                        if (target == null) {
                            playeruuid = ApiProxy.getPlayerUUID(player);

                            if (playeruuid == null) {
                                String nop = pl.getConfigurationManager().getLang().getPlayerNotFound();
                                nop = nop.replace("{player}",player);
                                b = new TextComponent(nop);
                                commandSender.sendMessage(b);
                                return;
                            }
                        }

                        ProxiedPlayer toMove;
                        if (!sender.getName().equalsIgnoreCase(player)){
                            toMove = target;
                        } else {
                            toMove = sender;
                        }

                        String etat;
                        if (target == null) {
                            String location = pl.getLocationQueries().getLogoutLocation(playeruuid);

                            String[] l = location.split("¤");
                            String server = l[0];
                            String world = l[1];
                            double lx = Double.parseDouble(l[2]);
                            double ly = Double.parseDouble(l[3]);
                            double lz = Double.parseDouble(l[4]);
                            float pitch = Float.parseFloat(l[5]);
                            float yaw = Float.parseFloat(l[6]);

                            etat = ApiProxy.teleportPlayerToLocation(toMove,server,world,lx,ly,lz,pitch,yaw);
                        } else {
                            etat = ApiProxy.teleportPlayerToPlayer(toMove.getUniqueId(),sender.getUniqueId());
                        }

                        if (etat.equalsIgnoreCase("fail")) {
                            etat = pl.getConfigurationManager().getLang().getFailTp();
                        } else if(etat.equalsIgnoreCase("succes")){
                            etat = pl.getConfigurationManager().getLang().getToPlayer();
                            etat = etat.replace("{player}", player);
                        }
                        b = new TextComponent(etat);
                        sender.sendMessage(b);

                    } else {
                        String nop = pl.getConfigurationManager().getLang().getNoConsole();
                        b = new TextComponent(nop);
                        commandSender.sendMessage(b);
                    }
                }
        );}

    @CommandAlias("tppos")
    @CommandPermission("hypercraftteleport.command.tppos")
    @CommandCompletion("@nothing")
    public void tppos(CommandSender commandSender, Double x, Double y, Double z) {
        pl.getProxy().getScheduler().runAsync(pl, () -> {
            BaseComponent b;

            if (commandSender instanceof ProxiedPlayer) {
                ProxiedPlayer sender = (ProxiedPlayer) commandSender;

                String etat = ApiProxy.teleportPlayerToLocationCoords(sender, x, y, z, 0, 0);

                if (etat.equalsIgnoreCase("fail")) {
                    etat = pl.getConfigurationManager().getLang().getFailTp();
                } else if (etat.equalsIgnoreCase("succes")) {
                    etat = pl.getConfigurationManager().getLang().getToCoords();
                    etat = etat.replace("{x}", x.toString());
                    etat = etat.replace("{y}", y.toString());
                    etat = etat.replace("{z}", z.toString());
                }
                b = new TextComponent(etat);
                sender.sendMessage(b);
            } else {
                String nop = pl.getConfigurationManager().getLang().getNoConsole();
                b = new TextComponent(nop);
                commandSender.sendMessage(b);
            }
        });
    }

    @CommandAlias("tpherepos")
    @CommandPermission("hypercraftteleport.command.tpherepos")
    @CommandCompletion("@players @nothing")
    public void tpposhere(CommandSender commandSender, String playerName, Double x, Double y, Double z) {
        pl.getProxy().getScheduler().runAsync(pl, () -> {
            BaseComponent b;

            if (commandSender instanceof ProxiedPlayer) {
                ProxiedPlayer sender = (ProxiedPlayer) commandSender;

                ProxiedPlayer player = pl.getProxy().getPlayer(playerName);

                UUID playerUUID;
                if (player == null){
                    playerUUID = ApiProxy.getPlayerUUID(playerName);
                } else {
                    playerUUID = player.getUniqueId();
                }
                if (playerUUID == null) {
                    String nop = pl.getConfigurationManager().getLang().getPlayerNotFound();
                    nop = nop.replace("{player}",playerName);
                    b = new TextComponent(nop);
                    commandSender.sendMessage(b);
                    return;
                }

                String etat = ApiProxy.teleportPlayerToLocationCoords(player, x, y, z, 0, 0);

                if (etat.equalsIgnoreCase("fail")) {
                    etat = pl.getConfigurationManager().getLang().getFailTp();
                } else if (etat.equalsIgnoreCase("succes")) {
                    etat = pl.getConfigurationManager().getLang().getToCoordsOther();
                    etat = etat.replace("{player}", playerName);
                    etat = etat.replace("{x}", x.toString());
                    etat = etat.replace("{y}", y.toString());
                    etat = etat.replace("{z}", z.toString());

                    String moved = pl.getConfigurationManager().getLang().getToCoords();
                    moved = moved.replace("{x}", x.toString());
                    moved = moved.replace("{y}", y.toString());
                    moved = moved.replace("{z}", z.toString());
                    b = new TextComponent(moved);
                    player.sendMessage(b);
                }

                b = new TextComponent(etat);
                sender.sendMessage(b);
            } else {
                String nop = pl.getConfigurationManager().getLang().getNoConsole();
                b = new TextComponent(nop);
                commandSender.sendMessage(b);
            }
        });
    }

}
