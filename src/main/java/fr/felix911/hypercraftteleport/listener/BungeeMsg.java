package fr.felix911.hypercraftteleport.listener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.felix911.apiproxy.ApiProxy;
import fr.felix911.hypercraftteleport.HypercraftTeleport;
import fr.felix911.hypercraftteleport.objects.HomeObject;
import fr.felix911.hypercraftteleport.objects.LocationObject;
import fr.felix911.hypercraftteleport.objects.WarpObject;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BungeeMsg implements Listener {
    private final HypercraftTeleport pl;

    public BungeeMsg(HypercraftTeleport hypercraftHomes) {
        this.pl = hypercraftHomes;
    }

    @EventHandler
    public void onPluginMessageReceived(PluginMessageEvent event) {
        String channel = event.getTag();
        byte[] bytes = event.getData();

        if (channel.equalsIgnoreCase("hypercraft:teleport")) {
            ByteArrayDataInput in = ByteStreams.newDataInput(bytes);
            String subChannel = in.readUTF();

            if (subChannel.equalsIgnoreCase("sethome")){

                String jsonString = in.readUTF();
                JsonObject json = (JsonObject) new JsonParser().parse(jsonString);
                UUID senderUUID = UUID.fromString(json.get("RequestedPlayerUUID").toString().replace("\"",""));
                UUID playeruuid = UUID.fromString(json.get("PlayerHomeUUID").toString().replace("\"",""));
                JsonObject jsonHome = (JsonObject) json.get("home");
                String name = jsonHome.get("Name").toString().replace("\"","");
                String server = jsonHome.get("Server").toString().replace("\"","");
                String world = jsonHome.get("World").toString().replace("\"","");
                double x = Double.parseDouble(jsonHome.get("X").toString().replace("\"",""));
                double y = Double.parseDouble(jsonHome.get("Y").toString().replace("\"",""));
                double z = Double.parseDouble(jsonHome.get("Z").toString().replace("\"",""));
                float pitch = Float.parseFloat(jsonHome.get("Pitch").toString().replace("\"",""));
                float yaw = Float.parseFloat(jsonHome.get("Yaw").toString().replace("\"",""));
                String block = jsonHome.get("Block").toString().replace("\"","");
                int customModelData = Integer.parseInt(jsonHome.get("CustomModelData").toString());

                String rank = "Player";

                HomeObject home = new HomeObject(name,server,world,x,y,z,pitch,yaw,block,customModelData);
                pl.getCreateHome().createHome(senderUUID, playeruuid, rank, home);
            }
            if (subChannel.equalsIgnoreCase("homeMaterial")){

                UUID senderUUID = UUID.fromString(in.readUTF());
                String home = in.readUTF();
                String material = in.readUTF();
                int customModelData = in.readInt();

                ProxiedPlayer sender = pl.getProxy().getPlayer(senderUUID);

                BaseComponent b;
                Map<UUID, Map<String, HomeObject>> cacheHomes = pl.getHomeCache().getHomesCache();
                Map<String, HomeObject> playerHomes = cacheHomes.get(sender.getUniqueId());
                if (playerHomes.containsKey(home)) {
                    pl.getHomeCache().getHomesCache().get(senderUUID).get(home).setBlock(material);
                    pl.getHomeCache().getHomesCache().get(senderUUID).get(home).setCustomModelData(customModelData);
                    pl.getHomeQueries().editMaterial(sender.getUniqueId(), home, material, customModelData);
                    String out = pl.getConfigurationManager().getLang().getHomeEditMaterial();
                    out = out.replace("{home}", home);
                    b = new TextComponent(out);

                } else {
                    String out = pl.getConfigurationManager().getLang().getHomeNoHomeFound();
                    out = out.replace("{home}", home);
                    b = new TextComponent(out);

                }
                sender.sendMessage(b);
            }
            if (subChannel.equalsIgnoreCase("buyHomes")){
                UUID playerUUID = UUID.fromString(in.readUTF());
                int num = in.readInt();

                pl.getBuyHomesManager().buyHomes(playerUUID, num);
            }
            if (subChannel.equalsIgnoreCase("needInfos")) {
                UUID uuid = UUID.fromString(in.readUTF());
                ProxiedPlayer player = pl.getProxy().getPlayer(uuid);

                String track = pl.getConfigurationManager().getConfig().getlPVIP();
                int wait = pl.getConfigurationManager().getConfig().getWaitTp();

                ServerInfo server = player.getServer().getInfo();
                try {
                    ByteArrayDataOutput out = ByteStreams.newDataOutput();

                    out.writeUTF("getInfos");
                    out.writeUTF(track);
                    out.writeInt(wait);

                    server.sendData("hypercraft:homes", out.toByteArray());
                } catch (Exception e) {
                    pl.getLogger().info(ChatColor.RED + "error envoi sur : " + ChatColor.GOLD + server.getName());
                }
            }
            if (subChannel.equalsIgnoreCase("homelistcache")){
                UUID uuid = UUID.fromString(in.readUTF());
                ProxiedPlayer player = pl.getProxy().getPlayer(uuid);

                List<String> homelist = new ArrayList<>(pl.getHomeCache().getHomesCache().get(uuid).keySet());

                ServerInfo server = player.getServer().getInfo();
                try {
                    ByteArrayDataOutput out = ByteStreams.newDataOutput();

                    out.writeUTF("homelistcache");
                    out.writeUTF(String.valueOf(player.getUniqueId()));
                    out.writeUTF(homelist.toString());

                    server.sendData("hypercraft:homes", out.toByteArray());
                } catch (Exception e) {
                    pl.getLogger().info(ChatColor.RED + "error envoi sur : " + ChatColor.GOLD + server.getName());
                }

            }
            if (subChannel.equalsIgnoreCase("setwarp")){
                UUID senderUUID = UUID.fromString(in.readUTF());
                String name = in.readUTF();
                String server = in.readUTF();
                String world = in.readUTF();
                double x = in.readDouble();
                double y = in.readDouble();
                double z = in.readDouble();
                float pitch = in.readFloat();
                float yaw = in.readFloat();
                String block = in.readUTF();
                int customModelData = in.readInt();
                boolean needPerm = in.readBoolean();

                WarpObject warp = new WarpObject(name,server,world,x,y,z,pitch,yaw,block,customModelData,needPerm);
                pl.getCreateWarp().createWarp(senderUUID, warp);
            }
            if (subChannel.equalsIgnoreCase("warpMaterial")){

                UUID senderUUID = UUID.fromString(in.readUTF());
                String warpName = in.readUTF();
                String material = in.readUTF();
                int customModelData = in.readInt();

                ProxiedPlayer sender = pl.getProxy().getPlayer(senderUUID);

                BaseComponent b;
                Map<String, WarpObject> warpCache = pl.getWarpCache().getWarpCache();
                WarpObject warp = warpCache.get(warpName);
                if (warpCache.containsKey(warp)) {
                    warp.setBlock(material);
                    warp.setCustomModelData(customModelData);
                    pl.getWarpQueries().editMaterial(warpName, material, customModelData);
                    String out = pl.getConfigurationManager().getLang().getWarpEditMaterial();
                    out = out.replace("{warp}", warpName);
                    b = new TextComponent(out);
                } else {
                    String out = pl.getConfigurationManager().getLang().getWarpNoWarpFound();
                    out = out.replace("{warp}", warpName);
                    b = new TextComponent(out);

                }
                sender.sendMessage(b);
            }
            if (subChannel.equalsIgnoreCase("LogoutLocation")){
                UUID player = UUID.fromString(in.readUTF());
                String location = in.readUTF();
                pl.getLocationQueries().logoutLocation(player, location);
            }
            if (subChannel.equalsIgnoreCase("deathLocation")){
                UUID player = UUID.fromString(in.readUTF());
                String location = in.readUTF();
                pl.getLocationQueries().deathLocation(player, location);
            }

            if (subChannel.equalsIgnoreCase("setspawn")){
                UUID senderUUID = UUID.fromString(in.readUTF());
                String server = in.readUTF();
                String world = in.readUTF();
                double x = in.readDouble();
                double y = in.readDouble();
                double z = in.readDouble();
                float pitch = in.readFloat();
                float yaw = in.readFloat();
                LocationObject spawn = new LocationObject(server,world,x,y,z,pitch,yaw);
                pl.getSpawnManager().setSpawn(senderUUID, spawn);

            }

        }

    }
}
