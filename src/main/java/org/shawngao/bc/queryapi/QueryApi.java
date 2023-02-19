package org.shawngao.bc.queryapi;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import org.shawngao.bc.queryapi.annotation.HandlerMappingHandler;
import org.shawngao.bc.queryapi.annotation.RequestObject;
import org.shawngao.bc.queryapi.command.Reload;
import org.shawngao.bc.queryapi.command.Test;
import org.shawngao.bc.queryapi.config.Configure;
import org.shawngao.bc.queryapi.lang.Result;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;

public final class QueryApi extends Plugin {

    private HttpServer server;
    public static Plugin instance;
    public static Configure staticConfigure;
    public static HttpServer simpleServer;

    @Override
    public void onEnable() {
        // Plugin startup logic
        ProxyServer.getInstance().getPluginManager().registerCommand(this,
                new Reload());
        ProxyServer.getInstance().getPluginManager().registerCommand(this,
                new Test());
        instance = this;
        try {
            initPlugin();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void initPlugin() throws IOException {
        Configure configure = processConfigure();
        staticConfigure = configure;
        getLogger().info("Starting http server ...");
        server = HttpServer.create(
                new InetSocketAddress(configure.getHost(),
                        configure.getPort()), 0);
        simpleServer = server;
        context();
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
        server.stop(0);
        getLogger().info("Stopped http server.");
        getLogger().info("Disabled this plugin.");
    }

    public void context() {
        HandlerMappingHandler handlerMappingHandler = new HandlerMappingHandler();
        handlerMappingHandler.getRequestMappingMethod();
        this.getLogger().info("RequestMapping length: "
                + handlerMappingHandler.getRequestMappingMap().size());
        handlerMappingHandler.getRequestMappingMap().forEach((k, v) ->
                this.getLogger().info(k + " ===> " + v.getMethod().getName()));
        simpleServer.createContext("/", exchange -> {
            Result result;
            try {
                result = preHandlerRequest(exchange, handlerMappingHandler);
            } catch (InvocationTargetException |
                     IllegalAccessException |
                     NoSuchMethodException |
                     InstantiationException e) {
                e.printStackTrace();
                result = new Result(500, null);
            }
            afterHandlerRequest(exchange, result);
        });
    }

    public Result preHandlerRequest(HttpExchange exchange,
                                  HandlerMappingHandler handlerMappingHandler)
            throws InvocationTargetException, IllegalAccessException,
            NoSuchMethodException, InstantiationException {
        String log = "[" + exchange.getRemoteAddress().getHostName() + " -> "
                + exchange.getRemoteAddress().getAddress() + ":"
                + exchange.getRemoteAddress().getPort() + "] "
                + "[" + exchange.getRequestMethod() + "] [" + exchange.getRequestURI().getPath()
                + "] ===> " + exchange.getRequestBody();
        this.getLogger().info(log);
        return requestHandler(exchange, handlerMappingHandler);
    }

    public Result requestHandler(HttpExchange exchange,
                                 HandlerMappingHandler handlerMappingHandler)
            throws InvocationTargetException, IllegalAccessException,
            NoSuchMethodException, InstantiationException {
        String path = exchange.getRequestURI().getPath();
        RequestObject requestObject = handlerMappingHandler.getRequestMappingMap().get(path);
        if (requestObject == null) {
            return new Result(404, "NotFound");
        }
        this.getLogger().info(requestObject.getMethod().getName());
        Object o = requestObject.getMethod().invoke(
                requestObject.getClazz().getConstructor().newInstance()
        );
        return new Result(200, o);
    }

    public void afterHandlerRequest(HttpExchange exchange, Result result) throws IOException {
        byte[] content = result.getData().toString().getBytes();
        exchange.getResponseHeaders().add("Content-Type", "text/html; charset=UTF-8");
        exchange.sendResponseHeaders(result.getCode(), content.length);
        exchange.getResponseBody()
                .write(content);
        exchange.close();
    }
}
