package fr.felix911.hypercraftteleport.manager;

import fr.felix911.hypercraftteleport.HypercraftTeleport;
import fr.felix911.hypercraftteleport.configuration.ConfigConfig;
import fr.felix911.hypercraftteleport.configuration.LangConfig;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigurationManager {
    private final HypercraftTeleport plugin;

    private Configuration langConfig;
    private final LangConfig lang = new LangConfig();
    private final ConfigConfig config = new ConfigConfig();


    public ConfigurationManager(HypercraftTeleport plugin) {
        this.plugin = plugin;
        this.reloadConfig(null);
        this.reloadLanguage(null);

    }

    public void reloadConfig(CommandSender sender) {


        BaseComponent b;

        String reloadConfig = "&eChargement du fichier de configuration".replace("&", "§");
        String reloadConfigDone = "&aChargement du fichier de configuration terminé".replace("&", "§");
        String reloadConfigFail = "&cChargement du fichier de configuration Echoué &6(Voir Console)".replace("&", "§");

        if (sender != null) {
            b = new TextComponent(reloadConfig);
            sender.sendMessage(b);
        } else {
            plugin.getLogger().info(reloadConfig);
        }
        Configuration configConfig;
        try {
            File configFile = new File(plugin.getDataFolder(), "config.yml");
            plugin.createDefaultConfiguration(configFile, "config.yml");
            configConfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
        } catch (IOException e) {
            e.printStackTrace();
            if (sender != null) {
                b = new TextComponent(reloadConfigFail);
                sender.sendMessage(b);
            } else {
                plugin.getLogger().info(reloadConfigFail);
            }
            return;
        }

        config.load(configConfig);
        if (sender != null) {
            b = new TextComponent(reloadConfigDone);
            sender.sendMessage(b);
        } else {
            plugin.getLogger().info(reloadConfigDone);
        }
    }

    public void reloadLanguage(CommandSender sender) {

        BaseComponent b;

        String reloadLanguage = "&eChargement du fichier de Langue".replace("&", "§");
        String reloadLanguageDone = "&aChargement du fichier de Langue terminé".replace("&", "§");
        String reloadLanguageFail = "&cChargement du fichier de Langue Echoué &6(Voir Console)".replace("&", "§");



        if (sender != null) {
            b = new TextComponent(reloadLanguage);
            sender.sendMessage(b);
        } else {
            plugin.getLogger().info(reloadLanguage);
        }


        try {
            File langFile = new File(plugin.getDataFolder(), "language.yml");
            plugin.createDefaultConfiguration(langFile, "language.yml");
            langConfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(langFile);
        } catch (IOException e) {

            if (sender != null) {
                b = new TextComponent(reloadLanguageFail);
                sender.sendMessage(b);
            } else {
                plugin.getLogger().info(reloadLanguageFail);
            }
            e.printStackTrace();
        }

        lang.load(langConfig);
        if (sender != null) {
            b = new TextComponent(reloadLanguageDone);
            sender.sendMessage(b);
        } else {
            plugin.getLogger().info(reloadLanguageDone);
        }
/*
 plugin.getLogger().info(lang.getSameUser());
        plugin.getLogger().info(lang.getNoPermWarn());
        plugin.getLogger().info(lang.getNotFound());
        plugin.getLogger().info(lang.getMuteNull());
        plugin.getLogger().info(lang.getWhatReload());
        plugin.getLogger().info(lang.getInvalidTime());
        plugin.getLogger().info(lang.getNotConnected());
        plugin.getLogger().info(lang.getNoConsoleVanish());
        plugin.getLogger().info("---------------");
        plugin.getLogger().info(lang.getBc());
        plugin.getLogger().info("---------------");
        plugin.getLogger().info(lang.getMuted());
        plugin.getLogger().info(lang.getAllReadyMuted());
        plugin.getLogger().info(lang.getNotMuted());
        plugin.getLogger().info(lang.getNoOneMute());
        plugin.getLogger().info(lang.getShowMuteTitle());
        plugin.getLogger().info(lang.getShowMute());
        plugin.getLogger().info(lang.getMuted());
        plugin.getLogger().info(lang.getUnMute());
        plugin.getLogger().info(lang.getUnMuteSilent());
        plugin.getLogger().info(lang.getChatMuted());
        plugin.getLogger().info(lang.getChatUnMuted());
        plugin.getLogger().info("---------------");
        plugin.getLogger().info(lang.getKickNoRaison());
        plugin.getLogger().info(lang.getKickNoRaisonPublic());
        plugin.getLogger().info(lang.getKickProxyRaison());
        plugin.getLogger().info(lang.getKickRaisonPublic());
        plugin.getLogger().info("---------------");
        plugin.getLogger().info(lang.getNotBan());
        plugin.getLogger().info(lang.getBan());
        plugin.getLogger().info(lang.getBanNoRaison());
        plugin.getLogger().info(lang.getBanNoRaisonPublic());
        plugin.getLogger().info(lang.getTempBan());
        plugin.getLogger().info(lang.getAllReadyBan());
        plugin.getLogger().info(lang.getUnBan());
        plugin.getLogger().info("---------------");
        plugin.getLogger().info(lang.getCheckNoRaison());
        plugin.getLogger().info(lang.getCheckBan());
        plugin.getLogger().info(lang.getCheckTempBan());
        plugin.getLogger().info(lang.getCheckNotBan());
        plugin.getLogger().info("---------------");
        plugin.getLogger().info(lang.getPreConnection());
        plugin.getLogger().info(lang.getPreBanPermMsg());
        plugin.getLogger().info(lang.getPreBanPermMsgNoRaison());
        plugin.getLogger().info(lang.getPreBanPermMsgPoints());
        plugin.getLogger().info(lang.getPreBanPermMsgCheat());
        plugin.getLogger().info(lang.getPreBanTempMsg());
        plugin.getLogger().info("---------------");
        plugin.getLogger().info(lang.getVanishCommande());
        plugin.getLogger().info(lang.getVanishOn());
        plugin.getLogger().info(lang.getVanishOff());
 */

    }

    public ConfigConfig getConfig() {
        return config;
    }

    public LangConfig getLang() {
        return lang;
    }


}
