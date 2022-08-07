package fr.felix911.hypercraftteleport.commands.tp;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.felix911.apiproxy.ApiProxy;
import fr.felix911.hypercraftteleport.HypercraftTeleport;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Locale;
import java.util.UUID;

@CommandAlias("tpa")
public class Tpa extends BaseCommand {
    private final HypercraftTeleport pl;

    public Tpa(HypercraftTeleport hypercraftTeleport) {
        pl = hypercraftTeleport;
    }

    @Default
    @CommandPermission("hypercraftteleport.command.tpa")
    @CommandCompletion("@players @nothing")
    public void tpa(CommandSender commandSender, String player){
        pl.getProxy().getScheduler().runAsync(pl, () -> {
        BaseComponent b;
        if (commandSender instanceof ProxiedPlayer){

            ProxiedPlayer sender = (ProxiedPlayer) commandSender;
            ProxiedPlayer player1 = pl.getProxy().getPlayer(player);

            if (player1 != null){
                System.out.println(player1.getUniqueId());

                try {
                    ByteArrayDataOutput out = ByteStreams.newDataOutput();

                    out.writeUTF("tpaDemand");
                    out.writeUTF(String.valueOf(sender.getUniqueId()));
                    out.writeUTF(String.valueOf(player1.getUniqueId()));

                    player1.getServer().sendData("hypercraft:teleport", out.toByteArray());
                } catch (Exception e) {
                    pl.getLogger().info(ChatColor.RED + "error envoi sur : " + ChatColor.GOLD + "tpaDemand");
                }
                System.out.println(player1.getServer().getInfo().getName().toLowerCase(Locale.ROOT));
                if (player1.getServer().getInfo().getName().toLowerCase(Locale.ROOT).contains("pvp")){
                    String pvp = pl.getConfigurationManager().getLang().getWarningPvp();
                    b = new TextComponent(pvp);
                    sender.sendMessage(b);
                }

            } else {
                UUID playeruuid = ApiProxy.getPlayerUUID(player);

                String nop;
                if (playeruuid == null){
                    nop = pl.getConfigurationManager().getLang().getPlayerNotFound();
                } else {
                    nop = pl.getConfigurationManager().getLang().getPlayerOffline();
                }
                nop = nop.replace("{player}", player);
                b = new TextComponent(nop);
                sender.sendMessage(b);
            }


        } else {
            String nop = pl.getConfigurationManager().getLang().getNoConsole();
            b = new TextComponent(nop);
            commandSender.sendMessage(b);
        }
    }
        );}
}
