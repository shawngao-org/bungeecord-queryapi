package org.shawngao.bc.queryapi.server;

import cn.hutool.http.ContentType;
import org.shawngao.bc.queryapi.QueryApi;

public class HttpServerHandler {

    public void rootPath() {
        QueryApi.simpleServer.addAction("/", ((request, response) -> {
            response.write("Hello, this is a api test page.");
        }));
    }

    public void getAllOnlinePlayerAmount() {
        QueryApi.simpleServer.addAction("/getOnlinePlayerAmount", ((request, response) -> {
            response.write(
                    String.valueOf(QueryApi.instance.getProxy().getServers().size()));
        }));
    }
}
