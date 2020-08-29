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
          <canvas id="background" width="1024" height="768" style="position: absolute; top: 1px; left: 1px" ></canvas>
          <canvas id="canvas" width="1164" height="768" style="border: 1px solid red" ></canvas>
          <canvas id="overlay" width="1164" height="768" style="position: absolute; top: 1px; left: 1px"></canvas>
          <debug-panel></debug-panel>
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
        MainView.loadTileMap()
            .then(_ => window.main());
    }
}
