package org.shawngao.bc.queryapi.channel;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.shawngao.bc.queryapi.QueryApi;

public class ChannelListener implements Listener {

    @EventHandler
    public void on(PluginMessageEvent event) {
        if (!event.getTag().equals(QueryApi.staticConfigure.getBukkitChannel())) {
            return;
        }
        ByteArrayDataInput arrayDataInput = ByteStreams.newDataInput(event.getData());
        String subChannel = arrayDataInput.readUTF();
        if (QueryApi.staticConfigure.getSubServerList().contains(subChannel)) {
            if (event.getReceiver() instanceof Server) {
                Server server = (Server) event.getReceiver();
                QueryApi.instance.getLogger().info(server.getInfo().getName());
            }
        }
    }
}
