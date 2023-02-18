package org.shawngao.bc.queryapi.command;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import org.shawngao.bc.queryapi.QueryApi;

public class Reload extends Command {
    public Reload() {
        super("reloadConfigure");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            sender.sendMessage(
                    new ComponentBuilder(
                            "This command is only allowed to be executed in the console.")
                            .color(ChatColor.RED).create());
            return;
        }
        QueryApi.instance.onDisable();
        QueryApi.instance.onEnable();
    }
}
