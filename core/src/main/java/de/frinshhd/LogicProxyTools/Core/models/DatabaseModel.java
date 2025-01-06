package de.frinshhd.LogicProxyTools.Core.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.frinshhd.LogicProxyTools.Core.utils.DatabaseType;

public class DatabaseModel {

    @JsonProperty
    public String username = null;
    @JsonProperty
    public String password = null;
    @JsonProperty
    public String database = "LogicProxyTools";
    @JsonProperty
    public String host = "127.0.0.1";
    @JsonProperty
    public int port = 3306;
    @JsonProperty
    private String type = "sqlite";

    @JsonIgnore
    public DatabaseType getType() {
        return DatabaseType.valueOf(type.toUpperCase());
    }

}
