import Game from "./game";

export default class Communicator {

    private readonly SIMULATED_PING = 200;

    private readonly socket: any;

    private readonly game: Game;

    private order = 0;

    // TODO: Rely on events instead of passing a Game reference
    constructor(game: Game) {
        this.game = game;

        const request = {
            url: `${document.location.toString()}/game/command`,
            transport: 'websocket',
            onMessage: this.messageReceived.bind(this)
        };

        this.socket = window.vaadinPush.atmosphere.subscribe(request);
    }

    sendPlayerCommand(direction: string) {
        console.warn(`Pressed ${direction}`);
        this.sendMessage({order: this.order++, direction});
    }

    private sendMessage(message: object) {
        setTimeout(() => {
            this.socket.push(JSON.stringify(message));
            console.warn("Message sent...");
        }, this.SIMULATED_PING / 2);
    }

    private messageReceived(response: any) {
        const responseBody = response.responseBody;
        setTimeout(() => {
            console.warn("Message received...");
            this.onGameSnapshotReceived(responseBody);
        }, this.SIMULATED_PING / 2);
    }

    private onGameSnapshotReceived(message: any) {
        if (this.game === undefined) {
            return;
        }
        try {
            const json = JSON.parse(message);
            this.game.draw(json.x, json.y);
        } catch (e) {
            console.error('Error: ', message.data);
            return;
        }
    }
}