import {css, customElement, html, LitElement, query} from 'lit-element';
import './debug-panel';
import '@vaadin/vaadin-ordered-layout/vaadin-horizontal-layout';
import '@vaadin/vaadin-button';
import * as ServerEndpoint from '../../generated/ServerEndpoint';

@customElement('main-view')
export class MainView extends LitElement {

    private hasJoined: boolean = false;

    @query("#joinButton")
    private joinButton: any;

    static get styles() {
        return [
            css`
        :host {
            position: relative;
          display: flex;
          flex-flow: column;
          align-items: flex-start;
          height: 100%;
        }
      `,
        ];
    }

    render() {
        return html`
          <vaadin-horizontal-layout>
            <vaadin-button @click=${this.joinGame} disabled id="joinButton">Join game</vaadin-button>
            <debug-panel></debug-panel>
          </vaadin-horizontal-layout>
          <div style="position: relative">
            <canvas id="background" width="1024" height="768" style="position: absolute; top: 1px; left: 1px" ></canvas>
            <canvas id="canvas" width="1164" height="768" style="border: 1px solid red" ></canvas>
            <canvas id="overlay" width="1164" height="768" style="position: absolute; top: 1px; left: 1px"></canvas>
          </div>
    `;
    }

    public static async loadTileMap() {
        try {
            let response = await fetch("tilemap.json");
            window.tileMapData = await response.json();
        } catch (e) {
            console.error("Error loading tile map: " + e);
        }

        // Used both by the tilemap-generator and the TeaVM code
        window.spriteCodeToColor = (spriteCode: number): string => {
            switch (spriteCode) {
                case 0:
                    return 'transparent';
                case 1:
                    return 'green';
                default:
                    return 'deeppink';
            }
        }
    }

    protected firstUpdated(): void {
        window.background = <HTMLCanvasElement>this.shadowRoot!.querySelector('#background');
        window.canvas = <HTMLCanvasElement>this.shadowRoot!.querySelector('#canvas');
        window.overlay = <HTMLCanvasElement>this.shadowRoot!.querySelector('#overlay');

        const that = this;
        (function getServerInfo() {
            ServerEndpoint.getServerInfo()
                .then(result => {
                    that.joinButton.disabled = that.hasJoined || result.full;
                })
                .catch(e => console.error(e));
            setTimeout(getServerInfo, 5000);
        })();

        MainView.loadTileMap()
            .then(_ => window.main());
    }

    private joinGame() {
        window.GameService.joinGame();
        this.hasJoined = true;
        this.joinButton.disabled = true;
    }
}
