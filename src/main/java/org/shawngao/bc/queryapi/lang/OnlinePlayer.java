package org.shawngao.bc.queryapi.lang;

import lombok.Data;

import java.io.Serializable;

@Data
public class OnlinePlayer implements Serializable {

    private String playerName;
    private String displayName;
    private String uniqueId;
    private String serverName;

    @Override
    public String toString() {
        return "{"
                + "\"playerName\": \"" + playerName + "\""
                + "\"displayName\": \"" + displayName + "\""
                + "\"uniqueId\": \"" + uniqueId + "\""
                + "\"serverName\": \"" + serverName + "\""
                + "}";
    }
}
