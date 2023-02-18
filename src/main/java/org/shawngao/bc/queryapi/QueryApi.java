package org.shawngao.bc.queryapi;

import cn.hutool.http.server.SimpleServer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import org.shawngao.bc.queryapi.command.Reload;
import org.shawngao.bc.queryapi.command.Test;
import org.shawngao.bc.queryapi.config.Configure;
import org.shawngao.bc.queryapi.server.HttpServerHandler;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public final class QueryApi extends Plugin {

    private SimpleServer server;
    public static Plugin instance;
    public static Configure staticConfigure;
    public static SimpleServer simpleServer;

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
        Configure configure = processConfigure();
        staticConfigure = configure;
        getLogger().info("Starting http server ...");
        server = new SimpleServer(configure.getHost(), configure.getPort());
        simpleServer = server;
        HttpServerHandler httpServerHandler = new HttpServerHandler();
        Map<String, Boolean> services = configure.getServiceMap();
        services.forEach((k, v) -> {
            if (v) {
                try {
                    Method method = httpServerHandler.getClass()
                            .getDeclaredMethod(k);
                    method.invoke(httpServerHandler);
                    getLogger().info(k + " was loaded .");
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        server.start();
        getLogger().info("Http server was started.");
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
        getLogger().info("Disabled this plugin.");
    }
}
