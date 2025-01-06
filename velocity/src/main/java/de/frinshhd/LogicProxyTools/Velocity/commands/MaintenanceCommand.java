package de.frinshhd.LogicProxyTools.Velocity.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import de.frinshhd.LogicProxyTools.Velocity.VelocityMain;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.List;

public class MaintenanceCommand implements SimpleCommand {

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        if (!source.hasPermission("logicvelocitytools.maintenance")) {
            source.sendMessage(Component.text("§cYou do not have permission to use this command!"));
            return;
        }

        if (args.length == 0) {
            source.sendMessage(Component.text("§7Maintenance mode is currently " + (VelocityMain.getManager().isMaintenance() ? "§aenabled" : "§cdisabled")));
            return;
        }

        switch (args[0]) {
            case "add" -> {
                if (!source.hasPermission("logicvelocitytools.maintenance.modify")) {
                    source.sendMessage(Component.text("§cYou do not have permission to use this command!"));
                    return;
                }

                if (args.length < 2) {
                    source.sendMessage(Component.text("§cPlease provide a player!"));
                    return;
                }

                VelocityMain.getManager().addWhitelist(args[1]);
                source.sendMessage(Component.text("§aAdded " + args[1] + " to the maintenance whitelist!"));
            }
            case "remove" -> {
                if (!source.hasPermission("logicvelocitytools.maintenance.modify")) {
                    source.sendMessage(Component.text("§cYou do not have permission to use this command!"));
                    return;
                }

                if (args.length < 2) {
                    source.sendMessage(Component.text("§cPlease provide a player!"));
                    return;
                }

                VelocityMain.getManager().removeWhitelist(args[1]);
                source.sendMessage(Component.text("§cRemoved " + args[1] + " from the maintenance whitelist!"));
            }
            case "list" -> {
                if (!source.hasPermission("logicvelocitytools.maintenance.list")) {
                    source.sendMessage(Component.text("§cYou do not have permission to use this command!"));
                    return;
                }

                source.sendMessage(Component.text("§7Maintenance whitelist:"));
                for (String name : VelocityMain.getManager().getWhitelist()) {
                    source.sendMessage(Component.text("§7- " + name));
                }
            }
            case "on" -> {
                if (!source.hasPermission("logicvelocitytools.maintenance.toggle")) {
                    source.sendMessage(Component.text("§cYou do not have permission to use this command!"));
                    return;
                }

                VelocityMain.getManager().setMaintenance(true);
                source.sendMessage(Component.text("§aMaintenance mode is now enabled!"));
            }
            case "off" -> {
                if (!source.hasPermission("logicvelocitytools.maintenance.toggle")) {
                    source.sendMessage(Component.text("§cYou do not have permission to use this command!"));
                    return;
                }

                VelocityMain.getManager().setMaintenance(false);
                source.sendMessage(Component.text("§cMaintenance mode is now disabled!"));
            }
            case "reload" -> {
                if (!source.hasPermission("logicvelocitytools.maintenance.reload")) {
                    source.sendMessage(Component.text("§cYou do not have permission to use this command!"));
                    return;
                }

                VelocityMain.getManager().loadConfig();
                source.sendMessage(Component.text("§aConfig reloaded!"));
            }
            default -> {}
        }
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        List<String> suggestions = new ArrayList<>();

        if (args.length == 1) {
            List<String> possibleCompletions = new ArrayList<>();

            if (source.hasPermission("logicvelocitytools.maintenance.modify")) {
                possibleCompletions.add("add");
                possibleCompletions.add("remove");
            }

            if (source.hasPermission("logicvelocitytools.maintenance.toggle")) {
                possibleCompletions.add("on");
                possibleCompletions.add("off");
            }

            if (source.hasPermission("logicvelocitytools.maintenance.reload")) {
                possibleCompletions.add("reload");
            }

            if (source.hasPermission("logicvelocitytools.maintenance.list")) {
                possibleCompletions.add("list");
            }

            for (String completion : possibleCompletions) {
                if (completion.startsWith(args[0].toLowerCase())) {
                    suggestions.add(completion);
                }
            }
        }

        return suggestions;
    }
}