package org.shawngao.bc.queryapi.command;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import org.shawngao.bc.queryapi.QueryApi;
import org.shawngao.bc.queryapi.channel.Channel;

public class Test extends Command {

    public Test() {
        super("testChannel");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Channel channel = new Channel();
        channel.sendCustomData(
                QueryApi.instance.getProxy().getServerInfo("lobby"),
                "This is a test msg.",
                1,
                "lobby"
        );
    }
}
