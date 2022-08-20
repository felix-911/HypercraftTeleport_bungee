package fr.felix911.hypercraftteleport.configuration;

import com.google.gson.internal.LinkedHashTreeMap;
import fr.felix911.hypercraftteleport.objects.SpawnObject;
import net.md_5.bungee.config.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigConfig {

    private String databaseName;
    private String databaseHost;
    private String databasePort;
    private String databaseUser;
    private String databasePassword;
    private boolean useSSL;

    private List<String> splitServer;

    private String lPplayers;
    private String lPVIP;

    Map<String, Integer> mapLimites = new HashMap<>();

    private int waitTp;
    private String requiredRank;
    private int priceHomes;
    int maxHomes;
    private String defaultSpawn;
    private Map<String, SpawnObject> spawnMap = new HashMap<>();

    public void load(Configuration config) {
        databaseName = config.getString("Database.name").replace("&", "§");
        databaseHost = config.getString("Database.host").replace("&", "§");
        databasePort = config.getString("Database.port").replace("&", "§");
        databaseUser = config.getString("Database.user").replace("&", "§");
        databasePassword = config.getString("Database.password").replace("&", "§");
        useSSL = config.getBoolean("Database.useSSL");

        //Split
        splitServer = (List<String>) config.getList("SplitServer");

        lPplayers = config.getString("System.LuckPerms.TrackLp").replace("&", "§");
        lPVIP = config.getString("System.LuckPerms.TrackVIP").replace("&", "§");

        for (String rank : config.getSection("Rank").getKeys()){
            int i = config.getInt("Rank." + rank);
            mapLimites.put(rank, i);
        }
        waitTp = config.getInt("Homes.WaitTp");
        requiredRank = config.getString("Homes.Rank");
        priceHomes = config.getInt("Homes.Price");
        maxHomes = config.getInt("Homes.Max");

        defaultSpawn = config.getString( "Spawn.Default");

        for (String spawn : config.getSection("Spawn").getKeys()) {
            if (!spawn.equalsIgnoreCase("Default")) {
                String world = config.getString("Spawn." + spawn + ".World");
                double x = config.getDouble("Spawn." + spawn + ".X");
                double y = config.getDouble("Spawn." + spawn + ".Y");
                double z = config.getDouble("Spawn." + spawn + ".Z");
                float pitch = config.getFloat("Spawn." + spawn + ".Pitch");
                float yaw = config.getFloat("Spawn." + spawn + ".Yaw");
                SpawnObject spawnObject = new SpawnObject(spawn, world, x, y, z, pitch, yaw);
                spawnMap.put(spawn, spawnObject);
            }
        }
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public String getDatabaseHost() {
        return databaseHost;
    }

    public String getDatabasePort() {
        return databasePort;
    }

    public String getDatabaseUser() {
        return databaseUser;
    }

    public String getDatabasePassword() {
        return databasePassword;
    }

    public boolean isUseSSL() {
        return useSSL;
    }

    public List<String> getSplitServer() {
        return splitServer;
    }

    public String getlPplayers() {
        return lPplayers;
    }

    public String getlPVIP() {
        return lPVIP;
    }

    public Map<String, Integer> getMapLimites() {
        return mapLimites;
    }

    public String getRequiredRank() {
        return requiredRank;
    }

    public int getWaitTp() {
        return waitTp;
    }

    public int getPriceHomes() {
        return priceHomes;
    }

    public int getMaxHomes() {
        return maxHomes;
    }

    public String getDefaultSpawn() {
        return defaultSpawn;
    }

    public Map<String, SpawnObject> getSpawnMap() {
        return spawnMap;
    }
}
