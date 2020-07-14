package org.vaadin.erik.game.communication;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import elemental.json.Json;
import elemental.json.JsonObject;
import org.atmosphere.config.service.WebSocketHandlerService;
import org.atmosphere.websocket.WebSocket;
import org.atmosphere.websocket.WebSocketHandlerAdapter;
import org.springframework.stereotype.Component;
import org.vaadin.erik.game.entity.PlayerCommand;

/**
 * @author erik@vaadin.com
 * @since 11/07/2020
 */
@WebSocketHandlerService(path = "/game/command")
public class PlayerCommandHandler extends WebSocketHandlerAdapter {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private int x = 100;
    private int y = 100;

    @Override
    public void onOpen(WebSocket webSocket) throws IOException {
        super.onOpen(webSocket);
    }

    @Override
    public void onTextMessage(WebSocket webSocket, String data) {
        try {
            PlayerCommand playerCommand = objectMapper.readValue(data, PlayerCommand.class);
            System.out.println("Received a player command: " + playerCommand);

            switch (playerCommand.getDirection()) {
                case UP:
                    y -= 10;
                    break;
                case DOWN:
                    y += 10;
                    break;
                case LEFT:
                    x -= 10;
                    break;
                case RIGHT:
                    x += 10;
                    break;
            }
            JsonObject object = Json.createObject();
            object.put("x", x);
            object.put("y", y);
            webSocket.write(object.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
