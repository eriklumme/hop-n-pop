import {css, customElement, html, LitElement, query, property} from 'lit-element';
import '@vaadin/vaadin-button';
import './main-view';
import {MainView} from "./main-view";

type TileType = { typeCode: number, spriteCode: number }

const tileTypes: TileType[] = [
    { typeCode: 0, spriteCode: 0x42 },
    { typeCode: 0, spriteCode: 0x40 },
    { typeCode: 0, spriteCode: 0x41 },
    { typeCode: 1, spriteCode: 0x00 },
    { typeCode: 1, spriteCode: 0x01 },
    { typeCode: 1, spriteCode: 0x02 },
    { typeCode: 1, spriteCode: 0x03 },
    { typeCode: 1, spriteCode: 0x10 },
    { typeCode: 1, spriteCode: 0x11 },
    { typeCode: 1, spriteCode: 0x20 },
    { typeCode: 1, spriteCode: 0x21 },
    { typeCode: 1, spriteCode: 0x22 },
    { typeCode: 1, spriteCode: 0x23 },
    { typeCode: 1, spriteCode: 0x30 },
    { typeCode: 1, spriteCode: 0x31 },
    { typeCode: 1, spriteCode: 0x32 },
];

const spriteSheet: HTMLImageElement = document.createElement("img");
spriteSheet.src = "img/tiles.png";

@customElement('tilemap-generator')
export class TilemapGenerator extends LitElement {

    @query("#board")
    private board: HTMLCanvasElement | undefined;

    @query("#palette")
    private palette: HTMLCanvasElement | undefined;

    private readonly width: number = 32;
    private readonly height: number = 24;

    private selectedTileType: TileType = tileTypes[0];

    private mouseDown: boolean = false;

    static get styles() {
        return [css`
            :host {
              display: block;
              height: 100%;
              overflow: auto;
            }
            #board {
                width: 1024px;
                height: 768px;
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
                width: 32px;
                height: 32px;
                overflow: hidden;
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

    protected async firstUpdated(): Promise<void> {
        await MainView.loadTileMap();

        const wrapper = this.shadowRoot?.getElementById("wrapper");
        wrapper?.addEventListener('mousedown', _ => this.mouseDown = true);
        wrapper?.addEventListener('mouseup', _ => this.mouseDown = false);

        for (let h = 0; h < this.height; h++) {
            let row = document.createElement('div');
            row.classList.add('row');
            this.board!.appendChild(row);

            for (let w = 0; w < this.width; w++) {
                const tile: Tile = <Tile> document.createElement('tilemap-tile');
                const eventListener = (_: any) => tile.tileType = this.selectedTileType;
                tile.addEventListener('click', eventListener);
                tile.addEventListener('mouseenter', _ => {
                    if (this.mouseDown) eventListener(null);
                });
                row.appendChild(tile);

                const tileType: TileType | undefined = TilemapGenerator.loadTileTypeFromTileMap(w, h);
                if (tileType) {
                    tile.tileType = tileType;
                }
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
            selector.style.background = Tile.getBackground(tileType);
            this.palette!.appendChild(selector);
        });
    }

    private static loadTileTypeFromTileMap(x: number, y: number): TileType | undefined {
        if (x < 0 || y < 0) {
            return undefined;
        }
        if (window.tileMapData && y < window.tileMapData.tiles.length) {
            const row: number[][] = window.tileMapData.tiles[y];
            if (row && x < row.length) {
                const tile: number[] = row[x];
                return { typeCode: tile[0], spriteCode: tile[1] };
            }
        }
        return undefined;
    }

    private export() {
        let data: number[][][] = [];
        this.shadowRoot?.querySelectorAll('.row').forEach(row => {
            let rowData: number[][] = [];
            data.push(rowData);

            row.querySelectorAll('tilemap-tile').forEach(el => {
                const tileType: TileType = (<Tile> el).tileType;
                rowData.push([tileType.typeCode, tileType.spriteCode]);
            });
        });

        console.warn("Export: \n" + JSON.stringify({tiles: data}));
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
        this.style.background = Tile.getBackground(value);
        this.requestUpdate('tileType', oldValue);
    }

    public static getBackground(tileType: TileType): string {
        const offsetX = tileType.spriteCode & 0xf;
        const offsetY = (tileType.spriteCode >> 4) & 0xf;
        return `url(img/tiles.png) ${offsetX * -32}px ${offsetY * -32}px`;
    }
}
