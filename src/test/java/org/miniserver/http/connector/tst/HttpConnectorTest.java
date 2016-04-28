package org.miniserver.http.connector.tst;

import org.miniserver.core.config.Config;
import org.miniserver.core.config.ConnectorConfig;
import org.miniserver.core.http.connector.HttpConnector;
import org.testng.annotations.BeforeMethod;

import sun.net.www.http.HttpClient;

public class HttpConnectorTest {

    private HttpClient client;

    private HttpConnector connector;

    private Config connectorConfig;

    @BeforeMethod
    public void BeforeMethod() {
        this.connectorConfig = prepareConnectorConfig();
        this.connector = new HttpConnector(connectorConfig);
    }

    private Config prepareConnectorConfig() {
        Config connectorConfig = new ConnectorConfig();

        return connectorConfig;
    }
}
