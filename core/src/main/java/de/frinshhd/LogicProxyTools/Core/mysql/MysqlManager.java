package de.frinshhd.LogicProxyTools.Core.mysql;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;
import de.frinshhd.LogicProxyTools.Core.mysql.entities.Maintenance;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class MysqlManager {

    public static JdbcConnectionSource connectionSource;

    public static Dao<Maintenance, Integer> getMaintenanceDao() throws SQLException {
        return DaoManager.createDao(connectionSource, Maintenance.class);
    }

    public static void connect(String url) {
        connect(url, null, null);
    }

    public static void connect(String url, String userName, String password) {
        if (userName == null && password == null) {
            try {
                connectionSource = new JdbcConnectionSource(url);
            } catch (SQLException e) {
                createNewDatabase();
                connect(url, userName, password);
            }
        } else {
            try {
                connectionSource = new JdbcConnectionSource(url, userName, password);
            } catch (SQLException e) {
                //Todo: logging
            }
        }

        try {
            TableUtils.createTableIfNotExists(connectionSource, Maintenance.class);
        } catch (SQLException e) {
            //Todo logging
        }
    }

    public static Maintenance getMaintenance() {
    Dao<Maintenance, Integer> maintenanceDao;
    try {
        maintenanceDao = getMaintenanceDao();
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }

    List<Maintenance> maintenanceList;
    try {
        maintenanceList = maintenanceDao.queryForAll();
    } catch (SQLException e) {
        return null;
    }

    if (maintenanceList.isEmpty()) {
        return null;
    }

    return maintenanceList.get(0);
}

    public static void disconnect() throws Exception {
        connectionSource.close();
    }

    public static void createNewDatabase() {

        String url = "jdbc:sqlite:plugins/LogicProxyTools/sqlite.db";

        try {
            Connection conn = DriverManager.getConnection(url);
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
