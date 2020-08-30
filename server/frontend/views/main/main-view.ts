import {css, customElement, html, LitElement, query} from 'lit-element';
import './debug-panel';
import '@vaadin/vaadin-ordered-layout/vaadin-horizontal-layout';
import '@vaadin/vaadin-button';
import '@vaadin/vaadin-text-field';
import * as ServerEndpoint from '../../generated/ServerEndpoint';

@customElement('main-view')
export class MainView extends LitElement {

    private hasJoined: boolean = false;

    @query("#joinButton")
    private joinButton: any;

    @query("#nicknameField")
    private nicknameField: any;

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
        #canvas {
          background: linear-gradient(0deg, #c53c1d, #1f346d);
        }
      `,
        ];
    }

    render() {
        return html`
          <vaadin-horizontal-layout style="align-items: baseline" theme="spacing">
            <h2 style="margin-left: var(--lumo-space-xl)">Hop 'n Pop</h2>
            <vaadin-text-field label="Nickname" minlength="2" maxlength="8" required id="nicknameField"></vaadin-text-field>
            <vaadin-button @click=${this.joinGame} disabled id="joinButton">Join game</vaadin-button>
            <debug-panel></debug-panel>
          </vaadin-horizontal-layout>
          <div style="position: relative; padding: 0 var(--lumo-space-l)">
            <canvas id="canvas" width="1164" height="768" style="border: 1px solid black" ></canvas>
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
    }

    protected firstUpdated(): void {
        window.canvas = <HTMLCanvasElement>this.shadowRoot!.querySelector('#canvas');

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
        if (this.nicknameField.validate()) {
            const nickname = this.nicknameField.value;
            window.GameService.joinGame(nickname);
            this.hasJoined = true;
            this.joinButton.disabled = true;
        }
    }
}
