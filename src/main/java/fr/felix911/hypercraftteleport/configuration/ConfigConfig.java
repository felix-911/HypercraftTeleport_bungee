package fr.felix911.hypercraftteleport.configuration;

import fr.felix911.hypercraftteleport.objects.LocationObject;
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
}
