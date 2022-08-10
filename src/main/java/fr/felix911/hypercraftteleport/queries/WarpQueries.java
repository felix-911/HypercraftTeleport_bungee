package fr.felix911.hypercraftteleport.queries;

import fr.felix911.hypercraftteleport.HypercraftTeleport;
import fr.felix911.hypercraftteleport.IDatabaseTable;
import fr.felix911.hypercraftteleport.manager.DatabaseManager;
import fr.felix911.hypercraftteleport.objects.WarpObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class WarpQueries implements IDatabaseTable {
    private final HypercraftTeleport pl;
    private final DatabaseManager databaseManager;
    private static final String LOAD_WARP = "SELECT * FROM Hc_Warps;";
    private static final String SAVE_WARP = "INSERT INTO Hc_Warps(name, server, world, x, y, z, pitch, yaw, block, custommodeldata, needperm) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
    private static final String UPDATE_WARP = "UPDATE Hc_Warps SET server=?, world=?, x=?, y=?, z=?, pitch=?, yaw=? WHERE name=?";
    private static final String DELETE_WARP = "DELETE FROM Hc_Warps WHERE name=?";
    private static final String UPDATE_MATERIAL = "UPDATE Hc_Warps SET block=?, custommodeldata=? WHERE name=?";
    private static final String UPDATE_PERM = "UPDATE Hc_Warps SET needperm=? WHERE name=?";

    public WarpQueries(HypercraftTeleport hypercraftTeleport) {
        this.pl = hypercraftTeleport;
        this.databaseManager = pl.getDatabaseManager();
    }

    public void loadWarp() {
        ResultSet result;
        PreparedStatement statement = null;
        Map<String, WarpObject> warpCache = pl.getWarpCache().getWarpCache();
        try (Connection connection = databaseManager.getConnection()){

            statement = connection.prepareStatement(LOAD_WARP);
            result = statement.executeQuery();

            while (result.next()) {

                String name = result.getString(2);
                String server = result.getString(3);
                String world = result.getString(4);
                double x = result.getDouble(5);
                double y = result.getDouble(6);
                double z = result.getDouble(7);
                float pitch = result.getFloat(8);
                float yaw = result.getFloat(9);
                String block = result.getString(10);
                int cmd = result.getInt(11);
                boolean b = result.getBoolean(12);


                WarpObject warpObject = new WarpObject(name,server,world,x,y,z,pitch,yaw,block,cmd,b);
                warpCache.put(name,warpObject);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public void saveWarp(WarpObject warp) {

        PreparedStatement statement = null;
        try (Connection connection = databaseManager.getConnection()){

            statement = connection.prepareStatement(SAVE_WARP);
            statement.setString(1, warp.getName());
            statement.setString(2, warp.getServer());
            statement.setString(3, warp.getWorld());
            statement.setDouble(4, warp.getX());
            statement.setDouble(5, warp.getY());
            statement.setDouble(6, warp.getZ());
            statement.setFloat(7, warp.getPitch());
            statement.setFloat(8, warp.getYaw());
            statement.setString(9,warp.getBlock());
            statement.setInt(10,warp.getCustomModelData());
            statement.setBoolean(11,warp.isNeedPerm());
            statement.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateWarp(WarpObject warp) {

        PreparedStatement statement = null;
        try (Connection connection = databaseManager.getConnection()){

            statement = connection.prepareStatement(UPDATE_WARP);
            statement.setString(1, warp.getServer());
            statement.setString(2, warp.getWorld());
            statement.setDouble(3, warp.getX());
            statement.setDouble(4, warp.getY());
            statement.setDouble(5, warp.getZ());
            statement.setFloat(6, warp.getPitch());
            statement.setFloat(7, warp.getYaw());

            statement.setString(9, warp.getName());
            statement.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteWarp(String warp) {

            PreparedStatement statement = null;

            try (Connection connection = databaseManager.getConnection()){

                statement = connection.prepareStatement(DELETE_WARP);
                statement.setString(1, warp);
                statement.executeUpdate();

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    if (statement != null) {
                        statement.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    public void editMaterial(String warp, String material, int customModelData) {
        PreparedStatement statement = null;
        try (Connection connection = databaseManager.getConnection()){

            statement = connection.prepareStatement(UPDATE_MATERIAL);
            statement.setString(1, material);
            statement.setInt(2, customModelData);
            statement.setString(3, warp);
            statement.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void editconfig(String warp, boolean b) {
        PreparedStatement statement = null;
        try (Connection connection = databaseManager.getConnection()){

            statement = connection.prepareStatement(UPDATE_PERM);
            statement.setBoolean(1, b);

            statement.setString(2, warp);
            statement.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public String[] getTable() {
        return new String[]{"Hc_Warps",
                "`id` INT NOT NULL AUTO_INCREMENT," +
                        "name VARCHAR(32) NOT NULL," +
                        "server VARCHAR(20) NOT NULL," +
                        "world VARCHAR(20) NOT NULL," +
                        "x DOUBLE NOT NULL," +
                        "y DOUBLE NOT NULL," +
                        "z DOUBLE NOT NULL," +
                        "pitch FLOAT NOT NULL," +
                        "yaw FLOAT NOT NULL," +
                        "block VARCHAR(40) NOT NULL,"  +
                        "custommodeldata INT DEFAULT -1," +
                        "needperm BOOLEAN DEFAULT 0," +
                        "PRIMARY KEY (`id`)"
                , "DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci"
        };
    }

}
