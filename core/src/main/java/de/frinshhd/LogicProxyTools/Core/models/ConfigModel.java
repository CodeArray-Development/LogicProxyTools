package de.frinshhd.LogicProxyTools.Core.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConfigModel {

    @JsonProperty
    private String maintenanceMessage = "§cThe network is currently in maintenance mode!";

    @JsonProperty
    private MotdModel motd = new MotdModel();

    @JsonProperty
    private MotdModel maintenanceMotd = new MotdModel();

    @JsonProperty
    private DatabaseModel database = new DatabaseModel();

    public String getMaintenanceMessage() {
        return maintenanceMessage;
    }

    public MotdModel getMotd() {
        return motd;
    }

    public MotdModel getMaintenanceMotd() {
        return maintenanceMotd;
    }

    public DatabaseModel getDatabase() {
        return database;
    }

}
