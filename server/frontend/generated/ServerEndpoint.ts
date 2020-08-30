/**
 * This module is generated from ServerEndpoint.java
 * All changes to this file are overridden. Please consider to make changes in the corresponding Java file if necessary.
 * @see {@link file:///Users/eriklumme/projects/2020/hop-n-pop/server/src/main/java/org/vaadin/erik/game/communication/endpoint/ServerEndpoint.java}
 * @module ServerEndpoint
 */

// @ts-ignore
import client from './connect-client.default';
import ServerInfo from './org/vaadin/erik/game/communication/endpoint/ServerInfo';

function _calculateAIPathing(): Promise<void> {
  return client.call('ServerEndpoint', 'calculateAIPathing');
}
export {_calculateAIPathing as calculateAIPathing};

function _despawnAIS(): Promise<void> {
  return client.call('ServerEndpoint', 'despawnAIS');
}
export {_despawnAIS as despawnAIS};

function _getServerInfo(): Promise<ServerInfo> {
  return client.call('ServerEndpoint', 'getServerInfo');
}
export {_getServerInfo as getServerInfo};

/**
 * Records the movement between two nodes, such that the AI can use it.  Starts recording when close enough to an existing node, and stops recording when first coming enough to any other node.
 *
 *
 */
function _recordMovementForAI(): Promise<void> {
  return client.call('ServerEndpoint', 'recordMovementForAI');
}
export {_recordMovementForAI as recordMovementForAI};

function _saveRecordedData(): Promise<void> {
  return client.call('ServerEndpoint', 'saveRecordedData');
}
export {_saveRecordedData as saveRecordedData};

function _setFixedDelta(
  fixedDelta: boolean
): Promise<void> {
  return client.call('ServerEndpoint', 'setFixedDelta', {fixedDelta});
}
export {_setFixedDelta as setFixedDelta};

function _setServerSlowDown(
  slowDownFactor: number
): Promise<void> {
  return client.call('ServerEndpoint', 'setServerSlowDown', {slowDownFactor});
}
export {_setServerSlowDown as setServerSlowDown};

function _spawnAI(): Promise<void> {
  return client.call('ServerEndpoint', 'spawnAI');
}
export {_spawnAI as spawnAI};
