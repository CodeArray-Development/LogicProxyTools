package de.frinshhd.LogicProxyTools.Bungee;

import de.frinshhd.LogicProxyTools.Bungee.commands.MaintenanceCommand;
import de.frinshhd.LogicProxyTools.Core.Manager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.awt.*;

public final class BungeeMain extends Plugin implements Listener {

    private static Plugin instance;
    private static Manager manager;

    public static Plugin getInstance() {
        return instance;
    }

    public static Manager getManager() {
        return manager;
    }

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        manager = new Manager();
        getManager().init();

        getInstance().getProxy().getPluginManager().registerListener(getInstance(), this);

        getInstance().getProxy().getPluginManager().registerCommand(getInstance(), new MaintenanceCommand());
    }

    public static String build(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLogin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();

        if (!getManager().isMaintenance()) {
            return;
        }

        if (player.hasPermission("logicbungeetools.maintenance.bypass")) {
            return;
        }

        if (!getManager().getWhitelist().contains(player.getName())) {
            player.disconnect(new TextComponent(build(getManager().getConfig().getMaintenanceMessage())));
            return;
        }
    }

    @EventHandler
    public void onProxyPing(ProxyPingEvent event) {
        if (getManager().isMaintenance() && getManager().getConfig().getMaintenanceMotd() != null) {
            event.getResponse().setDescriptionComponent(new TextComponent(build(getManager().getConfig().getMaintenanceMotd().getLine1() + "\n" + getManager().getConfig().getMaintenanceMotd().getLine2())));
            return;
        }

        if (getManager().getConfig().getMotd() != null) {
            event.getResponse().setDescriptionComponent(new TextComponent(build(getManager().getConfig().getMotd().getLine1() + "\n" + getManager().getConfig().getMotd().getLine2())));
            return;
        }
    }
}
