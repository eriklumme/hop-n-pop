package org.vaadin.erik.game.communication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.atmosphere.config.service.WebSocketHandlerService;
import org.atmosphere.websocket.WebSocket;
import org.atmosphere.websocket.WebSocketHandlerAdapter;
import org.vaadin.erik.game.server.GameSnapshotListener;
import org.vaadin.erik.game.server.Server;
import org.vaadin.erik.game.shared.Player;
import org.vaadin.erik.game.shared.communication.GameSnapshot;
import org.vaadin.erik.game.shared.communication.RegistrationMessage;
import org.vaadin.erik.game.shared.data.Event;
import org.vaadin.erik.game.shared.data.PlayerCommand;

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

    private final Map<WebSocket, Player> socketToPlayer = new HashMap<>();

    public PlayerCommandHandler(Server server) {
        this.server = server;
        server.addGameSnapshotListener(this);
    }

    @Override
    public void onOpen(WebSocket webSocket) throws IOException {
        super.onOpen(webSocket);
        socketToPlayer.put(webSocket, null);
    }

    @Override
    public void onTextMessage(WebSocket webSocket, String data) {
        try {
            PlayerCommand playerCommand = objectMapper.readValue(data, PlayerCommand.class);
            Player player = socketToPlayer.get(webSocket);
            if (player == null) {
                handleJoinGameMessage(webSocket, playerCommand.getNickname());
                return;
            }
            if (!player.getUUID().equals(playerCommand.getUUID())) {
                logger.warn("Ignoring received message with incorrect UUID");
                return;
            }
            server.handleCommand(playerCommand);
        } catch (IOException e) {
            logger.error("Error parsing player command", e);
        }
    }

    private void handleJoinGameMessage(WebSocket webSocket, String nickname) throws IOException {
        if (nickname == null || nickname.length() < 2 || nickname.length() > 8) {
            return;
        }
        Player player = server.spawn(nickname);
        String uuid = null;
        if (player != null) {
            socketToPlayer.put(webSocket, player);
            uuid = player.getUUID();
        }

        RegistrationMessage registrationMessage = new RegistrationMessage(uuid);
        String message = writeJson(registrationMessage);
        webSocket.write(message);
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

    // TODO: Jackson encoder as
    //  https://github.com/Atmosphere/atmosphere-samples/blob/master/samples/spring-boot-sample-atmosphere/src/main/java/org/atmosphere/samples/springboot/ChatService.java

    @Override
    public void onSnapshotPosted(Collection<Player> players, Collection<Event> events) {
        GameSnapshot gameSnapshot = new GameSnapshot(players, events);
        String message = null;
        try {
            message = writeJson(gameSnapshot);
            // TODO: Broadcast
            for(WebSocket socket: socketToPlayer.keySet()) {
                 socket.write(message);
            }
        } catch (IOException e) {
            logger.error("Error writing WebSocket message [" + message + "]", e);
        }
    }

    private String writeJson(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }
}
