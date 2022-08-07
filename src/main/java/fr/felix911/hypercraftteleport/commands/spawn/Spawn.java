package fr.felix911.hypercraftteleport.commands.spawn;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import fr.felix911.apiproxy.ApiProxy;
import fr.felix911.hypercraftteleport.HypercraftTeleport;
import fr.felix911.hypercraftteleport.objects.LocationObject;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Spawn extends BaseCommand {
    private final HypercraftTeleport pl;

    public Spawn(HypercraftTeleport hypercraftTeleport) {this.pl = hypercraftTeleport;}

        @CommandAlias("spawn")
        @CommandPermission("hypercraftteleport.command.spawn")
        public void spawn(CommandSender commandSender){

            BaseComponent b;

            if (commandSender instanceof ProxiedPlayer){
                ProxiedPlayer sender = (ProxiedPlayer) commandSender;
                LocationObject spawn = pl.getSpawn();

                if (spawn == null){
                    String notDefine = pl.getConfigurationManager().getLang().getNotDefine();
                    b = new TextComponent(notDefine);
                } else {
                    ApiProxy.teleportPlayerToLocation(sender, spawn.getServer(), spawn.getWorld(),
                            spawn.getX(), spawn.getY(), spawn.getZ(), spawn.getPitch(), spawn.getYaw());
                    String teleport = pl.getConfigurationManager().getLang().getTeleport();
                    b = new TextComponent(teleport);
                }
            } else {
                String nop = pl.getConfigurationManager().getLang().getNoConsole();
                b = new TextComponent(nop);
            }
            commandSender.sendMessage(b);
        }
}



