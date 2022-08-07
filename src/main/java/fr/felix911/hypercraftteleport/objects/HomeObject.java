package fr.felix911.hypercraftteleport.objects;

import fr.felix911.hypercraftteleport.HypercraftTeleport;

public class HomeObject {
    private HypercraftTeleport pl;
    private String name;
    private String server;
    private String world;
    private double x;
    private double y;
    private double z;
    private float pitch;
    private float yaw;
    private String block;
    private int customModelData;

    public HomeObject(HypercraftTeleport hypercraftHomes) {
        this.pl = hypercraftHomes;
    }

    public HomeObject(String name, String server, String world, double x, double y, double z, float pitch, float yaw, String block, int customModelData){

        this.name = name;
        this.server = server;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z =z;
        this.pitch = pitch;
        this.yaw = yaw;
        this.block = block;
        this.customModelData = customModelData;
    }

    public String getName() {
        return name;
    }

    public String getServer() {
        return server;
    }

    public String getWorld() {
        return world;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public int getCustomModelData() {
        return customModelData;
    }

    public void setCustomModelData(int customModelData) {
        this.customModelData = customModelData;
    }
}
