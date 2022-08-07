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
import java.util.concurrent.CompletableFuture;

public class LocationQueries {
    private final HypercraftTeleport plugin;
    private final DatabaseManager databaseManager;
    private static final String LOGOUT_LOCATION = "UPDATE Hc_Players SET logoutLocation=? where playerUUID=?";
    private static final String DEATH_LOCATION = "UPDATE Hc_Players SET deathLocation=? where playerUUID=?";
    private static final String NEW_LOCATION = "UPDATE Hc_Players SET newLocation=? where playerUUID=?";
    private static final String GET_LOGOUT_LOCATION = "SELECT logoutLocation FROM Hc_Players where playerUUID=?";


    public LocationQueries(HypercraftTeleport hypercraftHomes) {
        plugin = hypercraftHomes;
        this.databaseManager = plugin.getDatabaseManager();
    }

    public void logoutLocation(UUID playerUUID, String location) {

            PreparedStatement statement = null;
            try (Connection connection = databaseManager.getConnection()){

                statement = connection.prepareStatement(LOGOUT_LOCATION);
                statement.setString(1, location);
                statement.setString(2, String.valueOf(playerUUID));
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

    public void deathLocation(UUID playerUUID, String location) {

            PreparedStatement statement = null;
            try (Connection connection = databaseManager.getConnection()){

                statement = connection.prepareStatement(DEATH_LOCATION);
                statement.setString(1, location);
                statement.setString(2, String.valueOf(playerUUID));
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

    public void newLocation(UUID playerUUID, boolean b) {

        PreparedStatement statement = null;
        try (Connection connection = databaseManager.getConnection()){

            statement = connection.prepareStatement(NEW_LOCATION);
            statement.setBoolean(1, b);
            statement.setString(2, String.valueOf(playerUUID));
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

    public String getLogoutLocation(UUID playerUUID) {

        String location = null;
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        ResultSet result = null;
        PreparedStatement statement = null;
        try (Connection connection = databaseManager.getConnection()){

            statement = connection.prepareStatement(GET_LOGOUT_LOCATION);
            statement.setString(1, String.valueOf(playerUUID));
            statement.executeQuery();

            result = statement.executeQuery();
            if (result.next()){
                location = result.getString(1);
            }

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
        future.complete(null);
     return location;
    }

}
