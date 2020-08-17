import {css, customElement, html, LitElement} from 'lit-element';
import './debug-panel';

@customElement('main-view')
export class MainView extends LitElement {

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
          <canvas id="canvas" width="768" height="512" style="border: 1px solid red" ></canvas>
          <canvas id="overlay" width="768" height="512" style="position: absolute; top: 1px; left: 1px"></canvas>
          <debug-panel></debug-panel>
    `;
    }

    private static async loadTileMap() {
        try {
            let response = await fetch("tilemap.json");
            let data = await response.json();
            window.tileMapData = data;
        } catch (e) {
            console.error("Error loading tile map: " + e);
        }
    }

    protected firstUpdated(): void {
        window.canvas = <HTMLCanvasElement>this.shadowRoot!.querySelector('#canvas');
        window.overlay = <HTMLCanvasElement>this.shadowRoot!.querySelector('#overlay');
        MainView.loadTileMap()
            .then(_ => window.main());
    }
}
