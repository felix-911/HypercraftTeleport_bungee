package fr.felix911.hypercraftteleport.manager;


import co.aikar.commands.BungeeCommandManager;
import co.aikar.commands.MessageType;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.IOException;
import java.util.Locale;

public class CommandManager extends BungeeCommandManager {
    public CommandManager(Plugin plugin) {
        super(plugin);
        //this.enableUnstableAPI("help");
        this.setFormat(MessageType.SYNTAX, new ChatColor[]{ChatColor.YELLOW, ChatColor.GOLD});
        this.setFormat(MessageType.INFO, new ChatColor[]{ChatColor.YELLOW, ChatColor.GOLD});
        //this.setFormat(MessageType.HELP, new ChatColor[]{ChatColor.YELLOW, ChatColor.GOLD, ChatColor.RED});
        this.setFormat(MessageType.ERROR, new ChatColor[]{ChatColor.RED, ChatColor.GOLD});

        try {
            this.getLocales().loadYamlLanguageFile("language.yml", Locale.FRENCH);
        } catch (IOException var3) {
            plugin.getLogger().severe("Failed to load ACF core language file");
            var3.printStackTrace();
        }

        this.getLocales().setDefaultLocale(Locale.FRENCH);
    }
}

