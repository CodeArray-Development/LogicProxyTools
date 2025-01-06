package de.frinshhd.LogicProxyTools.Bungee.commands;

import de.frinshhd.LogicProxyTools.Bungee.BungeeMain;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;

public class MaintenanceCommand extends Command implements TabExecutor {
    public MaintenanceCommand() {
        super("maintenance");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("logicbungeetools.BungeeMaintenance")) {
            sender.sendMessage("§cYou do not have permission to use this command!");
            return;
        }

        if (args.length == 0) {
            sender.sendMessage(new TextComponent("§7BungeeMaintenance mode is currently " + (BungeeMain.getManager().isMaintenance() ? "§aenabled" : "§cdisabled")));
            return;
        }

        switch (args[0]) {
            case "add" -> {
                if (!sender.hasPermission("logicbungeetools.BungeeMaintenance.modify")) {
                    sender.sendMessage("§cYou do not have permission to use this command!");
                    return;
                }

                if (args.length < 2) {
                    sender.sendMessage(new TextComponent("§cPlease provide a player!"));
                    return;
                }

                BungeeMain.getManager().addWhitelist(args[1]);
                sender.sendMessage(new TextComponent("§aAdded " + args[1] + " to the BungeeMaintenance list!"));
            }
            case "remove" -> {
                if (!sender.hasPermission("logicbungeetools.BungeeMaintenance.modify")) {
                    sender.sendMessage("§cYou do not have permission to use this command!");
                    return;
                }

                if (args.length < 2) {
                    sender.sendMessage(new TextComponent("§cPlease provide a player!"));
                    return;
                }

                BungeeMain.getManager().removeWhitelist(args[1]);
                sender.sendMessage(new TextComponent("§cRemoved " + args[1] + " from the BungeeMaintenance list!"));
            }
            case "list" -> {
                if (!sender.hasPermission("logicbungeetools.BungeeMaintenance.list")) {
                    sender.sendMessage("§cYou do not have permission to use this command!");
                    return;
                }

                sender.sendMessage(new TextComponent("§7BungeeMaintenance whitelist:"));
                for (String name : BungeeMain.getManager().getWhitelist()) {
                    sender.sendMessage(new TextComponent("§7- " + name));
                }
            }
            case "on" -> {
                if (!sender.hasPermission("logicbungeetools.BungeeMaintenance.toggle")) {
                    sender.sendMessage("§cYou do not have permission to use this command!");
                    return;
                }

                BungeeMain.getManager().setMaintenance(true);
                sender.sendMessage(new TextComponent("§aBungeeMaintenance mode is now enabled!"));
            }
            case "off" -> {
                if (!sender.hasPermission("logicbungeetools.BungeeMaintenance.toggle")) {
                    sender.sendMessage("§cYou do not have permission to use this command!");
                    return;
                }

                BungeeMain.getManager().setMaintenance(false);
                sender.sendMessage(new TextComponent("§cBungeeMaintenance mode is now disabled!"));
            }
            case "reload" -> {
                if (!sender.hasPermission("logicbungeetools.BungeeMaintenance.reload")) {
                    sender.sendMessage("§cYou do not have permission to use this command!");
                    return;
                }

                BungeeMain.getManager().loadConfig();
                sender.sendMessage(new TextComponent("§aConfig reloaded!"));
            }
            default -> {}
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        ArrayList<String> list = new ArrayList<>();

        if (args.length == 1) {
            ArrayList<String> possibleCompletions = new ArrayList<>();

            if (sender.hasPermission("logicbungeetools.BungeeMaintenance.modify")) {
                possibleCompletions.add("add");
                possibleCompletions.add("remove");
            }

            if (sender.hasPermission("logicbungeetools.BungeeMaintenance.toggle")) {
                possibleCompletions.add("on");
                possibleCompletions.add("off");
            }

            if (sender.hasPermission("logicbungeetools.BungeeMaintenance.reload")) {
                possibleCompletions.add("reload");
            }

            if (sender.hasPermission("logicbungeetools.BungeeMaintenance.list")) {
                possibleCompletions.add("list");
            }

            for (String completion : possibleCompletions) {
                if (completion.startsWith(args[0].toLowerCase())) {
                    list.add(completion);
                }
            }
        }

        return list;
    }
}
