package fr.felix911.hypercraftteleport.manager;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.felix911.apiproxy.ApiProxy;
import fr.felix911.hypercraftteleport.HypercraftTeleport;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

public class BuyHomesManager {
    private final HypercraftTeleport pl;

    public BuyHomesManager(HypercraftTeleport hypercraftHomes) {
        pl = hypercraftHomes;
    }

    public void buyHomes(UUID playerUUID, int num) {

        BaseComponent b;

        ProxiedPlayer player = pl.getProxy().getPlayer(playerUUID);

        String rankS;
        rankS = ApiProxy.getSpecifiqueRank(playerUUID, pl.getConfigurationManager().getConfig().getlPVIP());
        if (rankS == null){
            rankS = ApiProxy.getSpecifiqueRank(playerUUID, pl.getConfigurationManager().getConfig().getlPplayers());
        }


        if (!(rankS.equalsIgnoreCase(pl.getConfigurationManager().getConfig().getRequiredRank()))){
            b = new TextComponent(pl.getConfigurationManager().getLang().getHomeBuyFailRequired());
            player.sendMessage(b);
            return;
        }

        int rank = pl.getConfigurationManager().getConfig().getMapLimites().get(rankS);
        int bonus = pl.getHomeCache().getHomesBonusCache().get(playerUUID);
        int max = pl.getConfigurationManager().getConfig().getMaxHomes();
        int price = pl.getConfigurationManager().getConfig().getPriceHomes();

        if (rank + bonus == max || max - pl.getConfigurationManager().getConfig().getMapLimites().get("mecene") == bonus){
            b = new TextComponent(pl.getConfigurationManager().getLang().getHomeBuyFail());
            player.sendMessage(b);
            return;
        }
        if (rank + bonus + num > max || bonus + num > max - pl.getConfigurationManager().getConfig().getMapLimites().get("mecene")){

            String s = pl.getConfigurationManager().getLang().getHomeBuyTooMuch();

            s = s.replace("{num}", String.valueOf(num));
            int left;
            if (rank + bonus + num > max){
                left = max - rank - bonus;
            } else {
                left = max - pl.getConfigurationManager().getConfig().getMapLimites().get("mecene") - bonus;
            }

            s = s.replace("{left}", String.valueOf(left));
            b = new TextComponent(s);
            player.sendMessage(b);
            return;
        }

        pl.getHomeCache().increaseBonus(player.getUniqueId(),num);

        String s = pl.getConfigurationManager().getLang().getHomeBuySuccess();
        s = s.replace("{num}", String.valueOf(num));
        int cost = price * num;
        s = s.replace("{price}", String.valueOf(cost));
        b = new TextComponent(s);
        player.sendMessage(b);

        ServerInfo server = player.getServer().getInfo();
        try {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();

            out.writeUTF("BuyHomes");
            out.writeUTF(String.valueOf(player.getUniqueId()));
            out.writeInt(cost);

            server.sendData("hypercraft:homes", out.toByteArray());
        } catch (Exception e) {
            pl.getLogger().info(ChatColor.RED + "error envoi sur : " + ChatColor.GOLD + server.getName());
        }
    }
}
