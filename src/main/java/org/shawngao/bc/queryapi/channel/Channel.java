package org.shawngao.bc.queryapi.channel;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.config.ServerInfo;
import org.shawngao.bc.queryapi.QueryApi;

import java.util.Map;

public class Channel {

    public static boolean isChannelRegistered(String channel) {
        return QueryApi.instance.getProxy().getChannels().contains(channel);
    }

    public void sendCustomData(ServerInfo serverInfo, String data1, int data2, String subServer) {
        if (!isChannelRegistered(QueryApi.staticConfigure.getBukkitChannel())) {
            QueryApi.instance.getLogger().warning(
                    "This "
                            + QueryApi.staticConfigure.getBukkitChannel()
                            + " channel is not registered.");
            return;
        }
        Map<String, ServerInfo> serverInfoMap = QueryApi.instance.getProxy().getServers();
        if (serverInfoMap.isEmpty() || serverInfoMap.get(subServer) == null) {
            QueryApi.instance.getLogger().warning("SubServer " + subServer + " is not online.");
            return;
        }
        QueryApi.instance.getLogger().info("Sender: BungeeCord");
        QueryApi.instance.getLogger().info("To: " + subServer);
        QueryApi.instance.getLogger().info("String Content: " + data1);
        QueryApi.instance.getLogger().info("Int data: " + data2);
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(subServer);
        out.writeUTF(data1);
        out.writeInt(data2);
        QueryApi.instance.getLogger().info("Sending ...");
        serverInfo.sendData(QueryApi.staticConfigure.getBukkitChannel(), out.toByteArray());
        QueryApi.instance.getLogger().info("Has been sent.");
    }
}
