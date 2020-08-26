import {css, customElement, html, LitElement, query} from 'lit-element';
import {render} from "lit-html";

import {registerStyles} from '@vaadin/vaadin-themable-mixin/register-styles.js';
import '@vaadin/vaadin-dialog';
import '@vaadin/vaadin-button';
import '@vaadin/vaadin-ordered-layout/vaadin-vertical-layout';
import '@vaadin/vaadin-text-field/vaadin-integer-field';
import '@vaadin/vaadin-tabs';
import '@vaadin/vaadin-tabs/vaadin-tab';
import '@vaadin/vaadin-checkbox';

import * as DebugEndpoint from '../../generated/DebugEndpoint';

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
                    <vaadin-checkbox @change=${(e: any) => this.fixedDeltaChanged(e)}>Use fixed delta</vaadin-checkbox>
                    <div class="row" style="margin-top: auto">
                        <vaadin-button @click="${(_: any) => this.dialog.opened = false}" style="margin-left: auto">Close</vaadin-button>
                    </div>
                </div>
                
                <div class="tab-content">
                    <vaadin-button @click=${this.spawnAI}>Spawn AI</vaadin-button>
                    <vaadin-button @click=${this.despawnAIS}>Despawn AIs</vaadin-button>
                    <vaadin-button @click=${this.sendCalculateAIPathing}>Calculate AI pathing</vaadin-button>
                    <vaadin-button @click=${this.startRecording}>Start path recording</vaadin-button>
                    <vaadin-button @click=${this.saveRecordings}>Save recordings to file</vaadin-button>
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

    private fixedDeltaChanged(event: any) {
        DebugEndpoint.setFixedDelta(event.target.checked).catch((e: Error) => console.error(e));
    }

    private sendCalculateAIPathing() {
        DebugEndpoint.calculateAIPathing().catch((e: Error) => console.error(e));
    }

    private startRecording() {
        DebugEndpoint.recordMovementForAI().catch((e: Error) => console.error(e));
    }

    private saveRecordings() {
        DebugEndpoint.saveRecordedData().catch((e: Error) => console.error(e));
    }

    private spawnAI() {
        DebugEndpoint.spawnAI().catch((e: Error) => console.error(e));
    }

    private despawnAIS() {
        DebugEndpoint.despawnAIS().catch((e: Error) => console.error(e));
    }
}
