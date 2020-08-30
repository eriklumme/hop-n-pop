package org.vaadin.erik.game.client.service;

import org.vaadin.erik.game.client.GameClient;

public class GameServiceImpl implements GameService {

    private final GameClient gameClient;

    public GameServiceImpl(GameClient gameClient) {
        this.gameClient = gameClient;
    }

    @Override
    public void joinGame(String nickname) {
        gameClient.joinGame(nickname);
    }
}
