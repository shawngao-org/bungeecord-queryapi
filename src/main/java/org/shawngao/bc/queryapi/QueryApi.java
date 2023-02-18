package org.shawngao.bc.queryapi;

import cn.hutool.http.server.SimpleServer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import org.shawngao.bc.queryapi.command.Reload;
import org.shawngao.bc.queryapi.command.Test;
import org.shawngao.bc.queryapi.config.Configure;

import java.io.IOException;

public final class QueryApi extends Plugin {

    private SimpleServer server;
    private Configure configure;

    public static Plugin instance;
    public static Configure staticConfigure;

    @Override
    public void onEnable() {
        // Plugin startup logic
        ProxyServer.getInstance().getPluginManager().registerCommand(this,
                new Reload());
        ProxyServer.getInstance().getPluginManager().registerCommand(this,
                new Test());
        instance = this;
        initPlugin();
    }

    public void initPlugin() {
        configure = processConfigure();
        staticConfigure = configure;
        getLogger().info("Enabling plugin channels ...");
        this.getProxy().registerChannel(configure.getBukkitChannel());
        getLogger().info("Channel " + configure.getBukkitChannel() + " was registered.");
        getLogger().info("Done.");
        getLogger().info("Starting http server ...");
        server = new SimpleServer(configure.getHost(), configure.getPort());
        server.start();
    }

    private Configure processConfigure() {
        Configure configure = new Configure();
        try {
            getLogger().info("Checking configure file ...");
            configure.createConfig();
            getLogger().info("Done.");
            getLogger().info("Loading configure file ...");
            configure.loadConfigure();
            getLogger().info("Loaded.");
        } catch (IOException e) {
            getLogger().warning(e.getMessage());
            throw new RuntimeException(e);
        }
        return configure;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Disabling this plugin ...");
        getLogger().info("Stopping http server ...");
        server.getRawServer().stop(0);
        getLogger().info("Stopped http server.");
        getLogger().info("Unregistering plugin channel ...");
        this.getProxy().unregisterChannel(configure.getBukkitChannel());
        getLogger().info("Channel " + configure.getBukkitChannel() + " was unregistered.");
        getLogger().info("Unregistered plugin channel.");
        getLogger().info("Disabled this plugin.");
    }
}
