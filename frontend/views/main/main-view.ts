import {css, customElement, html, LitElement} from 'lit-element';

@customElement('main-view')
export class MainView extends LitElement {

    static get styles() {
        return [
            // CSSModule('lumo-typography lumo-styles'),
            css`
        :host {
          display: flex;
          flex-flow: column;
          height: 100%;
        }
      `,
        ];
    }

    render() {
        return html`
      <canvas width="768" height="512" style="border: 1px solid red" id="canvas"></canvas>
      <input type="number" id="slowdown" />
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
        MainView.loadTileMap()
            .then(_ => window.main());
    }

    createRenderRoot() {
        return this;
    }
}
