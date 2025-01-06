package de.frinshhd.LogicProxyTools.Core.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MotdModel {

    @JsonProperty
    private String line1 = "§6§lLogic§e§lBungee§6§lTools";

    @JsonProperty
    private String line2 = "§7Your §6Network §7Management";

    public String getLine1() {
        return line1;
    }

    public String getLine2() {
        return line2;
    }
}
