/**
 * This module is generated from DebugEndpoint.java
 * All changes to this file are overridden. Please consider to make changes in the corresponding Java file if necessary.
 * @see {@link file://C:\projects\hop-n-pop\server\src\main\java\org\vaadin\erik\game\communication\endpoint\DebugEndpoint.java}
 * @module DebugEndpoint
 */

// @ts-ignore
import client from './connect-client.default';
import NodeData from './org/vaadin/erik/game/ai/NodeData';

function _calculateAIPathing(): Promise<void> {
  return client.call('DebugEndpoint', 'calculateAIPathing');
}
export {_calculateAIPathing as calculateAIPathing};

function _getPathingData(): Promise<{ [key: string]: NodeData; }> {
  return client.call('DebugEndpoint', 'getPathingData');
}
export {_getPathingData as getPathingData};

function _setServerSlowDown(
  slowDownFactor: number
): Promise<void> {
  return client.call('DebugEndpoint', 'setServerSlowDown', {slowDownFactor});
}
export {_setServerSlowDown as setServerSlowDown};
