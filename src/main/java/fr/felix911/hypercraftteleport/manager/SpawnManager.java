package fr.felix911.hypercraftteleport.manager;

import fr.felix911.hypercraftteleport.HypercraftTeleport;
import fr.felix911.hypercraftteleport.objects.LocationObject;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;


import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class SpawnManager {
    private final HypercraftTeleport pl;

    public SpawnManager(HypercraftTeleport hypercraftTeleport) {
        this.pl = hypercraftTeleport;
    }

    public void setSpawn(UUID senderUUID, LocationObject spawn) {

        File racine = pl.getDataFolder();
        File file = new File(racine.getAbsolutePath() + "/config.yml");
        ConfigurationProvider configurationProvider = YamlConfiguration.getProvider(YamlConfiguration.class);
        try {
            Configuration c = configurationProvider.load(file);

            if (c.getSection("Spawn") == null){
                c.set("", "Spawn:");
            }
            Configuration section = c.getSection("Spawn");

            section.set("Server:", spawn.getServer());
            section.set("World:", spawn.getWorld());
            section.set("X:", spawn.getX());
            section.set("Y:", spawn.getY());
            section.set("Z:", spawn.getZ());
            section.set("Pitch:", spawn.getPitch());
            section.set("Yaw:", spawn.getYaw());
            configurationProvider.save(c , file);

            ProxiedPlayer sender = pl.getProxy().getPlayer(senderUUID);
            sender.sendMessage(new TextComponent("Spawn save"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadSpawn() {

        File racine = pl.getDataFolder();
        File file = new File(racine.getAbsolutePath() + "/config.yml");
        ConfigurationProvider configurationProvider = YamlConfiguration.getProvider(YamlConfiguration.class);
        try {
            Configuration config = configurationProvider.load(file);
            LocationObject spawn;
            if (config.getSection("Spawn") != null) {

                String spawnServer = config.getString("Spawn.Server");
                String spawnWorld = config.getString("Spawn.World");
                int spawnX = config.getInt("Spawn.X");
                int spawnY = config.getInt("Spawn.Y");
                int spawnZ = config.getInt("Spawn.Z");
                float spawnPitch = config.getFloat("Spawn.Pitch");
                float spawnYaw = config.getFloat("Spawn.Yaw");
                spawn = new LocationObject(spawnServer, spawnWorld, spawnX, spawnY, spawnZ, spawnPitch, spawnYaw);
                pl.setSpawn(spawn);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
