package fr.felix911.hypercraftteleport.manager;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import fr.felix911.hypercraftteleport.HypercraftTeleport;
import fr.felix911.hypercraftteleport.IDatabaseTable;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;

public class DatabaseManager {
    private final HypercraftTeleport plugin;
    private final ArrayList<IDatabaseTable> repositories = new ArrayList();
    private final HikariDataSource hikariSource;

    public DatabaseManager(HypercraftTeleport hypercraftHomes) {
        this.plugin = hypercraftHomes;

        String dbName = plugin.getConfigurationManager().getConfig().getDatabaseName();
        String host = plugin.getConfigurationManager().getConfig().getDatabaseHost();
        String port = plugin.getConfigurationManager().getConfig().getDatabasePort();
        boolean useSSL = plugin.getConfigurationManager().getConfig().isUseSSL();
        String ssl = "?useSSL=true";
        if (!useSSL){ ssl = "?useSSL=false"; }
        String url = "jdbc:mysql://" + host + ":" + port + "/" + dbName + ssl;
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(plugin.getConfigurationManager().getConfig().getDatabaseUser());
        config.setPassword(plugin.getConfigurationManager().getConfig().getDatabasePassword());
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        this.hikariSource = new HikariDataSource(config);
    }
    public void addRepository(IDatabaseTable repository) {
        this.repositories.add(repository);
    }

    public Connection getConnection() throws SQLException {
        return this.hikariSource.getConnection();
    }
    public void initializeTables() {


        try {
            Connection connection = this.getConnection();
            Throwable var2 = null;

            try {
                Iterator var3 = this.repositories.iterator();
                while(var3.hasNext()) {
                    IDatabaseTable repository = (IDatabaseTable)var3.next();
                    String[] tableInformation = repository.getTable();
                    if (!this.tableExists(tableInformation[0])) {
                        try {
                            Statement statement = connection.createStatement();
                            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `" + tableInformation[0] + "` (" + tableInformation[1] + ") " + tableInformation[2] + ";");
                            this.plugin.getLogger().info("La table " + tableInformation[0] + " n'existait pas et à été crée !");
                        } catch (SQLException var16) {
                            this.plugin.getLogger().severe("Impossible de crée la table " + tableInformation[0] + " !");
                            var16.printStackTrace();
                        }
                    }
                }

                this.plugin.getLogger().info("Initialisation des tables de base de donnees");
            } catch (Throwable var17) {
                var2 = var17;
                throw var17;
            } finally {
                if (connection != null) {
                    if (var2 != null) {
                        try {
                            connection.close();
                        } catch (Throwable var15) {
                            var2.addSuppressed(var15);
                        }
                    } else {
                        connection.close();
                    }
                }

            }
        } catch (SQLException var19) {
            var19.printStackTrace();
        }

    }

    private boolean tableExists(String tableName) throws SQLException {
        Connection connection = this.getConnection();
        DatabaseMetaData dbm = connection.getMetaData();
        ResultSet tables = dbm.getTables(null, null, tableName, null);
        return tables.next();
    }

}
