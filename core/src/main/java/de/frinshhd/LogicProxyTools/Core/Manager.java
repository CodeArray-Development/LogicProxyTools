package de.frinshhd.LogicProxyTools.Core;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.j256.ormlite.dao.Dao;
import de.frinshhd.LogicProxyTools.Core.models.ConfigModel;
import de.frinshhd.LogicProxyTools.Core.mysql.MysqlManager;
import de.frinshhd.LogicProxyTools.Core.mysql.entities.Maintenance;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Manager {

    public static String prefix = "§8[§6LogicProxyTools§8] §7";

    private ConfigModel config;
    private boolean maintenance;
    private List<String> whitelist = new ArrayList<>();

    public void init() {
        new File("plugins/LogicProxyTools").mkdir();
        List<String> files = new ArrayList<>(List.of("config.yml"));

        for (String fileRaw : files) {
            File file = new File("plugins/LogicProxyTools/" + fileRaw);
            if (file.exists()) {
                continue;
            }

            try (InputStream link = getClass().getClassLoader().getResourceAsStream(fileRaw)) {
                if (link == null) {
                    throw new RuntimeException(prefix + "Resource not found: " + fileRaw);
                }
                Files.copy(link, file.getAbsoluteFile().toPath());
            } catch (IOException e) {
                throw new RuntimeException(prefix + "Failed to copy resource: " + fileRaw, e);
            }
        }

        loadConfig();
        connectSQL();
        updateMaintenanceStatus();
    }

    public void loadConfig() {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            config = mapper.readValue(Files.newInputStream(Paths.get("plugins/LogicProxyTools/config.yml")), ConfigModel.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration", e);
        }
    }

    private void connectSQL() {
        String url;
        switch (getConfig().getDatabase().getType()) {
            case MYSQL:
                url = String.format("jdbc:mysql://%s:%d/%s", getConfig().getDatabase().host, getConfig().getDatabase().port, getConfig().getDatabase().database);
                MysqlManager.connect(url, getConfig().getDatabase().username, getConfig().getDatabase().password);
                break;
            case SQLITE:
                MysqlManager.connect("jdbc:sqlite:plugins/LogicProxyTools/sqlite.db");
                break;
            case MARIADB:
                url = String.format("jdbc:mariadb://%s:%d/%s", getConfig().getDatabase().host, getConfig().getDatabase().port, getConfig().getDatabase().database);
                MysqlManager.connect(url, getConfig().getDatabase().username, getConfig().getDatabase().password);
                break;
            default:
                throw new IllegalArgumentException("Unsupported database type: " + getConfig().getDatabase().getType());
        }

        initializeMaintenance();
    }

    private void initializeMaintenance() {
        if (MysqlManager.getMaintenance() == null) {
            try {
                Dao<Maintenance, Integer> maintenanceDao = MysqlManager.getMaintenanceDao();
                Maintenance maintenance = new Maintenance();
                maintenance.create();
                maintenanceDao.create(maintenance);
            } catch (SQLException e) {
                throw new RuntimeException("Failed to initialize maintenance", e);
            }
        }
    }

    private void updateMaintenanceStatus() {
        Maintenance maintenanceEntity = MysqlManager.getMaintenance();
        if (maintenanceEntity != null) {
            maintenance = maintenanceEntity.isActive();
            whitelist = maintenanceEntity.getNamesList();
        }
    }

    public ConfigModel getConfig() {
        return config;
    }

    public boolean isMaintenance() {
        return maintenance;
    }

    public List<String> getWhitelist() {
        return whitelist;
    }

    public void setMaintenance(boolean maintenance) {
        this.maintenance = maintenance;
        updateMaintenanceEntity();
    }

    public void addWhitelist(String name) {
        whitelist.add(name);
        updateMaintenanceEntity();
    }

    public void removeWhitelist(String name) {
        whitelist.remove(name);
        updateMaintenanceEntity();
    }

    private void updateMaintenanceEntity() {
        Maintenance maintenanceEntity = MysqlManager.getMaintenance();
        if (maintenanceEntity != null) {
            maintenanceEntity.setActive(maintenance);
            maintenanceEntity.setNamesList(whitelist);
            try {
                MysqlManager.getMaintenanceDao().update(maintenanceEntity);
            } catch (SQLException e) {
                throw new RuntimeException("Failed to update maintenance entity", e);
            }
        }
    }
}