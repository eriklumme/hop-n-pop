import {css, customElement, html, LitElement, query} from 'lit-element';
import {render} from "lit-html";

import {registerStyles} from '@vaadin/vaadin-themable-mixin/register-styles.js';
import '@vaadin/vaadin-dialog';
import '@vaadin/vaadin-button';
import '@vaadin/vaadin-ordered-layout/vaadin-vertical-layout';
import '@vaadin/vaadin-text-field/vaadin-integer-field';

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
                <div class="row">
                    <vaadin-integer-field 
                        id="slowDownFactorField" 
                        label="Server slow down" 
                        class="full-width"
                        @change="${(e: any) => this.slowDownFactor = e.target.value}"></vaadin-integer-field>
                    <vaadin-button @click="${(_: any) => this._sendSlowDown(this.slowDownFactor)}">Apply</vaadin-button>
                </div>
                <div class="row" style="margin-top: auto">
                    <vaadin-button @click="${(_: any) => this.dialog.opened = false}" style="margin-left: auto">Close</vaadin-button>
                </div>
            </vaadin-vertical-layout>
        `, root)
    }

    private _openDialog() {
        this.dialog.opened = true;
    }

    private _sendSlowDown(slowDownFactor: number) {
        if (slowDownFactor) {
            DebugEndpoint.setServerSlowDown(slowDownFactor);
        }
    }

    /*

        slowdownInput = (HTMLInputElement) Window.current().getDocument().getElementById("slowdown");
        slowdownInput.addEventListener("change", e -> sendDebugCommand(Double.parseDouble(slowdownInput.getValue())));
     */
}
