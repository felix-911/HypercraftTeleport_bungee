package fr.felix911.hypercraftteleport.commands.spawn;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import fr.felix911.apiproxy.ApiProxy;
import fr.felix911.hypercraftteleport.HypercraftTeleport;
import fr.felix911.hypercraftteleport.objects.SpawnObject;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Map;

public class Spawn extends BaseCommand {
    private final HypercraftTeleport pl;

    public Spawn(HypercraftTeleport hypercraftTeleport) {this.pl = hypercraftTeleport;}

        @CommandAlias("spawn")
        @CommandPermission("hypercraftteleport.command.spawn")
        public void spawn(CommandSender commandSender){

            BaseComponent b;

            if (commandSender instanceof ProxiedPlayer){
                ProxiedPlayer sender = (ProxiedPlayer) commandSender;

                Map<String, SpawnObject> spawnMap = pl.getConfigurationManager().getConfig().getSpawnMap();

                if (spawnMap.isEmpty()){
                    String notDefine = pl.getConfigurationManager().getLang().getNotDefine();
                    b = new TextComponent(notDefine);
                } else {
                    SpawnObject spawnObject;
                    if (spawnMap.containsKey(sender.getServer().getInfo().getName())){
                        spawnObject = spawnMap.get(sender.getServer().getInfo().getName());
                    } else {
                        spawnObject = spawnMap.get(pl.getConfigurationManager().getConfig().getDefaultSpawn());
                    }

                    String s;
                    if (spawnObject != null){
                        ApiProxy.teleportPlayerToLocation(sender, spawnObject.getServer(), spawnObject.getWorld(),
                                spawnObject.getX(), spawnObject.getY(), spawnObject.getZ(), spawnObject.getPitch(), spawnObject.getYaw());
                        s = pl.getConfigurationManager().getLang().getTeleport();
                        System.out.println("teleport");
                    } else {
                        s = pl.getConfigurationManager().getLang().getNoDefaultSpawn();
                        System.out.println("nodefault");
                    }

                    b = new TextComponent(s);
                }
            } else {
                String nop = pl.getConfigurationManager().getLang().getNoConsole();
                b = new TextComponent(nop);
            }
            commandSender.sendMessage(b);
        }
}



