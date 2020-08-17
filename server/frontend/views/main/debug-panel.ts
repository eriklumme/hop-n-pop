import {css, customElement, html, LitElement, query} from 'lit-element';
import {render} from "lit-html";

import {registerStyles} from '@vaadin/vaadin-themable-mixin/register-styles.js';
import '@vaadin/vaadin-dialog';
import '@vaadin/vaadin-button';
import '@vaadin/vaadin-ordered-layout/vaadin-vertical-layout';
import '@vaadin/vaadin-text-field/vaadin-integer-field';
import '@vaadin/vaadin-tabs';
import '@vaadin/vaadin-tabs/vaadin-tab';

import * as DebugEndpoint from '../../generated/DebugEndpoint';
import NodeData from "../../generated/org/vaadin/erik/game/ai/NodeData";

registerStyles('vaadin-dialog-overlay', css`
    [part="content"] { 
        background-color: var(--lumo-shade-40pct); 
        width: 400px;
        height: 400px;
    }
`);

@customElement('debug-panel')
export class DebugPanel extends LitElement {

    @query("#dialog")
    private dialog: any;

    private slowDownFactor: number = 5;

    private dialogRenderer = this._renderDialog.bind(this);

    static get styles() {
        return [
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
        <vaadin-dialog modeless draggable resizable id="dialog" .renderer="${this.dialogRenderer}"></vaadin-dialog>
        <vaadin-button @click="${this._openDialog}">Debug</vaadin-button>
    `;
    }

    private _renderDialog(root: any, _dialog: any) {
        // language=HTML
        render(html`
            <vaadin-vertical-layout id="debug-panel">
                <vaadin-tabs>
                    <vaadin-tab selected @click=${(_: any) => this.setSelectedTab(0)}>General</vaadin-tab>
                    <vaadin-tab @click=${(_: any) => this.setSelectedTab(1)}>AI</vaadin-tab>
                </vaadin-tabs>
                
                <div class="tab-content" style="display: flex;">
                    <div class="row">
                        <vaadin-integer-field 
                            id="slowDownFactorField" 
                            label="Server slow down" 
                            class="full-width"
                            @change="${(e: any) => this.slowDownFactor = e.target.value}"></vaadin-integer-field>
                        <vaadin-button @click="${(_: any) => DebugPanel.sendSlowDown(this.slowDownFactor)}">Apply</vaadin-button>
                    </div>
                    <div class="row" style="margin-top: auto">
                        <vaadin-button @click="${(_: any) => this.dialog.opened = false}" style="margin-left: auto">Close</vaadin-button>
                    </div>
                </div>
                
                <div class="tab-content">
                    <vaadin-button>Spawn AI</vaadin-button>
                    <vaadin-button @click=${this.sendCalculateAIPathing}>Calculate AI pathing</vaadin-button>
                </div>
            </vaadin-vertical-layout>
        `, root)
    }

    private _openDialog() {
        this.dialog.opened = true;
    }

    private setSelectedTab(indexToShow: number) {
        document.querySelectorAll('.tab-content').forEach((value: any, index: number) => {
            if (index === indexToShow) {
                value.style.display = 'flex';
            } else {
                value.style.display = 'none';
            }
        });
    }

    private static sendSlowDown(slowDownFactor: number) {
        if (slowDownFactor) {
            DebugEndpoint.setServerSlowDown(slowDownFactor);
        }
    }

    private sendCalculateAIPathing() {
        DebugEndpoint.calculateAIPathing().then((_: any) => DebugPanel.printPathingData());
    }

    private static printPathingData() {
        const bSize = 32;
        DebugEndpoint.getPathingData().then((data: { [key: string]: NodeData; }) => {
            let ctx: CanvasRenderingContext2D = <CanvasRenderingContext2D> window.overlay.getContext("2d");

            ctx.fillStyle = "rgba(255, 0, 0, 0.5)";
            for(let key in data) {
                let nodeData = data[key];
                ctx.beginPath();
                ctx.arc(
                    nodeData.x * bSize + bSize / 2,
                    nodeData.y * bSize + bSize / 2,
                    bSize / 2,
                0, 2 * Math.PI, false);
                ctx.closePath();
                ctx.fill();
            }
        });
    }
}
