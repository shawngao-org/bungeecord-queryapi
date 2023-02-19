package org.shawngao.bc.queryapi.server;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.shawngao.bc.queryapi.QueryApi;
import org.shawngao.bc.queryapi.annotation.RequestMapping;
import org.shawngao.bc.queryapi.lang.OnlinePlayer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RequestMapping("/")
public class HttpServerHandler {

    @RequestMapping("getAllOnlinePlayerList")
    public List<OnlinePlayer> getAllOnlinePlayerList() {
        Collection<ProxiedPlayer> players = QueryApi.instance.getProxy().getPlayers();
        List<OnlinePlayer> onlinePlayerList = new ArrayList<>();
        players.forEach(p -> {
            OnlinePlayer onlinePlayer = new OnlinePlayer();
            onlinePlayer.setPlayerName(p.getName());
            onlinePlayer.setDisplayName(p.getDisplayName());
            onlinePlayer.setUniqueId(p.getUniqueId().toString());
            onlinePlayer.setServerName(p.getServer().getInfo().getName());
            onlinePlayerList.add(onlinePlayer);
        });
        return onlinePlayerList;
    }
}
