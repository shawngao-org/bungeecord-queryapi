package org.shawngao.bc.queryapi.config;

import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.shawngao.bc.queryapi.QueryApi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Getter
public class Configure {

    private static final String CONFIG_FILE = "config.yml";

    private String host;
    private int port;
    private List<String> clientList;

    private final Plugin plugin = QueryApi.instance;

    public void createConfig() throws IOException {
        if (!plugin.getDataFolder().exists()) {
            plugin.getLogger().info("Created config folder: " + plugin.getDataFolder().mkdir());
        }
        File config = new File(plugin.getDataFolder(), CONFIG_FILE);
        if (!config.exists()) {
            FileOutputStream fileOutputStream = new FileOutputStream(config);
            InputStream inputStream = plugin.getResourceAsStream(CONFIG_FILE);
            inputStream.transferTo(fileOutputStream);
        }
    }

    public void loadConfigure() throws IOException {
        Configuration configuration = ConfigurationProvider
                .getProvider(YamlConfiguration.class)
                .load(new File(plugin.getDataFolder(), CONFIG_FILE));
        host = configuration.getString("http.api.host");
        port = configuration.getInt("http.api.port");
        clientList = configuration.getStringList("bukkit.client");
    }
}
