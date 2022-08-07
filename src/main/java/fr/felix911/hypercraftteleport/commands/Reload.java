package fr.felix911.hypercraftteleport.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import fr.felix911.hypercraftteleport.HypercraftTeleport;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

@CommandAlias("homesReloadBungee")
public class Reload extends BaseCommand {
    private final HypercraftTeleport plugin;

    public Reload(HypercraftTeleport hypercraftHomes) {
        this.plugin = hypercraftHomes;
    }

    @Default
    @CommandPermission("hypercraftteleport.admin.reload")
    @CommandCompletion("@reload @nothing")
    public void homesreload(CommandSender sender, String args) {

        plugin.getProxy().getScheduler().runAsync(plugin, () -> {

            switch (args){

                case "config":
                    plugin.getConfigurationManager().getConfig().getMapLimites().clear();
                    plugin.getConfigurationManager().reloadConfig(sender);
                    break;
                case "language":
                    plugin.getConfigurationManager().reloadLanguage(sender);
                    break;
                case "cache":
                    plugin.reloadCache(sender);
                    break;
                case "all":
                    plugin.getConfigurationManager().getConfig().getMapLimites().clear();
                    plugin.getConfigurationManager().reloadConfig(sender);
                    plugin.getConfigurationManager().reloadLanguage(sender);
                    plugin.reloadCache(sender);
                    break;
                default:
                    String msg = plugin.getConfigurationManager().getLang().getReload();
                    BaseComponent b = new TextComponent(msg);
                    sender.sendMessage(b);
            }
        });
    }
}