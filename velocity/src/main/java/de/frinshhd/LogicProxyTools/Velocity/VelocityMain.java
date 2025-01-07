package de.frinshhd.LogicProxyTools.Velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.ServerPing;
import de.frinshhd.LogicProxyTools.Core.Manager;
import de.frinshhd.LogicProxyTools.Velocity.commands.MaintenanceCommand;
import net.kyori.adventure.text.Component;
import org.slf4j.Logger;

@Plugin(
        id = "logicproxytools",
        name = "LogicProxyTools",
        version = "0.0.1",
        authors = {"FrinshHD"}
)
public final class VelocityMain {

    private static VelocityMain instance;
    private static Manager manager;

    @Inject
    private ProxyServer server;

    @Inject
    private Logger logger;

    public static VelocityMain getInstance() {
        return instance;
    }

    public static Manager getManager() {
        return manager;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        instance = this;
        manager = new Manager();
        manager.init();

        // Register commands
        CommandMeta maintenanceCommandMeta = server.getCommandManager().metaBuilder("maintenance")
                .plugin(this)
                .build();
        server.getCommandManager().register(maintenanceCommandMeta, new MaintenanceCommand());
    }

    @Subscribe
    public void onPlayerLogin(PostLoginEvent event) {
        Player player = event.getPlayer();

        if (!manager.isMaintenance()) {
            return;
        }

        if (player.hasPermission("logicvelocitytools.maintenance.bypass")) {
            return;
        }

        if (!manager.getWhitelist().contains(player.getUsername())) {
            Component disconnectMessage = build(manager.getConfig().getMaintenanceMessage());
            player.disconnect(disconnectMessage);
        }
    }

    @Subscribe
    public void onProxyPing(ProxyPingEvent event) {
        ServerPing.Builder builder = event.getPing().asBuilder();

        if (manager.isMaintenance() && manager.getConfig().getMaintenanceMotd() != null) {
            builder.description(build(manager.getConfig().getMaintenanceMotd().getLine1() + "\n" + manager.getConfig().getMaintenanceMotd().getLine2()));

            if (manager.getConfig().getMaintenanceMotd().getProtocolText() != null) {
                builder.version(new ServerPing.Version(1, manager.getConfig().getMaintenanceMotd().getProtocolText()));
            }
        } else if (manager.getConfig().getMotd() != null) {
            builder.description(build(manager.getConfig().getMotd().getLine1() + "\n" + manager.getConfig().getMotd().getLine2()));

            if (manager.getConfig().getMotd().getProtocolText() != null) {
                builder.version(new ServerPing.Version(1, manager.getConfig().getMotd().getProtocolText()));
            }
        }

        event.setPing(builder.build());
    }

    /**
     * Utility method to build a Component from a string with color codes.
     *
     * @param string the input string
     * @return the formatted Component
     */
    public static Component build(String string) {
        return Component.text(hexToMinecraftColor(string).replace('&', '§'));
    }

    private static String hexToMinecraftColor(String hex) {
        // Remove the &# symbol if present
        if (hex.startsWith("&#")) {
            hex = hex.substring(2);
        }

        // Ensure the string is 6 characters long
        if (hex.length() == 6) {
            return "§x" + hex.chars()
                    .mapToObj(c -> "§" + Integer.toHexString(c).toLowerCase())
                    .reduce("", String::concat);
        }
        return hex; // Return the original if not valid
    }
}