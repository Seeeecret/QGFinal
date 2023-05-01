package controller;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import java.util.List;

public class MyEndpointConfigurator extends ServerEndpointConfig.Configurator {
    @Override
    public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request, HandshakeResponse response) {
        super.modifyHandshake(config, request, response);
        List<String> printerIdValues = request.getParameterMap().get("printerId");
        if (printerIdValues != null && printerIdValues.size() > 0) {
            String printerId = printerIdValues.get(0);
            config.getUserProperties().put("printerId", printerId);
        }
    }
}
