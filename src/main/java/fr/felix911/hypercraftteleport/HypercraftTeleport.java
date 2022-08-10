package fr.felix911.hypercraftteleport;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.felix911.apiproxy.ApiProxy;
import fr.felix911.hypercraftteleport.commands.Reload;
import fr.felix911.hypercraftteleport.commands.home.DelHome;
import fr.felix911.hypercraftteleport.commands.home.Home;
import fr.felix911.hypercraftteleport.commands.home.Homes;
import fr.felix911.hypercraftteleport.commands.spawn.Spawn;
import fr.felix911.hypercraftteleport.commands.tp.Tp;
import fr.felix911.hypercraftteleport.commands.tp.Tpa;
import fr.felix911.hypercraftteleport.commands.tp.Tpahere;
import fr.felix911.hypercraftteleport.commands.warp.DelWarp;
import fr.felix911.hypercraftteleport.commands.warp.Warp;
import fr.felix911.hypercraftteleport.commands.warp.WarpConfig;
import fr.felix911.hypercraftteleport.listener.BungeeMsg;
import fr.felix911.hypercraftteleport.listener.JoinListener;
import fr.felix911.hypercraftteleport.manager.*;
import fr.felix911.hypercraftteleport.manager.cache.HomesCache;
import fr.felix911.hypercraftteleport.manager.cache.WarpCache;
import fr.felix911.hypercraftteleport.objects.HomeObject;
import fr.felix911.hypercraftteleport.objects.SpawnObject;
import fr.felix911.hypercraftteleport.objects.WarpObject;
import fr.felix911.hypercraftteleport.queries.HomeQueries;
import fr.felix911.hypercraftteleport.queries.LocationQueries;
import fr.felix911.hypercraftteleport.queries.WarpQueries;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import org.json.simple.JSONObject;

