package fr.felix911.hypercraftteleport.objects;

import fr.felix911.hypercraftteleport.HypercraftTeleport;

public class SpawnObject {
    private HypercraftTeleport pl;
    private String server;
    private String world;
    private double x;
    private double y;
    private double z;
    private float pitch;
    private float yaw;
    public SpawnObject(HypercraftTeleport hypercraftTeleport) {this.pl = hypercraftTeleport;
    }

    public SpawnObject(String server, String world, double x, double y, double z, float pitch, float yaw){

        this.server = server;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z =z;
        this.pitch = pitch;
        this.yaw = yaw;
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
}
