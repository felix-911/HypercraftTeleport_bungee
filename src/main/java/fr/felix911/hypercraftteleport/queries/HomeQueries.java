package fr.felix911.hypercraftteleport.queries;

import fr.felix911.hypercraftteleport.HypercraftTeleport;
import fr.felix911.hypercraftteleport.IDatabaseTable;
import fr.felix911.hypercraftteleport.manager.DatabaseManager;
import fr.felix911.hypercraftteleport.objects.HomeObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HomeQueries implements IDatabaseTable {
    private final HypercraftTeleport plugin;
    private final DatabaseManager databaseManager;
    private static final String SAVE_HOME = "INSERT INTO Hc_Homes(ownerUUID, name, server, world, x, y, z, pitch, yaw, block, custommodeldata) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
    private static final String UPDATE_HOME = "UPDATE Hc_Homes SET server=?, world=?, x=?, y=?, z=?, pitch=?, yaw=? WHERE ownerUUID=? and name=?";
    private static final String UPDATE_MATERIAL = "UPDATE Hc_Homes SET block=?, custommodeldata=? WHERE ownerUUID=? and name=?";
    private static final String DELETE_HOME = "DELETE FROM Hc_Homes WHERE ownerUUID=? and name=?";
    private static final String GET_PLAYER_HOMES = "SELECT * FROM Hc_Homes WHERE ownerUUID=?";
    private static final String GET_PLAYER_HOMES_BONUS = "SELECT homesBonus FROM Hc_Players WHERE playerUUID=?";
    private static final String UPDATE_HOME_BONUS = "UPDATE Hc_Players SET homesBonus=? WHERE playerUUID=?";

    public HomeQueries(HypercraftTeleport hypercraftHomes) {
        plugin = hypercraftHomes;
        this.databaseManager = plugin.getDatabaseManager();
    }

    public void saveHome(UUID playerUUID, HomeObject home) {

            PreparedStatement statement = null;
            try (Connection connection = databaseManager.getConnection()){

                statement = connection.prepareStatement(SAVE_HOME);
                statement.setString(1, String.valueOf(playerUUID));
                statement.setString(2, home.getName());
                statement.setString(3, home.getServer());
                statement.setString(4, home.getWorld());
                statement.setDouble(5, home.getX());
                statement.setDouble(6, home.getY());
                statement.setDouble(7, home.getZ());
                statement.setFloat(8, home.getPitch());
                statement.setFloat(9, home.getYaw());
                statement.setString(10,home.getBlock());
                statement.setInt(11,home.getCustomModelData());
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

    public void updateHome(UUID playerUUID, HomeObject home) {

            PreparedStatement statement = null;
            try (Connection connection = databaseManager.getConnection()){

                statement = connection.prepareStatement(UPDATE_HOME);
                statement.setString(1, home.getServer());
                statement.setString(2, home.getWorld());
                statement.setDouble(3, home.getX());
                statement.setDouble(4, home.getY());
                statement.setDouble(5, home.getZ());
                statement.setFloat(6, home.getPitch());
                statement.setFloat(7, home.getYaw());
                statement.setString(8, String.valueOf(playerUUID));
                statement.setString(9, home.getName());
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

    public void deleteHome(UUID playerUUID, String name) {

            PreparedStatement statement = null;

            try (Connection connection = databaseManager.getConnection()){

                statement = connection.prepareStatement(DELETE_HOME);
                statement.setString(1, String.valueOf(playerUUID));
                statement.setString(2, name);
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

    public void getPlayerHomes(UUID uuid){

            ResultSet result = null;
            PreparedStatement statement = null;
            Map<String,HomeObject> playerHomes = new HashMap<>();

        try (Connection connection = databaseManager.getConnection()){

            statement = connection.prepareStatement(GET_PLAYER_HOMES);
            statement.setString(1,uuid.toString());

            result = statement.executeQuery();
            while (result.next()){

                String name = result.getString(3);
                String server = result.getString(4);
                String world = result.getString(5);
                double x = result.getDouble(6);
                double y = result.getDouble(7);
                double z = result.getDouble(8);
                float pitch = result.getFloat(9);
                float yaw = result.getFloat(10);
                String block = result.getString(11);
                int customModelData = result.getInt(12);


                HomeObject object = new HomeObject(
                        name,
                        server,
                        world,
                        x,
                        y,
                        z,
                        pitch,
                        yaw,
                        block,
                        customModelData
                );

                playerHomes.put(name,object);
            }
            plugin.getHomeCache().getHomesCache().put(uuid,playerHomes);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (result != null) {
                    result.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void getPlayerHomesBonus(UUID uuid) {

        ResultSet result = null;
        PreparedStatement statement = null;
        int bonus = 0;

        try (Connection connection = databaseManager.getConnection()){

            statement = connection.prepareStatement(GET_PLAYER_HOMES_BONUS);
            statement.setString(1,uuid.toString());
            result = statement.executeQuery();

            if (result.next()){
                bonus = result.getInt(1);
            }
            plugin.getHomeCache().getHomesBonusCache().put(uuid,bonus);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (result != null) {
                    result.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void setHomesBonus(UUID uuid, int n) {


        PreparedStatement statement = null;
        try (Connection connection = databaseManager.getConnection()){

            statement = connection.prepareStatement(UPDATE_HOME_BONUS);
            statement.setInt(1, n);
            statement.setString(2, uuid.toString());
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

    public void editMaterial(UUID uuid, String home, String material, int customModelData) {
        PreparedStatement statement = null;
        try (Connection connection = databaseManager.getConnection()){

            statement = connection.prepareStatement(UPDATE_MATERIAL);
            statement.setString(1, material);
            statement.setInt(2, customModelData);

            statement.setString(3, String.valueOf(uuid));
            statement.setString(4, home);
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
        return new String[]{"Hc_Homes",
                                "`id` INT NOT NULL AUTO_INCREMENT," +
                                "ownerUUID VARCHAR(40) NOT NULL," +
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
                                "PRIMARY KEY (`id`)"
                            , "DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci"
        };
    }
}