import java.io.*;
import java.util.*;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class HypercraftTeleport extends Plugin {


    private ConfigurationManager configurationManager;
    private DatabaseManager databaseManager;
    private CommandManager commandManager;
    private BuyHomesManager buyHomesManager;

    private HomeObject homeObject;
    private WarpObject warpObject;
    private SpawnObject locationObject;

    private HomeQueries homeQueries;
    private WarpQueries warpQueries;
    private LocationQueries locationQueries;

    private WarpCache warpCache;
    private HomesCache homeCache;

    private CreateHome createHome;
    private CreateWarp createWarp;
    private SpawnManager spawnManager;

    @Override
    public void onEnable() {
        this.getLogger().info(ChatColor.GOLD + "Ouverture du botin");
        configurationManager = new ConfigurationManager(this);
        commandManager = new CommandManager(this);
        CommandCompletionsManager cCM = new CommandCompletionsManager(this);

        getLogger().info(ChatColor.YELLOW + "Initialisation des connections à la base de données");
        try{
            databaseManager = new DatabaseManager(this);
            this.homeQueries = new HomeQueries(this);
            this.warpQueries = new WarpQueries(this);
            this.locationQueries = new LocationQueries(this);
            this.databaseManager.addRepository(this.homeQueries);
            this.databaseManager.addRepository(this.warpQueries);
            this.databaseManager.initializeTables();
            getLogger().info(ChatColor.GREEN + "Initialisation des connections à la base de données reussi");
        } catch (Exception e){
            e.printStackTrace();
            getLogger().info(ChatColor.RED + "Echec de Initialisation des connections à la base de données");
            onDisable();
        }

        getLogger().info(ChatColor.YELLOW + "Ecoute des Channel BungeeCord");
        try {
            this.getProxy().registerChannel( "hypercraft:teleport");
            getProxy().getPluginManager().registerListener(this, new BungeeMsg(this));
        } catch (Exception e) {
            getLogger().info(ChatColor.DARK_RED + "Erreur des Channels BungeeCord");
            onDisable();
        }

        this.homeCache = new HomesCache(this);
        this.warpCache = new WarpCache(this);
        getWarpQueries().loadWarp();

        homeObject = new HomeObject(this);
        warpObject = new WarpObject(this);
        locationObject = new SpawnObject(this);

        createHome = new CreateHome(this);
        createWarp = new CreateWarp(this);

        spawnManager = new SpawnManager(this);

        buyHomesManager = new BuyHomesManager(this);

        this.getProxy().getPluginManager().registerListener(this, new JoinListener(this));

        commandManager.registerCommand(new DelHome(this));
        commandManager.registerCommand(new Homes(this));
        commandManager.registerCommand(new Home(this));

        commandManager.registerCommand(new Warp(this));
        commandManager.registerCommand(new DelWarp(this));
        commandManager.registerCommand(new WarpConfig(this));

        commandManager.registerCommand(new Reload(this));

        commandManager.registerCommand(new Tp(this));
        commandManager.registerCommand(new Tpa(this));
        commandManager.registerCommand(new Tpahere(this));


        commandManager.registerCommand(new Spawn(this));

        this.getLogger().info(ChatColor.GREEN + "botin prêt à être utilisé");
    }

    public void createDefaultConfiguration(File actual, String defaultName) {
        // Make parent directories
        File parent = actual.getParentFile();
        if (!parent.exists()) {
            parent.mkdirs();
        }

        if (actual.exists()) {
            return;
        }

        InputStream input = null;
        try {
            JarFile file = new JarFile(this.getFile());
            ZipEntry copy = file.getEntry(defaultName);
            if (copy == null) throw new FileNotFoundException();
            input = file.getInputStream(copy);
        } catch (IOException e) {
            getLogger().severe("Unable to read default configuration: " + defaultName);
        }

        if (input != null) {
            FileOutputStream output = null;

            try {
                output = new FileOutputStream(actual);
                byte[] buf = new byte[8192];
                int length;
                while ((length = input.read(buf)) > 0) {
                    output.write(buf, 0, length);
                }

                getLogger().info("Default configuration file written: " + actual.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    input.close();
                } catch (IOException ignored) {
                }

                try {
                    if (output != null) {
                        output.close();
                    }
                } catch (IOException ignored) {
                }
            }
        }


    }


    public boolean checkLimits(UUID uuid, String rank) {
        boolean check = true;
        if (rank.equalsIgnoreCase("Player")){
            rank = ApiProxy.getSpecifiqueRank(uuid, getConfigurationManager().getConfig().getlPVIP());
            if (rank == null){
                rank = ApiProxy.getSpecifiqueRank(uuid, getConfigurationManager().getConfig().getlPplayers());
            }
        }

        if (rank.equalsIgnoreCase("error")){
            System.out.println("error " + rank);
        }else {
            int limit = configurationManager.getConfig().getMapLimites().get(rank);
            Map<String, HomeObject> homesPlayer = getHomeCache().getHomesCache().get(uuid);
            int bonus = getHomeCache().getHomesBonusCache().get(uuid);
            if (homesPlayer == null){
                getHomeCache().loadPlayer(uuid);
                homesPlayer = getHomeCache().getHomesCache().get(uuid);
            }

            int homes = 0;
            if (!(homesPlayer == null)){
                homes = homesPlayer.size();
            }

            if (homes >= limit + bonus){
                check = false;
            }
        }
        return check;
    }



    public void reloadCache(CommandSender sender) {
        BaseComponent b;

        String reloadCache = "&eRechargement des Homes".replace("&", "§");
        String reloadCacheDone = "&aRechargement des Caches terminé".replace("&", "§");
        b = new TextComponent(reloadCache);
        sender.sendMessage(b);
        getHomeCache().getHomesCache().clear();
        getHomeCache().reload();
        b = new TextComponent(reloadCacheDone);
        sender.sendMessage(b);
    }

    public void updateHomeBukkit(ProxiedPlayer sender, UUID playeruuid, Map<String, HomeObject> homeMap){

        List<String> homelist = new ArrayList<>(homeMap.keySet());

        ServerInfo server = sender.getServer().getInfo();
        try {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();

            out.writeUTF("homelistcache");
            out.writeUTF(String.valueOf(playeruuid));
            out.writeUTF(homelist.toString());

            server.sendData("hypercraft:teleport", out.toByteArray());
        } catch (Exception e) {
            getLogger().info(ChatColor.RED + "error envoi sur : " + ChatColor.GOLD + server.getName());
        }
    }

    public void updateWarpBukkit(ProxiedPlayer sender, Map<String, WarpObject> warpCache){

        List<String> warplist = new ArrayList<>(warpCache.keySet());

        ServerInfo server = sender.getServer().getInfo();
        try {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();

            out.writeUTF("warplistcache");
            out.writeUTF(warplist.toString());

            server.sendData("hypercraft:teleport", out.toByteArray());
        } catch (Exception e) {
            getLogger().info(ChatColor.RED + "error envoi sur : " + ChatColor.GOLD + server.getName());
        }
    }

    public static JSONObject serializeAllWarpToJson(ProxiedPlayer sender, List<WarpObject> warpList, boolean edit) {
        JSONObject json = new JSONObject();


        json.put("RequestedPlayerUUID", sender.getUniqueId().toString());
        json.put("Editor", String.valueOf(edit));

        JSONObject jsonWarpList = new JSONObject();
        for (WarpObject warp : warpList){
            JSONObject jsonWarp = new JSONObject();

            jsonWarp.put("Server", warp.getServer());
            jsonWarp.put("World", warp.getWorld());
            jsonWarp.put("X", warp.getX());
            jsonWarp.put("Y", warp.getY());
            jsonWarp.put("Z", warp.getZ());
            jsonWarp.put("Pitch", warp.getPitch());
            jsonWarp.put("Yaw", warp.getY());
            jsonWarp.put("Block", warp.getBlock());
            jsonWarp.put("CustomModelData", warp.getCustomModelData());
            jsonWarp.put("NeedPerm", warp.isNeedPerm());

            jsonWarpList.put(warp.getName(), jsonWarp);
        }

        json.put("WarpList", jsonWarpList);

        return json;
    }

    //getter

    public ConfigurationManager getConfigurationManager() {
        return configurationManager;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public CreateHome getCreateHome() {
        return createHome;
    }

    public CreateWarp getCreateWarp() {
        return createWarp;
    }

    public BuyHomesManager getBuyHomesManager() {
        return buyHomesManager;
    }

    public SpawnManager getSpawnManager() {
        return spawnManager;
    }

    //Spawn

    //Queries

    public HomeQueries getHomeQueries() {
        return homeQueries;
    }

    public WarpQueries getWarpQueries() {
        return warpQueries;
    }

    public LocationQueries getLocationQueries() {
        return locationQueries;
    }

    //Cache
    public HomesCache getHomeCache() {
        return homeCache;
    }

    public WarpCache getWarpCache() {
        return warpCache;
    }

    //Object

    public HomeObject getHomeObject() {
        return homeObject;
    }

    public WarpObject getWarpObject() {
        return warpObject;
    }

}
