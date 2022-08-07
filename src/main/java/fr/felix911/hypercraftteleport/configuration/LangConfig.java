package fr.felix911.hypercraftteleport.configuration;

import net.md_5.bungee.config.Configuration;

public class LangConfig {

    //error

    private String noConsole;
    private String noPerm;
    private String PlayerNotFound;
    private String reload;
    private String coordInvalid;
    private String playerOffline;
    private String failTp;
    private String failRank;

    //spawn

    private String notDefine;
    private String teleport;

    //homes

    private String homeTeleport;
    private String homeDelete;
    private String homeDeleteOther;
    private String homeNoHome;
    private String homeNoHomes;
    private String homeSenderNoHomes;
    private String homeNoHomeFound;
    private String homeCreate;
    private String homeUpdate;
    private String homeLimits;
    private String homeBuySuccess;
    private String homeBuyFail;
    private String homeBuyTooMuch;
    private String homeBuyFailRequired;
    private String homeEditMaterial;

    //warp

    private String warpDelete;
    private String warpNoWarpFound;
    private String warpNoWarpsFound;
    private String warpCreate;
    private String warpUpdate;
    private String warpEditMaterial;
    private String warpNoPermWarp;
    private String warpPermWarp;

    //tp

    private String toPlayer;
    private String toPlayerHere;
    private String toCoords;
    private String toCoordsOther;
    private String tpaDemand;
    private String warningPvp;

    public void load(Configuration language) {

        //error

        noConsole = language.getString("Error.NoConsole").replace("&", "§");
        noPerm = language.getString("Error.NoPerm").replace("&", "§");
        PlayerNotFound = language.getString("Error.PlayerNotFound").replace("&", "§");
        reload = language.getString("Error.WhatReload").replace("&", "§");
        coordInvalid = language.getString("Error.CoordsInvalid").replace("&", "§");
        playerOffline = language.getString("Error.PlayerOffline").replace("&", "§");
        failTp = language.getString("Error.FailTp").replace("&", "§");
        failRank = language.getString("Error.FailRank").replace("&", "§");

        //Spawn

        notDefine = language.getString("Spawn.NotDefien").replace("&", "§");
        teleport = language.getString("Spawn.Teleport").replace("&", "§");

        //homes

        homeTeleport = language.getString("Homes.Teleport").replace("&", "§");
        homeDelete = language.getString("Homes.Delete").replace("&", "§");
        homeDeleteOther = language.getString("Homes.DeleteOther").replace("&", "§");
        homeNoHome = language.getString("Homes.NoHome").replace("&", "§");
        homeNoHomes = language.getString("Homes.NoHomes").replace("&", "§");
        homeSenderNoHomes = language.getString("Homes.SenderNoHomes").replace("&", "§");
        homeNoHomeFound = language.getString("Homes.NoHomeFound").replace("&", "§");
        homeCreate = language.getString("Homes.Create").replace("&", "§");
        homeUpdate = language.getString("Homes.Update").replace("&", "§");
        homeLimits = language.getString("Homes.Limits").replace("&", "§");
        homeBuySuccess = language.getString("Homes.BuySuccess").replace("&", "§");
        homeBuyFail = language.getString("Homes.BuyFail").replace("&", "§");
        homeBuyFailRequired = language.getString("Homes.BuyFailRequired").replace("&", "§");
        homeBuyTooMuch = language.getString("Homes.BuyTooMuch").replace("&", "§");
        homeEditMaterial = language.getString("Homes.EditMaterial").replace("&", "§");

        //warp

        warpDelete = language.getString("Homes.Delete").replace("&", "§");
        warpNoWarpFound = language.getString("Homes.NoWarpFound").replace("&", "§");
        warpNoWarpsFound = language.getString("Homes.NoWarpsFound").replace("&", "§");
        warpCreate = language.getString("Homes.Create").replace("&", "§");
        warpUpdate = language.getString("Homes.Update").replace("&", "§");
        warpEditMaterial = language.getString("Homes.EditMaterial").replace("&", "§");
        warpNoPermWarp = language.getString("Homes.NoPermWarp").replace("&", "§");
        warpPermWarp = language.getString("Homes.PermWarp").replace("&", "§");

        //tp

        toPlayer = language.getString("Tp.ToPlayer").replace("&", "§");
        toPlayerHere = language.getString("Tp.ToPlayerHere").replace("&", "§");
        toCoords = language.getString("Tp.ToCoords").replace("&", "§");
        toCoordsOther = language.getString("Tp.ToCoordsOther").replace("&", "§");
        tpaDemand = language.getString("Tp.TpaDemand").replace("&", "§");
        warningPvp = language.getString("Tp.WarningTpPVP").replace("&", "§");

    }

    //error

    public String getNoConsole() {
        return noConsole;
    }

    public String getNoPerm() {
        return noPerm;
    }

    public String getPlayerNotFound() {
        return PlayerNotFound;
    }

    public String getReload() {
        return reload;
    }

    public String getCoordInvalid() {
        return coordInvalid;
    }

    public String getPlayerOffline() {
        return playerOffline;
    }

    public String getFailTp() {
        return failTp;
    }

    public String getFailRank() {
        return failRank;
    }

    //Spawn

    public String getNotDefine() {
        return notDefine;
    }

    public String getTeleport() {
        return teleport;
    }


    //homes

    public String getHomeTeleport() {
        return homeTeleport;
    }

    public String getHomeDelete() {
        return homeDelete;
    }

    public String getHomeDeleteOther() {
        return homeDeleteOther;
    }

    public String getHomeNoHome() {
        return homeNoHome;
    }

    public String getHomeNoHomes() {
        return homeNoHomes;
    }

    public String getHomeSenderNoHomes() {
        return homeSenderNoHomes;
    }

    public String getHomeNoHomeFound() {
        return homeNoHomeFound;
    }

    public String getHomeCreate() {
        return homeCreate;
    }

    public String getHomeUpdate() {
        return homeUpdate;
    }

    public String getHomeLimits() {
        return homeLimits;
    }

    public String getHomeBuySuccess() {
        return homeBuySuccess;
    }

    public String getHomeBuyFail() {
        return homeBuyFail;
    }

    public String getHomeBuyTooMuch() {
        return homeBuyTooMuch;
    }

    public String getHomeBuyFailRequired() {
        return homeBuyFailRequired;
    }

    public String getHomeEditMaterial() {
        return homeEditMaterial;
    }

    //warp

    public String getWarpDelete() {
        return warpDelete;
    }

    public String getWarpNoWarpFound() {
        return warpNoWarpFound;
    }

    public String getWarpNoWarpsFound() {
        return warpNoWarpsFound;
    }

    public String getWarpCreate() {
        return warpCreate;
    }

    public String getWarpUpdate() {
        return warpUpdate;
    }

    public String getWarpEditMaterial() {
        return warpEditMaterial;
    }

    public String getWarpNoPermWarp() {
        return warpNoPermWarp;
    }

    public String getWarpPermWarp() {
        return warpPermWarp;
    }

    //tp

    public String getToPlayer() {
        return toPlayer;
    }

    public String getToPlayerHere() {
        return toPlayerHere;
    }

    public String getToCoords() {
        return toCoords;
    }

    public String getToCoordsOther() {
        return toCoordsOther;
    }

    public String getTpaDemand() {
        return tpaDemand;
    }

    public String getWarningPvp() {
        return warningPvp;
    }
}
