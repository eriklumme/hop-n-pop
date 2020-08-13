import {css, customElement, html, LitElement, query, property} from 'lit-element';
import '@vaadin/vaadin-button';

type TileType = { color: string, typeCode: number }

const tileTypes: TileType[] = [
    { color: 'transparent', typeCode: 0 },
    { color: 'green', typeCode: 1 },
    { color: 'blue', typeCode: 2 }
];

@customElement('tilemap-generator')
export class TilemapGenerator extends LitElement {

    @query("#board")
    private board: HTMLCanvasElement | undefined;

    @query("#palette")
    private palette: HTMLCanvasElement | undefined;

    private readonly width: number = 24;
    private readonly height: number = 16;

    private selectedTileType: TileType = tileTypes[0];

    static get styles() {
        return [css`
            :host {
              display: block;
              height: 100%;
              overflow: auto;
            }
            #board {
                width: 768px;
                height: 512px;
                display: flex;
                flex-flow: column;
                margin: var(--lumo-space-m);
            }
            .row {
                display: flex;
                flex-grow: 1;
                width: 100%;
            }
            #toolbar {
                display: flex;
                margin: var(--lumo-space-m);
            }
            #palette {
                display: flex;
                margin: var(--lumo-space-s);
            }
            .palette-tile {
                width: 20px;
                height: 20px;
                border: 1px solid black;
                margin: var(--lumo-space-xs);
            }
            .palette-tile.selected {
                border-color: red;
            }
        `,
        ];
    }

    render() {
        return html`
        <div id="wrapper">
            <div id="board">
            
            </div>
            <div id="toolbar">
                <div id="palette">
                
                </div>
                <vaadin-button @click=${this.export}>Export</vaadin-button>
            </div>
        </div>
    `;
    }

    protected firstUpdated(): void {
        for (let h = 0; h < this.height; h++) {
            let row = document.createElement('div');
            row.classList.add('row');
            this.board!.appendChild(row);

            for (let w = 0; w < this.width; w++) {
                let tile: Tile = <Tile> document.createElement('tilemap-tile');
                tile.addEventListener('click', _ => {
                    tile.tileType = this.selectedTileType;
                });
                row.appendChild(tile);
            }
        }

        tileTypes.forEach((tileType, index) => {
            let selector = document.createElement('div');
            selector.classList.add('palette-tile');
            if (index === 0) {
                selector.classList.add('selected');
            }
            selector.addEventListener('click', _ => {
                this.selectedTileType = tileType;
                this.shadowRoot?.querySelectorAll('.palette-tile')
                    .forEach(element => element.classList.remove('selected'));
                selector.classList.add('selected');
            });
            selector.style.backgroundColor = tileType.color;
            this.palette!.appendChild(selector);
        });
    }

    private export() {
        let data: number[][] = [];
        this.shadowRoot?.querySelectorAll('.row').forEach(row => {
            let rowData: number[] = [];
            data.push(rowData);

            row.querySelectorAll('tilemap-tile').forEach(tile => {
                rowData.push((<Tile> tile).tileType.typeCode);
            });
        });

        console.warn("Export: \n" + JSON.stringify(data));
    }
}

@customElement('tilemap-tile')
class Tile extends LitElement {

    private _tileType: TileType;

    render() {
        return html``
    }

    static get styles() {
        return css`:host { border: 1px solid gray; box-sizing: border-box; flex: 1; }`
    }

    constructor() {
        super();
        this._tileType = tileTypes[0];
    }

    @property()
    get tileType(): TileType {
        return this._tileType;
    }

    set tileType(value: TileType) {
        const oldValue = this._tileType;
        this._tileType = value;
        this.style.backgroundColor = value.color;

        this.requestUpdate('tileType', oldValue);
    }
}
