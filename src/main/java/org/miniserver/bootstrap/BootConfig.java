package org.miniserver.bootstrap;

import org.miniserver.core.config.ServerConfig;
import org.miniserver.core.container.Server;

public class BootConfig {

    public Server server(ServerConfig serverConfig) {
        Server server = new Server(serverConfig);
        return server;
    }

}
