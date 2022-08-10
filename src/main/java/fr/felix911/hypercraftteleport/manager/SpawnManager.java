package fr.felix911.hypercraftteleport.manager;

import fr.felix911.hypercraftteleport.HypercraftTeleport;
import fr.felix911.hypercraftteleport.objects.SpawnObject;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;


import java.io.File;
import java.io.IOException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SpawnManager {
    private final HypercraftTeleport pl;
    private String defaultSpawn;

    public SpawnManager(HypercraftTeleport hypercraftTeleport) {
        this.pl = hypercraftTeleport;

    }

    public void setSpawn(UUID senderUUID, SpawnObject spawn) {

        File racine = pl.getDataFolder();
        File file = new File(racine.getAbsolutePath() + "/config.yml");
        ConfigurationProvider configurationProvider = YamlConfiguration.getProvider(YamlConfiguration.class);
        try {
            Configuration c = configurationProvider.load(file);

            if (c.getSection("Spawn") == null){
                c.set("", "Spawn");
            }

            if (c.getSection("Spawn." + spawn.getServer()) == null){
                c.set("", spawn.getServer());
            }

            Configuration section = c.getSection("Spawn." + spawn.getServer());
            section.set("World", spawn.getWorld());
            section.set("X", spawn.getX());
            section.set("Y", spawn.getY());
            section.set("Z", spawn.getZ());
            section.set("Pitch", spawn.getPitch());
            section.set("Yaw", spawn.getYaw());
            configurationProvider.save(c , file);

            ProxiedPlayer sender = pl.getProxy().getPlayer(senderUUID);
            Map<String, SpawnObject> spawnMap = pl.getConfigurationManager().getConfig().getSpawnMap();
            spawnMap.put(spawn.getServer(),spawn);
            sender.sendMessage(new TextComponent("Spawn save"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getDefaultSpawn() {
        return defaultSpawn;
    }
}
