import {css, customElement, html, LitElement, query} from 'lit-element';
import Game from './game';

@customElement('main-view')
export class MainView extends LitElement {

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
      <canvas width="768" height="512" style="border: 1px solid red" id="canvas"></canvas>
    `;
    }

    constructor() {
        super();

        document.addEventListener("keydown", e => {
            let direction;
            switch (e.code) {
                case 'ArrowUp':
                    direction = 'UP';
                    break;
                case 'ArrowDown':
                    direction = 'DOWN';
                    break;
                case 'ArrowLeft':
                    direction = 'LEFT';
                    break;
                case 'ArrowRight':
                    direction = 'RIGHT';
                    break;
            }
            if (direction) {
                this.game!.move(direction);
            }
        });
    }

    protected firstUpdated(): void {
        this.game = new Game(this.canvas!);
    }
}
