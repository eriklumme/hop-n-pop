package org.vaadin.erik.game.communication;

import com.fasterxml.jackson.databind.ObjectMapper;
import elemental.json.Json;
import elemental.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.atmosphere.config.service.WebSocketHandlerService;
import org.atmosphere.websocket.WebSocket;
import org.atmosphere.websocket.WebSocketHandlerAdapter;
import org.vaadin.erik.game.server.GameSnapshotListener;
import org.vaadin.erik.game.server.Server;
import org.vaadin.erik.game.entity.Action;
import org.vaadin.erik.game.shared.Player;
import org.vaadin.erik.game.entity.PlayerCommand;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author erik@vaadin.com
 * @since 11/07/2020
 */
@WebSocketHandlerService(path = "/game/command")
public class PlayerCommandHandler extends WebSocketHandlerAdapter implements GameSnapshotListener {

    private static final Logger logger = LogManager.getLogger(PlayerCommandHandler.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final Server server;

    private Map<WebSocket, Player> socketToPlayer = new HashMap<>();

    public PlayerCommandHandler(Server server) {
        this.server = server;
        server.addGameSnapshotListener(this);
    }

    @Override
    public void onOpen(WebSocket webSocket) throws IOException {
        super.onOpen(webSocket);
        Player player = server.spawn();
        socketToPlayer.put(webSocket, player);

        JsonObject message = createReturnMessage(player);
        message.put("action", Action.SPAWN.ordinal());
        webSocket.write(message.toString());
    }

    @Override
    public void onTextMessage(WebSocket webSocket, String data) {
        try {
            PlayerCommand playerCommand = objectMapper.readValue(data, PlayerCommand.class);
            server.handleCommand(playerCommand);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClose(WebSocket webSocket) {
        super.onClose(webSocket);
        Player player = socketToPlayer.get(webSocket);

        if (player != null) {
            server.despawn(player);
        }
        socketToPlayer.remove(webSocket);
    }

    private JsonObject createReturnMessage(Player player) {
        JsonObject message = Json.createObject();
        message.put("x", player.getX());
        message.put("y", player.getY());
        message.put("uuid", player.getUUID());
        return message;
    }

    @Override
    public void onSnapshotPosted(Collection<Player> players) {
        // TODO: Write all players
        Player first = players.stream().findFirst().orElse(null);
        if (first == null) {
            return;
        }
        JsonObject object = createReturnMessage(first);
        try {
            for(WebSocket socket: socketToPlayer.keySet()) {
                 socket.write(object.toString());
            }
        } catch (IOException e) {
            logger.error("Error writing WebSocket message [" + object.toString() + "]", e);
        }
    }

    // TODO: Jackson encoder as https://github.com/Atmosphere/atmosphere-samples/blob/master/samples/spring-boot-sample-atmosphere/src/main/java/org/atmosphere/samples/springboot/ChatService.java
}
