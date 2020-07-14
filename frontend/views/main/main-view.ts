import {css, customElement, html, LitElement, query} from 'lit-element';
import Game from './game';

@customElement('main-view')
export class MainView extends LitElement {

    private readonly socket: any;

    private game: Game | undefined;

    @query("#canvas")
    private canvas: HTMLCanvasElement | undefined;

    static get styles() {
        return [
            // CSSModule('lumo-typography lumo-styles'),
            css`
        :host {
          display: block;
          height: 100%;
        }
      `,
        ];
    }

    render() {
        return html`
      <input type="button" value="Click me" @click=${this.onClicked}>
      <canvas width="768" height="512" style="border: 1px solid red" id="canvas"></canvas>
    `;
    }

    constructor() {
        super();

        const request = {
            url: `${document.location.toString()}/game/command`,
            transport: 'websocket',
            onMessage: this.onGameSnapshotReceived
        };

        this.socket = window.vaadinPush.atmosphere.subscribe(request);
    }

    protected firstUpdated(): void {
        this.game = new Game(this.canvas!);
    }

    private onGameSnapshotReceived(response: any) {
        console.warn("Message received...");
        console.warn(response.responseBody);
        if (this.game === undefined) {
            return;
        }
        const message = response.responseBody;
        try {
            const json = JSON.parse(message);
            this.game.draw(json.x, json.y);
        } catch (e) {
            console.error('Error: ', message.data);
            return;
        }
    }

    onClicked() {
        this.socket.push(JSON.stringify({order: 1, direction: 'UP'}));
        console.warn("Clicked...");
    }
}
